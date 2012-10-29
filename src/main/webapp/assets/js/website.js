/*!
 * Copyright (c) 2012 Kirchner
 * web:     http://www.kirchnerei.de
 * mail:    infos@kirchnerei.de
 * Project: Wimm-Online (github)
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

