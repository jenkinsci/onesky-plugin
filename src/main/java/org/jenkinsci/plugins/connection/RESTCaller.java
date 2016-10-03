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

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.Map;

/**
 * A Wrapper around the implementation of the REST Call to the one sky services
 */
public class RESTCaller {

	private RESTCaller() {
		// Preventing instantiation
	}

	protected static HttpResponse<JsonNode> makeTheCall(Map<String, String> headers, String method, String endpoint,
			Map<String, String> arguments) {
		if (arguments == null) {
			try {
				return Unirest.get(endpoint).headers(headers).header("content-type", "application/json").asJson();
			} catch (UnirestException e) {
				e.printStackTrace();
			}
		}
		try {
			return Unirest.get(endpoint).header("content-type", "application/json").headers(headers).asJson();
		} catch (UnirestException e) {
			e.printStackTrace();
		}
		return null;
	}

	protected static HttpResponse<JsonNode> makeTheCall(Map<String, String> headers, String method, String endpoint) {
		return makeTheCall(headers, method, endpoint, null);
	}
}
