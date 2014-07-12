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

package bootdragon.demo.sourcecode;

import static org.hamcrest.Matchers.endsWith;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import org.junit.Test;

import bootdragon.demo.DemoRequest;
import bootdragon.demo.sourcecode.DemoSourceCode;
import bootdragon.demo.sourcecode.DemoSourceCodeFactory;

/**
 * Tests for {@link DemoSourceCodeFactory}.
 *
 * @author Phillip Webb
 */
public class DemoSourceCodeFactoryTests {

	private static final String QUOTE = "\"";

	private static final String SLASH = "\\";

	private DemoSourceCodeFactory factory = new DemoSourceCodeFactory();

	@Test
	public void normal() throws Exception {
		DemoSourceCode code = factory.createSourceCode(mockRequest("Hello World!"));
		System.out.println(code);
		String[] lines = code.toString().split("\n");
		assertThat(lines[3], endsWith("for @springboot"));
		assertThat(lines[4], endsWith("from http://localhost"));
		assertThat(lines[21], endsWith(QUOTE + "Hello World!" + QUOTE));
	}

	@Test
	public void escapeQuotes() throws Exception {
		DemoSourceCode code = factory.createSourceCode(mockRequest("a\"b"));
		System.out.println(code);
		String[] lines = code.toString().split("\n");
		assertThat(lines[21], endsWith(QUOTE + "a" + SLASH + QUOTE + "b" + QUOTE));
	}

	@Test
	public void escapeSlash() throws Exception {
		DemoSourceCode code = factory.createSourceCode(mockRequest("a\\b"));
		System.out.println(code);
		String[] lines = code.toString().split("\n");
		assertThat(lines[21], endsWith(QUOTE + "a" + SLASH + SLASH + "b" + QUOTE));
	}

	@Test
	public void escapeUnicode() throws Exception {
		DemoSourceCode code = factory.createSourceCode(mockRequest("\uD83D\uDE00"));
		System.out.println(code);
		String[] lines = code.toString().split("\n");
		assertThat(lines[21], endsWith(QUOTE + SLASH + "uD83D" + SLASH + "uDE00" + QUOTE));
	}

	private DemoRequest mockRequest(String message) {
		DemoRequest request = mock(DemoRequest.class);
		given(request.getMessage()).willReturn(message);
		given(request.getUser()).willReturn("@springboot");
		given(request.getSource()).willReturn("http://localhost");
		return request;
	}

}
