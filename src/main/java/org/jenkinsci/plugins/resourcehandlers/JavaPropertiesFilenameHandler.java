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

import org.apache.commons.lang3.StringUtils;

/**
 * This class follows the traditional Java conventions for naming internationalisation resources.
 *
 * @see <a href="https://docs.oracle.com/javase/tutorial/i18n/resbundle/concept.html">About the ResourceBundle Class</a>
 */
public class JavaPropertiesFilenameHandler implements FilenameHandler {

    @Override
    public String generateFileNameFrom(String baseFilename, ProjectLanguage language) {

        if (language.isBaseLanguage()) {
            return baseFilename;
        }

        String baseFilenameNoSuffix = StringUtils.removeEndIgnoreCase(baseFilename, ".properties");

        String newFullFilename = baseFilenameNoSuffix;
        String locale = language.getLocale();
        if (StringUtils.isNotBlank(locale)) {
            newFullFilename += "_" + locale;
        }

        String region = language.getRegion();
        if (StringUtils.isNotBlank(region)) {
            newFullFilename += "_" + region.toUpperCase();
        }

        return newFullFilename + StringUtils.difference(baseFilenameNoSuffix, baseFilename);
    }
}
