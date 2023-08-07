<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<h1>添加账户信息</h1>
<form name="accountForm" action="${pageContext.request.contextPath}/account/save" method="post">
    <%--与实体类字段保持一致--%>
    账户名称：<input type="text" name="username"><br>
    账户金额：<input type="text" name="balance"><br>
    <input type="submit" value="保存">
</form>
</body>
</html>
