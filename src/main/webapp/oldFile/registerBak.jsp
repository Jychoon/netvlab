<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.opensymphony.xwork2.ActionContext"%>
<%@ page import="java.util.Map" %>
<%@ page import="com.netvlab.model.Userinfo" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
Map<String, Object> sessionContext = ActionContext.getContext().getSession();
Userinfo user = (Userinfo) sessionContext.get("user");
sessionContext.clear();
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html xmlns="http://www.w3.org/1999/xhtml">
	
	<head><meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
				
		<title>中南大学计算机网络实验平台</title>
		
		<script type="text/javascript" src="js/jquery-easyui/jquery-1.7.2.min.js"></script>
		<!--<script type="text/javascript">
			$(document).ready(function() {
				if (!($.browser.safari || $.browser.mozilla)) {
					$("form").hide();
					$("<div class='error' />").html("<h2>Browser Not Supported</h2>The Treemo Labs Dashboard utilizes cutting-edge browser technologies.<br /><br />We currently only support <ul><li>Firefox (version &gt; 3.5 recommended)</li><li>Safari (version &gt; 3 recommended)</li></ul>").appendTo('#login-content');
				}
			});
		</script>
		
		-->
		<style type="text/css">
			* {
				font-family:"Microsoft YaHei","Helvetica Neue", Helvetica, Arial, sans-serif;
				font-size:  16px ;
				font-weight: bold;
			}
			
			body {
				margin: 0;
				padding: 0;
				color: #fff;
				background: url('images/login/bg-login.png') repeat #1b1b1b;
				font-size: 14px;
				text-shadow: #050505 0 -1px 0;
				font-weight: bold;
			}
			
			li {
				list-style: none;
			}
			
			#dummy {
				position: absolute;
				top: 0;
				left: 0;
				border-bottom: solid 3px #777973;
				height: 250px;
				width: 100%;
				background: url('images/login/bg-login.png') repeat #fff;
				z-index: 1;
			}
			
			#dummy2 {
				position: absolute;
				top: 0;
				left: 0;
				border-bottom: solid 2px #545551;
				height: 252px;
				width: 100%;
				background: transparent;
				z-index: 2;
			}
			
			#login-wrapper {
				margin: 0 0 0 -160px;
				width: 320px;
				text-align: center;
				z-index: 99;
				position: absolute;
				top: 0;
				left: 50%;
			}
			
			#login-top {
				height: 120px;
				padding-top: 140px;
				text-align: center;
				margin-left: -44px;
			}
			
			#changePwd-content{
				position: absolute;
				top: 260px;
			}

			label {
				width: 70px;
				float: left;
				padding: 8px;
				height: 14px;
				line-height: 14px;
				margin-top: 6px;
			}
			
			input.text-input {
				width: 200px;
				float: right;
				-moz-border-radius: 4px;
                -webkit-border-radius: 4px;
				border-radius: 4px;
				background: #fff;
				border: solid 1px transparent;
				color: #555;
				padding: 8px;
				font-size: 13px;
			}
			
			input.button {
				float: right;
				padding: 6px 10px;
				color: #fff;
				font-size: 14px;
				background: -webkit-gradient(linear, 0% 0%, 0% 100%, from(#a4d04a), to(#459300));
				text-shadow: #050505 0 -1px 0;
				background-color: #459300;
				-moz-border-radius: 4px;
                -webkit-border-radius: 4px;
				border-radius: 4px;
				border: solid 1px transparent;
				font-weight: bold;
				cursor: pointer;
				letter-spacing: 1px;
			}
			
			input.button:hover {
				background: -webkit-gradient(linear, 0% 0%, 0% 100%, from(#a4d04a), to(#a4d04a), color-stop(80%, #76b226));
				text-shadow: #050505 0 -1px 2px;
				background-color: #a4d04a;
				color: #fff;
			}
			
			div.error {
				padding: 8px;
				background: rgba(52, 4, 0, 0.4);
				-moz-border-radius: 8px;
                -webkit-border-radius: 8px;
				border-radius: 8px;
				border: solid 1px transparent;
				margin: 6px 0;
			}
		</style>		
	</head>
  
	<body id="changePwd">
		
		<div id="login-wrapper" class="png_bg">
			<div id="login-top">
				<img src="images/login/logo.png" alt="中南大学计算机网络实验平台" title="中南大学计算机网络实验平台">
			</div>
			
			<div id="changePwd-content">
					<p>
						<label>用户名</label>
						<input id="studentId" class="text-input" type="text">
					</p>
					<br style="clear: both;">
					<p>
						<label>昵称</label>
						<input id="name" class="text-input" type="text">
					</p>
					<br style="clear: both;">
					<p>
						<label>学校</label>
						<input id="school" class="text-input" type="text">
					</p>
					<br style="clear: both;">
					<p>
						<label>班级</label>
						<input id="classes" class="text-input" type="text">
					</p>
					<br style="clear: both;">
					<p>
						<label>密码</label>
						<input id="password" class="text-input" type="password">
					</p>
					<br style="clear: both;">

					<a href="javascript:void(0);" id="register" plain="true">
						<p>
							<input class="button" type="submit" value="注册" align="left">
						</p>
					</a>
			</div>
		</div>
		<div id="dummy"></div>
		<div id="dummy2"></div>
		<script type="text/javascript"
			src="js/jquery-easyui/jquery-1.7.2.min.js"></script>
		<script type="text/javascript"
			src="js/jquery-easyui/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="js/layout/common.js"></script>
  		<script type="text/javascript" src="js/register/register.js"></script>
</body></html>