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

import java.util.Collections;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.twitter.api.StreamDeleteEvent;
import org.springframework.social.twitter.api.StreamListener;
import org.springframework.social.twitter.api.StreamWarningEvent;
import org.springframework.social.twitter.api.TimelineOperations;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.UserStreamParameters;
import org.springframework.social.twitter.api.UserStreamParameters.WithOptions;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

import bootdragon.demo.Demo;
import bootdragon.demo.DemoCreator;

/**
 * {@link StreamListener} to listen to mentions and start interactions from twitter.
 */
@Component
public class TwitterIteractionListener implements StreamListener {

	private static Logger logger = LoggerFactory
			.getLogger(TwitterIteractionListener.class);

	private final Twitter twitter;

	private final DemoCreator demoApplicationCreator;

	private long userId;

	@Autowired
	public TwitterIteractionListener(Twitter twitter, DemoCreator demoApplicationCreator) {
		this.twitter = twitter;
		this.demoApplicationCreator = demoApplicationCreator;
	}

	@PostConstruct
	void registerMentionListener() {
		this.userId = this.twitter.userOperations().getUserProfile().getId();
		twitter.streamingOperations().user(
				new UserStreamParameters().with(WithOptions.USER),
				Collections.singletonList(this));
	}

	@Override
	public void onTweet(Tweet tweet) {
		logger.info("Recieved tweet from {} with message {} addressed to ID {}",
				tweet.getFromUser(), tweet.getUnmodifiedText(), tweet.getToUserId());
		if (tweet.getToUserId() == this.userId) {
			TimelineOperations timeline = this.twitter.timelineOperations();
			String url = timeline.getStatusOEmbed(tweet.getId()).getUrl();
			ListenableFuture<Demo> future = this.demoApplicationCreator
					.addRequest(new TweetDemoRequest(tweet, url));
			future.addCallback(new TwitterReplyCallback(twitter, tweet));
		}
	}

	@Override
	public void onDelete(StreamDeleteEvent deleteEvent) {
	}

	@Override
	public void onLimit(int numberOfLimitedTweets) {
		logger.info("Tweets limited to {}", numberOfLimitedTweets);
	}

	@Override
	public void onWarning(StreamWarningEvent warningEvent) {
		logger.warn("Twitter warning: {}", warningEvent.getMessage());
	}

}
