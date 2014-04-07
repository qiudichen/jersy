<%@page contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page import="com.iex.tv.domain.system.SystemProperty" %>

<jsp:useBean id="sysList" type="java.util.List<SystemProperty>" scope="request" />
<jsp:useBean id="errorMsg" type="java.lang.String" scope="request" />

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
 
<html>
    <head>
        <title>System properties</title>
    </head>
 
    <body>
		<form method="POST" action="addProperty.html">
			<input type="hidden" name="subtype" value="WEBSTATION"/>
			<table>
				<tr>
					<td>Name: <input type="text" name="name" /> </td>
				</tr>
				<tr>
					<td>Value: <input type="text" name="value" />  </td>
				</tr>
	
				<tr>
					<td><input type="submit" value="Add" /></td>
				</tr>			
			</table>
        </form>
    
    	<table>
    		<th>
    			<td> name </td>
    			<td> value</td>
    			<td> delete</td>
    		</th>
    		
        <%
        	for(SystemProperty prop : sysList) {
        		String name = prop.getName();
        		String value = prop.getValue();
        %>
        	<tr>
        		<td><%= name %></td>
        		<td><%=value %></td>
        		<td><a href="<%="deleteProperty.html?oid=" + prop.getOid() %>">delete</a></td>
        	</tr>
       <%
        	}       
       %>
	   </table>
     </body>
 </html>