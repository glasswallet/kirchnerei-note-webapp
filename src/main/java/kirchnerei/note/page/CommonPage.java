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

import kirchnerei.grundstein.composite.CompositeInit;
import org.apache.click.Page;
import org.apache.click.element.CssImport;
import org.apache.click.element.Element;
import org.apache.click.element.JsImport;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

public abstract class CommonPage extends Page implements CompositeInit {

	public static final String HOME_ACTION = "home";

	public static final String EDIT_ACTION = "edit";

	public static final String REMOVE_ACTION = "remove";

	private static final Log log = LogFactory.getLog(CommonPage.class);

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
