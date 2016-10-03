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

package org.jenkinsci.plugins.security;

import lombok.SneakyThrows;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Create the authentication requirements to make calls to One sky
 */
public class Authentication {

	private static Authentication instance;

	private OneSkyAPICredentials credentials;

	private String lastUsedTimestamp;

	public static Authentication getInstance() {
		if (instance == null) {
			instance = new Authentication();
		}
		return instance;
	}

	public Map<String, String> getAuthentication() {
		Map<String, String> authentication = new HashMap<>();
		authentication.put("api_key", credentials.getApiKey());
		authentication.put("timestamp", lastUsedTimestamp);
		authentication.put("dev_hash", generateDevHash());
		return authentication;
	}

	@SneakyThrows(NoSuchAlgorithmException.class) private String generateDevHash() {
		MessageDigest messageDigest = MessageDigest.getInstance("MD5");
		String timestampAndSecret = getTimestamp() + credentials.getApiSecret();
		return messageDigest.digest(timestampAndSecret.getBytes()).toString();
	}

	private String getTimestamp() {
		lastUsedTimestamp = String.valueOf(new Date().getTime());
		return lastUsedTimestamp;
	}
}
