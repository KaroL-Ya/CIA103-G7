<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-Hant">
<head>
    <meta charset="UTF-8">
    <title>新增貼文</title>
    <link rel="stylesheet" type="text/css" href="styles.css"> <!-- 引入CSS樣式 -->
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
        }
        label {
            display: block;
            margin: 10px 0 5px;
        }
        input[type="text"], textarea {
            width: 100%;
            padding: 8px;
            margin-bottom: 10px;
            border: 1px solid #ccc;
            border-radius: 4px;
        }
        button {
            padding: 10px 15px;
            background-color: #28a745;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }
        button:hover {
            background-color: #218838;
        }
    </style>
</head>
<body>
    <h1>新增貼文</h1>
    <form action="/forum/create" method="post">
        <input type="hidden" name="cafeId" value="${cafeId}"> <!-- 自動抓取的咖啡廳編號 -->
        <input type="hidden" name="memId" value="${memberId}"> <!-- 自動抓取的會員編號 -->
        
        <label for="title">貼文標題</label>
        <input type="text" id="title" name="title" required>

        <label for="content">貼文內容</label>
        <textarea id="content" name="content" required></textarea>

        <input type="hidden" name="count" value="0"> <!-- 按讚人數預設為 0 -->
        <input type="hidden" name="status" value="1"> <!-- 貼文狀態預設為 1 -->

        <button type="submit">發佈貼文</button>
    </form>
    <a href="/forum">返回</a> <!-- 返回首頁 -->
</body>
</html>
