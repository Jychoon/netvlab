$(function(){
	$('#registered').bind('click',register);
});
function register(){

	if($("#studentId").val() == '' || $("#password").val() == ''
		|| $("#classes").val() == '' || $("#name").val() == ''){
		alert('输入不能为空！');
		return;
	}
	var ajaxOptions = new AJAXOptions();
	ajaxOptions.data = {
		"studentId":$("#studentId").val(),
		"name":$("#name").val(),
		"school":'CSU',
		"classes":$("#classes").val(),
		"password":$("#password").val()
	};

	ajaxOptions.url = '/netvlab/register';
	ajaxOptions.async=false;
	ajaxOptions.success=registerSuccess;
	$.ajax(ajaxOptions);
}
function registerSuccess(data){
	json = JSON.parse(JSON.stringify(data));
	var result = json["result"];
	if(result == 'userExist'){
		alert("账号已存在！");
	}else if(result == 'registerSuccess'){
		alert("注册成功！");
		window.location.href='login.html';
	}else{
		alert("注册失败，请重试！");
	}
}