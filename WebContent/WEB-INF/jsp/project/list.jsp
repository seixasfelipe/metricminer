<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Projects</title>
		<link rel="stylesheet" href="<c:url value='/css/style.css' />">
	</head>
	<body>
	
		<section id="content">
			<table id="projects">
					<tr>
						<th>Name</th>
						<th>SCM URL</th>
						<th>Actions</th>
					</tr>
				<c:forEach items="${projects}" var="project">
					<tr>
						<td>${project.name}</td>
						<td>${project.scmUrl}</td>
						<td>
							<a href="<c:url value='/project/${project.id}/clone' />">Clone repository</a>
						</td>
					</tr>
				</c:forEach>
			</table>
		</section>
	</body>
</html>