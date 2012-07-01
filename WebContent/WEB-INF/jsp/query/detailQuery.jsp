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
		h2 {
			float: left;
		}
		form {
			margin-left: 80px;
		}
		pre {
			margin: 20px 0;
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
					<form method="post" action="<c:url value="/query/run" />">
						<input type="submit" class="submit small" value="Run again" />
						<input type="hidden" name="queryId" value="${query.id}" />
					</form>
					<pre>${query.sqlQuery}</pre>
					<h3>Results:</h3>
					<table>
						<c:forEach items="${query.results}" var="result" >
							<tr>
								<td><a href="<c:url value="/query/download/${result.id}" />">${result.csvFilename}</a></td>
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

