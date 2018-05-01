<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="org.apache.shiro.web.filter.authc.FormAuthenticationFilter"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>某某学校兼职平台</title>
<link href="${ctx}/../static/css.css" rel="stylesheet" type="text/css" />
</head>

<body class="indexBg">
<div class="index">
	<div style="hight:300px;float:right"><a href="login"><font size="5" color="black">登录</font></a></div>
    <div class="indexT4"><h1><label>招聘信息预览</label></h1></div>
    <div class="indexT5">Copyright © zhazhashop</div>
    <div class="indexNew">
    	<c:forEach items="${jobList}" var="job" >
    		<li><span>发布时间：<fmt:formatDate value="${job.pub_time}" pattern="yyyy年MM月dd日HH点mm分ss秒"/></span><a href="岗位详情.html">${job.name}</a></li>
    	</c:forEach>
    </div>
    
</div>
</body>
</html>

