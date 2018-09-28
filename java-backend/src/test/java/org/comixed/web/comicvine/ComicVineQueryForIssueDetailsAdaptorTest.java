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

package org.comixed.web.comicvine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.comixed.library.model.Comic;
import org.comixed.web.ComicVineIssueDetailsWebRequest;
import org.comixed.web.WebRequestException;
import org.comixed.web.WebRequestProcessor;
import org.comixed.web.model.ComicIssue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.ObjectFactory;

@RunWith(MockitoJUnitRunner.class)
public class ComicVineQueryForIssueDetailsAdaptorTest
{
    private static final String TEST_API_KEY = "12345";
    private static final long TEST_COMIC_ID = 512L;
    private static final String TEST_ISSUE_ID = "58312";
    private static final byte[] TEST_CONTENT = "This is the response body".getBytes();
    private static final String TEST_VOLUME_ID = "21385";

    @InjectMocks
    private ComicVineQueryForIssueDetailsAdaptor adaptor;

    @Mock
    private ObjectFactory<ComicVineIssueDetailsWebRequest> requestFactory;

    @Mock
    private ComicVineIssueDetailsWebRequest request;

    @Mock
    private WebRequestProcessor webRequestProcessor;

    @Mock
    private ComicVineIssueDetailsResponseProcessor responseProcessor;

    @Mock
    private ComicIssue comicIssue;

    @Mock
    private Comic comic;

    @Test(expected = ComicVineAdaptorException.class)
    public void testExecuteWebRequestProcessorRaisesException() throws WebRequestException, ComicVineAdaptorException
    {
        Mockito.when(requestFactory.getObject()).thenReturn(request);
        Mockito.doNothing().when(request).setApiKey(TEST_API_KEY);
        Mockito.doNothing().when(request).setIssueNumber(TEST_ISSUE_ID);
        Mockito.when(webRequestProcessor.execute(Mockito.any(ComicVineIssueDetailsWebRequest.class)))
               .thenThrow(new WebRequestException("expected"));

        try
        {
            adaptor.execute(TEST_API_KEY, TEST_COMIC_ID, TEST_ISSUE_ID, comic);
        }
        finally
        {
            Mockito.verify(requestFactory, Mockito.times(1)).getObject();
            Mockito.verify(request, Mockito.times(1)).setApiKey(TEST_API_KEY);
            Mockito.verify(request, Mockito.times(1)).setIssueNumber(TEST_ISSUE_ID);
            Mockito.verify(webRequestProcessor, Mockito.times(1)).execute(request);
        }
    }

    @Test(expected = ComicVineAdaptorException.class)
    public void testExecuteResponseProcessorRaisesException() throws WebRequestException, ComicVineAdaptorException
    {
        Mockito.when(requestFactory.getObject()).thenReturn(request);
        Mockito.doNothing().when(request).setApiKey(TEST_API_KEY);
        Mockito.doNothing().when(request).setIssueNumber(TEST_ISSUE_ID);
        Mockito.when(webRequestProcessor.execute(Mockito.any(ComicVineIssueDetailsWebRequest.class)))
               .thenReturn(TEST_CONTENT);
        Mockito.doThrow(new ComicVineAdaptorException("expected")).when(responseProcessor)
               .process(Mockito.any(byte[].class), Mockito.any(Comic.class));

        try
        {
            adaptor.execute(TEST_API_KEY, TEST_COMIC_ID, TEST_ISSUE_ID, comic);
        }
        finally
        {
            Mockito.verify(requestFactory, Mockito.times(1)).getObject();
            Mockito.verify(request, Mockito.times(1)).setApiKey(TEST_API_KEY);
            Mockito.verify(request, Mockito.times(1)).setIssueNumber(TEST_ISSUE_ID);
            Mockito.verify(webRequestProcessor, Mockito.times(1)).execute(request);
            Mockito.verify(responseProcessor, Mockito.times(1)).process(TEST_CONTENT, comic);
        }
    }

    @Test(expected = ComicVineAdaptorException.class)
    public void testExecuteNoSuchIssueInComicVine() throws WebRequestException, ComicVineAdaptorException
    {
        Mockito.when(requestFactory.getObject()).thenReturn(request);
        Mockito.doNothing().when(request).setApiKey(TEST_API_KEY);
        Mockito.doNothing().when(request).setIssueNumber(TEST_ISSUE_ID);
        Mockito.when(webRequestProcessor.execute(Mockito.any(ComicVineIssueDetailsWebRequest.class)))
               .thenReturn(TEST_CONTENT);
        Mockito.doThrow(new ComicVineAdaptorException("expected")).when(responseProcessor)
               .process(Mockito.any(byte[].class), Mockito.any(Comic.class));

        try
        {
            adaptor.execute(TEST_API_KEY, TEST_COMIC_ID, TEST_ISSUE_ID, comic);
        }
        finally
        {
            Mockito.verify(requestFactory, Mockito.times(1)).getObject();
            Mockito.verify(request, Mockito.times(1)).setApiKey(TEST_API_KEY);
            Mockito.verify(request, Mockito.times(1)).setIssueNumber(TEST_ISSUE_ID);
            Mockito.verify(webRequestProcessor, Mockito.times(1)).execute(request);
            Mockito.verify(responseProcessor, Mockito.times(1)).process(TEST_CONTENT, comic);
        }
    }

    @Test
    public void testExecute() throws WebRequestException, ComicVineAdaptorException
    {
        Mockito.when(requestFactory.getObject()).thenReturn(request);
        Mockito.doNothing().when(request).setApiKey(TEST_API_KEY);
        Mockito.doNothing().when(request).setIssueNumber(TEST_ISSUE_ID);
        Mockito.when(webRequestProcessor.execute(Mockito.any(ComicVineIssueDetailsWebRequest.class)))
               .thenReturn(TEST_CONTENT);
        Mockito.when(responseProcessor.process(Mockito.any(byte[].class), Mockito.any(Comic.class)))
               .thenReturn(TEST_VOLUME_ID);

        String result = adaptor.execute(TEST_API_KEY, TEST_COMIC_ID, TEST_ISSUE_ID, comic);

        assertNotNull(result);
        assertEquals(TEST_VOLUME_ID, result);

        Mockito.verify(requestFactory, Mockito.times(1)).getObject();
        Mockito.verify(request, Mockito.times(1)).setApiKey(TEST_API_KEY);
        Mockito.verify(request, Mockito.times(1)).setIssueNumber(TEST_ISSUE_ID);
        Mockito.verify(webRequestProcessor, Mockito.times(1)).execute(request);
        Mockito.verify(responseProcessor, Mockito.times(1)).process(TEST_CONTENT, comic);
    }
}
