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

import java.io.IOException;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.AsyncListenableTaskExecutor;
import org.springframework.core.task.support.TaskExecutorAdapter;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

import bootdragon.demo.preview.PreviewGif;
import bootdragon.demo.preview.PreviewGifFactory;
import bootdragon.demo.sourcecode.DemoSourceCode;
import bootdragon.demo.sourcecode.DemoSourceCodeFactory;
import bootdragon.demo.sourcecode.DemoSourceCodeRepository;

/**
 * Process {@link DemoRequest}s to create demo applications. Demos are created within a
 * single thread executor (demos are never created in parallel). A
 * {@link DemoCreatorListener} can be used to update a UI during creation.
 *
 * @author Phillip Webb
 */
@Component
public class DemoCreator {

	private static Logger logger = LoggerFactory.getLogger(DemoCreator.class);

	private final DemoCreatorListener listener;

	private final DemoSourceCodeFactory sourceCodeFactory;

	private final PreviewGifFactory previewFactory;

	private final DemoSourceCodeRepository sourceCodeRepository;

	private final AsyncListenableTaskExecutor executor;

	@Autowired
	public DemoCreator(DemoCreatorListener listener,
			DemoSourceCodeFactory sourceCodeFactory, PreviewGifFactory previewFactory,
			DemoSourceCodeRepository sourceCodeRepository) {
		this.listener = listener;
		this.sourceCodeFactory = sourceCodeFactory;
		this.previewFactory = previewFactory;
		this.sourceCodeRepository = sourceCodeRepository;
		this.executor = new TaskExecutorAdapter(Executors.newSingleThreadExecutor());
	}

	/**
	 * Add the given request to the executor queue, returning a {@link ListenableFuture}
	 * that may be used to detect completion.
	 * @param request the request
	 * @return a {@link ListenableFuture} for the created {@link Demo}
	 */
	public ListenableFuture<Demo> addRequest(DemoRequest request) {
		logger.debug("Added request for {} from {}", request.getUser(),
				request.getSource());
		return this.executor.submitListenable(() -> handleRequest(request));
	}

	private Demo handleRequest(DemoRequest request) throws Exception {
		logger.info("Handling request for {} from {}", request.getUser(),
				request.getSource());
		this.listener.start(request);
		try {
			Demo demo = createDemo(request);
			this.listener.finish(demo);
			logger.debug("Handled request for {} from {}", request.getUser(),
					request.getSource());
			return demo;
		}
		catch (Exception ex) {
			logger.error("Unable to handler request", ex);
			listener.error(ex);
			throw ex;
		}
	}

	private Demo createDemo(DemoRequest request) throws IOException {
		DemoSourceCode sourceCode = this.sourceCodeFactory.createSourceCode(request);
		PreviewGif preview = this.previewFactory.createPreview(request, sourceCode);
		String sourceCodeUri = this.sourceCodeRepository.save(sourceCode);
		return new Demo(request, sourceCode, sourceCodeUri, preview);
	}

}
