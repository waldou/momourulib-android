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
import com.waldou.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Waldo Urribarri - www.waldou.com
 *
 * This class is used to process the server's response, parsing
 * the contents of the page into LibraryItems.
 *
 */
public class LibraryPageProcessorImpl implements LibraryPageProcessor {

    private static final String REGEX_GENERAL_ITEM_TABLE = "<table width=100% border=0 cellpadding=0 cellspacing=0>.*?<td  align=left valign=top width=100%>(.*?)<\\/td><\\/table>";
    private static final String REGEX_SINGLE_ITEM = "<font [0-9a-zA-Z= \"':;_\\-\\%]+>(.*?)<span id=\"mmarc[0-9a-zA-Z]+\"[0-9a-zA-Z= \"':;_\\-\\%]+>.*?<td [0-9a-zA-Z= \"':;_\\-\\%]+>(.*)<br><font [0-9a-zA-Z= \"':;_\\-\\%]+>(.*?)<br><b> Solicite el material por este código: (.*?)<\\/font>";
    private static final String REGEX_SINGLE_ITEM_PUBLICACIONES_SERIADAS = "<font [0-9a-zA-Z= \"':;_\\-\\%]+>(.*?)<span id=\"mmarc[0-9a-zA-Z]+\"[0-9a-zA-Z= \"':;_\\-\\%]+>.*?<td [0-9a-zA-Z= \"':;_\\-\\%]+>(.*)<br><font [0-9a-zA-Z= \"':;_\\-\\%]+>(.*?)<\\/font>";
    private static final String REGEX_KEYWORDS = "javascript:CRUZARD\\(\\'([0-9a-zA-Záéíóúàèìòùñ\\-, ^]+)\\'(\\,|\\))";

    private static final String CATEGORY_PUBLICACIONES_SERIADAS = "Publicaciones seriadas";

    /**
     * This method processes the server's response body.
     * @param page Response from the server as HTML contents.
     * @param category Category to assign to the LibraryItems present in the page.
     * @return List of LibraryItems.
     * @throws LibraryException
     */
    @Override
    public List<LibraryItem> process(String page, String category) throws LibraryException {
        Pattern generalPattern = Pattern.compile(REGEX_GENERAL_ITEM_TABLE);
        Pattern singleItemPattern = !category.equals(CATEGORY_PUBLICACIONES_SERIADAS) ?
                Pattern.compile(REGEX_SINGLE_ITEM) : Pattern.compile(REGEX_SINGLE_ITEM_PUBLICACIONES_SERIADAS);
        Pattern keywordsPattern = Pattern.compile(REGEX_KEYWORDS);
        Matcher generalMatcher = generalPattern.matcher(page);

        List<LibraryItem> items = new ArrayList<LibraryItem>();

        try {
            while (generalMatcher.find()) {

                String itemContents = generalMatcher.group(1).trim();
                Matcher singleItemMatcher = singleItemPattern.matcher(itemContents);

                if (singleItemMatcher.find()) {
                    String strName = singleItemMatcher.group(1).trim();
                    String strDescription = removeTags(singleItemMatcher.group(2).trim());
                    String strKeywords = singleItemMatcher.group(3).trim();
                    List<String> keywords = null;
                    Matcher keywordsMatcher = keywordsPattern.matcher(strKeywords);
                    if (keywordsMatcher.find()) {
                        keywords = new ArrayList<String>();
                        keywords.add(keywordsMatcher.group(1).trim());
                        while (keywordsMatcher.find()) {
                            keywords.add(keywordsMatcher.group(1).trim());
                        }
                    }

                    String strCode = !category.equals(CATEGORY_PUBLICACIONES_SERIADAS) ? singleItemMatcher.group(4).trim() : "";

                    items.add(new LibraryItem(category, strName, strDescription, strCode, keywords));
                }

            }
        } catch(Exception e) {
            throw new LibraryException(-1, "Error procesando respuesta", e);
        }

        return items;
    }

    /**
     * This replaces some tags that are present in the body.
     * TODO: find a better way
     * @param text String with tags.
     * @return String without tags.
     */
    private String removeTags(String text) {
        StringBuilder sb = new StringBuilder(text);
        Utils.replaceAll(sb, "<br>", "\n");
        Utils.replaceAll(sb, "<b>", "");
        Utils.replaceAll(sb, "</b>", "");
        Utils.replaceAll(sb, "<B>", "");
        Utils.replaceAll(sb, "</B>", "");
        return sb.toString();
    }

}
