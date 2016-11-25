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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.annotation.Nonnull;

import com.onesky.api.ServiceBuilder;
import com.onesky.api.services.FilesService;
import com.onesky.api.services.ProjectService;
import com.onesky.api.services.TranslationService;
import com.onesky.model.ProjectLanguage;
import com.onesky.model.ProjectLanguageList;
import com.onesky.model.UploadedFile;
import com.onesky.model.UploadedFilesResponse;

import org.apache.commons.lang.StringUtils;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;

import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.BuildListener;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import hudson.util.FormValidation;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;
import net.sf.json.JSONObject;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;

/**
 *
 */
@Getter
@Setter
@Log
public class OneSkyResourceDownloader extends Builder {

    private String projectId;

    private String resourcesPath;

    private String apiKey;

    private String apiSecret;

    @DataBoundConstructor
    public OneSkyResourceDownloader(String projectId, String apiSecret, String apiKey, String resourcesPath) {
        this.projectId = projectId;
        this.apiSecret = apiSecret;
        this.apiKey = apiKey;
        this.resourcesPath = resourcesPath;
    }

    @Override
    public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener)
            throws InterruptedException, IOException {

        listener.getLogger().println("Downloading translations from One Sky");

        DescriptorImpl descriptor = (DescriptorImpl) getDescriptor();

        Retrofit serviceBuilder = ServiceBuilder.build(descriptor.getApiKey(), descriptor.getApiSecret());
        FilesService fileService = serviceBuilder.create(FilesService.class);
        UploadedFilesResponse uploadedFilesResponse = fileService.listUploadedFiles(projectId);

        ProjectService projectService = serviceBuilder.create(ProjectService.class);
        ProjectLanguageList projectLanguageList = projectService.listAllLanguagesForAProject(projectId);

        listener.getLogger()
                .println("Found: " + projectLanguageList.getLanguages() + " languages for project: " + projectId);

        TranslationService translationService = serviceBuilder.create(TranslationService.class);
        for (UploadedFile fileUploadDetail : uploadedFilesResponse.getUploadedFiles()) {
            for (ProjectLanguage language : projectLanguageList.getLanguages()) {

                if (!language.isReadyToPublish()) {
                    listener.getLogger().println(
                            "Skipping file: " + fileUploadDetail.getFileName() + " in language: " + language
                                    .getEnglishName() + " as it was marked as not being ready to be published");
                    continue;
                }

                ResponseBody translationInFiles = translationService
                        .exportTranslationInFiles(projectId, language.getCode(), fileUploadDetail.getFileName());

                if (translationInFiles == null) {
                    listener.getLogger().println("Couldn't find a file: " + fileUploadDetail.getFileName() + " for language: " + language);
                    continue;
                }

                listener.getLogger().println(
                        "Writing translations to file: " + fileUploadDetail.getFileName() + " in language: " + language
                                + " to: " + resourcesPath);

                writeContentToFile(build.getWorkspace(), translationInFiles.bytes(), fileUploadDetail, language);
            }
        }

        return true;
    }

    private void writeContentToFile(FilePath workspace, byte[] content, UploadedFile fileUploadDetail,
            ProjectLanguage language) throws IOException {

        String path = workspace + "//" + resourcesPath + "//";
        String fileName = generateFileNameFrom(fileUploadDetail.getFileName(), language.getLocale(),
                language.getRegion());

        File translationFile = new File(path, fileName);
        translationFile.getParentFile().mkdirs();

        try (FileOutputStream fos = new FileOutputStream(translationFile)) {
            fos.write(content);
        }
    }

    private String generateFileNameFrom(String baseFilename, String locale, String region) {
        String baseFilenameNoSuffix = StringUtils.removeEndIgnoreCase(baseFilename, ".properties");

        String newFullFilename = baseFilenameNoSuffix;
        if (StringUtils.isNotBlank(locale)) {
            newFullFilename += "_" + locale;
        }

        if (StringUtils.isNotBlank(region)) {
            newFullFilename += "_" + region.toUpperCase();
        }
        return newFullFilename + StringUtils.difference(baseFilenameNoSuffix, baseFilename);
    }

    @Data
    @Extension
    public static class DescriptorImpl extends BuildStepDescriptor<Builder> {

        private String apiKey;

        private String apiSecret;

        public DescriptorImpl() {
            load();
        }

        public FormValidation doCheckTarget(@QueryParameter String value) {
            if (value.length() == 0) {
                return FormValidation.error("Ups");
            }
            return FormValidation.ok();
        }

        @Override
        public boolean configure(StaplerRequest req, JSONObject json) throws FormException {
            req.bindJSON(this, json.getJSONObject("onesky"));
            save();
            return true;
        }

        @Nonnull
        @Override
        public String getDisplayName() {
            return "Download translation resources from One Sky";
        }

        @Override
        public boolean isApplicable(Class<? extends AbstractProject> aClass) {
            return true;
        }

    }
}
