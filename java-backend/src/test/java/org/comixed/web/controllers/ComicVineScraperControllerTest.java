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

package org.comixed.web.controllers;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import java.util.List;

import org.comixed.web.WebRequestException;
import org.comixed.web.comicvine.ComicVineAdaptorException;
import org.comixed.web.comicvine.ComicVineQueryForIssueAdaptor;
import org.comixed.web.comicvine.ComicVineQueryForVolumesAdaptor;
import org.comixed.web.model.ComicIssue;
import org.comixed.web.model.ComicVolume;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class ComicVineScraperControllerTest
{
    private static final String TEST_API_KEY = "12345";
    private static final String TEST_SERIES_NAME = "Awesome Comic";
    private static final String TEST_VOLUME = "2018";
    private static final String TEST_ISSUE_NUMBER = "15";

    @InjectMocks
    private ComicVineScraperController controller;

    @Mock
    private ComicVineQueryForVolumesAdaptor comicVineQueryForVolumesAdaptor;

    @Mock
    private ComicVineQueryForIssueAdaptor comicVineQueryForIssueAdaptor;

    @Mock
    private List<ComicVolume> comicVolumeList;

    @Mock
    private ComicIssue comicIssue;

    @Test(expected = ComicVineAdaptorException.class)
    public void testQueryForVolumesAdaptorRaisesException() throws WebRequestException, ComicVineAdaptorException
    {
        Mockito.when(comicVineQueryForVolumesAdaptor.execute(Mockito.anyString(), Mockito.anyString()))
               .thenThrow(new ComicVineAdaptorException("expected"));

        try
        {
            controller.queryForVolumes(TEST_API_KEY, TEST_SERIES_NAME, TEST_VOLUME, TEST_ISSUE_NUMBER);
        }
        finally
        {
            Mockito.verify(comicVineQueryForVolumesAdaptor, Mockito.times(1)).execute(TEST_API_KEY, TEST_SERIES_NAME);
        }
    }

    @Test
    public void testQueryForVolumes() throws ComicVineAdaptorException, WebRequestException
    {
        Mockito.when(comicVineQueryForVolumesAdaptor.execute(Mockito.anyString(), Mockito.anyString()))
               .thenReturn(comicVolumeList);

        List<ComicVolume> result = controller.queryForVolumes(TEST_API_KEY, TEST_SERIES_NAME, TEST_VOLUME,
                                                              TEST_ISSUE_NUMBER);

        assertSame(comicVolumeList, result);

        Mockito.verify(comicVineQueryForVolumesAdaptor, Mockito.times(1)).execute(TEST_API_KEY, TEST_SERIES_NAME);
    }

    @Test(expected = ComicVineAdaptorException.class)
    public void testQueryForIssueAdaptorRaisesException() throws ComicVineAdaptorException
    {
        Mockito.when(comicVineQueryForIssueAdaptor.execute(Mockito.anyString(), Mockito.anyString(),
                                                            Mockito.anyString()))
               .thenThrow(new ComicVineAdaptorException("expected"));

        try
        {
            controller.queryForIssue(TEST_API_KEY, TEST_VOLUME, TEST_ISSUE_NUMBER);
        }
        finally
        {
            Mockito.verify(comicVineQueryForIssueAdaptor, Mockito.times(1)).execute(TEST_API_KEY, TEST_VOLUME,
                                                                                     TEST_ISSUE_NUMBER);
        }
    }

    @Test
    public void testQueryForIssue() throws ComicVineAdaptorException
    {
        Mockito.when(comicVineQueryForIssueAdaptor.execute(Mockito.anyString(), Mockito.anyString(),
                                                            Mockito.anyString()))
               .thenReturn(comicIssue);

        ComicIssue result = controller.queryForIssue(TEST_API_KEY, TEST_VOLUME, TEST_ISSUE_NUMBER);

        assertNotNull(result);
        assertSame(comicIssue, result);

        Mockito.verify(comicVineQueryForIssueAdaptor, Mockito.times(1)).execute(TEST_API_KEY, TEST_VOLUME,
                                                                                 TEST_ISSUE_NUMBER);
    }
}
