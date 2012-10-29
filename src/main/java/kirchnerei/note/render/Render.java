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
package kirchnerei.note.render;

import kirchnerei.grundstein.NumberUtils;
import kirchnerei.grundstein.composite.CompositeBuilder;
import kirchnerei.grundstein.composite.CompositeInit;
import kirchnerei.grundstein.proxy.InvokeDirection;
import kirchnerei.grundstein.proxy.ProxyFilter;
import kirchnerei.grundstein.proxy.ProxyHandler;
import kirchnerei.grundstein.proxy.ProxyListHandler;
import kirchnerei.grundstein.proxy.filter.StringNotEmptyProxyFilter;
import kirchnerei.mapper.JsonMapper;
import kirchnerei.mapper.Mapper;
import kirchnerei.note.model.CategoryData;
import kirchnerei.note.model.NoteData;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The class <code>Render</code>
 *
 * @author Sarah Kirchner<br>Kirchnerei 2012
 */
public class Render implements CompositeInit {

	private final Map<String, ProxyFilter> form2Properties = new HashMap<String, ProxyFilter>();

	private final Map<String, ProxyFilter> view2Properties = new HashMap<String, ProxyFilter>();

	private JsonMapper mapper;

	public Render() {
		StringNotEmptyProxyFilter notEmpty = new StringNotEmptyProxyFilter("");
		IdNotNullProxyFilter idNotNull = new IdNotNullProxyFilter();
		BreakLineProxyFilter breakLine = new BreakLineProxyFilter();

		form2Properties.put("id", idNotNull);
		form2Properties.put("title", notEmpty);
		form2Properties.put("title", notEmpty);
		form2Properties.put("text", notEmpty);
		form2Properties.put("category", notEmpty);

		view2Properties.put("id", idNotNull);
		view2Properties.put("title", notEmpty);
		view2Properties.put("text", breakLine);
		view2Properties.put("category", notEmpty);
	}

	@Override
	public void init(CompositeBuilder builder) {
		mapper = builder.getSingleton(JsonMapper.class);
	}

	public NoteData createFormNote(NoteData note) {
		return ProxyHandler.createProxy(note, NoteData.class, form2Properties);
	}

	public List<NoteData> createNoteList(List<NoteData> list) {
		return ProxyListHandler.createProxy(list, NoteData.class, view2Properties);
	}

	public String createCategoryList(List<CategoryData> list) {
		List<String> items = new ArrayList<String>(list.size());
		for (CategoryData cd : list) {
			items.add(cd.getTitle());
		}
		return mapper.toString(items);
	}

	private static class IdNotNullProxyFilter implements ProxyFilter {

		@Override
		public Object invoke(InvokeDirection dir, Class<?> type, Object value) {
			if (value instanceof Long && NumberUtils.isEmpty((Long) value)) {
				return 0L;
			}
			return value;
		}
	}

	private static class BreakLineProxyFilter implements ProxyFilter {

		@Override
		public Object invoke(InvokeDirection dir, Class<?> type, Object value) {
			if (type == String.class && dir == InvokeDirection.READ) {
				String text = (String) value;
				if (StringUtils.isEmpty(text)) {
					return "";
				}
				return text.replace("\n", "<br>\n");
			}
			return value;
		}
	}
}
