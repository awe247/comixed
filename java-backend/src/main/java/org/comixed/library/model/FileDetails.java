/*
 * ComiXed - A digital comic book library management application.
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

package org.comixed.library.model;

import org.h2.store.fs.FileUtils;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FileDetails
{
    private String filename;
    private String baseFilename;
    private long size;

    public FileDetails(String filename, long size)
    {
        super();
        this.baseFilename = FileUtils.getName(filename);
        this.filename = filename.replaceAll("\\\\", "/");
        this.size = size;
    }

    public String getFilename()
    {
        return filename;
    }

    public long getSize()
    {
        return size;
    }

    @JsonProperty("base_filename")
    public String getBaseFilename()
    {
        return baseFilename;
    }
}
