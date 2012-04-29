function sendTag(tag, type) {
	var id = $('#projectId').val();
	var postUrl = CONTEXT_ROOT + '/projects/' + id + '/tags/' + type;
	$.post(postUrl, {
		tagName : tag
	});
}

function addTag(tag) {
	sendTag(tag, '');
}

function removeTag(tag) {
	sendTag(tag, 'remove');
}

function sendMetric(metricClass) {
	var id = $('#projectId').val();
	var postUrl = CONTEXT_ROOT + '/projects/' + id + '/metrics';
	$.ajax({
		type : 'POST',
		url : postUrl,
		data : {
			"metric.metricFactoryClass" : metricClass
		},
		success : function() {
		},
		error : function() {
			alert("Could not add metric");
		}
	});
}

$(document).ready(function() {
	$('#tags').tagsInput({
		width : 'auto',
		'onAddTag' : addTag,
		'onRemoveTag' : removeTag,
	});

	$("button").click(function() {
		sendMetric($(this).attr("metric-class"));
		$(this).parent().parent().fadeOut();
	});

});