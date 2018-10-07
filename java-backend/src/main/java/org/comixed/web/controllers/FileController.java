/*
 * ComixEd - A digital comic book library management application.
 * Copyright (C) 2017, Darryl L. Pierce
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.package
 * org.comixed;
 */

package org.comixed.web.controllers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.comixed.library.model.FileDetails;
import org.comixed.repositories.ComicRepository;
import org.comixed.tasks.AddComicWorkerTask;
import org.comixed.tasks.QueueComicsWorkerTask;
import org.comixed.tasks.Worker;
import org.comixed.utils.ComicFileUtils;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <code>FileController</code> allows the remote agent to query directories and
 * import files, to download files and work with the file system.
 *
 * @author Darryl L. Pierce
 *
 */
@RestController
@RequestMapping("/api/files")
public class FileController
{
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ComicRepository comicRepository;

    @Autowired
    private Worker worker;

    @Autowired
    private ObjectFactory<QueueComicsWorkerTask> taskFactory;

    private int requestId = 0;

    @RequestMapping(value = "/contents",
                    method = RequestMethod.GET)
    @Secured("ROLE_ADMIN")
    public List<FileDetails> getAllComicsUnder(@RequestParam(value = "directory") String directory) throws IOException,
                                                                                                    JSONException
    {
        this.logger.debug("Searching for comics below: " + directory);

        File root = new File(directory);
        List<FileDetails> result = new ArrayList<>();

        if (root.isDirectory())
        {
            this.getAllFilesUnder(root, result);
            result.sort((o1, o2) -> o1.getFilename().compareTo(o2.getFilename()));
        }
        else if (root.isFile())
        {
            result.add(new FileDetails(root.getAbsolutePath(), root.length()));
        }
        else
        {
            this.logger.debug("Not a file or directory: " + directory);
        }

        this.logger.debug("Returning {} comic filenames", result.size());

        return result;
    }

    private void getAllFilesUnder(File root, List<FileDetails> result) throws IOException
    {
        for (File file : root.listFiles())
        {
            if (file.isDirectory())
            {
                this.logger.debug("Searching directory: " + file.getAbsolutePath());
                this.getAllFilesUnder(file, result);
            }
            else
            {

                if (ComicFileUtils.isComicFile(file)
                    && (this.comicRepository.findByFilename(file.getCanonicalPath()) == null))
                {
                    this.logger.debug("Adding file: " + file.getCanonicalPath());
                    result.add(new FileDetails(file.getCanonicalPath(), file.length()));
                }
            }
        }
    }

    @RequestMapping(value = "/import/status",
                    method = RequestMethod.GET)
    public int getImportStatus()
    {
        long started = System.currentTimeMillis();
        this.logger.debug("Received import status request [{}]", ++this.requestId);
        int result = this.worker.getCountFor(AddComicWorkerTask.class);
        this.logger.debug("Responding to import status request [{}] in {}ms (BTW, there are {} imports pending)",
                          this.requestId, (System.currentTimeMillis() - started), result);

        return result;
    }

    @RequestMapping(value = "/import",
                    method = RequestMethod.POST)
    @Secured("ROLE_ADMIN")
    public void importComicFiles(@RequestBody String[] filenames)
    {
        this.logger.debug("Queueing {} files for import", filenames.length);

        QueueComicsWorkerTask task = this.taskFactory.getObject();
        task.setFilenames(Arrays.asList(filenames));
        this.worker.addTasksToQueue(task);
    }
}