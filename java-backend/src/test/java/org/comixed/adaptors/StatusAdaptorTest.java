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

package org.comixed.adaptors;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class StatusAdaptorTest
{
    private static final String TEST_STATUS_MESSAGE = "This is a status message...";

    @InjectMocks
    private StatusAdaptor adaptor;

    @Mock
    private StatusListener listener;

    @Test
    public void testAddStatusListener()
    {
        assertTrue(adaptor.listeners.isEmpty());

        adaptor.addStatusListener(listener);

        assertFalse(adaptor.listeners.isEmpty());
    }

    @Test
    public void testFireStatusTextChangedEvent()
    {
        Mockito.doNothing().when(listener).statusTextChanged(Mockito.anyString());

        adaptor.addStatusListener(listener);
        adaptor.updateStatusText(TEST_STATUS_MESSAGE);

        Mockito.verify(listener, Mockito.times(1)).statusTextChanged(TEST_STATUS_MESSAGE);
    }
}
