<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>员工信息管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
		});
	</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
	<div class="ibox">
	<div class="ibox-title">
		<h5>员工信息列表 </h5>
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
	<form:form id="searchForm" modelAttribute="tuser" action="${ctx}/platform/tuser/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
		<div class="form-group">
			<%-- <span>主键：</span>
				<form:input path="id" htmlEscape="false"  class=" form-control input-sm"/>--%>
			<span>姓名：</span> 
				<form:input path="name" htmlEscape="false" maxlength="64"  class=" form-control input-sm"/>
			<<%-- span>状态：</span>
				<form:select path="status"  class="form-control m-b">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select> --%>
			<%-- <span>类型：</span>
				<form:select path="type"  class="form-control m-b">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('userType')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
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
			<shiro:hasPermission name="platform:tuser:add">
				<table:addRow url="${ctx}/platform/tuser/form" title="员工信息"></table:addRow><!-- 增加按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="platform:tuser:edit">
			    <table:editRow url="${ctx}/platform/tuser/form" title="员工信息" id="contentTable"></table:editRow><!-- 编辑按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="platform:tuser:del">
				<table:delRow url="${ctx}/platform/tuser/deleteAll" id="contentTable"></table:delRow><!-- 删除按钮 -->
			</shiro:hasPermission>
			<%-- <shiro:hasPermission name="platform:tuser:import">
				<table:importExcel url="${ctx}/platform/tuser/import"></table:importExcel><!-- 导入按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="platform:tuser:export">
	       		<table:exportExcel url="${ctx}/platform/tuser/export"></table:exportExcel><!-- 导出按钮 -->
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
				<th  class="sort-column id">主键</th>
				<th  class="sort-column name">姓名</th>
				<th  class="sort-column phone">手机</th>
				<th  class="sort-column status">状态</th>
				<th  class="sort-column type">类型</th>
				<!-- <th  class="sort-column empCount">求职次数</th>
				<th  class="sort-column empedCount">被雇佣次数</th>
				<th  class="sort-column score">信任分</th>
				<th  class="sort-column website">商家网址</th>
				<th  class="sort-column servQQ">客服QQ</th>
				<th  class="sort-column pubCount">发布职位次数</th>
				<th  class="sort-column photo">图片地址</th> -->
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="tuser">
			<tr>
				<td> <input type="checkbox" id="${tuser.id}" class="i-checks"></td>
				<td><a  href="#" onclick="openDialogView('查看员工信息', '${ctx}/platform/tuser/form?id=${tuser.id}','800px', '500px')">
					${tuser.id}
				</a></td>
				<td>
					${tuser.name}
				</td>
				<td>
					${tuser.phone}
				</td>
				<td>
					${fns:getDictLabel(tuser.status, 'status', '')}
				</td>
				<td>
					${fns:getDictLabel(tuser.type, 'userType', '')}
				</td>
				<%-- <td>
					${tuser.empCount}
				</td>
				<td>
					${tuser.empedCount}
				</td>
				<td>
					${tuser.score}
				</td>
				<td>
					${tuser.website}
				</td>
				<td>
					${tuser.servQQ}
				</td>
				<td>
					${tuser.pubCount}
				</td>
				<td>
					${tuser.photo}
				</td> --%>
				<td>
					<shiro:hasPermission name="platform:tuser:view">
						<a href="#" onclick="openDialogView('查看员工信息', '${ctx}/platform/tuser/form?id=${tuser.id}','800px', '500px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="platform:tuser:edit">
    					<a href="#" onclick="openDialog('修改员工信息', '${ctx}/platform/tuser/form?id=${tuser.id}','800px', '500px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 修改</a>
    				</shiro:hasPermission>
    				<shiro:hasPermission name="platform:tuser:del">
						<a href="${ctx}/platform/tuser/delete?id=${tuser.id}" onclick="return confirmx('确认要删除该员工信息吗？', this.href)"   class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
					</shiro:hasPermission>
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