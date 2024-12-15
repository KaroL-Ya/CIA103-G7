<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Post Detail</title>
</head>
<body>
    <h1>${post.title}</h1>
    <p>${post.content}</p>
    <p>Posted on: ${post.time}</p>
    <a href="/post">Back to List</a>
</body>
</html>
