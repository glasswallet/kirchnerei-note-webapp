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
package kirchnerei.note.page;

import kirchnerei.grundstein.LogUtils;
import kirchnerei.grundstein.NumberUtils;
import kirchnerei.grundstein.click.service.RequestBeanService;
import kirchnerei.grundstein.composite.CompositeBuilder;
import kirchnerei.note.model.CategoryData;
import kirchnerei.note.model.DataService;
import kirchnerei.note.model.NoteData;
import kirchnerei.note.model.NoteView;
import kirchnerei.note.render.Render;
import org.apache.click.Context;
import org.apache.click.util.Bindable;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Arrays;
import java.util.List;

public class IndexPage extends CommonPage {

	private static final Log log = LogFactory.getLog(IndexPage.class);

	private static final List<String> noteViewProperties = Arrays.asList("id", "title", "text", "category");

	private DataService dataService;
	private Render render;
	private RequestBeanService requestBean;

	@Bindable
	protected Long id;

	/**
	 * Describes the action calling be user.
	 */
	@Bindable
	protected String action;

	@Override
	public void init(CompositeBuilder builder) {
		dataService = builder.getSingleton(DataService.class);
		render = builder.getSingleton(Render.class);
		requestBean = builder.getSingleton(RequestBeanService.class);
	}

	@Override
	public void onInit() {
		super.onInit();
		if (NumberUtils.isEmpty(id)) {
			id = 0L;
		}
		if (StringUtils.isEmpty(action)) {
			action = HOME_ACTION;
		}
		LogUtils.debug(log, "IndexPage: call action=%s, id=%s", action, id);
	}

	@Override
	public void onGet() {
		if (isRemoveAction(action)) {
			dataService.removeNote(id);
			setRedirect("/home.html");
			return;
		}
		List<NoteData> noteList = dataService.getNoteList();
		addModel("notelist", render.createNoteList(noteList));
		NoteData note = dataService.getNoteById(id);
		String actionUrl;
		String editFormTitle;
		if (!NumberUtils.isEmpty(id)) {
			actionUrl = String.format("edit/%s.html", id);
			editFormTitle = getMessage("editFormEdit");
		} else {
			actionUrl = "new.html";
			editFormTitle = getMessage("editFormNew");
		}
		addModel("note", render.createFormNote(note));
		addModel("actionUrl", actionUrl);
		addModel("editFormTitle", editFormTitle);
		// categorySources laden
		List<CategoryData> categoryDataList = dataService.getCategoryList();
		addModel("categorySource", render.createCategoryList(categoryDataList));
	}

	@Override
	public void onPost() {
		if (!isEditAction(action)) {
			return;
		}
		Context ctx = getContext();
		NoteView noteView = requestBean.read(ctx, NoteView.class, noteViewProperties);
		id = dataService.storeNote(noteView);
		setRedirect(String.format("/home/%s.html", id));
	}
}
