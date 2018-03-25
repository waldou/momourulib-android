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
package com.waldou.momourulib.tasks;

/**
 * @author Waldo Urribarri - www.waldou.com
 *
 *
 */
public interface TaskConstants {
    // Response Codes
    public static final int RESPONSE_CODE_UNKNOWN = -99;
    public static final int RESPONSE_CODE_NO_RESULTS = 1;
    public static final int RESPONSE_CODE_RESULTS_FOUND = 0;
    public static final int RESPONSE_CODE_SERVER_OFFLINE = -1;
    public static final int RESPONSE_CODE_IO_ERROR = -2;
    public static final int RESPONSE_CODE_NO_INTERNET = -3;
}
