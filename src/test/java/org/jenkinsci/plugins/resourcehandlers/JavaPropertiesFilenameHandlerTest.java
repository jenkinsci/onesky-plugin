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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class JavaPropertiesFilenameHandlerTest {

    private JavaPropertiesFilenameHandler jpfH;

    @Before
    public void setup() {
        this.jpfH = new JavaPropertiesFilenameHandler();
    }

    @Test
    public void generateFileNameFrom() throws Exception {
        String locale = "locale";
        String region = "region";
        ProjectLanguage projectLanguage = new ProjectLanguage("code", "englishName", "localName", locale, region,
                false, true, "translationProgress", "uploadedAt", 1L);
        String fileName = jpfH.generateFileNameFrom("base.properties", projectLanguage);

        Assert.assertEquals("base_" + locale + "_" + region.toUpperCase() + ".properties", fileName);
    }

    @Test
    public void generateFileNameWithoutRegion() {
        String locale = "locale";
        ProjectLanguage projectLanguage = new ProjectLanguage("code", "englishName", "localName", locale, "",
                false, true, "translationProgress", "uploadedAt", 1L);
        String fileName = jpfH.generateFileNameFrom("base.properties", projectLanguage);

        Assert.assertEquals("base_" + locale + ".properties", fileName);
    }

    @Test
    public void generateFileNameWithoutLocaleOrRegion() {
        ProjectLanguage projectLanguage = new ProjectLanguage("code", "englishName", "localName", "", "",
                false, true, "translationProgress", "uploadedAt", 1L);
        String fileName = jpfH.generateFileNameFrom("base.properties", projectLanguage);

        Assert.assertEquals("base.properties", fileName);
    }

}
