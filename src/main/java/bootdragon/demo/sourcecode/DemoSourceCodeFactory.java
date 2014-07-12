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

package bootdragon.demo.sourcecode;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertyResolver;
import org.springframework.core.env.PropertySources;
import org.springframework.core.env.PropertySourcesPropertyResolver;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import bootdragon.demo.DemoRequest;

/**
 * Factory to create a {@link DemoSourceCode} customized for a particular user.
 *
 * @author Phillip Webb
 */
@Component
public class DemoSourceCodeFactory {

	private final String template;

	public DemoSourceCodeFactory() {
		try {
			InputStream stream = DemoSourceCodeFactory.class
					.getResourceAsStream("app-template.groovy");
			this.template = FileCopyUtils.copyToString(new InputStreamReader(stream));
		}
		catch (IOException ex) {
			throw new IllegalStateException(ex);
		}
	}

	public DemoSourceCode createSourceCode(DemoRequest request) {
		PropertySources propertySources = extractVars(request);
		PropertyResolver resolver = new PropertySourcesPropertyResolver(propertySources);
		return new DemoSourceCode(resolver.resolvePlaceholders(template));
	}

	private PropertySources extractVars(DemoRequest request) {
		Map<String, Object> vars = new HashMap<>();
		vars.put("user", request.getUser());
		vars.put("source", request.getSource());
		vars.put("quotedmessage", quoteAndEscape(request.getMessage()));
		MutablePropertySources propertySources = new MutablePropertySources();
		propertySources.addFirst(new MapPropertySource("vars", vars));
		return propertySources;
	}

	private String quoteAndEscape(String message) {
		return "\"" + StringEscapeUtils.escapeJava(message) + "\"";
	}

}
