<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.opensymphony.xwork2.ActionContext"%>
<%@ page import="java.util.Map" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>虚拟网络实验平台拓扑图</title>
	<link rel="stylesheet" href="js/jquery-easyui/themes/gray/easyui.css" type="text/css"></link>
	<link rel="stylesheet" href="js/jquery-easyui/themes/icon.css" type="text/css"></link>
	<link rel="stylesheet" href="css/layout.css" type="text/css"></link>
  </head>
  
  <!--<body style="background-color: #828282;">-->
<body>
		<!-- ------------菜单栏---------- -->
		<div class="region-north">
			<!--<img alt="虚拟网络实验平台拓扑图" src="../images/logo.png">
			--><!-- 菜单  -->
			<div class="menuTop">
				<span> <a href="javascript:void(0);" id="menuSave"
					class="easyui-linkbutton" iconCls="icon-save" plain="true">保存</a> <a
					
					href="javascript:void(0);" id="menuLoad" class="easyui-linkbutton"
					iconCls="icon-load" plain="true">加载</a> <a
					
					href="javascript:void(0);" id="menuClear" class="easyui-linkbutton"
					iconCls="icon-remove" plain="true">清空</a> <a
					
					href="javascript:void(0);" id="menuHelp" class="easyui-linkbutton"
					iconCls="icon-help" plain="true">帮助</a> <a
					
					href="javascript:void(0);" id="menuSubmit" class="easyui-linkbutton"
					iconCls="icon-ok" plain="true">提交实验</a> <a
					
					href="javascript:void(0);" id="menuRelease" class="easyui-linkbutton"
					iconCls="icon-no" plain="true">结束实验</a> 
				</span>
					
				<span style="float:right;">
					<span id="submittime" style="color:blue"></span>
					<span id="releasetime" style="color:blue"></span>
					<s:if test="#session.user==null">未登录</s:if>
					<s:else>欢迎您,<s:property value="#session.user.name"></s:property> </s:else>
					&nbsp;&nbsp;
					<!-- <a href="javascript:void(0);" id="changePwd" style="color:white" >修改密码</a> -->
					<a href="javascript:void(0);" id="logout" style="color:white" >注销</a>
				</span>
			</div>
		</div>
		<!-- ------------中心区域---------- -->
		<div id="content" class="easyui-layout">
			<!-- ------------组件+实验指导书---------- -->
			<div region="west" style="width: 182px;" title="">
				<!-- 组件 -->
				<div class="easyui-accordion">
					<div title="组件" border="false">
						<div id="toolbar"></div>
					</div>
				</div>
				<!-- 实验指导书 -->
				<div class="easyui-accordion">
					<div title="实验指导书" selected="true" border="false" style="height:600px;" fit="true">
						<ul id="catalog">

						</ul>
					</div>
				</div>
			</div>
			<!-- ------------切换拓扑图----------- -->
			<div region="center" style="overflow: hidden;">
				<div id="selectSpan" style="position: absolute; top:5px; right: 10px; z-index: 999;">
					<!-- 实验拓扑图select -->
					<span> 实验拓扑图： <select id="topoList">
							<option value="示例" selected="selected">
								拓扑示例
							</option>
							<option value="配置http服务器">
								配置http服务器
							</option>
							<option value="以太网帧分析">
								以太网帧分析
							</option>
							<option value="ARP">
								ARP
							</option>
							<option value="IP/ICMP">
								IP/ICMP
							</option>
							<option value="路由表学习与配置">
								路由表学习与配置
							</option>
							<option value="RIP">
								RIP
							</option>
							<option value="OSPF">
								OSPF
							</option>
                            <option value="IPV6">
								IPV6
							</option>
							<option value="TCP">
								TCP
							</option>
                            <option value="UDP">
								UDP
							</option>
                            <option value="NAT虚拟服务器">
								NAT
							</option>
							<option value="HTTP">
								HTTP
							</option>
							<option value="DNS">
								DNS
							</option>
							<option value="SMTP">
								SMTP
							</option>
							<%--<option value="ShareServer">
								共享服务器
							</option>
							<option value="Extranet">
								外网
							</option>--%>

							<option value="自定义实验">
								自定义实验
							</option>
						</select> </span>
				</div>
				<!-- ----------Topo----------- -->
				<div id="centerTabs" class="easyui-tabs" style="overflow: hidden;" border="false" >
					<div title="网络拓扑图" id="container" style="overflow: hidden;" border="false"></div>
				</div>
			</div>
		</div>

		<div id="footer">
			联系地址: 中南大学计算机学院 邮编: 410083 E-mail:taihangyang@csu.edu.cn<br>
			版权所有  @中南大学计算机学院 2013-2019<br><br><br>
		</div>
			
		<!-- 设备信息 -->
		<div id="infoDialog" style="display: none">
			<div id="infoContent">

			</div>
		</div>

		<!-- ChangeIPDialog -->
		<div id = "changIPDialog" style="display: none">
			<div id ="ipInfo"></div>
		</div>

		<script type="text/javascript" src="js/jquery-easyui/jquery-1.7.2.min.js"></script>
		<script type="text/javascript" src="js/jquery-easyui/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="js/jquery-easyui/easyui-lang-zh_CN.js"></script>
		<script type="text/javascript" src="js/layout/json2.js"></script>
		<script type="text/javascript" src="js/layout/uuid.js"></script>
		<script type="text/javascript" src="js/layout/kinetic-v3.10.4.js"></script>
		<script type="text/javascript" src="js/layout/kinetic_topology.js"></script>
		<script type="text/javascript" src="js/layout/common.js"></script>
		<script type="text/javascript" src="js/layout/layout.js"></script>
	</body>
</html>
