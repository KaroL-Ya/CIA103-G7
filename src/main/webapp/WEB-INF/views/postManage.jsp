<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="com.forum.model.PostVO"%>
<%@ page import="java.util.List"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>貼文管理</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            margin: 20px;
            padding: 0;
        }
        
        .container {
            max-width: 1600px;
            margin: auto;
            background-color: #fff;
            padding: 50px;
            border-radius: 50px;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
        }
        
        h1, h2 {
            text-align: center;
        }
        
        .button {
            padding: 10px 15px;
            background-color: #dc3545;
            color: white;
            border-radius: 4px;
            text-decoration: none;
            transition: background-color 0.3s;
            margin-bottom: 5px; /* 增加底部邊距以分開按鈕 */
        }

        .button:hover {
            background-color: #c82333;
        }

        form {
            margin: 20px 0;
        }
        
        label {
            display: block;
            margin: 10px 0 5px;
        }
        
        input, textarea {
            width: 100%;
            padding: 8px;
            margin-bottom: 10px;
            border: 1px solid #ccc;
            border-radius: 4px;
            background-color: #fff;
            transition: border-color 0.3s;
        }
        
        input:focus, textarea:focus {
            border-color: #66afe9;
            outline: none;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }
        
        th, td {
            padding: 10px;
            border: 1px solid #ddd;
            text-align: left;
        }
        
        th {
            background-color: #f2f2f2;
        }

        .action-buttons {
            display: flex;
            justify-content: space-between;
        }
        
        .action-buttons form,
        .action-buttons a {
            margin-bottom: 0;
            margin-right: 10px;
        }
        
        .button {
            padding: 12px 8px;
            background-color: #dc3545;
            color: white;
            border-radius: 4px;
            text-decoration: none;
            transition: background-color 0.3s;
            margin-right: 10px;
        }
        
        .button:hover {
            background-color: #c82333;
        }
    </style>
</head>
<body>
    <div class="container">
        <a href="<%= request.getContextPath() %>/back-end/forum/postManage" class="button">返回首頁</a>
        <h1>貼文管理</h1>

        <table>
            <tr>
                <th>貼文編號</th>
                <th>咖啡廳編號</th>
                <th>會員編號</th>
                <th>發表時間</th>
                <th>貼文標題</th>
                <th>貼文內容</th>
                <th>操作</th>
            </tr>
            
            <!-- 使用 JSTL 遍歷 posts -->
            <c:forEach var="post" items="${posts}">
                <tr>
                    <td>${post.postId}</td>
                    <td>${post.cafeId}</td>
                    <td>${post.memId}</td>
                    <td>${post.time}</td>
                    <td>${post.title}</td>
                    <td>${post.content}</td>
                    
                    <td>
                        <div class="action-buttons">
                            <!-- 停權/恢復按鈕 -->
                            <form action="/forum/updateStatus" method="post" style="display: inline;">
                                <input type="hidden" name="id" value="${post.postId}">
                                <input type="hidden" name="status" value="${post.status == 0 ? 1 : 0}">
                                <button type="submit" class="button">
                                    ${post.status == 0 ? '恢復' : '停權'}
                                </button>
                            </form>
                        </div>
                    </td>
                </tr>
            </c:forEach>
            
            <!-- 如果沒有貼文 -->
            <c:if test="${empty posts}">
                <tr>
                    <td colspan="7">尚未發布任何貼文。</td>
                </tr>
            </c:if>
        </table>
    </div>
</body>
</html>
