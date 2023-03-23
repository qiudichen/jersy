<%@ page session="false" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%
	String nodeName = System.getProperty("wfm.node.name") ;

	if (nodeName == null) {
		nodeName = "wfm.node.name is null.";
	}
	java.util.Map<String, String> envMap = System.getenv();
	java.util.SortedSet<String> keys = new java.util.TreeSet<>(envMap.keySet());
	
	java.util.Properties properties = System.getProperties();
	java.util.SortedSet<Object> pkeys = new java.util.TreeSet<>(properties.keySet());
	
%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8" />
        <title><%=request.getServletContext().getServerInfo() %></title>
    </head>

    <body>
        <h1>Welcome WFM <%=nodeName %> Server</h1>
        
        <H2>Java Options</H2>
        <br><br>
        <% for (Object envName : pkeys) {  %>
        
        	<%=String.format("%s = %s%n", envName, properties.get(envName)) %><br>
        <% } %>
        
        <H2>Environment variables</H2>
        <br><br>
        <% for (String envName : keys) {  %>
        
        	<%=String.format("%s = %s%n", envName, envMap.get(envName)) %><br>
        <% } %>
    </body>
</html>
