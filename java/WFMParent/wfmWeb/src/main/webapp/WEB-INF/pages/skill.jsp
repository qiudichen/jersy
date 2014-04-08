<%@page contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
  <head><title>Skill</title></head>
  <body>
    <h1>Skill Management</h1>
    
    <h2>add Skill</h2>
	<form method="POST" action="addSkill.html">
		<table>
			<tr>
				<td>Skill Name: <input type="text" name="skillName" /> </td>
			</tr>
			<tr>
				<td><input type="submit" value="Add" /></td>
			</tr>			
		</table>
    </form>

	<br/>
    <h2>update Skill</h2>
	<form method="POST" action="updateSkill.html">
		<table>
			<tr>
				<td>Skill Id: <input type="text" name="skillId" /> </td>
			</tr>
			<tr>
				<td>Skill Name: <input type="text" name="skillName" /> </td>
			</tr>		
			<tr>
				<td><input type="submit" value="Update" /></td>
			</tr>			
		</table>
    </form>
	            
    <h3>Skills</h3>
    <table border="1">
   		<tr>
   			<td> skill Id </td>
   			<td> name </td>
   			<td> delete </td>
   		</tr>
	    <c:forEach items="${skills}" var="skill">
		<tr>
			<td><c:out value="${skill.id}"/></td>
        	<td><c:out value="${skill.name}"/></td>
       		<td><a href="deleteSkill.html?skillId=<c:out value='${skill.id}'/>">delete</a></td>
       	</tr>	      
	    </c:forEach>
    </table>
  </body>
</html>