<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

[["Month", "count"]
<c:forEach items="${lastSixMonthsCommitCountMap}" var="entry">
	,["<fmt:formatDate value="${entry.key.time}" pattern="MMM yy"/>",
    ${entry.value}]
</c:forEach>
]
