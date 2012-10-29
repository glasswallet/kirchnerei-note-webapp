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

import kirchnerei.grundstein.LogUtils;
import kirchnerei.grundstein.NumberUtils;
import kirchnerei.grundstein.bean.BeanCopy;
import kirchnerei.grundstein.composite.CompositeBuilder;
import kirchnerei.grundstein.composite.CompositeInit;
import kirchnerei.grundstein.persistence.EntityService;
import kirchnerei.note.model.persistence.Category;
import kirchnerei.note.model.persistence.Note;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DataService implements CompositeInit {

	private static final Log log = LogFactory.getLog(DataService.class);

	/**
	 * A list of property names for copy values between Note and NoteData.
	 */
	private static final List<String> noteProperties = Arrays.asList("id", "title", "text");

	private static final List<String> categoryProperties = Arrays.asList("id", "title");

	private EntityService entityService;
	private BeanCopy beanCopy;

	@Override
	public void init(CompositeBuilder builder) {
		entityService = builder.getSingleton(EntityService.class);
		beanCopy = builder.getSingleton(BeanCopy.class);
	}

	public Long storeNote(NoteData noteData) {
		EntityManager em = null;
		EntityTransaction tx = null;
		try {
			em = entityService.get();
			tx = em.getTransaction();
			tx.begin();
			// search for category
			String category = noteData.getCategory();
			if (StringUtils.isEmpty(category)) {
				LogUtils.warn(log, "note without category is not allow [%s]", noteData);
				return null;
			}
			TypedQuery<Category> q = em.createNamedQuery("findCategory", Category.class);
			q.setParameter("category", category);
			Category cat = getSingleResult(q);
			if (cat == null) {
				// a new category
				cat = new Category();
				cat.setTitle(category);
				em.persist(cat);
			}
			final Note note;
			if (NumberUtils.isEmpty(noteData.getId())) {
				// create a new note
				note = beanCopy.copy(noteData, Note.class, noteProperties);
				note.setCategory(cat);
				cat.getNotes().add(note);
			} else {
				// update an existed note
				note = em.find(Note.class, noteData.getId());
				beanCopy.copy(noteData, note, noteProperties);
				if (!NumberUtils.compare(note.getCategory().getId(), cat.getId()))
				{
					// move to other category
					note.getCategory().getNotes().remove(note);
					cat.getNotes().add(note);
					note.setCategory(cat);
				}
			}
			EntityService.commit(tx);
			return note.getId();
		} catch (Exception e) {
			EntityService.rollback(tx);
			return null;
		} finally {
			EntityService.close(em);
		}
	}

	public boolean removeNote(Long id) {
		if (NumberUtils.isEmpty(id)) {
			return false;
		}
		EntityManager em = null;
		EntityTransaction tx = null;
		try {
			em = entityService.get();
			tx = em.getTransaction();
			tx.begin();
			Note note = em.find(Note.class, id);
			if (note == null) {
				return false;
			}
			Category category = note.getCategory();
			category.getNotes().remove(note);
			note.setCategory(null);
			em.remove(note);
			if (category.getNotes().isEmpty()) {
				LogUtils.debug(log, "empty category '%s', then remove it", category.getTitle());
				em.remove(category);
			}
			EntityService.commit(tx);
			return true;
		} catch (Exception e) {
			EntityService.rollback(tx);
			return false;
		} finally {
			EntityService.close(em);
		}
	}

	public CategoryData findCategory(String category) {
		EntityManager em = null;
		try {
			em = entityService.get();
			TypedQuery<Category> q = em.createNamedQuery("findCategory", Category.class);
			q.setParameter("category", category);
			Category cat = getSingleResult(q);
			if (cat == null) {
				return new CategoryView();
			} else {
				return new CategoryView(cat.getId(), cat.getTitle());
			}
		} finally {
			EntityService.close(em);
		}
	}

	public List<NoteData> getNoteList() {
		EntityManager em = null;
		try {
			em = entityService.get();
			TypedQuery<Note> q = em.createNamedQuery("selectAll", Note.class);
			List<Note> notes = getResultList(q);
			if (notes == null || notes.isEmpty()) {
				return Collections.emptyList();
			}
			int initSize = notes.size();
			List<NoteData> noteList = new ArrayList<NoteData>(initSize);
			for (Note note : notes) {
				NoteView noteData = beanCopy.copy(note, NoteView.class, noteProperties);
				if (note.getCategory() != null) {
					noteData.setCategory(note.getCategory().getTitle());
				}
				noteList.add(noteData);
			}
			return noteList;
		} finally {
			EntityService.close(em);
		}
	}

	public List<CategoryData> getCategoryList() {
		EntityManager em = null;
		try {
			em = entityService.get();
			TypedQuery<Category> q = em.createNamedQuery("allCategories", Category.class);
			List<Category> list = getResultList(q);
			if (list == null || list.isEmpty()) {
				return Collections.emptyList();
			}
			int initSize = list.size();
			List<CategoryData> categoryList = new ArrayList<CategoryData>(initSize);
			for (Category category : list) {
				CategoryView categoryData = beanCopy.copy(category, CategoryView.class,
					categoryProperties);
				categoryList.add(categoryData);
			}
			return categoryList;
		} finally {
			EntityService.close(em);
		}
	}


	private <T> T getSingleResult(TypedQuery<T> q) {
		try {
			return q.getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}

	private <T> List<T> getResultList(TypedQuery<T> q) {
		try {
			return q.getResultList();
		} catch (Exception e) {
			return Collections.emptyList();
		}
	}
}
