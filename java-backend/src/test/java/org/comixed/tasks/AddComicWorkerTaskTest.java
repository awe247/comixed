/*
 * ComixEd - A digital comic book library management application.
 * Copyright (C) 2017, The ComiXed Project
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

package org.comixed.tasks;

import java.io.File;

import org.comixed.adaptors.StatusAdaptor;
import org.comixed.library.adaptors.FilenameScraperAdaptor;
import org.comixed.library.model.BlockedPageHash;
import org.comixed.library.model.Comic;
import org.comixed.library.model.ComicFileHandler;
import org.comixed.library.model.ComicFileHandlerException;
import org.comixed.library.model.ComicSelectionModel;
import org.comixed.library.model.Page;
import org.comixed.repositories.BlockedPageHashRepository;
import org.comixed.repositories.ComicRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class AddComicWorkerTaskTest
{
    private static final String TEST_CBZ_FILE = "src/test/resources/example.cbz";
    private static final String PAGE_HASH = "0123456789ABCDEF";

    @InjectMocks
    private AddComicWorkerTask task;

    @Mock
    private MessageSource messageSource;

    @Mock
    private StatusAdaptor statusAdaptor;

    @Mock
    private ComicFileHandler comicFileHandler;

    @Mock
    private ComicRepository comicRepository;

    @Mock
    private BlockedPageHashRepository blockedPageHashRepository;

    @Mock
    private ObjectFactory<Comic> comicFactory;

    @Mock
    private Comic comic;

    @Mock
    private Page page;

    @Mock
    private ComicSelectionModel comicSelectionModel;

    @Mock
    private FilenameScraperAdaptor filenameScraperAdaptor;

    @Mock
    private BlockedPageHash blockedPageHash;

    @Test
    public void testAddFile() throws WorkerTaskException, ComicFileHandlerException
    {
        Mockito.when(comicFactory.getObject()).thenReturn(comic);
        Mockito.doNothing().when(comicFileHandler).loadComic(Mockito.any(Comic.class));
        Mockito.doNothing().when(comicSelectionModel).reload();
        Mockito.doNothing().when(filenameScraperAdaptor).execute(Mockito.any(Comic.class));
        Mockito.when(comicRepository.save(Mockito.any(Comic.class))).thenReturn(Mockito.any(Comic.class));

        File file = new File(TEST_CBZ_FILE);

        task.setFile(file);
        task.startTask();

        Mockito.verify(comicFactory, Mockito.times(1)).getObject();
        Mockito.verify(comicFileHandler, Mockito.times(1)).loadComic(comic);
        Mockito.verify(comicRepository, Mockito.times(1)).save(comic);
        Mockito.verify(comicSelectionModel, Mockito.times(1)).reload();
        Mockito.verify(blockedPageHashRepository, Mockito.never()).findByHash(Mockito.anyString());
        Mockito.verify(filenameScraperAdaptor, Mockito.times(1)).execute(comic);
    }

    @Test
    public void testAddFileWithBlockedPageToBeDeleted() throws WorkerTaskException, ComicFileHandlerException
    {
        Mockito.when(comicFactory.getObject()).thenReturn(comic);
        Mockito.doNothing().when(comicFileHandler).loadComic(Mockito.any(Comic.class));
        Mockito.doNothing().when(comicSelectionModel).reload();
        Mockito.doNothing().when(filenameScraperAdaptor).execute(Mockito.any(Comic.class));
        Mockito.when(comic.getPageCount()).thenReturn(1);
        Mockito.when(comic.getPage(Mockito.anyInt())).thenReturn(page);
        Mockito.when(page.getHash()).thenReturn(PAGE_HASH);
        Mockito.when(blockedPageHashRepository.findByHash(Mockito.anyString())).thenReturn(blockedPageHash);
        Mockito.doNothing().when(page).markDeleted(Mockito.anyBoolean());
        Mockito.when(comicRepository.save(Mockito.any(Comic.class))).thenReturn(comic);

        File file = new File(TEST_CBZ_FILE);

        task.setFile(file);
        task.setDeleteBlockedPages(true);
        task.startTask();

        Mockito.verify(comicFactory, Mockito.times(1)).getObject();
        Mockito.verify(comicFileHandler, Mockito.times(1)).loadComic(comic);
        Mockito.verify(comicSelectionModel, Mockito.times(1)).reload();
        Mockito.verify(filenameScraperAdaptor, Mockito.times(1)).execute(comic);
        Mockito.verify(comic, Mockito.times(2)).getPageCount();
        Mockito.verify(comic, Mockito.times(2)).getPage(0);
        Mockito.verify(blockedPageHashRepository, Mockito.times(1)).findByHash(PAGE_HASH);
        Mockito.verify(page, Mockito.times(1)).markDeleted(true);
        Mockito.verify(comicRepository, Mockito.times(1)).save(comic);
    }

    @Test
    public void testAddFileWithNoBlockedPageButBlockedPagesToBeDeleted() throws WorkerTaskException,
                                                                         ComicFileHandlerException
    {
        Mockito.when(comicFactory.getObject()).thenReturn(comic);
        Mockito.doNothing().when(comicFileHandler).loadComic(Mockito.any(Comic.class));
        Mockito.doNothing().when(comicSelectionModel).reload();
        Mockito.doNothing().when(filenameScraperAdaptor).execute(Mockito.any(Comic.class));
        Mockito.when(comicRepository.save(Mockito.any(Comic.class))).thenReturn(comic);

        File file = new File(TEST_CBZ_FILE);

        task.setFile(file);
        task.setDeleteBlockedPages(true);
        task.startTask();

        Mockito.verify(comicFactory, Mockito.times(1)).getObject();
        Mockito.verify(comicFileHandler, Mockito.times(1)).loadComic(comic);
        Mockito.verify(comicSelectionModel, Mockito.times(1)).reload();
        Mockito.verify(filenameScraperAdaptor, Mockito.times(1)).execute(comic);
        Mockito.verify(comicRepository, Mockito.times(1)).save(comic);
    }

    @Test(expected = WorkerTaskException.class)
    public void testAddFileComicFileHandlerException() throws ComicFileHandlerException, WorkerTaskException
    {
        Mockito.when(comicFactory.getObject()).thenReturn(comic);
        Mockito.doThrow(ComicFileHandlerException.class).when(comicFileHandler).loadComic(Mockito.any(Comic.class));

        try
        {
            File file = new File(TEST_CBZ_FILE);

            task.setFile(file);
            task.startTask();
        }
        finally
        {
            Mockito.verify(comicFactory, Mockito.times(1)).getObject();
            Mockito.verify(comicFileHandler, Mockito.times(1)).loadComic(comic);
        }
    }
}
