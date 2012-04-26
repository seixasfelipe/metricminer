<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div id="header">
	<div class="hdrl"></div>
	<div class="hdrr"></div>
	
	<h1><a href="<c:url value="/projects" />">MetricMiner</a></h1>
	<ul id="nav">
		<li class="active"><a href="<c:url value="/projects" />">Projects</a>
			<ul>
				<li><a href="<c:url value="/projects" />">List</a></li>
				<li><a href="<c:url value="/projects/new" />">New project</a></li>
			</ul>
		</li>
		<li class="active"><a href="<c:url value="/query" />">Execute query</a></li>
	</ul>
</div>		<!-- #header ends -->
