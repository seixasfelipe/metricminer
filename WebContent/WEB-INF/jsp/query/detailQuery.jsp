<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
	<c:import url="../import/head.jsp" />
	<title>Metric Miner</title>
	<style type="text/css">
		form {
			margin: 20px 0;
			float: left;
		}
		pre {
			margin: 20px 0;
		}
		.clear {
			clear: both;
		}
	</style>
</head>

<body>
	<div id="hld">
		<div class="wrapper">		<!-- wrapper begins -->
			<c:import url="../import/header.jsp" />
			<div class="block">
				<div class="block_head">
					<div class="bheadl"></div>
					<div class="bheadr"></div>
					<h2>Query</h2>
				</div>		<!-- .block_head ends -->
				
				<div class="block_content">
					<h2>${query.name}</h2>
					
					<pre>${query.sqlQuery}</pre>
					
					<form method="post" action="<c:url value="/query/run" />">
						<input type="submit" class="submit small" value="Run again" />
						<input type="hidden" name="queryId" value="${query.id}" />
					</form>
					
					<c:if test="${allowedToEdit}">
						<form method="get" action="<c:url value="/query/edit/${query.id}" />">
							<input type="submit" class="submit small" value="Edit" />
						</form>
					</c:if>
					
					<h3 class="clear">Results:</h3>
					<table class="clear">
						<c:forEach items="${query.results}" var="result" >
							<tr>
								<td><a href="<c:url value="/query/download/${result.id}" />">${result.csvFilename}</a></td>
								<td><fmt:formatDate value="${result.executedDate.time}" pattern="yyyy/MM/dd - HH:mm:ss"/></td>
							</tr>
						</c:forEach>
					</table>
				</div>		<!-- .block_content ends -->
				<div class="bendl"></div>
				<div class="bendr"></div>
			</div>		<!-- .block ends -->
		</div>						<!-- wrapper ends -->
	</div>		<!-- #hld ends -->
	<c:import url="../import/footer.jsp" />
	<c:import url="../import/javascripts.jsp" />
</body>
</html>

