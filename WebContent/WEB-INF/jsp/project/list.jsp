<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Projects</title>
		<c:import url="../import/head.jsp" />
	</head>
	<body>
		<c:import url="../import/header.jsp" />
		
				<table id="projects">
						<tr>
							<th>Name</th>
							<th>SCM URL</th>
							<th>Actions</th>
						</tr>
					<c:forEach items="${projects}" var="project">
						<tr>
							<td><a href="<c:url value="/projects/${project.id}" />">${project.name}</a></td>
							<td>${project.scmUrl}</td>
							<td>
								<a href="<c:url value='/project/${project.id}/clone' />">Clone repository</a>
								<a href="<c:url value='/project/${project.id}/parse' />">Parse SCM logs</a>
							</td>
						</tr>
					</c:forEach>
				</table>
				
		<c:import url="../import/footer.jsp" />
	</body>
</html>