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

package org.jenkinsci.plugins;

import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import com.onesky.api.ServiceBuilder;
import com.onesky.api.services.FilesService;
import com.onesky.api.services.ProjectService;
import com.onesky.api.services.TranslationService;
import com.onesky.model.ProjectLanguage;
import com.onesky.model.ProjectLanguageList;
import com.onesky.model.UploadedFile;
import com.onesky.model.UploadedFilesResponse;

import org.jenkinsci.plugins.exceptions.TranslationFileIOException;
import org.jenkinsci.plugins.resourcehandlers.TranslationOutputer;

import hudson.FilePath;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;

/**
 * The actual worker for the translation resources to be downloaded and recreated.
 */
class TranslationFetcher {

    private TranslationOutputer translationOutputer;

    TranslationFetcher() {
        this.translationOutputer = new TranslationOutputer();
    }

    boolean fetchRemoteTranslations(String apiKey, String apiSecret, String projectId, String resourcesPath,
            FilePath workspace, PrintStream logger) {

        logger.println("Downloading translations from One Sky");

        Retrofit serviceBuilder = ServiceBuilder.build(apiKey, apiSecret);
        FilesService fileService = serviceBuilder.create(FilesService.class);

        ProjectService projectService = serviceBuilder.create(ProjectService.class);
        ProjectLanguageList projectLanguageList = projectService.listAllLanguagesForAProject(projectId);

        List<ProjectLanguage> languages = projectLanguageList.getLanguages();
        logger.println("Found: " + languages + " languages for project: " + projectId);

        List<ProjectLanguage> readyToBePublishedLanguages = new ArrayList<>();
        for (ProjectLanguage language : languages) {
            if (language.isReadyToPublish()) {
                readyToBePublishedLanguages.add(language);
            }
        }

        UploadedFilesResponse uploadedFilesResponse = fileService.listUploadedFiles(projectId);
        TranslationService translationService = serviceBuilder.create(TranslationService.class);
        for (UploadedFile fileUploadDetail : uploadedFilesResponse.getUploadedFiles()) {
            for (ProjectLanguage language : readyToBePublishedLanguages) {

                String filename = fileUploadDetail.getFileName();
                ResponseBody translationInFiles = translationService
                        .exportTranslationInFiles(projectId, language.getCode(), filename);

                if (translationInFiles == null) {
                    logger.println(
                            "Couldn't find a file: " + filename + " for language: " + language);
                    continue;
                }

                String content = null;
                try {
                    content = translationInFiles.string();
                } catch (IOException e) {
                    logger.println("Failed to retrieve the content from the translation file");
                    logger.println(e);
                }

                logger.println("Writing translations to file: " + filename + " in language: " + language
                        + " to: " + resourcesPath);

                try {
                    translationOutputer
                            .writeContentToFile(workspace, content, filename, language, resourcesPath);
                } catch (TranslationFileIOException e) {
                    logger.println("Failed to write the content into a resource file");
                    logger.println(e);
                }
            }
        }

        return true;
    }
}
