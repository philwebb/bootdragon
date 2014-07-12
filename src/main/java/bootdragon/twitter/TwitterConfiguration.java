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

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.social.twitter.api.impl.TwitterTemplate;

/**
 * Spring configuration for Twitter.
 *
 * @author Phillip Webb
 */
@Configuration
public class TwitterConfiguration {

	@Bean
	public TwitterTemplate twitter(Environment env) {
		try {
			return new TwitterTemplate(
					env.getRequiredProperty("twitter-consumer-key"),
					env.getRequiredProperty("twitter-consumer-secret"),
					env.getRequiredProperty("twitter-access-token"),
					env.getRequiredProperty("twitter-access-token-secret"));
		} catch (IllegalStateException ex) {
			throw new IllegalStateException("Unable to configure twitter. "
					+ "Check that your application-secrets.properties "
					+ "file is configured", ex);
		}
	}

}
