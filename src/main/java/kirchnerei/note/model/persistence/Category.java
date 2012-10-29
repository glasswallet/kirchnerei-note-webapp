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
package kirchnerei.note.model.persistence;

import kirchnerei.note.model.CategoryData;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@NamedQueries({
	@NamedQuery(name = "findCategory", query = "SELECT c FROM Category c WHERE c.title = :category"),
	@NamedQuery(name = "allCategories", query = "SELECT c FROM Category c ORDER BY c.title")
})
public class Category implements Serializable {

	private static final long serialVersionUID = -1322362663896642568L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(length = 60, nullable = false)
	private String title;

	@OneToMany(mappedBy = "category", cascade = {CascadeType.ALL})
	private List<Note> notes;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<Note> getNotes() {
		if (notes == null) {
			notes = new ArrayList<Note>();
		}
		return notes;
	}

	public void setNotes(List<Note> notes) {
		this.notes = notes;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("Category");
		sb.append("{id=").append(id);
		sb.append(", title='").append(title).append('\'');
		sb.append('}');
		return sb.toString();
	}
}
