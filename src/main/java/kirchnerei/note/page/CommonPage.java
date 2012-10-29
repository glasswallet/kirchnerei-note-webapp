/*
 * Copyright (c) 2012 Kirchner
 * web:     http://www.kirchnerei.de
 * mail:    infos@kirchnerei.de
 * Project: Wimm-Online (github)
 */
package kirchnerei.note.page;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import kirchnerei.wimm.composite.CompositeInit;
import kirchnerei.wimm.util.LogUtils;
import org.apache.click.Context;
import org.apache.click.Page;
import org.apache.click.element.CssImport;
import org.apache.click.element.Element;
import org.apache.click.element.JsImport;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

/**
 * The class <code>CommonPage</code>
 *
 * @author Sarah Kirchner<br>Kirchnerei 2012
 */
public abstract class CommonPage extends Page implements CompositeInit {

	public static final String HOME_ACTION = "home";

	public static final String EDIT_ACTION = "edit";

	public static final String REMOVE_ACTION = "remove";

	private static final Log log = LogFactory.getLog(CommonPage.class);

/*-
	@Override
	public boolean onSecurityCheck() {
		UserService userService = UserServiceFactory.getUserService();
		boolean isLogged = userService.isUserLoggedIn();
		LogUtils.logDebug(log, "user is logged: %s", isLogged ? "yes" : "no");
		if (!isLogged) {
			Context ctx = getContext();
			String redirectUrl = "";
			LogUtils.logDebug(log, "login and redirect to %s", redirectUrl);
			String urlLogin = userService.createLoginURL(redirectUrl);
			setRedirect(urlLogin);
		}
		return isLogged;
	}
-*/

	@Override
	public List<Element> getHeadElements() {
		if (headElements == headElements) {
			headElements = super.getHeadElements();
			headElements.add(new CssImport("/assets/css/bootstrap.css"));
			headElements.add(new CssImport("/assets/css/font-awesome.css"));
			CssImport ie7 = new CssImport("/assets/css/font-awesome-ie7.css");
			ie7.setConditionalComment(CssImport.IF_IE7);
			headElements.add(ie7);
			headElements.add(new CssImport("/assets/css/website.css"));

			headElements.add(new JsImport("/assets/js/jquery.js"));
			headElements.add(new JsImport("/assets/js/jquery-ui-sortable.js"));
			headElements.add(new JsImport("/assets/js/bootstrap.js"));
			headElements.add(new JsImport("/assets/js/website.js"));

		}
		return headElements;
	}

	@Override
	public String getTemplate() {
		return "/assets/templates/commonPage.html";
	}

	protected static boolean isHomeAction(String action) {
		return HOME_ACTION.equalsIgnoreCase(action);
	}

	protected static boolean isEditAction(String action) {
		return EDIT_ACTION.equalsIgnoreCase(action);
	}

	protected static boolean isRemoveAction(String action) {
		return REMOVE_ACTION.equalsIgnoreCase(action);
	}
}
