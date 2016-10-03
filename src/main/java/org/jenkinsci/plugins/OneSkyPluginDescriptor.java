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

import hudson.Extension;
import hudson.model.AbstractProject;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;

import javax.annotation.Nonnull;

/**
 * Created by ricardo on 03/10/2016.
 */
@Extension
public class OneSkyPluginDescriptor extends BuildStepDescriptor<Builder> {

	@Nonnull @Override public String getDisplayName() {
		return "One Sky";
	}

	@Override public boolean isApplicable(Class<? extends AbstractProject> aClass) {
		return true;
	}
}
