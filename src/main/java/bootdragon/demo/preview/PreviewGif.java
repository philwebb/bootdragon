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

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

/**
 * A preview of the demo as an animated GIF.
 *
 * @author Phillip Webb
 */
public class PreviewGif {

	private final byte[] bytes;

	private final long runTime;

	public PreviewGif(byte[] bytes, long runTime) {
		this.bytes = bytes;
		this.runTime = runTime;
	}

	/**
	 * @return the runtime in milliseconds.
	 */
	public long getRunTime() {
		return this.runTime;
	}

	/**
	 * @return an input stream for the GIF data.
	 */
	public InputStream getInputStream() {
		return new ByteArrayInputStream(this.bytes);
	}

	/**
	 * @return the GIF data as a {@link Resource}.
	 */
	public Resource asResource() {
		return new ByteArrayResource(this.bytes) {
			@Override
			public String getFilename() {
				return "preview.gif";
			}
		};
	}

}
