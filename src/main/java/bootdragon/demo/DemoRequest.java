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

/**
 * A request to create a demo application.
 *
 * @author Phillip Webb
 */
public interface DemoRequest {

	/**
	 * @return the user that made the request.
	 */
	String getUser();

	/**
	 * @return the message that should be used to make the application.
	 */
	String getMessage();

	/**
	 * @return the source of the request as a URL
	 */
	String getSource();

}
