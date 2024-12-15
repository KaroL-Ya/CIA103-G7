<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.forum.model.PostVO" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Date" %>
<html>
<head>
    <style>
        body { font-family: Arial, sans-serif; }
        form { margin: 20px; }
        label { display: block; margin: 10px 0 5px; }
        input, textarea { width: 100%; padding: 8px; margin-bottom: 10px; }
        table { margin-top: 20px; width: 100%; border-collapse: collapse; }
        td, th { border: 1px solid #ddd; padding: 8px; }
        th { background-color: #f4f4f4; }
    </style>
</head>
<body>
    <h1>論壇貼文管理</h1>

    <table>
        
        <tbody>
            <%
                @SuppressWarnings("unchecked")
                List<PostVO> posts = (List<PostVO>) request.getAttribute("posts");
                Integer currentMemberId = (Integer) session.getAttribute("mem_Id");
                if (posts != null) {
                    for (PostVO post : posts) {
                        boolean isOwner = (currentMemberId != null && currentMemberId.equals(post.getMemId()));
            %>
            <tr>
                <td><%= post.getPostId() %></td>
                <td><%= post.getCafeId() %></td>
                <td><%= post.getMemId() %></td>
                <td><%= post.getTime() %></td>
                <td><%= post.getTitle() %></td>
                <td><%= post.getContent() %></td>
                <td><%= post.getCount() %></td>
                <td><%= post.getStatus() %></td>
                <td>
                    <%-- 顯示「編輯」按鈕，只允許貼文作者編輯 --%>
                    <%
                        if (isOwner) {
                    %>
                    <form action="/forum/update" method="get">
                        <input type="hidden" name="id" value="<%= post.getPostId() %>" />
                        <button type="submit">編輯</button>
                    </form>
                    <% } %>
                </td>
            </tr>
            <%
                    }
                }
            %>
        </tbody>
    </table>

    <%-- 更新貼文表單 --%>
    <%
        PostVO postToEdit = (PostVO) request.getAttribute("post");
        if (postToEdit != null && currentMemberId != null && currentMemberId.equals(postToEdit.getMemId())) {
    %>
    <h2>更新貼文</h2>
    <form action="/forum/update" method="post">
        <input type="hidden" name="id" value="<%= postToEdit.getPostId() %>" />
        <label for="title">標題:</label>
        <input type="text" id="title" name="title" value="<%= postToEdit.getTitle() %>" required />
        
        <label for="content">內容:</label>
        <textarea id="content" name="content" required><%= postToEdit.getContent() %></textarea>
        
        <button type="submit">更新貼文</button>
    </form>
    <% } else { %>
        <p>您無權編輯此貼文。</p>
    <% } %>
</body>
</html>
