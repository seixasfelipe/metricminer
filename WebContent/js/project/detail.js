function sendTag(tag, type) {
	var id = $('#projectId').val();
	var postUrl = CONTEXT_ROOT + '/projects/' + id + '/tags/' + type; 
	$.post(postUrl, { tagName : tag });
}

function addTag(tag) {
	sendTag(tag, '');
}

function removeTag(tag) {
	sendTag(tag, 'remove');
}

$(document).ready(function() {
	
	$('#tags').tagsInput({
		width:'auto',
		'onAddTag':addTag,
		'onRemoveTag':removeTag,
	});
});