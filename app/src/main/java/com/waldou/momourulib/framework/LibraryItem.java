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

import java.io.Serializable;
import java.util.List;

/**
 * @author Waldo Urribarri - www.waldou.com
 *
 * This class is used to represent a generic item from the library.
 *
 */
public class LibraryItem implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String category;
    private String name;
    private String description;
    private List<String> keywords;
    private String code;

    public LibraryItem(String category, String name, String description, String code, List<String> keywords) {
        this.id = code.toUpperCase()+category.toUpperCase()+name.toUpperCase();
        this.category = category;
        this.name = name;
        this.description = description;
        this.code = code;
        this.keywords = keywords;
    }

    public String getId() {
        if(id == null)
            id = code.toUpperCase()+category.toUpperCase()+name.toUpperCase();
        return id;
    }
    public void setId(String id) { this.id = id; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public List<String> getKeywords() { return keywords; }
    public void setKeywords(List<String> keywords) { this.keywords = keywords; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

}
