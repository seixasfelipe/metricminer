<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>New project</title>
	</head>
	<body>
		<form method="post" action='<c:url value="/projects"></c:url>' '>
			<label for="name">Name: </label><input type="text" name="name" />
			<label for="scmUrl">Git url: </label><input type="text" name="scmUrl" />
			<input type="submit" value="Send" />
		</form>
	</body>
</html>