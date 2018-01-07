/*******************************************************************************
 * Copyright 2017 Waldo Urribarri.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.waldou.momourulib;

import com.waldou.momourulib.framework.Library;
import com.waldou.momourulib.framework.LibraryItem;
import com.waldou.momourulib.framework.SearchArguments;
import com.waldou.momourulib.framework.SearchConstants;
import com.waldou.momourulib.framework.exceptions.LibraryException;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.util.List;

/**
 * @author Waldo Urribarri - www.waldou.com
 *
 *
 */
@RunWith(JUnit4.class)
public class MomoURULibraryTestSuite extends TestCase {

    @Test
    public void testTesisPageParsing() throws IOException, LibraryException {
        Library lib = new Library(new FakeConnection(null));
        SearchArguments args = new SearchArguments(SearchConstants.CAMPOS.get("Todos"), "albergue", "Tesis");
        List<LibraryItem> items = lib.search(args);
        assertEquals(items.size(), 3);
    }

}
