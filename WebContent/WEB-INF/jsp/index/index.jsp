<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<c:import url="../import/head.jsp" />

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
					<h2></h2>
				</div>
				<!-- .block_head ends -->

				<div class="block_content">
					<table>
						<tr>
							<th>Total projects</th> 
							<td>${totalProjects}</td>
						</tr>
						<tr>
							<th>Total committers</th> 
							<td>${totalAuthors}</td>
						</tr>
						<tr>
							<th>Total commits processed</th> 
							<td>${totalCommits}</td>
						</tr>
						<tr>
							<th>Total artifacts processed</th> 
							<td>${totalArtifacts}</td>
						</tr>
					</table>
					<h2>Last projects added</h2>
					<table>
						<tr>
							<th>Name</th>
						</tr>
						<c:forEach items="${newProjects}" var="project">
							<tr>
								<td><a href="<c:url value="/project/${project.id}"/>">${project.name}</a></td>
							</tr>
						</c:forEach>
					</table>
					<h2>Last tasks executed</h2>
					<table>
						<tr>
							<th>Name</th>
							<th>Project</th>
						</tr>
						<c:forEach items="${lastTasks}" var="task">
							<tr>
								<td>${task.name}</td>
								<td>${task.project.name}</td>
							</tr>
						</c:forEach>
					</table>
				</div>
				<div class="bendl"></div>
				<div class="bendr"></div>
			</div>
			<!-- .block ends -->
		</div>
		<!-- wrapper ends -->
	</div>
	<!-- #hld ends -->
	<c:import url="../import/footer.jsp" />
	<c:import url="../import/javascripts.jsp" />
	<script src='<c:url value="/js/project/detail.js"/>'></script>
</body>

