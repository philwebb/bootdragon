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

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import bootdragon.demo.Demo;
import bootdragon.demo.DemoCreatorListener;
import bootdragon.demo.DemoRequest;
import bootdragon.demo.preview.PreviewGif;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

/**
 * {@link DemoCreatorListener} that updates the web UI.
 *
 * @author Phillip Webb
 */
@Controller
public class WebUserInterface implements DemoCreatorListener {

	private final SimpMessagingTemplate messages;

	private Cache<String, PreviewGif> previewCache = CacheBuilder.newBuilder()
			.maximumSize(40).expireAfterAccess(4, TimeUnit.MINUTES).build();

	private final String twitterScreenName;

	@Autowired
	public WebUserInterface(SimpMessagingTemplate messages, Twitter twitter) {
		this.messages = messages;
		this.twitterScreenName = "@" + twitter.userOperations().getScreenName();
	}

	@Override
	public void start(DemoRequest request) {
		messages.convertAndSend("/control/start", request);
	}

	@Override
	public void finish(Demo demo) {
		// Leave some time for typing
		sleep(TimeUnit.SECONDS.toMillis(3));

		String previewId = UUID.randomUUID().toString();
		previewCache.put(previewId, demo.getPreview());
		PreviewDetails details = new PreviewDetails("/preview/" + previewId, demo
				.getPreview().getRunTime());

		messages.convertAndSend("/control/finish", details);
	}

	@RequestMapping({"/", "/index.html"})
	public ModelAndView index() {
		return new ModelAndView("index").addObject("twitterScreenName", twitterScreenName);
	}

	@RequestMapping("/preview/{id}")
	public ResponseEntity<Resource> preview(@PathVariable String id) {
		PreviewGif gif = this.previewCache.getIfPresent(id);
		if (gif == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_GIF);
		return new ResponseEntity<>(gif.asResource(), headers, HttpStatus.OK);
	}

	@Override
	public void error(Exception ex) {
		messages.convertAndSend("/control/error", "");
	}

	private void sleep(long millis) {
		try {
			Thread.sleep(millis);
		}
		catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
	}

}
