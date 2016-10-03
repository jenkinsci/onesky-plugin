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

import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import hudson.tasks.BuildStep;
import hudson.tasks.Builder;
import org.jenkinsci.plugins.connection.TranslationExporter;
import org.jenkinsci.plugins.security.Authentication;

import java.io.IOException;
import java.util.Map;

/**
 *
 */
public class OneSkyResourceDownloader extends Builder implements BuildStep {

	@Override public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener)
			throws InterruptedException, IOException {

		Map<String, String> authentication = Authentication.getInstance().getAuthentication();

		TranslationExporter.export(authentication, "projectId");

		return true;
	}
}
