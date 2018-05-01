<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>职位发布管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			laydate({
	            elem: '#pubTime', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
	            event: 'focus' //响应事件。如果没有传入event，则按照默认的click
	        });
		});
	</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
	<div class="ibox">
	<div class="ibox-title">
		<h5>职位发布列表 </h5>
		<div class="ibox-tools">
			<a class="collapse-link">
				<i class="fa fa-chevron-up"></i>
			</a>
			<a class="dropdown-toggle" data-toggle="dropdown" href="#">
				<i class="fa fa-wrench"></i>
			</a>
			<ul class="dropdown-menu dropdown-user">
				<li><a href="#">选项1</a>
				</li>
				<li><a href="#">选项2</a>
				</li>
			</ul>
			<a class="close-link">
				<i class="fa fa-times"></i>
			</a>
		</div>
	</div>
    
    <div class="ibox-content">
	<sys:message content="${message}"/>
	
	<!--查询条件-->
	<div class="row">
	<div class="col-sm-12">
	<form:form id="searchForm" modelAttribute="tjob" action="${ctx}/platform/tjob/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
		<div class="form-group">
			<%-- <span>商家的userid：</span>
				<form:input path="sellerId" htmlEscape="false" maxlength="64"  class=" form-control input-sm"/>--%>
			<span>商家名称：</span> 
				<form:input path="name" htmlEscape="false" maxlength="64"  class=" form-control input-sm"/>
			<span>工资：</span>
				<form:input path="salary" htmlEscape="false"  class=" form-control input-sm"/>
			<span>发布时间：</span>
				<input id="pubTime" name="pubTime" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm"
					value="<fmt:formatDate value="${tjob.pubTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
			<%-- <span>审核状态：</span>
				<form:select path="status"  class="form-control m-b">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select> --%>
		 </div>	
	</form:form>
	<br/>
	</div>
	</div>
	
	<!-- 工具栏 -->
	<div class="row">
	<div class="col-sm-12">
		<div class="pull-left">
			<%-- <shiro:hasPermission name="platform:tjob:add">
				<table:addRow url="${ctx}/platform/tjob/form" title="职位发布"></table:addRow><!-- 增加按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="platform:tjob:edit">
			    <table:editRow url="${ctx}/platform/tjob/form" title="职位发布" id="contentTable"></table:editRow><!-- 编辑按钮 -->
			</shiro:hasPermission> --%>
		<%-- 	<shiro:hasPermission name="platform:tjob:del">
				<table:delRow url="${ctx}/platform/tjob/deleteAll" id="contentTable"></table:delRow><!-- 删除按钮 -->
			</shiro:hasPermission> --%>
			<%-- <shiro:hasPermission name="platform:tjob:import">
				<table:importExcel url="${ctx}/platform/tjob/import"></table:importExcel><!-- 导入按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="platform:tjob:export">
	       		<table:exportExcel url="${ctx}/platform/tjob/export"></table:exportExcel><!-- 导出按钮 -->
	       	</shiro:hasPermission> --%>
	       <button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick="sortOrRefresh()" title="刷新"><i class="glyphicon glyphicon-repeat"></i> 刷新</button>
		
			</div>
		<div class="pull-right">
			<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="search()" ><i class="fa fa-search"></i> 查询</button>
			<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="reset()" ><i class="fa fa-refresh"></i> 重置</button>
		</div>
	</div>
	</div>
	
	<!-- 表格 -->
	<table id="contentTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
		<thead>
			<tr>
				<th> <input type="checkbox" class="i-checks"></th>
				<!-- <th  class="sort-column sellerId">商家的userid</th> -->
				<th  class="sort-column name">商家名称</th>
				<th  class="sort-column salary">工资</th>
				<th  class="sort-column pubTime">发布时间</th>
				<th  class="sort-column status">审核状态</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="tjob">
			<tr>
				<td> <input type="checkbox" id="${tjob.id}" class="i-checks"></td>
				<%-- <td><a  href="#" onclick="openDialogView('查看职位发布', '${ctx}/platform/tjob/form?id=${tjob.id}','800px', '500px')">
					${tjob.sellerId}
				</a></td> --%>
				<td>
					<a onclick="openDialogView('联系商家', '${ctx}/platform/tuser/viewSeller?id=${tjob.sellerId}','800px', '500px')" href="#">${tjob.name}</a>
				</td>
				<td>
					${tjob.salary}
				</td>
				<td>
					<fmt:formatDate value="${tjob.pubTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${fns:getDictLabel(tjob.status, 'status', '')}
				</td>
				<td>
					<a href="#" onclick="openDialogView('查看职位详情', '${ctx}/platform/tjob/view?id=${tjob.id}','800px', '500px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看职位详情</a>
    				<a href="${ctx}/platform/tjob/shenhe?id=${tjob.id}"  class="btn btn-success btn-xs" ><i class="fa fa-edit"></i>报名</a>
    				<%-- <shiro:hasPermission name="platform:tjob:del">
						<a href="${ctx}/platform/tjob/delete?id=${tjob.id}" onclick="return confirmx('确认要删除该职位发布吗？', this.href)"   class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
					</shiro:hasPermission> --%>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	
		<!-- 分页代码 -->
	<table:page page="${page}"></table:page>
	<br/>
	<br/>
	</div>
	</div>
</div>
</body>
</html>