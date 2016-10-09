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
import net.sf.json.JSONObject;

/**
 *
 */
public class OneSkyResourceDownloader extends Builder {

    private String projectId;

    private String resourcesPath;

    @DataBoundConstructor
    public OneSkyResourceDownloader(String projectId, String resourcesPath) {
        this.projectId = projectId;
        this.resourcesPath = resourcesPath;
    }

    @Override
    public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener)
            throws InterruptedException, IOException {

        Map<String, String> authentication = Authentication.getInstance().getAuthentication();

        TranslationExporter.export(authentication, projectId);

        return true;
    }

    public String getProjectId() {
        return projectId;
    }

    public String getResourcesPath() {
        return resourcesPath;
    }

    @Extension
    public static class DescriptorImpl extends BuildStepDescriptor<Builder> {

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
            save();
            return super.configure(req, json);
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
