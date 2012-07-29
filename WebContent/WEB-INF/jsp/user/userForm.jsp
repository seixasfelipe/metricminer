<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<html>
	<head>
	<c:import url="../import/head.jsp" />
	
	<title>Metric Miner</title>
	</head>
	<body>
		<div id="hld">
			<div class="wrapper">		<!-- wrapper begins -->
				<div class="block center small">
					<div class="block_head">
						<div class="bheadl"></div>
						<div class="bheadr"></div>
						<h2>MetricMiner - Register</h2>
					</div>		<!-- .block_head ends -->
					<div class="block_content">
					
						<c:if test="${! empty errors}">
							<div class="message errormsg"><p>
						</c:if>
						<c:forEach var="error" items="${errors}">
							${error.message}<br />
						</c:forEach>
						<c:if test="${! empty errors}">
							</p></div>
						</c:if>
						
						<form action='<c:url value="/signup" />' method="post">
							<p>
								<label>Name:</label> <br />
								<input type="text" class="text" name="user.name" value="${user.name}" />
							</p>
							<p>
								<label>Email:</label> <br />
								<input type="text" class="text" name="user.email" value="${user.email}" />
							</p>
							<p>
								<label>University:</label> <br />
								<input type="text" class="text" name="user.university" value="${user.university}" />
							</p>
							<p>
								<label>CV link:</label> <br />
								<input type="text" class="text" name="user.cvUrl" value="${user.cvUrl}" />
							</p>
							<p>
								<label>Twitter:</label> <br />
								<input type="text" class="text" name="user.twitter" value="${user.twitter}" />
							</p>
							<p>
								<label>Password:</label> <br />
								<input type="password" class="text" name="user.password" />
							</p>
							<p>
								<label>Confirm your password:</label> <br />
								<input type="password" class="text" name="user.passwordConfirmation" />
							</p>
							<p>
								<input type="submit" class="submit" value="Register" />
							</p>
						</form>
					</div>		<!-- .block_content ends -->
					<div class="bendl"></div>
					<div class="bendr"></div>
				</div>		<!-- .login ends -->
			</div>						<!-- wrapper ends -->
		</div>		<!-- #hld ends -->
	<c:import url="../import/footer.jsp" />
	<c:import url="../import/javascripts.jsp" />
</body>
</html>