<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div id="header">
	<div class="hdrl"></div>
	<div class="hdrr"></div>
	
	<h1><a href="${linkTo[ProjectController].list}">MetricMiner</a></h1>
	<ul id="nav">
		<li class="active"><a href="${linkTo[ProjectController].list}">Projects</a>
			<ul>
				<li><a href="${linkTo[ProjectController].list}">List</a></li>
				<li><a href="<c:url value="/projects/new" />">New project</a></li>
			</ul>
		</li>
		<li class="active"><a href="<c:url value="/query" />">Execute query</a></li>
		<li><a href="${linkTo[TaskController].listTasks}">Tasks</a></li>
	</ul>
</div>		<!-- #header ends -->
