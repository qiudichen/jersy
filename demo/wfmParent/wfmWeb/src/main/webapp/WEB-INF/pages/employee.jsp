<%@page contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
  <head><title>Employee</title></head>
  <body>
    <h1>Employee Management</h1>
    <p>Greetings, it is now <c:out value="${now}"/></p>
    
    <h2>add Employee</h2>
	<form method="POST" action="addEmployee.html">
		<table>
			<tr>
				<td>First Name: <input type="text" name="firstName" /> </td>
			</tr>
			<tr>
				<td>Last Name: <input type="text" name="lastName" /> </td>
			</tr>			
			<tr>
				<td>Gender:<select name="gender">
								<option value="M">Male</option>
								<option value="F">Female</option>
							</select> 				
			</tr>

			<tr>
				<td><input type="submit" value="Add" /></td>
			</tr>			
		</table>
    </form>

	<br/>
    <h2>update Employee</h2>
	<form method="POST" action="updateEmployee.html">
		<table>
			<tr>
				<td>Employee Number: <input type="text" name="empNum" /> </td>
			</tr>
			<tr>
				<td>First Name: <input type="text" name="firstName" /> </td>
			</tr>
			<tr>
				<td>Last Name: <input type="text" name="lastName" /> </td>
			</tr>			
			<tr>
				<td><input type="submit" value="Update" /></td>
			</tr>			
		</table>
    </form>
	            
    <h3>Employees</h3>
    <table border="1">
   		<tr>
   			<td> number </td>
   			<td> name </td>
   			<td> gender</td>
   			<td> delete</td>
   		</tr>
	    <c:forEach items="${employees}" var="employee">
		<tr>
			<td><c:out value="${employee.empNum}"/></td>
        	<td><c:out value="${employee.lastName}"/>, <c:out value="${employee.firstName}"/></td>
       		<td><c:out value="${employee.gender}"/></td>
       		<td><a href="deleteEmployee.html?empNum=<c:out value='${employee.empNum}'/>">delete</a></td>
       	</tr>	      
	    </c:forEach>
    </table>
  </body>
</html>