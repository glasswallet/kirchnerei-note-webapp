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
package kirchnerei.note.model;

import static org.junit.Assert.*;

import kirchnerei.grundstein.NumberUtils;
import kirchnerei.grundstein.composite.CompositeBuilder;
import kirchnerei.grundstein.persistence.EntityService;
import kirchnerei.note.SetupConfig;
import org.junit.After;
import org.junit.Before;

public class DataServiceBase {

	protected CompositeBuilder builder;

	protected DataService dataService;

	protected EntityService entityService;

	@Before
	public void setUp() {
		builder = new CompositeBuilder();
		builder.setup(SetupConfig.class);

		dataService = builder.getSingleton(DataService.class);
		entityService = builder.getSingleton(EntityService.class);
	}

	@After
	public void tearDown() {
		entityService = null;
		dataService = null;
		builder.destroy();
		builder = null;
	}

	public Long addDataView(Long id, String title, String text, String category) {
		NoteView noteView = new NoteView();
		if (id != null) {
			noteView.setId(id);
		}
		noteView.setTitle(title);
		noteView.setText(text);
		noteView.setCategory(category);
		Long noteId = dataService.storeNote(noteView);
		return noteId;
	}
}
