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
            display: block; /* 使用 block 來上下排列 */
        }
		.action-buttons {
		    display: flex;
		    justify-content: space-between;
		}
		
		.action-buttons form,
		.action-buttons a {
		    margin-bottom: 0; /* 移除底部邊距 */
		    margin-right: 10px; /* 增加按鈕之間的水平間距 */
		}
		
		.button {
		    padding: 12px 8px; /* 增加按鈕的內邊距和大小 */
		    background-color: #dc3545;
		    color: white;
		    border-radius: 4px;
		    text-decoration: none;
		    transition: background-color 0.3s;
		    margin-right: 10px; /* 增加按鈕之間的水平間距 */
		}
		
		.button:hover {
		    background-color: #c82333;
		}


        
    </style>
</head>
<body>
    <div class="container">
        <h1>貼文管理</h1>
        <a href="/forum/create" class="button">新增貼文</a>

        <h2>已發布的貼文</h2>
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
            <%
                List<PostVO> posts = (List<PostVO>) request.getAttribute("posts");
                if (posts != null && !posts.isEmpty()) {
                    for (PostVO post : posts) {
            %>
            <tr>
                <td><%= post.getPostId() %></td>
                <td><%= post.getCafeId() %></td>
                <td><%= post.getMemId() %></td>
                <td><%= post.getTime() %></td>
                <td><%= post.getTitle() %></td>
                <td><%= post.getContent() %></td>
                
                <td>
                    <div class="action-buttons">
                        <form action="/forum/delete" method="post" style="display: inline;">
                            <input type="hidden" name="id" value="<%=post.getPostId()%>">
                            <button type="submit" class="button">刪除</button>
                        </form>
                        <a href="/forum/post?id=<%=post.getPostId()%>" class="button">修改</a>
                        
<%--                         <a href="/forum/post=<%=post.getPostId()%>" class="button">修改</a> --%>
                    </div>
                </td>
            </tr>
            <%
                    }
                } else {
            %>
            <tr>
                <td colspan="7">尚未發布任何貼文。</td>
            </tr>
            <%
                }
            %>
        </table>


        <!-- 分頁顯示 -->
       <div>
    <c:if test="${currentPage > 1}">
        <a href="?mem_Id=${mem_Id}&page=${currentPage - 1}" class="button">上一頁</a>
    </c:if>
    
    <c:if test="${currentPage < totalPages}">
        <a href="?mem_Id=${mem_Id}&page=${currentPage + 1}" class="button">下一頁</a>
    </c:if>
</div>

<!-- 顯示當前頁碼和總頁碼 -->
<div>
    <span>當前頁: ${currentPage} / 總頁數: ${totalPages}</span>
</div>


    </div>
</body>
</html>
