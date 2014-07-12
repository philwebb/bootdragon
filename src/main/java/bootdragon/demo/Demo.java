/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package bootdragon.demo;

import bootdragon.demo.preview.PreviewGif;
import bootdragon.demo.sourcecode.DemoSourceCode;

/**
 * A completed demo application.
 *
 * @author Phillip Webb
 * @see DemoCreator
 */
public class Demo {

	private DemoRequest request;

	private DemoSourceCode sourceCode;

	private String sourceCodeUrl;

	private PreviewGif preview;

	public Demo(DemoRequest request, DemoSourceCode sourceCode, String sourceCodeUrl,
			PreviewGif preview) {
		this.request = request;
		this.sourceCode = sourceCode;
		this.sourceCodeUrl = sourceCodeUrl;
		this.preview = preview;
	}

	/**
	 * @return the original request used to create the {@link Demo}
	 */
	public DemoRequest getRequest() {
		return request;
	}

	/**
	 * @return the {@link DemoSourceCode}
	 */
	public DemoSourceCode getSourceCode() {
		return sourceCode;
	}

	/**
	 * @return a URL for the saved {@link DemoSourceCode} that may be returned to the user
	 */
	public String getSourceCodeUrl() {
		return sourceCodeUrl;
	}

	/**
	 * @return an animated preview of the code running
	 */
	public PreviewGif getPreview() {
		return preview;
	}

}
