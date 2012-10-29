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

import kirchnerei.note.model.NoteData;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@NamedQueries({
	@NamedQuery(name = "selectAll", query = "SELECT n FROM Note n")
})
public class Note implements Serializable {

	private static final long serialVersionUID = 1565400773404824213L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(length = 60, nullable = false)
	private String title;

	@Column(length = 500, nullable = true)
	private String text;

	@ManyToOne(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
	@JoinColumn(columnDefinition = "category_id", nullable = false)
	private Category category;

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

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("Note");
		sb.append("{id=").append(id);
		sb.append(", title='").append(title).append('\'');
		sb.append(", text='").append(text).append('\'');
		sb.append('}');
		return sb.toString();
	}
}
