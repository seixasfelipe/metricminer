<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
	<c:import url="../import/head.jsp" />
	<title>Metric Miner</title>
</head>

<body>
	<div id="hld">
		<div class="wrapper">		<!-- wrapper begins -->
			<c:import url="../import/header.jsp" />
			<div class="block">
				<div class="block_head">
					<div class="bheadl"></div>
					<div class="bheadr"></div>
					<h2>${project.name}</h2>
				</div>		<!-- .block_head ends -->
				
				<div class="block_content">
				
					<h3>Details</h3>
					
					<table>
						<tr>
							<td>Name</td>
							<td>${project.name} <input type="hidden" name="id" id="projectId" value="${project.id}" /></td>
						</tr>
						
						<tr>
							<td>Repo's path</td>
							<td>${project.scmUrl}</td>
						</tr>
						
						<tr>
							<td>Tags</td>
							<td>
								<input type="text" class="tags" name="tags" id="tags" value="${tags}" style="display: none;" />
							</td>
						</tr>
					</table>
				
				
					<h3>Scheduled Tasks</h3>
					<table>
						<tr>
							<th></th>
							<th>Submit date</th>
							<th>Name</th>
							<th>Task Factory Class</th>
							<th>Task Status</th>
						</tr>
						<c:forEach items="${project.tasks}" var="task">
							<tr>
								<td>#${task.position}</td>
								<td>${task.submitDate.time}</td>							
								<td>${task.name}</td>
								<td>${task.runnableTaskFactoryClass}</td>
								<td class="${fn:toLowerCase(task.status)} task-status">${task.status}</td>
							</tr>
						</c:forEach>
					</table>
				</div>		<!-- .block_content ends -->
				<div class="bendl"></div>
				<div class="bendr"></div>
			</div>		<!-- .block ends -->
			<div id="footer">
				<p class="left"><a href="#"></a></p>
			</div>
		</div>						<!-- wrapper ends -->
	</div>		<!-- #hld ends -->
	<c:import url="../import/javascripts.jsp" />
	<script src='<c:url value="/js/project/detail.js"/>'></script>
</body>

