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

import java.io.IOException;

import com.onesky.model.ProjectLanguage;

import org.jenkinsci.plugins.exceptions.TranslationFileIOException;

import hudson.FilePath;

/**
 * An utility class to encapsulate the logic of outputting the translation content onto a resource file.
 */
public class TranslationOutputer {

    private FilenameHandler filenameHandler;

    public TranslationOutputer() {
        this.filenameHandler = new JavaPropertiesFilenameHandler();
    }

    public void writeContentToFile(FilePath workspace, String content, String filename, ProjectLanguage language,
            String resourcesPath) throws TranslationFileIOException {

        String fileSeparator = System.getProperty("file.separator");
        String path = workspace + fileSeparator + resourcesPath + fileSeparator;

        String finalFilename = filenameHandler.generateFileNameFrom(filename, language);


        try {
            FilePath translationFolder = new FilePath(workspace.getChannel(), path);
            translationFolder.mkdirs();

            FilePath translationFile = new FilePath(workspace.getChannel(), path.concat(finalFilename));
            translationFile.write(content, "utf-8");
        } catch (IOException | InterruptedException e) {
            throw new TranslationFileIOException(e);
        }
    }
}
