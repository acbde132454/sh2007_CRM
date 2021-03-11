<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">

<link href="/crm/jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="/crm/jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />

<script type="text/javascript" src="/crm/jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="/crm/jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="/crm/jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="/crm/jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>

	<%--分页插件--%>
	<script type="text/javascript" src="/crm/jquery/bs_pagination/en.js"></script>
	<script type="text/javascript" src="/crm/jquery/bs_pagination/jquery.bs_pagination.min.js"></script>
	<link rel="stylesheet" href="/crm/jquery/bs_pagination/jquery.bs_pagination.min.css" />

	<script type="text/javascript" src="/crm/jquery/layer/layer.js"></script>
<script type="text/javascript">

	$(function(){
		
		
		
	});
	
</script>
</head>
<body>

	<!-- 创建市场活动的模态窗口 -->
	<div class="modal fade" id="createActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form class="form-horizontal" id="createActivityForm" role="form">

						<div class="form-group">
							<label for="create-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select name="owner" class="form-control" id="create-marketActivityOwner">
								 <c:forEach items="${applicationScope.users}" var="user">
									 <option value="${user.id}">${user.name}</option>
								 </c:forEach>
								</select>
							</div>
                            <label for="create-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input name="name" type="text" class="form-control" id="create-marketActivityName">
                            </div>
						</div>
						
						<div class="form-group">
							<label for="create-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input name="startDate" type="text" class="form-control" id="create-startTime">
							</div>
							<label for="create-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input name="endDate" type="text" class="form-control" id="create-endTime">
							</div>
						</div>
                        <div class="form-group">

                            <label for="create-cost" class="col-sm-2 control-label">成本</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input name="cost" type="text" class="form-control" id="create-cost">
                            </div>
                        </div>
						<div class="form-group">
							<label for="create-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea name="description" class="form-control" rows="3" id="create-describe"></textarea>
							</div>
						</div>
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" onclick="createActivity()">保存</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 修改市场活动的模态窗口 -->
	<div class="modal fade" id="editActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form class="form-horizontal" id="editActivityForm" role="form">
						<input type="hidden" name="id" id="activityId" />
						<div class="form-group">
							<label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" name="owner" id="edit-marketActivityOwner">
									<c:forEach items="${applicationScope.users}" var="user">
										<option value="${user.id}">${user.name}</option>
									</c:forEach>
								</select>
							</div>
                            <label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" name="name" class="form-control" id="edit-marketActivityName" >
                            </div>
						</div>

						<div class="form-group">
							<label for="edit-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" name="startDate" class="form-control" id="edit-startTime" >
							</div>
							<label for="edit-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" name="endDate" class="form-control" id="edit-endTime" >
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-cost" class="col-sm-2 control-label">成本</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" name="cost" class="form-control" id="edit-cost">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea name="description" class="form-control" rows="3" id="edit-describe"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" onclick="updateActivity()">更新</button>
				</div>
			</div>
		</div>
	</div>
	
	
	
	
	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>市场活动列表</h3>
			</div>
		</div>
	</div>
	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">
		
			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form id="activityForm" class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input class="form-control" id="name" type="text">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" id="owner" type="text">
				    </div>
				  </div>


				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">开始日期</div>
					  <input class="form-control" id="startDate" type="text" />
				    </div>
				  </div>
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">结束日期</div>
					  <input class="form-control" id="endDate" type="text">
				    </div>
				  </div>
				  
				  <button type="button" onclick="query()" class="btn btn-default">查询</button>
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#createActivityModal"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" onclick="enableEdit()" class="btn btn-default"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" onclick="deleteActivity()" class="btn btn-danger"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input id="father" type="checkbox" /></td>
							<td>名称</td>
                            <td>所有者</td>
							<td>开始日期</td>
							<td>结束日期</td>
						</tr>
					</thead>
					<tbody id="activityBody">
                        <%--<tr class="active">
                            <td><input type="checkbox" /></td>
                            <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.jsp';">发传单</a></td>
                            <td>zhangsan</td>
                            <td>2020-10-10</td>
                            <td>2020-10-20</td>
                        </tr>--%>
					</tbody>
				</table>
			</div>

			<div style="height: 50px; position: relative;top: 30px;">
				<div id="activityPage" style="background: #fff">

				</div>
			</div>
		</div>
	</div>

