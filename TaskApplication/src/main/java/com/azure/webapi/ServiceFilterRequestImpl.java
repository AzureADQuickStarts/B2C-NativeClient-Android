/*
Copyright (c) Microsoft
All Rights Reserved
Apache 2.0 License
 
   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at
 
     http://www.apache.org/licenses/LICENSE-2.0
 
   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 
See the Apache Version 2.0 License for specific language governing permissions and limitations under the License.
 */
/*
 * ServiceFilterRequestImpl.java
 */

package com.azure.webapi;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;

import com.microsoft.aad.test.todoapi.Constants;

import android.net.http.AndroidHttpClient;
import android.os.Build;

/**
 * 
 * ServiceFilterRequest implementation
 * 
 */
public class ServiceFilterRequestImpl implements ServiceFilterRequest {

	/**
	 * The request to execute
	 */
	private HttpRequestBase mRequest;

	
	
	/**
	 * The request content
	 */
	private byte[] mContent;

	/**
	 * Constructor
	 * 
	 * @param request
	 *            The request to use
	 */
	public ServiceFilterRequestImpl(HttpRequestBase request) {
		mRequest = request;
	}

	@Override
	public ServiceFilterResponse execute() throws Exception {
		// Execute request
		AndroidHttpClient client = AndroidHttpClient.newInstance(getUserAgent());
		try {
			final HttpResponse response = client.execute(mRequest);
			ServiceFilterResponse serviceFilterResponse = new ServiceFilterResponseImpl(response);
			return serviceFilterResponse;
		} finally {
			client.close();
		}
	}

	/**
	 * Generates the User-Agent
	 */
	static String getUserAgent() {
		String userAgent = String.format(
				"ZUMO/%s (lang=%s; os=%s; os_version=%s; arch=%s)",
				Constants.SDK_VERSION, "Java", "Android", Build.VERSION.RELEASE,
				Build.CPU_ABI);

		return userAgent;
	}

	@Override
	public Header[] getHeaders() {
		return mRequest.getAllHeaders();
	}

	@Override
	public void addHeader(String name, String val) {
		mRequest.addHeader(name, val);
	}

	@Override
	public void removeHeader(String name) {
		mRequest.removeHeaders(name);
	}


	@Override
	public void setContent(byte[] content) throws Exception {
		((HttpEntityEnclosingRequestBase) mRequest).setEntity(new ByteArrayEntity(content));
		mContent = content;
	}
	
	@Override
	public void setContent(String content) throws UnsupportedEncodingException {
		((HttpEntityEnclosingRequestBase) mRequest).setEntity(new StringEntity(
				content, Constants.UTF8_ENCODING));
		mContent = content.getBytes(Constants.UTF8_ENCODING);
	}

	@Override
	public String getContent() {
		if (mContent != null) {
			String content = null;
			try {
				content = new String(mContent, Constants.UTF8_ENCODING);
			} catch (UnsupportedEncodingException e) {
			}
			return content;
		} else {
			return null;
		}
	}
	
	@Override
	public byte[] getRawContent() {
		return mContent;
	}

	@Override
	public String getUrl() {
		return mRequest.getURI().toString();
	}

	@Override
	public void setUrl(String url) throws URISyntaxException {
		mRequest.setURI(new URI(url));

	}

	@Override
	public String getMethod() {
		return mRequest.getMethod();
	}

}
