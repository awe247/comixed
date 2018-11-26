/*
 * ComiXed - A digital comic book library management application.
 * Copyright (C) 2018, The ComiXed Project
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

package org.comixed.web.comicvine;

import java.util.ArrayList;
import java.util.List;

import org.comixed.library.model.comicvine.ComicVineVolumeQueryCacheEntry;
import org.comixed.repositories.ComicVineVolumeQueryCacheRepository;
import org.comixed.web.ComicVineQueryWebRequest;
import org.comixed.web.WebRequestException;
import org.comixed.web.WebRequestProcessor;
import org.comixed.web.model.ComicVolume;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ComicVineQueryForVolumesAdaptor
{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private WebRequestProcessor webRequestProcessor;

    @Autowired
    private ObjectFactory<ComicVineQueryWebRequest> webRequestFactory;

    @Autowired
    private ComicVineVolumesResponseProcessor responseProcessor;

    @Autowired
    private ComicVineVolumeQueryCacheRepository queryRepository;

    public List<ComicVolume> execute(String apiKey, String name) throws ComicVineAdaptorException, WebRequestException
    {
        this.logger.debug("Fetching volumes: name=\"{}\"", name, apiKey);

        List<ComicVolume> result = new ArrayList<>();
        boolean done = false;
        int page = 0;

        List<ComicVineVolumeQueryCacheEntry> entries = queryRepository.findBySeriesName(name);

        if (entries == null || entries.size() == 0)
        {
            while (!done)
            {
                logger.debug("Fetching volumes from ComicVine...");

                ComicVineQueryWebRequest request = this.webRequestFactory.getObject();
                request.setApiKey(apiKey);
                request.setSeriesName(name);

                page++;
                if (page > 1)
                {
                    this.logger.debug("Setting offset to {}", page);
                    request.setPage(page);
                }

                try
                {
                    String response = this.webRequestProcessor.execute(request);

                    // put the entry in the cache
                    ComicVineVolumeQueryCacheEntry entry = new ComicVineVolumeQueryCacheEntry();

                    entry.setSeriesName(name);
                    entry.setContent(response);
                    entry.setIndex(page);
                    queryRepository.save(entry);
                    done = this.responseProcessor.process(result, response.getBytes());
                }
                catch (WebRequestException error)
                {
                    throw new ComicVineAdaptorException("unable to query for volumes", error);
                }
            }
        }
        else
        {
            logger.debug("Processing {} cached query entries...", entries.size());
            for (int index = 0;
                 index < entries.size();
                 index++)
            {
                this.responseProcessor.process(result, entries.get(index).getContent().getBytes());
            }
        }

        this.logger.debug("Returning {} volumes", result.size());

        return result;
    }
}
