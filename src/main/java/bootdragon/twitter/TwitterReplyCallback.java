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

package bootdragon.twitter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.TweetData;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.util.concurrent.ListenableFutureCallback;

import bootdragon.demo.Demo;

/**
 * {@link ListenableFutureCallback} to respond to created {@link Demo}s with a twitter
 * reply.
 *
 * @author Phillip Webb
 */
class TwitterReplyCallback implements ListenableFutureCallback<Demo> {

	private static Logger logger = LoggerFactory.getLogger(TwitterReplyCallback.class);

	private final Twitter twitter;

	private final Tweet source;

	public TwitterReplyCallback(Twitter twitter, Tweet source) {
		this.twitter = twitter;
		this.source = source;
	}

	@Override
	public void onSuccess(Demo result) {
		logger.info("Sending tweet response to {}", source.getFromUser());
		String message = getMessage(result);
		Resource media = result.getPreview().asResource();
		TweetData tweet = new TweetData(message).withMedia(media);
		tweet.inReplyToStatus(source.getId());
		twitter.timelineOperations().updateStatus(tweet);
	}

	private String getMessage(Demo result) {
		return "@" + this.source.getFromUser()
				+ " Your tweet is now a Spring Boot application! "
				+ "Click the link to learn more... " + result.getSourceCodeUrl();
	}

	@Override
	public void onFailure(Throwable ex) {
		logger.error("Failed to create demo application", ex);
	}

}
