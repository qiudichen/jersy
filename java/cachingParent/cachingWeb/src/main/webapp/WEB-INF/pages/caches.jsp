<%@page contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
  <head><title>Caching</title></head>
<body>
	<h1>Caching Management</h1>
    <table border="1">
   		<tr>
   			<td> cache name </td>
   		</tr>
	    <c:forEach items="${cahces}" var="cahce">
		<tr>
			<td><c:out value="${cahce}"/></td>
       	</tr>	      
	    </c:forEach>
    </table>
    
    <h2>remove object</h2>
	<form method="POST" action="caching/remove.html">
		<table>
			<tr><td>First Name: <input type="text" name="firstName" /></td></tr>
			<tr><td>Last Name: <input type="text" name="lastName" /> </td></tr>			
			<tr><td><input type="submit" value="remove" /></td></tr>			
		</table>
    </form>
	<br/>    
</body>
</html> 