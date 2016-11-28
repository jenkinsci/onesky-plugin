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

package org.jenkinsci.plugins.validators;

import hudson.util.FormValidation;

/**
 * Validate configurations local to the plugin
 */
public class LocalConfigurationValidator {

    private LocalConfigurationValidator() {
        // Prevent instantiation
    }

    /**
     * Currently only validating that the id of the project is a positive integer.
     *
     * @param projectId the id of the project
     * @return A FormValidation object
     */
    public static FormValidation validateProjectId(String projectId) {
        return FormValidation.validatePositiveInteger(projectId);
    }

    /**
     * Currently only checking that a value exists
     *
     * @param resourcesPath the path for the resources
     * @return A FormValidation object
     */
    public static FormValidation validateResourcePath(String resourcesPath) {
        return FormValidation.validateRequired(resourcesPath);
    }

}
