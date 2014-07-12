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

package bootdragon.demo.preview;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.Test;
import org.springframework.util.FileCopyUtils;

import bootdragon.demo.DemoRequest;
import bootdragon.demo.preview.PreviewGif;
import bootdragon.demo.preview.PreviewGifFactory;
import bootdragon.demo.sourcecode.DemoSourceCode;

/**
 * Tests for {@link PreviewGifFactory}. Creates sample images in the target directory
 * for manual inspection.
 *
 * @author Phillip Webb
 */
public class PreviewGifFactoryTests {

	private PreviewGifFactory factory = new PreviewGifFactory();

	@Test
	public void writeSampleImage() throws Exception {
		doWriteSample("Hello World!", "sample.gif");
	}

	@Test
	public void writeLongerImage() throws Exception {
		doWriteSample("This is and example of a message which extends to the "
				+ "length of one hundred and fourty chars. Thats the most we're "
				+ "going to get on twitter!", "sample2.gif");
	}

	@Test
	public void writeSpecialImage() throws Exception {
		doWriteSample("This is and example\n\twith tabs\n\n\t\t!", "sample3.gif");
	}

	private void doWriteSample(String message, String file) throws IOException,
			FileNotFoundException {
		DemoSourceCode sourceCode = null; // not needed
		DemoRequest request = mock(DemoRequest.class);
		given(request.getMessage()).willReturn(message);
		PreviewGif preview = factory.createPreview(request, sourceCode);
		FileCopyUtils.copy(preview.getInputStream(), new FileOutputStream("target/"
				+ file));
	}

}
