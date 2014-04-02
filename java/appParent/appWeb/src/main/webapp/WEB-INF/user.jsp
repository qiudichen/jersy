<%@page contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<jsp:useBean id="userName" type="java.lang.String" scope="request" />
<jsp:useBean id="result" type="java.lang.String" scope="request" />

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
 
<html>
    <head>
        <title>User</title>
    </head>
 
    <body>
        <form method="POST" action="index.jsp">
            Name: <input type="text" name="name" />
            <input type="submit" value="Add" />
        </form>
 
        <hr><%= userName %><hr>
        <hr><%= result %><hr>
     </body>
 </html>