<script>

	refresh(1,3);

	//表单序列化
	function query(){
		refresh(1,3);
	}



	//刷新页面
	//	'pageSize' : pageSize,
	function refresh(page,pageSize){
		$.get("/crm/workbench/activity/list",
				{
					'page' : page,
					'pageSize' : pageSize,
					'name' : $('#name').val(),
					'owner':$('#owner').val(),
					'startDate':$('#startDate').val(),
					'endDate' : $('#endDate').val()
				}
		,function(data){
			console.log(data);
			//条件回显
			$('#name').val(data.v.name);
			$('#owner').val(data.v.owner);
			$('#startDate').val(data.v.startDate);
			$('#endDate').val(data.v.endDate);
			var content = "";
			for(var i = 0; i < data.t.list.length; i++){
				var activity = data.t.list[i];
				content += "<tr class='active'>\n" +
						"                            <td><input class='son' value="+activity.id+"  type=\"checkbox\" /></td>\n" +
						"                            <td><a style=\"text-decoration: none; cursor: pointer;\" href='/crm/workbench/activity/queryDetail?id="+activity.id+"'>"+activity.name+"</a></td>\n" +
						"                            <td>"+activity.owner+"</td>\n" +
						"                            <td>"+activity.startDate+"</td>\n" +
						"                            <td>"+activity.endDate+"</td>\n" +
						"                        </tr>";
			}
			$('#activityBody').html(content);

			//分页js 100 11 10  count % pageSize == 0 ? count / pageSize : count / pageSize + 1
			$("#activityPage").bs_pagination({
				currentPage: data.t.pageNum, // 页码
				rowsPerPage: data.t.pageSize, // 每页显示的记录条数
				maxRowsPerPage: 20, // 每页最多显示的记录条数
				totalPages: data.t.pages, // 总页数
				totalRows: data.t.total, // 总记录条数
				visiblePageLinks: 3, // 显示几个卡片
				showGoToPage: true,
				showRowsPerPage: true,
				showRowsInfo: true,
				showRowsDefaultInfo: true,
				onChangePage : function(event, obj){
					refresh(obj.currentPage,obj.rowsPerPage);
					//pageList(obj.rowsPerPage,obj.currentPage);
				}
			});
		},'json');
	}

	//条件查询的日历插件
	$("#startTime").datetimepicker({
		language:  "zh-CN",
		format: "yyyy-mm-dd",//显示格式
		minView: "month",//设置只显示到月份
		initialDate: new Date(),//初始化当前日期
		autoclose: true,//选中自动关闭
		todayBtn: true, //显示今日按钮
		clearBtn : true,
		pickerPosition: "bottom-left"
	});

	$("#endTime").datetimepicker({
		language:  "zh-CN",
		format: "yyyy-mm-dd",//显示格式
		minView: "month",//设置只显示到月份
		initialDate: new Date(),//初始化当前日期
		autoclose: true,//选中自动关闭
		todayBtn: true, //显示今日按钮
		clearBtn : true,
		pickerPosition: "bottom-left"
	});

	//新建市场活动日期
	$("#create-startTime").datetimepicker({
		language:  "zh-CN",
		format: "yyyy-mm-dd",//显示格式
		minView: "month",//设置只显示到月份
		initialDate: new Date(),//初始化当前日期
		autoclose: true,//选中自动关闭
		todayBtn: true, //显示今日按钮
		clearBtn : true,
		pickerPosition: "bottom-left"
	});

	$("#create-endTime").datetimepicker({
		language:  "zh-CN",
		format: "yyyy-mm-dd",//显示格式
		minView: "month",//设置只显示到月份
		initialDate: new Date(),//初始化当前日期
		autoclose: true,//选中自动关闭
		todayBtn: true, //显示今日按钮
		clearBtn : true,
		pickerPosition: "bottom-left"
	});


	//异步保存市场活动信息
	function createActivity() {
		$.post("/crm/workbench/activity/saveOrUpdateActivity",$('#createActivityForm').serialize(),
			function(data){
				if(data.ok){
					//弹出消息
					layer.alert(data.mess, {icon: 6});
					//关闭模特窗口
					$('#createActivityModal').modal('hide');
					//重置表单 只能用原生态的js函数,jquery没有重置表单功能
					document.getElementById("createActivityForm").reset();
					//刷新页面
					refresh(1,3);
				}
			},'json');
	}

	//复选框全选和全不选事件
	$('#father').click(function () {
		//方式一
		/*if($(this).prop('checked')){
			$('.son').each(function () {
				//attr:自定义属性 prop:固有属性 attribute <a name="" id="" href="">
				$(this).attr('checked',true);
			});
		}else{
			$('.son').each(function () {
				$(this).attr('checked',false);
			});
		}*/
		//方案二
		$('.son').each(function () {
			$(this).prop('checked',$('#father').prop('checked'));
		});
		checkedLength =  $('.son:checked').length;
	});

	//事件委托 动态生成的元素js会失效，把事件委托给父元素(也不能是动态生成)
	//son决定father是否勾中
	$('#activityBody').on('click','.son',function () {
		var checkedLength = $('.son:checked').length;
		var length =  $('.son').length;
		if(checkedLength == length){
			$('#father').prop('checked',true);
		}else{
			$('#father').prop('checked',false);
		}
	});
	var checkedLength = 0;
	$('#activityBody').on('click','.son',function () {
		checkedLength = $('.son:checked').length;
	});
	//点击修改按钮，触发判断是否能进行修改操作
	function enableEdit() {
		if(checkedLength > 1){
			//弹出消息
			layer.alert("只能选择一条记录", {icon: 5});
		}else if(checkedLength == 0){
			layer.alert("至少选择一条记录", {icon: 5});
		}else if(checkedLength == 1){
			//获取当前勾中的记录的主键，
			//jquery-->js $变量[0]/$变量.get(0) js-->jquery $(变量)
			//js对象
			var selectedId = $('.son:checked')[0].value;
			//发送异步请求从后台查询出数据
			$.get("/crm/workbench/activity/queryById",{'id':selectedId},function(data){
				$('#edit-marketActivityName').val(data.name);
				$('#edit-startTime').val(data.startDate);
				$('#edit-endTime').val(data.endDate);
				$('#edit-cost').val(data.cost);
				$('#edit-describe').text(data.description);
				//把id的值存在隐藏域中
				$('#activityId').val(data.id);

				//弹出模态窗口后，所有者第一条信息对应的是当前市场活动的所有者信息
				$('#edit-marketActivityOwner option').each(function () {
					if($(this).val() == data.owner){
						$(this).prop('selected',true);
					}
				});
			},'json');
			//手动弹出修改市场活动模态窗口
			$('#editActivityModal').modal('show');
		}
	}

	//异步修改市场活动
	function updateActivity() {
		$.post("/crm/workbench/activity/saveOrUpdateActivity",$('#editActivityForm').serialize(),
				function(data){
					if(data.ok){
						//弹出消息
						layer.alert(data.mess, {icon: 6});
						//关闭模态窗口
						$('#editActivityModal').modal('hide');
						//刷新页面
						refresh(1,3);
					}
				},'json');
	}
	
	//点击删除按钮删除市场活动
	function deleteActivity() {
		layer.confirm('确定要删除该条记录吗？', {
			btn: ['确定','取消'] //按钮
		}, function(){
			//点击确定按钮触发的事件
			if(checkedLength > 1){
				//弹出消息
				layer.alert("只能选择一条记录", {icon: 5});
			}else if(checkedLength == 0){
				layer.alert("至少选择一条记录", {icon: 5});
			}else if(checkedLength == 1){
				//获取当前勾中的记录的主键，
				//jquery-->js $变量[0]/$变量.get(0) js-->jquery $(变量)
				//js对象
				var selectedId = $('.son:checked')[0].value;
				//发送异步请求从后台查询出数据
				$.get("/crm/workbench/activity/deleteActivity",{'id':selectedId},function(data){
					if(data.ok){
						layer.alert(data.mess, {icon: 6});
						//刷新页面
						refresh(1,3);
					}
				},'json');
			}
		}, function(){
			return;
		});


	}
</script>
</body>
</html>