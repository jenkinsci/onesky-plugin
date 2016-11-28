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
 * A validator to check global configurations
 */
public class GlobalConfigurationValidator {

    private GlobalConfigurationValidator() {
        // Prevent instantiation
    }

    /**
     * Currently only ensures that the api secret is not null
     *
     * @param apiSecret the api secret
     * @return A FormValidation object
     */
    public static FormValidation validateApiSecret(String apiSecret) {
        return FormValidation.validateRequired(apiSecret);
    }

    /**
     * Currently only ensures that the api key is not null
     * @param apiKey the api key
     * @return A FormValidation object
     */
    public static FormValidation validateApiKey(String apiKey) {
        return FormValidation.validateRequired(apiKey);
    }

}
