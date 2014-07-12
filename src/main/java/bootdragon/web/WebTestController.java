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

package bootdragon.web;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import bootdragon.demo.Demo;
import bootdragon.demo.DemoRequest;
import bootdragon.demo.preview.PreviewGif;
import bootdragon.demo.preview.PreviewGifFactory;
import bootdragon.demo.sourcecode.DemoSourceCode;

/**
 * Controller to trigger the UI for testing.
 *
 * @author Phillip Webb
 */
@Controller
@RequestMapping("/test")
public class WebTestController {

	private static final DemoRequest REQUEST = new DemoRequest() {

		@Override
		public String getUser() {
			return "@Test";
		}

		@Override
		public String getSource() {
			return "http://localhost";
		}

		@Override
		public String getMessage() {
			return "Hello World!";
		}

	};

	private final WebUserInterface userInterface;

	private final PreviewGifFactory previewFactory;

	@Autowired
	public WebTestController(WebUserInterface userInterface,
			PreviewGifFactory previewFactory) {
		this.userInterface = userInterface;
		this.previewFactory = previewFactory;
	}

	@RequestMapping("/start")
	@ResponseStatus(HttpStatus.OK)
	public void start() {
		userInterface.start(REQUEST);
	}

	@RequestMapping("/finish")
	@ResponseStatus(HttpStatus.OK)
	public void finish() throws IOException {
		DemoSourceCode sourceCode = new DemoSourceCode("");
		PreviewGif preview = previewFactory.createPreview(REQUEST, sourceCode);
		Demo demo = new Demo(REQUEST, sourceCode, "http://localhost", preview);
		userInterface.finish(demo);
	}

	@RequestMapping("/error")
	@ResponseStatus(HttpStatus.OK)
	public void error() {
		userInterface.error(new RuntimeException());
	}

}
