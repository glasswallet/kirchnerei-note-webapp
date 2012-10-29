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
package kirchnerei.note.webapp;

import kirchnerei.grundstein.composite.CompositeException;
import kirchnerei.grundstein.persistence.EntityService;

public class NoteSetup {


	public static String getParam(String name) {
		String value = System.getenv(name);
		if (value == null) {
			value = System.getProperty(name);
		}
		if (value == null) {
			throw new CompositeException(
				"parameter '%s' is not present in system env oder system properties", name);
		}
		return value;
	}

	/**
	 * Configure the EntityService with the persistence unit name.
	 */
	public static class entityService extends EntityService {
		{
			setName("note-app");

			String driver = getParam("NOTE_DRIVER");
			String url = getParam("NOTE_URL");
			String user = getParam("NOTE_USER");
			String password = getParam("NOTE_PASSWORD");
			setUpPersistence(driver, url, user, password);
		}
	}
}
