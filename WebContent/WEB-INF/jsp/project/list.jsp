<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
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
					<h2>Projects</h2>
					
					<ul>
						<li><a href="<c:url value="/projects/new" />">New project</a></li>
					</ul>
				</div>		<!-- .block_head ends -->
				
				<div class="block_content">
					<table cellpadding="0" cellspacing="0" width="100%" class="sortable">
						<thead>
							<tr>
								<th>Name</th>
								<th>Repo's URL</th>
								<th>Tags</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${projects}" var="project">
								<tr>
									<td><a href="<c:url value="/projects/${project.id}" />">${project.name}</a></td>
									<td>${project.scmUrl}</td>
									<td>
										<c:forEach items="${project.tags}" var="tag">
											${tag.name}&nbsp;
										</c:forEach>
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
</body>
</html>