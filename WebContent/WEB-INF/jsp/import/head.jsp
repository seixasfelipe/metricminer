<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="metricminer" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

	<meta http-equiv="X-UA-Compatible" content="IE=7" />

	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	
    <style type="text/css" media="all">
		@import url("<c:url value='/css/style.css' />");
		@import url("<c:url value='/css/jquery.wysiwyg.css' />");
		@import url("<c:url value='/css/facebox.css' />");
		@import url("<c:url value='/css/visualize.css' />");
		@import url("<c:url value='/css/date_input.css' />");
    </style>
    
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/jquery.tagsinput.css' />" />
    <link rel="stylesheet" type="text/css" href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.13/themes/start/jquery-ui.css" />
	
	<!--[if lt IE 8]><style type="text/css" media="all">@import url("css/ie.css");</style><![endif]-->
	
	<script type="text/javascript">
		var CONTEXT_ROOT = "${pageContext.request.contextPath}";
	</script>
