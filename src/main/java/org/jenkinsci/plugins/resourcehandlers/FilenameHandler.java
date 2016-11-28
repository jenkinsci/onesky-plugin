/*
 * Copyright 2016 Dustriel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jenkinsci.plugins.resourcehandlers;

import com.onesky.model.ProjectLanguage;

/**
 * An interface to handle filenames.
 */
interface FilenameHandler {

    /**
     * Given the base filename, a potential locale and region, this method should produce a String representation of the
     * filename.
     *
     * @param baseFilename the base filename
     * @param language the language details
     * @return a formatted String representing the final filename
     */
    String generateFileNameFrom(String baseFilename, ProjectLanguage language);
}
