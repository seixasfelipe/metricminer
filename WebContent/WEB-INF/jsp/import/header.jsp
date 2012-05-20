<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div id="header">
	<div class="hdrl"></div>
	<div class="hdrr"></div>
	
	<h1><a href="${linkTo[ProjectController].list}">MetricMiner</a></h1>
	<ul id="nav">
		<li><a href="${linkTo[ProjectController].list}">Projects</a>
			<ul>
				<li><a href="<c:url value="/projects/new" />">New</a></li>
				<li><a href="${linkTo[ProjectController].list}">List</a></li>
			</ul>
		</li>
		<li><a href="${linkTo[QueryController].listQueries}">Queries</a>
			<ul>
				<li><a href="${linkTo[QueryController].queryForm}">New</a></li>
				<li><a href="${linkTo[QueryController].listQueries}">List</a></li>
			</ul>
		</li>
		<li><a href="${linkTo[TaskController].listTasks}">Tasks</a></li>
	</ul>
</div>		<!-- #header ends -->
