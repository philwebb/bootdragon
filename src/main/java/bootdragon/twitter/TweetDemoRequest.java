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

import java.util.regex.Pattern;

import org.springframework.social.twitter.api.Tweet;

import bootdragon.demo.DemoRequest;

/**
 * A {@link DemoRequest} originating from a {@link Tweet}.
 *
 * @author Phillip Webb
 */
class TweetDemoRequest implements DemoRequest {

	private static final Pattern MENTIONS = Pattern.compile("@\\S+(\\s|$)");

	private final Tweet tweet;

	private final String message;

	private String url;

	public TweetDemoRequest(Tweet tweet, String url) {
		this.tweet = tweet;
		this.message = removeMentions(tweet.getUnmodifiedText());
		this.url = url;
	}

	private String removeMentions(String text) {
		return MENTIONS.matcher(text).replaceAll("").trim();
	}

	@Override
	public String getMessage() {
		return this.message;
	}

	@Override
	public String getUser() {
		return "@" + this.tweet.getFromUser();
	}

	@Override
	public String getSource() {
		return this.url;
	}

}
