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

package org.jenkinsci.plugins.connection;

import java.util.Map;
import java.util.logging.Logger;

/**
 * The implementation of the details to Export translations from One sky
 */
public class TranslationExporter {

    private static final Logger LOGGER = Logger.getLogger(TranslationExporter.class.getName());

    private static final String HTTP_METHOD = "GET";

    private static final String BASE_ENDPOINT = "https://platform.api.onesky.io/1/projects/:project_id/translations";

    private TranslationExporter() {
        // Preventing instantiations
    }

    public static void export(Map<String, String> authentication, String projectId) {
        String endpoint = BASE_ENDPOINT.replace(":project_id", projectId);
        LOGGER.info("Preparing to export");
        RESTCaller.makeTheCall(authentication, HTTP_METHOD, endpoint);
    }
}
