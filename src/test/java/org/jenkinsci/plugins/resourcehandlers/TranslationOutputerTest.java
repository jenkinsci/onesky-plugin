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

import java.io.File;
import java.io.IOException;

import com.onesky.model.ProjectLanguage;

import org.jenkinsci.plugins.exceptions.TranslationFileIOException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import hudson.FilePath;
import hudson.Util;

public class TranslationOutputerTest {

    private TranslationOutputer translationOutputer;

    private FilePath workspace;

    @Before
    public void setup() throws Exception {
        this.createWorkspace();
        this.translationOutputer = new TranslationOutputer();
    }

    @After
    public void after() throws Exception {
        this.deleteWorkspace();
    }

    private void createWorkspace() throws Exception {
        File parentFile = Util.createTempDir();
        workspace = new FilePath(parentFile);
        workspace.mkdirs();
    }

    private void deleteWorkspace() throws Exception {
        workspace.deleteRecursive();
    }

    @Test
    public void exportingTranslationsToFileWillSucceed() throws IOException, TranslationFileIOException, InterruptedException {
        ProjectLanguage projectLanguage = new ProjectLanguage("code", "englishName", "localName", "locale", "region",
                false, true, "translationProgress", "uploadedAt", 1L);
        String resourcesPath = "src/main/resources";
        String content = "content";
        translationOutputer
                .writeContentToFile(this.workspace, content, "base.properties", projectLanguage, resourcesPath);

        Assert.assertTrue(new FilePath(workspace, resourcesPath).exists());
        String filePathAndName = resourcesPath.concat("/base_locale_REGION.properties");
        Assert.assertTrue(new FilePath(workspace, filePathAndName).exists());
        Assert.assertEquals(new FilePath(workspace, filePathAndName).readToString(), content);
    }
}
