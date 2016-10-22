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
import java.util.Map;
import javax.annotation.Nonnull;

import org.jenkinsci.plugins.connection.TranslationExporter;
import org.jenkinsci.plugins.security.Authentication;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;

import hudson.Extension;
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
import net.sf.json.JSONObject;

/**
 *
 */
@Getter
@Setter
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

        Map<String, String> authentication = Authentication.getInstance().getAuthentication(apiKey, apiSecret);

        TranslationExporter.export(authentication, projectId);

        return true;
    }

    @Data
    @Extension
    public static class DescriptorImpl extends BuildStepDescriptor<Builder> {

        public DescriptorImpl() {
            load();
        }

        private String apiKey;

        private String apiSecret;

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
