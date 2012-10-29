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
import kirchnerei.grundstein.persistence.EntityService;
import kirchnerei.note.model.persistence.Category;
import kirchnerei.note.model.persistence.Note;

import org.junit.Test;

import javax.persistence.EntityManager;
import java.util.List;

public class DataServiceTest extends DataServiceBase {

	@Test
	public void testAddNewNoteInBlankTable() {
		Long noteId = addDataView(null, "Note 1", "Long text for note 1", "Aircraft");

		EntityManager em = entityService.get();
		Note note = em.find(Note.class, noteId);
		assertNotNull(note);
		assertEquals("Note 1", note.getTitle());
		assertEquals("Long text for note 1", note.getText());
		assertNotNull(note.getCategory());
		assertEquals("Aircraft", note.getCategory().getTitle());
		EntityService.close(em);
	}

	@Test
	public void testAddMoreNotesAndUpdateNote() {
		Long noteId1 = addDataView(null, "Note 1", "Long text for note 1", "Aircraft");
		Long noteId2 = addDataView(null, "Note 2", "Long text for note 2", "Summer");
		Long noteId3 = addDataView(null, "Note 3", "Long text for note 3", "Aircraft");
		assertNotNull(noteId1);
		assertFalse(NumberUtils.isEmpty(noteId1));
		assertNotNull(noteId2);
		assertFalse(NumberUtils.isEmpty(noteId2));
		assertNotNull(noteId3);
		assertFalse(NumberUtils.isEmpty(noteId3));

		EntityManager em = entityService.get();
		Note note2 = em.find(Note.class, noteId2);
		assertNotNull(note2);
		assertNotNull(note2.getCategory());
		assertEquals(1, note2.getCategory().getNotes().size());
		em.close();

		Long noteId4 = addDataView(noteId2, "Note 2/4", "Long text for Note 2", "Aircraft");
		assertNotNull(noteId4);
		assertFalse(NumberUtils.isEmpty(noteId4));
		assertTrue(NumberUtils.compare(noteId4, noteId2));
		em = entityService.get();
		Note note4 = em.find(Note.class, noteId4);
		assertNotNull(note4);
		assertEquals("Note 2/4", note4.getTitle());
		assertNotNull(note4.getCategory());
		assertEquals("Aircraft", note4.getCategory().getTitle());
		assertEquals(3, note4.getCategory().getNotes().size());
		em.close();
	}

	@Test
	public void testAddNoteWithoutCategory() {
		assertNull(addDataView(null, "Note 1", "Note without category", null));
		assertNull(addDataView(null, "Note 1", "Note without category", ""));
	}

	@Test
	public void testAddMoreNotesAndFindCategory() {
		addDataView(null, "Note 1", "Long text for note 1", "Aircraft");
		addDataView(null, "Note 2", "Long text for note 2", "Summer");
		addDataView(null, "Note 3", "Long text for note 3", "Aircraft");
		CategoryData categoryData = dataService.findCategory("Aircraft");
		assertNotNull(categoryData);
		assertTrue(!NumberUtils.isEmpty(categoryData.getId()));

		EntityManager em = entityService.get();
		Category cat = em.find(Category.class, categoryData.getId());
		assertNotNull(cat);
		assertNotNull(cat.getNotes());
		assertEquals(2, cat.getNotes().size());
		em.close();
	}

	@Test
	public void testFindCategory() {
		addDataView(null, "Note 1", "Long text for note 1", "Aircraft");
		CategoryData cat = dataService.findCategory("Aircraft");
		assertNotNull(cat);
		assertFalse(NumberUtils.isEmpty(cat.getId()));

		cat = dataService.findCategory("The the west");
		assertNotNull(cat);
		assertTrue(NumberUtils.isEmpty(cat.getId()));

		cat = dataService.findCategory(null);
		assertNotNull(cat);
		assertTrue(NumberUtils.isEmpty(cat.getId()));

		cat = dataService.findCategory("");
		assertNotNull(cat);
		assertTrue(NumberUtils.isEmpty(cat.getId()));
	}

	@Test
	public void testSelectNotes() {
		addDataView(null, "Note 1", "Long text for note 1", "Aircraft");
		addDataView(null, "Note 2", "Long text for note 2", "Summer");
		addDataView(null, "Note 3", "Long text for note 3", "Aircraft");

		List<NoteData> notes = dataService.getNoteList();
		assertNotNull(notes);
		assertEquals(3, notes.size());
		for (NoteData nd : notes) {
			assertNotNull(nd);
			assertFalse(NumberUtils.isEmpty(nd.getId()));
			assertNotNull(nd.getTitle());
			assertNotNull(nd.getText());
			assertNotNull(nd.getCategory());
		}
	}


	@Test
	public void testSelectEmptyNoteList() {
		List<NoteData> notes = dataService.getNoteList();
		assertNotNull(notes);
		assertTrue(notes.isEmpty());
	}

	@Test
	public void testRemoveNotes() {
		Long noteId1 = addDataView(null, "Note 1", "Long text for note 1", "Aircraft");
		Long noteId2 = addDataView(null, "Note 2", "Long text for note 2", "Summer");
		Long noteId3 = addDataView(null, "Note 3", "Long text for note 3", "Aircraft");

		assertFalse(dataService.removeNote(null));
		assertFalse(dataService.removeNote(-13L));
		assertFalse(dataService.removeNote(4711L));

		assertTrue(dataService.removeNote(noteId1));
		CategoryData cat = dataService.findCategory("Aircraft");
		assertNotNull(cat);
		assertFalse(NumberUtils.isEmpty(cat.getId()));
		assertTrue(dataService.removeNote(noteId3));

		cat = dataService.findCategory("Aircraft");
		assertNotNull(cat);
		assertTrue(NumberUtils.isEmpty(cat.getId()));
	}

	@Test
	public void testGetCategoryList() {
		List<CategoryData> list = dataService.getCategoryList();
		assertNotNull(list);
		assertTrue(list.isEmpty());

		addDataView(null, "Note 1", "Long text for note 1", "Aircraft");
		addDataView(null, "Note 2", "Long text for note 2", "Summer");
		addDataView(null, "Note 3", "Long text for note 3", "Aircraft");

		list = dataService.getCategoryList();
		assertNotNull(list);
		assertEquals(2, list.size());

		for (CategoryData data : list) {
			assertNotNull(data);
			assertFalse(NumberUtils.isEmpty(data.getId()));
			assertNotNull(data.getTitle());
		}
	}
}
