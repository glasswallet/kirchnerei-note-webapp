/*
 * Copyright (c) 2012 Kirchner
 * web:     http://www.kirchnerei.de
 * mail:    infos@kirchnerei.de
 * Project: Wimm-Online (github)
 */
package kirchnerei.note.page;

import kirchnerei.wimm.composite.CompositeBuilder;
import kirchnerei.wimm.db.CategoryDataSource;
import kirchnerei.wimm.db.NoteDataSource;
import kirchnerei.wimm.db.model.Category;
import kirchnerei.wimm.db.model.Note;
import kirchnerei.wimm.json.JsonService;
import kirchnerei.wimm.util.LogUtils;
import kirchnerei.wimm.util.NumberUtils;
import kirchnerei.wimm.view.NoteUI;
import kirchnerei.wimm.view.NoteView;
import kirchnerei.wimm.view.NoteViewFactory;
import org.apache.click.Context;
import org.apache.click.util.Bindable;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

/**
 * The class <code>IndexPage</code>
 *
 * @author Sarah Kirchner<br>Kirchnerei 2012
 */
public class IndexPage extends CommonPage {

	private static final Log log = LogFactory.getLog(IndexPage.class);

	private NoteDataSource noteSource;
	private CategoryDataSource categorySource;
	private NoteViewFactory noteViewFactory;
	private JsonService jsonService;

	@Bindable
	protected Long id;

	/**
	 * Describes the action calling be user.
	 */
	@Bindable
	protected String action;

	@Override
	public void init(CompositeBuilder cb) {
		noteSource = cb.getSingleton(NoteDataSource.class);
		categorySource = cb.getSingleton(CategoryDataSource.class);
		jsonService = cb.getSingleton(JsonService.class);
		noteViewFactory = cb.getSingleton(NoteViewFactory.class);
	}

	@Override
	public void onInit() {
		super.onInit();
		if (id == null || id < 0) {
			id = 0L;
		}
		if (StringUtils.isEmpty(action)) {
			action = HOME_ACTION;
		}
		LogUtils.logDebug(log, "call action=%s, id=%s", action, id);
	}

	@Override
	public void onGet() {
		if (isRemoveAction(action)) {
			noteSource.remove(id);
			setRedirect("/home.html");
			return;
		}
		addModel("notelist", noteViewFactory.getAllNotes());
		NoteUI note = noteViewFactory.getNoteForEdit(id);
		String actionUrl = null;
		String editFormTitle = null;
		if (!NumberUtils.isEmpty(id)) {
			actionUrl = String.format("/edit/%s.html", id);
			editFormTitle = getMessage("editFormEdit");
		} else {
			actionUrl = "/new.html";
			editFormTitle = getMessage("editFormNew");
		}
		addModel("note", note);
		addModel("actionUrl", actionUrl);
		addModel("editFormTitle", editFormTitle);
		// categorySources laden
		List<String> list = categorySource.getAllCategoryTitles();
		String categorySource = jsonService.write(list);
		LogUtils.logDebug(log, "1. category sources: '%s'", categorySource);
		addModel("categorySource", categorySource);
	}

	@Override
	public void onPost() {
		if (!isEditAction(action)) {
			return;
		}
		Context ctx = getContext();
		id = noteViewFactory.saveNote(id, ctx.getRequestParameter("category"),
			ctx.getRequestParameter("title"), ctx.getRequestParameter("text"));
		setRedirect(String.format("/home/%s.html", id));
	}
}
