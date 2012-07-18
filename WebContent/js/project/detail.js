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
			"metricFactoryClassName" : metricClass
		},
		success : function() {
		},
		error : function() {
			alert("Could not add metric");
		}
	});
}

function drawCommitChart() {
	var id = $('#projectId').val();
	$.ajax({
		type : 'GET',
		url : CONTEXT_ROOT + '/projects/' + id + '/commitChartData',
		dataType : 'json',
		success : function(commitData) {
			var data = google.visualization.arrayToDataTable(commitData);

			var options = {
				title : 'Commits of the last twelve months'
			};

			var chart = new google.visualization.LineChart(document
					.getElementById('commit_chart'));
			chart.draw(data, options);

		},
		error : function() {
			alert("Could load chart data");
		}
	});
}

function drawFileCountChart() {
	var id = $('#projectId').val();
	$.ajax({
		type : 'GET',
		url : CONTEXT_ROOT + '/projects/' + id + '/fileCountChartData',
		dataType : 'json',
		success : function(fileCountData) {
			var data = google.visualization
					.arrayToDataTable(fileCountData);
	
			var options = {
				title : 'Number of modified files per commit for the last six months'
			};
	
			var chart = new google.visualization.LineChart(document
					.getElementById('fileCount_chart'));
			chart.draw(data, options);
	
		},
		error : function() {
			alert("Could load chart data");
		}
	});
}

function drawCharts() {
	drawCommitChart();
	drawFileCountChart();
}

$(document).ready(function() {
	google.load("visualization", "1", {
		packages : [ "corechart" ]
	});
	google.setOnLoadCallback(drawCharts);
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