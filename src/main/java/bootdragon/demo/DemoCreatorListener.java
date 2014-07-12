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
 * Listener for events triggered from {@link DemoCreator} as a {@link Demo} is created.
 * mainly intended for UI updates.
 *
 * @author Phillip Webb
 */
public interface DemoCreatorListener {

	/**
	 * Called when a new {@link DemoRequest} starts.
	 * @param request the request.
	 */
	void start(DemoRequest request);

	/**
	 * Called then a {@link Demo} has been created
	 * @param demo the demo
	 */
	void finish(Demo demo);

	/**
	 * Called on error
	 * @param ex the cause
	 */
	void error(Exception ex);

}
