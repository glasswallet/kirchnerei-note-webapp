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


var website = window.website = {};

(function (window, $) {

	var website = window.website || {};

	var __contextPath = '';

	var __config = {};

	website.initApplication = function (contextPath, actionListAsJsonString, config) {
		__contextPath = contextPath;
		for (var name in config) {
			var value = config[name];
			__config[name] = value;
		}
		this.executeActionList(actionListAsJsonString);
		this.initSortableNote();
		this.initEditorForm();
	};

	website.executeActionList = function (actionListAsJsonString) {
		var jsonArray = JSON.parse(actionListAsJsonString);
		for (var i in jsonArray) {
			var action = jsonArray[i];
			console.debug(action);
		}
	};

	website.initSortableNote = function () {
		var stopFunc = function (ev, ui) {
			console.debug('Stop sortable');
			var orderList = [];
			$('#note-table tbody tr').each(function () {
				orderList.push($(this).attr('id'));
			});
			var orderBy = JSON.stringify(orderList);
			console.debug('orderList: ', orderBy);
			$.post('/service/noteOrderBy', { 'orderBy': orderBy}, function (json) {
				console.debug('Receive: ', json);
				if (json.status && json.noteList && json.status == 'okay') {
					for(var i in json.noteList) {
						var note = json.noteList[i];
						console.debug('note: ', note.id, ', title=', note.title, 'orderBy=', note.orderBy);
					}
				}
			});
		}
		$('#note-table tbody').sortable({
			handle: 'td:first',
			items: 'tr',
			start: function() { console.debug('Start sortable'); },
			stop: stopFunc
		}).disableSelection();
	};

	website.initEditorForm = function () {
		console.debug("typeahead??")
	};

} (window, jQuery));

