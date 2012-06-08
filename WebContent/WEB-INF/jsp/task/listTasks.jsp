<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
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
					<h2>Task Queue</h2>
				</div>		<!-- .block_head ends -->
				
				<div class="block_content">
					<table cellpadding="0" cellspacing="0" class="tablesorter zebra">
						<thead>
							<tr>
								<th>Name</th>
								<th>Status</th>
								<th>Submission date</th>
								<th>Start date</th>
								<th>End date</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${tasks}" var="task">
								<tr>
									<td>${task.name}</td>
									<td>${task.status}</td>
									<td>
										<fmt:formatDate value="${task.submitDate.time}" 
											pattern="yyyy/MM/dd - HH:mm:ss"/>
									</td>
									<td>
										<c:if test="${task.hasStarted() or task.hasFinished()}">
											<fmt:formatDate value="${task.startDate.time}" 
												pattern="yyyy/MM/dd - HH:mm:ss"/>
										</c:if>
										<c:if test="${!task.hasStarted() and !task.hasFinished()}">
											-
										</c:if>
									</td>
									<td>
										<c:if test="${task.hasFinished()}">
											<fmt:formatDate value="${task.endDate.time}" 
												pattern="yyyy/MM/dd - HH:mm:ss"/>
										</c:if>
										<c:if test="${!task.hasFinished()}">
											-
										</c:if>
									</td>
								</tr>
							</c:forEach>
						</tbody>
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
	<script>
		$(".tablesorter").tablesorter();
	</script>
</body>
</html>

