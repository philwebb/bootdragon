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

package bootdragon.github;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.github.api.GitHubFile;
import org.springframework.social.github.api.GitHubGist;
import org.springframework.social.github.api.impl.GitHubTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Service to create GitHub Gists.
 *
 * @author Phillip Webb
 */
@Component
public class GitHubGistCreator {

	private static final String URL = "https://api.github.com/gists";

	private final RestTemplate restTemplate;

	@Autowired
	public GitHubGistCreator(GitHubTemplate gitHub) {
		this.restTemplate = gitHub.getRestTemplate();
	}

	public GitHubGist createGist(GitHubFile... files) {
		GitHubGist gist = new GitHubGist();
		Map<String, GitHubFile> fileMap = new LinkedHashMap<>();
		for (GitHubFile file : files) {
			fileMap.put(file.getFilename(), file);
		}
		gist.setFiles(fileMap);
		return restTemplate.postForObject(URL, gist, GitHubGist.class);
	}

}
