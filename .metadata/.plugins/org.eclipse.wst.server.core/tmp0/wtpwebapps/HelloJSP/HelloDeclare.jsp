<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Hello Declare + Code + Expression</title>
</head>
<body>
	<h1>Hello Declare</h1>
	<%
		int x = 10, y = 200;
		int z = x + y;
		out.print(z);
	%>
	<hr>
	<h2>Hoặc ta có thể dùng Expression</h2>
	<hr>
	<%= z%>
</body>
</html>