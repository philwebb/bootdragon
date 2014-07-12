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

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import org.junit.Test;
import org.springframework.social.twitter.api.Tweet;

import bootdragon.twitter.TweetDemoRequest;

/**
 * Tests for {@link TweetDemoRequest}.
 *
 * @author Phillip Webb
 */
public class TweetRequestTests {

	@Test
	public void removeMentions() {
		assertThat(getMessage("@BootDemo Hello World!"),
				equalTo("Hello World!"));
		assertThat(getMessage("@BootDemo @phillip_webb Hello World!"),
				equalTo("Hello World!"));
		assertThat(getMessage("Hello @BootDemo @phillip_webb World!"),
				equalTo("Hello World!"));
		assertThat(getMessage("Hello @BootDemo @phillip_webb World! @atend"),
				equalTo("Hello World!"));
	}

	private String getMessage(String text) {
		return new TweetDemoRequest(tweet(text), "http://localhost").getMessage();
	}

	private Tweet tweet(String text) {
		Tweet tweet = mock(Tweet.class);
		given(tweet.getUnmodifiedText()).willReturn(text);
		return tweet;
	}

}
