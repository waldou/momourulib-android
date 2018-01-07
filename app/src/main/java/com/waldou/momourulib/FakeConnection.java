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
import com.waldou.momourulib.framework.SearchArguments;
import com.waldou.utils.Connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URLDecoder;
import java.util.Map;

/**
 * @author Waldo Urribarri - www.waldou.com
 *
 * This Connection class is used for testing purposes.
 *
 */
public class FakeConnection extends Connection {

    public FakeConnection(Map<String, String> requestProperties) {
        super(requestProperties);
    }

    @Override
    public String get(String url) throws ProtocolException, MalformedURLException, IOException {
        String[] arguments = url.substring(url.indexOf("?") + 1).split("&");
        String category = null;
        for(String argument : arguments) {
            argument = URLDecoder.decode(argument, Library.CONTENT_ENCODING);
            if(argument.startsWith(SearchArguments.ARG_EXPRESION)) {
                category = argument.split(" ~~~ ")[1];
                break;
            }
        }
        switch(category) {
            case "Libro":
                return readFile("res/raw/libro_libro_response_1.txt");
            case "Libro Digital":
                return readFile("res/raw/libro_libro_response_1.txt");
            case "Publicaciones seriadas":
                return readFile("res/raw/psicologia_publicaciones_seriadas_response_1.txt");
            case "Tesis":
                return readFile("res/raw/albergue_tesis_response.txt");
        }
        return null;
    }

    @Override
    public String post(String url, String postParams) throws ProtocolException, MalformedURLException, IOException {
        String[] arguments = postParams.split("&");
        String category = null;
        for(String argument : arguments) {
            argument = URLDecoder.decode(argument, Library.CONTENT_ENCODING);
            if(argument.startsWith(SearchArguments.ARG_EXPRESION)) {
                category = argument.split(" ~~~ ")[1];
                break;
            }
        }
        switch(category) {
            case "Libro":
                return readFile("res/raw/libro_libro_response_2.txt");
            case "Libro Digital":
            case "Publicaciones seriadas":
            case "Tesis":
                return null;
        }
        return null;
    }

    /**
     * Read response from raw text file.
     * @param file File name.
     * @return File contents.
     */
    private String readFile(String file) {
        StringBuffer buffer = new StringBuffer();
        try {
            InputStream is = this.getClass().getClassLoader().getResourceAsStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(is, Library.CONTENT_ENCODING));
            String line;
            while ((line = br.readLine()) != null)
                buffer.append(line);
        } catch(Exception e) { }
        return buffer.toString();
    }

}
