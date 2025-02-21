<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Tính toán</title>
</head>
<body>
<%
String strA = request.getParameter("a");
int valueA = Integer.parseInt(strA);
String strB = request.getParameter("b");
int valueB = Integer.parseInt(strB);
out.print("Tổng của ");
out.print(valueA);
out.print(" và ");
out.print(valueB);
out.print(" là: ");
out.print(valueA + valueB);
%>
</body>
</html>