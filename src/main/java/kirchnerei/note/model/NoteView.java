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

public class NoteView implements NoteData {

	private Long id;

	private String title;

	private String text;

	private String category;

	public NoteView() {
	}

	public NoteView(Long id, String title, String text) {
		this.id = id;
		this.title = title;
		this.text = text;
	}

	public NoteView(Long id, String title, String text, String category) {
		this.id = id;
		this.title = title;
		this.text = text;
		this.category = category;
	}

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

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("NoteView");
		sb.append("{id=").append(id);
		sb.append(", title='").append(title).append('\'');
		sb.append(", text='").append(text).append('\'');
		sb.append(", category='").append(category).append('\'');
		sb.append('}');
		return sb.toString();
	}
}
