<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<c:import url="../import/head.jsp" />
<style type="text/css">
#commit_chart {
	width: 850px;
	height: 350px;
	position: relative;
	float: right;
}
</style>
<script type="text/javascript" src="https://www.google.com/jsapi"></script>
<script type="text/javascript">
      google.load("visualization", "1", {packages:["corechart"]});
      google.setOnLoadCallback(drawChart);
      function drawChart() {
        var data = google.visualization.arrayToDataTable([["Month", "count"]
			<c:forEach items="${lastSixMonthsCommitCountMap}" var="entry">
		    ,["<fmt:formatDate value="${entry.key.time}" pattern="yyyy/MM/dd"/>",
		    ${entry.value}]
			</c:forEach>
		]);

        var options = {
          title: 'Commits of the last six months'
        };

        var chart = new google.visualization.LineChart(document.getElementById('commit_chart'));
        chart.draw(data, options);
      }
    </script>

<title>Metric Miner</title>
</head>

<body>
	<div id="hld">
		<div class="wrapper">
			<!-- wrapper begins -->
			<c:import url="../import/header.jsp" />
			<div class="block">
				<div class="block_head">
					<div class="bheadl"></div>
					<div class="bheadr"></div>
					<h2>${project.name}</h2>
					<ul class="tabs">
						<li><a href="<c:url value="/projects/${project.id}/delete" />">Delete project</a></li>
					</ul>
				</div>
				<!-- .block_head ends -->

				<div class="block_content">

					<table id="details">
						<tr>
							<td>Name</td>
							<td>${project.name} <input type="hidden" name="id"
								id="projectId" value="${project.id}" /></td>
						</tr>

						<tr>
							<td>Repo's path</td>
							<td>${project.scmUrl}</td>
						</tr>
						
						<tr>
							<td>Total commits</td>
							<td>${commitCount}</td>
						</tr>
						
						<tr>
							<td>Total commiters</td>
							<td>${commiterCount}</td>
						</tr>
						
						<tr>
							<td>First commit</td>
							<td><fmt:formatDate value="${firstCommit.date.time}" pattern="yyyy/MM/dd"/></td>
						</tr>
						
						<tr>
							<td>Last commit</td>
							<td><fmt:formatDate value="${lastCommit.date.time}" pattern="yyyy/MM/dd"/></td>
						</tr>

						<tr>
							<td>Tags</td>
							<td><input type="text" class="tags" name="tags" id="tags"
								value="${tags}" style="display: none;" /></td>
						</tr>
						
					    <div id="commit_chart"></div>
						
					</table>
				</div>
				<div class="bendl"></div>
				<div class="bendr"></div>
			</div>

			<div class="block small left">
				<div class="block_head">
					<div class="bheadl"></div>
					<div class="bheadr"></div>
					<h2>Avaiable metrics to calculate</h2>
				</div>
				<!-- .block_head ends -->
				<div class="block_content">
					<table>
						<c:forEach items="${avaiableMetrics}" var="metric">
							<tr>
								<td>${metric.name}</td>
								<td><button project-id="${project.id}"
										metric-class="${metric.metricFactoryClass}">Calculate</button></td>
							</tr>
						</c:forEach>
					</table>
				</div>
				<div class="bendl"></div>
				<div class="bendr"></div>
			</div>
			<div class="block small right">
				<div class="block_head">
					<div class="bheadl"></div>
					<div class="bheadr"></div>
					<h2>Scheduled Tasks</h2>
				</div>
				<!-- .block_head ends -->

				<div class="block_content">
					<table>
						<tr>
							<th></th>
							<th>Name</th>
							<th>Task Status</th>
						</tr>
						<c:forEach items="${project.tasks}" var="task">
							<tr>
								<td>#${task.position}</td>
								<td>${task.name}</td>
								<td class="${fn:toLowerCase(task.status)} task-status">${task.status}</td>
							</tr>
						</c:forEach>
					</table>
				</div>
				<div class="bendl"></div>
				<div class="bendr"></div>
			</div>
			<!-- .block ends -->
			<div id="footer">
				<p class="left">
					<a href="#"></a>
				</p>
			</div>
		</div>
		<!-- wrapper ends -->
	</div>
	<!-- #hld ends -->
	<c:import url="../import/javascripts.jsp" />
	<script src='<c:url value="/js/project/detail.js"/>'></script>
</body>