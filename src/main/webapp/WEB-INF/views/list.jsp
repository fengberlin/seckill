<%--
  Created by IntelliJ IDEA.
  User: fengberlin
  Date: 18-7-22
  Time: 上午12:30
  To change this template use File | Settings | File Templates.
--%>
<%@include file="taglib.jsp"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <title>秒杀列表页</title>
    <%@include file="common.jsp"%>
</head>
<body>
    <div class="container">
        <div class="panel panel-default">
            <div class="panel-heading text-center">
                <h2>秒杀列表</h2>
            </div>
            <div class="panel-body">
                <table class="table table-hover">
                    <thead>
                        <tr>
                            <th>名称</th>
                            <th>库存量</th>
                            <th>开始时间</th>
                            <th>结束时间</th>
                            <th>创建时间</th>
                            <th>详情页</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="stock" items="${stockList}">
                            <tr>
                                <td>${stock.name}</td>
                                <td>${stock.number}</td>
                                <td>
                                    <fmt:formatDate value="${stock.startTime}" pattern="yyyy-MM-dd HH:mm:ss" />
                                        <%--${stock.startTime}--%>
                                </td>
                                <td>
                                    <fmt:formatDate value="${stock.endTime}" pattern="yyyy-MM-dd HH:mm:ss" />
                                        <%--${stock.endTime}--%>
                                </td>
                                <td>
                                    <fmt:formatDate value="${stock.createTime}" pattern="yyyy-MM-dd HH:mm:ss" />
                                        <%--${stock.createTime}--%>
                                </td>
                                <td>
                                    <a class="btn btn-info" href="/stock/${stock.seckillId}/detail" target="_blank">link</a>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</body>
<!-- jQuery文件。务必在bootstrap.min.js 之前引入 -->
<script src="https://cdn.bootcss.com/jquery/2.1.1/jquery.min.js"></script>
<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
<script src="https://cdn.bootcss.com/bootstrap/3.3.0/js/bootstrap.min.js"></script>
</html>
