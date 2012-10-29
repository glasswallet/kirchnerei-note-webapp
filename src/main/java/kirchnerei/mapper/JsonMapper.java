/*
 * Copyright 2012 Kirchnerei
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package kirchnerei.mapper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import kirchnerei.grundstein.composite.CompositeBuilder;
import kirchnerei.grundstein.composite.CompositeInit;

public class JsonMapper extends Mapper implements CompositeInit {

	private ObjectMapper mapper;

	@Override
	public void init(CompositeBuilder builder) {
		mapper = new ObjectMapper();
	}

	@Override
	public String toString(Object value) {
		try {
			return mapper.writeValueAsString(value);
		} catch (Exception e) {
			throw new MapperException(e, "mapping '%s' into json is failed",
				value == null ? "null" : value.getClass().getSimpleName());
		}
	}

	@Override
	public <T> T toObject(String test, Class<T> type) {
		try {
			return mapper.readValue(test, type);
		} catch (Exception e) {
			throw new MapperException(e, "mapping from json into class is failed");
		}
	}

	public <T> T toObject(String text, TypeReference<T> ref) {
		try {
			return mapper.readValue(text, ref);
		} catch (Exception e) {
			throw new MapperException(e, "mapping from json into class is failed");
		}
	}
}
