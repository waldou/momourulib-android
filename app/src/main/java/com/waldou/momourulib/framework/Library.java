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
package com.waldou.momourulib.framework;

import com.waldou.momourulib.framework.exceptions.LibraryException;
import com.waldou.utils.Connection;

import java.io.IOException;
import java.util.List;

/**
 * @author Waldo Urribarri - www.waldou.com
 *
 * This Library class is intended to be used as a Singleton
 * representing the University's library, and is used to do
 * searches in its server.
 *
 */
public class Library {

    public static final String CONTENT_ENCODING = "ISO-8859-1";
    public static final String SEARCH_URL = "http://200.35.84.131/portal/php/buscar.php";

    private Connection conn;
    private LibraryPageProcessor processor;

    public Library(Connection conn) {
        conn.setEncoding(CONTENT_ENCODING);
        this.conn = conn;
        processor = new LibraryPageProcessorImpl();
    }

    public List<LibraryItem> search(SearchArguments searchArguments) throws LibraryException, IOException {
        String response = conn.get(SEARCH_URL + "?" + searchArguments.toQueryString());
        return processor.process(response, searchArguments.getExpre());
    }

    public List<LibraryItem> more(SearchArguments searchArguments) throws LibraryException, IOException {
        String response = conn.post(SEARCH_URL, searchArguments.toPostParams());
        return processor.process(response, searchArguments.getExpre());
    }

}
