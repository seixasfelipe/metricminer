<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>MetricMiner - New Project</title>
		<c:import url="../import/head.jsp" />
		<link rel="stylesheet" href="<c:url value='/css/project_detail.css' />">
	</head>
	<body>
		<c:import url="../import/header.jsp" />
		<form method="post" action='<c:url value="/projects"></c:url>' '>
			<label for="name">Name: </label><input type="text" name="name" /><br />
			<label for="scmUrl">Git url: </label><input type="text" name="scmUrl" /><br />
			<input type="submit" value="Send" />
		</form>
		<c:import url="../import/footer.jsp" />
	</body>
</html>