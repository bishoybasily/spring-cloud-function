/*
 * Copyright 2016-2017 the original author or authors.
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
package org.springframework.cloud.function.web.util;

import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Dave Syer
 *
 */
@RunWith(Parameterized.class)
public class MapperTests {

	private JsonMapper mapper;

	@Parameters
	public static List<Object[]> params() {
		return Arrays.asList(new Object[] { new GsonMapper(new Gson()) },
				new Object[] { new JacksonMapper(new ObjectMapper()) });
	}

	public MapperTests(JsonMapper mapper) {
		this.mapper = mapper;
	}

	@Test
	public void vanillaArray() {
		List<Foo> list = mapper.toList("[{\"value\":\"foo\"}, {\"value\":\"foo\"}]",
				Foo.class);
		assertThat(list).hasSize(2);
		assertThat(list.get(0).getValue()).isEqualTo("foo");
	}

	@Test
	public void intArray() {
		List<Integer> list = mapper.toList("[123,456]", Integer.class);
		assertThat(list).hasSize(2);
		assertThat(list.get(0)).isEqualTo(123);
	}

	@Test
	public void emptyArray() {
		List<Foo> list = mapper.toList("[]", Foo.class);
		assertThat(list).hasSize(0);
	}

	@Test
	public void vanillaObject() {
		Foo foo = mapper.toSingle("{\"value\":\"foo\"}", Foo.class);
		assertThat(foo.getValue()).isEqualTo("foo");
	}

	@Test
	public void intValue() {
		int foo = mapper.toSingle("123", Integer.class);
		assertThat(foo).isEqualTo(123);
	}

	@Test
	public void empty() {
		Foo foo = mapper.toSingle("{}", Foo.class);
		assertThat(foo.getValue()).isNull();
	}

	public static class Foo {
		private String value;

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

	}
}
