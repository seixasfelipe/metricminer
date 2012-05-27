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
					<h2>System status</h2>
				</div>		<!-- .block_head ends -->
				
				<div class="block_content">
					<c:if test="${empty status.taskQueue}">
						<div class="message info"><p>There is no tasks running currently</p></div>
					</c:if>
					<h2>Configurations</h2>
					<table>
						<tr>
							<th>Max concurrent tasks</th>
							<td>${status.configs.maxConcurrentTasks }</td>
						</tr>
						<tr>
							<th>Temp directory</th>
							<td>${status.configs.metricMinerHome}</td>
						</tr>
					</table>
					<h2>Registered metrics</h2>
					<table>
						<c:forEach items="${status.configs.registeredMetrics}" var="metric">
							<tr>
								<td>${metric.name}</td>
								<td>${metric.metricFactoryClass}</td>
							</tr>
						</c:forEach>
					</table>
					<c:if test="!${empty status.taskQueue}">
						<h2>Tasks Running</h2>
						<table>
							<c:forEach items="${status.taskQueue}" var="task">
								<tr>
									<td>
										${task.name}
									</td>
									<td>
										${task.status}
									</td>
									<td>
										<fmt:formatDate value="${task.submitDate.time}" pattern="yyyy/MM/dd - HH:mm:ss"/>
									</td>
								</tr>
							</c:forEach>
						</table>
					</c:if>
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
</body>
</html>

