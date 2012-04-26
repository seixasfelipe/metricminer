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
					<h2>New Project</h2>
				</div>		<!-- .block_head ends -->
				
				<div class="block_content">
					<form method="post" action='<c:url value="/query"></c:url>' '>
						<p>
							<textarea type="text" class="text small" name="query.sql"></textarea>
						</p>
						
						<p>
							<input type="submit" class="submit small" value="Execute" />
						</p>
					</form>
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

