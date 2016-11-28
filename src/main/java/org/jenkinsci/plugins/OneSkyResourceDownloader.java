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

import javax.annotation.Nonnull;

import org.jenkinsci.Symbol;
import org.jenkinsci.plugins.validators.GlobalConfigurationValidator;
import org.jenkinsci.plugins.validators.LocalConfigurationValidator;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;

import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.AbstractProject;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import hudson.util.FormValidation;
import jenkins.tasks.SimpleBuildStep;
import lombok.Data;
import lombok.Getter;
import lombok.extern.java.Log;
import net.sf.json.JSONObject;

/**
 * The Builder class / step that orchestrates the call for the translation files to be downloaded
 */
@Getter
@Log
public class OneSkyResourceDownloader extends Builder implements SimpleBuildStep {

    private String projectId;

    private String resourcesPath;

    @DataBoundConstructor
    public OneSkyResourceDownloader(String projectId, String resourcesPath) {
        this.resourcesPath = resourcesPath;
        this.projectId = projectId;
    }

    @DataBoundSetter
    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    @DataBoundSetter
    public void setResourcesPath(String resourcesPath) {
        this.resourcesPath = resourcesPath;
    }

    @Override
    public void perform(@Nonnull Run<?, ?> run, @Nonnull FilePath workspace, @Nonnull Launcher launcher,
            @Nonnull TaskListener listener) {
        DescriptorImpl descriptor = (DescriptorImpl) getDescriptor();

        TranslationFetcher translationFetcher = new TranslationFetcher();

        translationFetcher
                .fetchRemoteTranslations(descriptor.getApiKey(), descriptor.getApiSecret(), projectId, resourcesPath,
                        workspace, listener.getLogger());
    }

    /**
     * The Build step descriptor implementation for One Sky plugin
     */
    @Symbol("OneSky")
    @Data
    @Extension
    public static class DescriptorImpl extends BuildStepDescriptor<Builder> {

        private String apiKey;

        private String apiSecret;

        public DescriptorImpl() {
            load();
        }

        public FormValidation doValidateProjectId(@QueryParameter String value) {
            return LocalConfigurationValidator.validateProjectId(value);
        }

        public FormValidation doValidateResourcePath(@QueryParameter String value) {
            return LocalConfigurationValidator.validateResourcePath(value);
        }

        public FormValidation doValidateApiSecret(@QueryParameter String value) {
            return GlobalConfigurationValidator.validateApiSecret(value);
        }

        public FormValidation doValidateApiKey(@QueryParameter String value) {
            return GlobalConfigurationValidator.validateApiKey(value);
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
