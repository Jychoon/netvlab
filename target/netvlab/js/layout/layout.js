var jsonStr='';
if(sessionStorage.getItem('jsonStr') != null){
    jsonStr = sessionStorage.getItem('jsonStr');
}
var tree;
var topology = null;
var imagespath = {
	background:"images/topo/background.png",
	connector:"images/topo/connector.png",
	router:"images/device/Router_Icon.png",
	centos:"images/device/Centos_Icon.png",
	winxp:"images/device/WinXp_Icon.png",
	switch:"images/device/Switch_Icon.png",
    extranet:"images/device/Extranet.png"
};

$(function () {
    $(document).keydown(function(event){
        event=event||window.event;
        //Alt+F4
        if((event.altKey)&&(event.keyCode==115))
        {
            //将关闭时间转给另外对话框
            window.showModelessDialog("about:blank","","dialogWidth:0px;dialogheight:0px");
            var r = confirm("关闭页面将结束实验，您确定要关闭吗?")
            if(r==true)
            {
                return true;
            }else{
                return false;
            }
        }else if ( event.keyCode==116)	//F5
        {
            event.keyCode = 0;
            event.cancelBubble = true;
            return false;
        }
    });
	//init topology
	topology = new Kinetic.Topology({
		container:"container",
		width:$("#centerTabs").width(),
		height:2000,
		backgroundImage:imagespath.background,
        connectorImage:imagespath.connector,
		toolbar:{
			container:"toolbar",
			data:[
				{id:"router",name:"ROUTER",image:imagespath.router,width:32,height:32},
				{id:"centos",name:"ApplicationServer",image:imagespath.centos,width:32,height:32},
				{id:"winxp",name:"WinXP",image:imagespath.winxp,width:32,height:32},
				{id:"switch",name:"Switch",image:imagespath.switch,width:32,height:32}
			]
		}
	});
	topology.load(jsonStr);
    InitializeCatalog();
	$("#menuSave").click(saveJson);
	$("#menuLoad").click(loadJson);
	$("#menuClear").click(clearTopo);
	$("#menuHelp").click(help);
	$("#menuSubmit").click(submitTopo);
	$("#menuRelease").click(releaseTopo);
	$("#logout").click(logout);
	$("#topoList").change(changeTopo);

    $("#menuRelease").hide();
    changeTopo();
});
function submitTopo() {
    /*$('.subnet').each(function(){
        var vl=$(this).val();
        if(vl=='undefined'||vl==''||vl==null) //还有undefined还有去空格的什么的还是用原来的
        {
            $.messager.alert("操作提示", "请点击连线设置子网网段！","warning");
            flag=false;
        }
    });*/
    var inpuJsonData = topology.toJson();
    console.log(inpuJsonData);
    console.log($("#topoList").val())
    var ajaxOptions = new AJAXOptions();
    ajaxOptions.dataType = "json";
    ajaxOptions.async = true;
    ajaxOptions.url = "/netvlab/ajax/topo_submit";
    ajaxOptions.data = {
        "inpuJsonData":inpuJsonData,
        "experimentname":$("#topoList").val()
    }
    ajaxOptions.success = submitSuccess;
    ajaxOptions.error = function(){
        console.log("error...");
    }
    ajaxOptions.complete = function(XMLHttpRequest,status){
        $.messager.progress('close');
        var message;
        if(status == 'success'){

        }else{
            if(status == 'error'){
                message = '服务器内部错误，请联系管理员！'
            }else if(status == 'timeout'){
                message = '请求超时，请稍后重试!'
            }
            $.messager.show({
                title:'友情提示',
                msg:message,
                timeout:3000,
                showType:'slide'
            });
        }
    }
    $.ajax(ajaxOptions);
    $.messager.progress( {
        text:'正在申请资源...'
    });
    $.messager.progress('bar').addClass('pro');
}
function submitSuccess(data) {
    console.log("success...");
    device2Url = data;
    var returnJsonData = JSON.parse(JSON.stringify(data));
    var result = returnJsonData["result"];
    var message;
    if(result == 'notlogin'){
        window.location.href("login.html");
        message = null;
    }else if(result == 'busy'){
        message = '服务器忙，请稍后重试!'
    }else if(result == 'success'){
        message = '资源分配成功！'
        $("#menuSubmit").hide();
        $("#menuRelease").show();
        //记录开始实验时间
        function getNow(s) {
            return s < 10 ? '0' + s: s;
        }
        var myDate = new Date();
        //获取当前年
        var year=myDate.getFullYear();
        //获取当前月
        var month=myDate.getMonth()+1;
        //获取当前日
        var date=myDate.getDate();
        var h=myDate.getHours();       //获取当前小时数(0-23)
        var m=myDate.getMinutes();     //获取当前分钟数(0-59)
        var s=myDate.getSeconds();
        var now="提交时间：" + getNow(month)+"-"+getNow(date)+" "+getNow(h)+':'+getNow(m)+":"+getNow(s)+"   ";
        $('#submittime').html(now);
        $('#releasetime').html('');
    }else if(result == 'noSwitchNetwork'){
        message = '交换机网络未设置子网地址!';
    }else if(result == 'noSubnet'){
        message = '子网网段未设置!';
    }else if(result == 'createNetFailed'){
        message = '网络创建失败！';
    }else if(result == 'createSubFailed'){
        message = '网络创建失败！';
    }else if(result == 'createServerFailed'){
        message = '虚拟机创建失败！';
    }
    if(message != null){
        $.messager.show({
            title:'友情提示',
            msg:message,
            timeout:3000,
            showType:'slide'
        });
    }

}
function releaseTopo() {
    var inputJsonData = '';
    var ajaxOptions = new AJAXOptions();
    ajaxOptions.dataType = "json";
    ajaxOptions.async = true;
    ajaxOptions.url = "/netvlab/ajax/topo_release";
    ajaxOptions.data = {
        "inpuJsonData":inputJsonData
    }
    ajaxOptions.success = function(data){
        var returnJsonData = JSON.parse(JSON.stringify(data));
        console.log("returnJsonData:" + returnJsonData);
        var result = returnJsonData["result"];
        if(result == 'notLogin'){
            window.location.href("login.html");
        }
    }
    ajaxOptions.error = function(){
        console.log("release error...");
    }
    ajaxOptions.complete = function(XMLHttpRequest,status){
        console.log(status);
        //结束实验时间
        function getNow(s) {
            return s < 10 ? '0' + s: s;
        }
        var myDate = new Date();
        //获取当前年
        var year=myDate.getFullYear();
        //获取当前月
        var month=myDate.getMonth()+1;
        //获取当前日
        var date=myDate.getDate();
        var h=myDate.getHours();       //获取当前小时数(0-23)
        var m=myDate.getMinutes();     //获取当前分钟数(0-59)
        var s=myDate.getSeconds();
        var now="结束时间：" + getNow(month)+"-"+getNow(date)+" "+getNow(h)+':'+getNow(m)+":"+getNow(s)+"  ";
        $('#releasetime').html(now);
        $.messager.progress('close');
        var message;
        if(status == 'success'){
            message = '资源释放成功!';
        }else{
            if(status == 'error'){
                message = '服务器内部错误，请联系管理员！'
            }else if(status == 'timeout'){
                message = '请求超时。'
            }
        }
        $.messager.show({
            title:'友情提示',
            msg:message,
            timeout:3000,
            showType:'slide'
        });
        $("#menuSubmit").show();
        $("#menuRelease").hide();
    }
    $.ajax(ajaxOptions);
    $.messager.progress( {
        text:'正在释放资源...'
    });
    $.messager.progress('bar').addClass('pro');
}
function logout() {
    if($("#menuSubmit").is(":hidden")){
        alert("您有资源未释放,请先释放资源!");
        return;
    }
    var ajaxOptions = new AJAXOptions();
    ajaxOptions.async = true;
    ajaxOptions.url = "/netvlab/logout";
    ajaxOptions.complete = function(XMLHttpRequest,status){
        console.log("logout complete status :" + status);
        window.location.href = "login.html";
    }
    $.ajax(ajaxOptions);
}
function saveJson() {
    jsonStr=topology.toJson();
    var options = {
        title: "提示",
        msg: jsonStr,
        showType: "slide",
        timeout: 3000,
        /*width: 250,
        height: 80*/
        width: 600,
        height: 500
    };
    $.messager.show(options);
}
function loadJson(){
    topology.load(jsonStr);
    var options = {
        title: "提示",
        msg: "已加载上一次保存的拓扑图",
        showType: "slide",
        timeout: 3000,
        width: 250,
        height: 80
    };
    $.messager.show(options);
}
function clearTopo() {
    var jsonStrNull='{"devices":[],"lines":[]}';
    topology.load(jsonStrNull);
    var options = {
        title: "提示",
        msg: "拓扑图已清空",
        showType: "slide",
        timeout: 3000,
        width: 250,
        height: 80
    };
    $.messager.show(options);
}
function help() {
    window.open('expbook/help.pdf');
}
function InitializeCatalog() {
	var data = [
		{
			"text":"操作配置类",
			"children":[
				{
                    "text": '常用网络命令',
                    "attributes":{
                        "url":"expbook/1-1NetworkCommand.pdf"
                    }
				},{
                    "text": "配置HTTP服务器",
                    "attributes":{
                        "url":"expbook/1-2ConfigureHttp.pdf"
                    }
				}
			]
		},{
            "text": '数据链路层',
            "children":[
                {
                    "text": '以太网帧分析',
                    "attributes":{
                        "url":"expbook/2-1EthernetFrameAnalysis.pdf"
                    }
                }
			]
		},{
            "text": "网络层",
            "children": [
                {
                    "text": 'ARP',
                    "attributes":{
                        "url":"expbook/3-1ARP.pdf"
                    }
                },{
                    "text": 'IP&ICMP',
                    "attributes":{
                        "url":"expbook/3-2IpAndICMP.pdf"
                    }
                },{
                    "text": '路由表学习与配置',
                    "attributes":{
                        "url":"expbook/3-3RoutingTableLearn.pdf"
                    }
                },{
                    "text": 'RIP实验',
                    "attributes":{
                        "url":"expbook/3-4RIP.pdf"
                    }
                },{
                    "text": 'OSPF实验',
                    "attributes":{
                        "url":"expbook/3-5OSPF.pdf"
                    }
                },{
                    "text": 'IPV6实验',
                    "attributes":{
                        "url":"expbook/3-6IPV6.pdf"
                    }
                },{
                    "text": 'IGMP实验',
                    "attributes":{
                        "url":"expbook/3-7IGMP.pdf"
                    }
                }
			]
		},{
            "text":"传输层",
            "children":[
                {
                    "text": 'TCP实验',
                    "attributes":{
                        "url":"expbook/4-1TCPProtocolAnalysis.pdf"
                    }
                },{
                    "text": 'UDP实验',
                    "attributes":{
                        "url":"expbook/4-2UDPProtocolAnalysis.pdf"
                    }
                },{
                    "text": 'NAT虚拟服务器实验',
                    "attributes":{
                        "url":"expbook/4-3NAT.pdf"
                    }
                }
            ]
        },{
            "text": "应用层实验",
            "children": [
            	{
					"text": 'DHCP实验',
					"attributes":{
						"url":"expbook/5-1DHCP.pdf"
                	}
				},{
					"text": 'HTTP实验',
					"attributes":{
						"url":"expbook/5-2HTTP.pdf"
					}
				},{
					"text": 'FTP实验',
					"attributes":{
						"url":"expbook/5-3FTP.pdf"
					}
				},{
					"text": 'DNS实验',
					"attributes":{
						"url":"expbook/5-4DNS.pdf"
					}
				},{
					"text": 'SMTP&POP3实验',
					"attributes":{
						"url":"expbook/5-5SMTP&POP3.pdf"
					}
				}
            ]
		},{
	        "text":"其它",
            "children":[
                {
                    "text": '代理机制实验',
                    "attributes":{
                        "url":"expbook/6-1Agency.pdf"
                    }
                },{
                    "text": 'NetBIOS实验',
                    "attributes":{
                        "url":"expbook/6-1Agency.pdf"
                    }
	            },{
                    "text":"网络通讯程序设计实验",
                    "attributes":{
                        "url":"expbook/6-1Agency.pdf"
                    }
                },{
                    "text":"路由欺骗实验",
                    "attributes":{
                        "url":"expbook/6-1Agency.pdf"
                    }
                },{
                    "text":"冲突与网络广播风暴实验",
                    "attributes":{
                        "url":"expbook/6-1Agency.pdf"
                    }
                },{
                    "text":"路由环与网络回路实验",
                    "attributes":{
                        "url":"expbook/6-1Agency.pdf"
                    }
                }
            ]
        }
	];
    tree = $('#catalog').tree({
        data: data,
        lines:true,
        onClick: function(node){
            addTab(node);
        }
    });
}
function addTab(node){
    var centerTabs = $("#centerTabs");
    if (centerTabs.tabs('exists',node.text)) {
        centerTabs.tabs('select',node.text)
    } else {
        if (node.attributes && node.attributes.url && node.attributes.url.length > 0) {
            /*window.open(node.attributes.url,'实验指导书','');*/
            centerTabs.tabs('add',{
                title:node.text,
                closable:true,
                fit:true,
                content:'<iframe src="'+node.attributes.url+'" frameborder="0" style="border:0;padding:5px;margin:5px;width:98%;height:100%;overflow:hidden;"></iframe>'
            });
        } else {
            if (node.state=='open') {
                $('#experienceName').tree('collapse',node.target);
            } else {
                $('#experienceName').tree('expand',node.target);
            }
        }
    }
}
function changeTopo() {
	var topo = $("#topoList").val();
	switch (topo) {
	    //**************
        case "自定义实验":
            jsonStr='{"devices":[],"lines":[]}';
            break;
        //************
        case "示例":
            jsonStr='{"devices":[{"id":"router_2C1DDE","name":"ROUTER","src":"images/device/Router_Icon.png","x":411,"y":45,"width":78,"height":49},{"id":"router_8C4862","name":"ROUTER","src":"images/device/Router_Icon.png","x":547,"y":121,"width":78,"height":49},{"id":"switch_C4B9FE","name":"Switch","src":"images/device/Switch_Icon.png","x":263,"y":114,"width":88,"height":47},{"id":"pc_AE9612","name":"WinXP","src":"images/device/WinXp_Icon.png","x":160,"y":236,"width":96,"height":73},{"id":"applicationServer_998BF1","name":"ApplicationServer","src":"images/device/Centos_Icon.png","x":397,"y":227,"width":62,"height":73},{"id":"pc_0D766D","name":"WinXP","src":"images/device/WinXp_Icon.png","x":599,"y":238,"width":96,"height":73}],"lines":[{"srcDeviceId":"router_2C1DDE","dstDeviceId":"router_8C4862","stroke":"black","strokeWidth":1},{"srcDeviceId":"router_2C1DDE","dstDeviceId":"switch_C4B9FE","stroke":"black","strokeWidth":1},{"srcDeviceId":"switch_C4B9FE","dstDeviceId":"pc_AE9612","stroke":"black","strokeWidth":1},{"srcDeviceId":"switch_C4B9FE","dstDeviceId":"applicationServer_998BF1","stroke":"black","strokeWidth":1},{"srcDeviceId":"router_8C4862","dstDeviceId":"pc_0D766D","stroke":"black","strokeWidth":1}]}';
            break;
        //**********
        case "RIP":
        case "OSPF":
            jsonStr='{"devices":[{"id":"router_88E8AA","name":"ROUTER","src":"images/device/Router_Icon.png","x":200,"y":55,"width":78,"height":49},{"id":"router_EED3B2","name":"ROUTER","src":"images/device/Router_Icon.png","x":361,"y":189,"width":78,"height":49},{"id":"router_D2F424","name":"ROUTER","src":"images/device/Router_Icon.png","x":526,"y":49,"width":78,"height":49},{"id":"pc_08C80E","name":"WinXP","src":"images/device/WinXp_Icon.png","x":352,"y":341,"width":96,"height":73},{"id":"pc_077E39","name":"WinXP","src":"images/device/WinXp_Icon.png","x":118,"y":230,"width":96,"height":73},{"id":"pc_C98303","name":"WinXP","src":"images/device/WinXp_Icon.png","x":588,"y":237,"width":96,"height":73}],"lines":[{"srcDeviceId":"router_88E8AA","dstDeviceId":"router_EED3B2","stroke":"black","strokeWidth":1},{"srcDeviceId":"router_88E8AA","dstDeviceId":"router_D2F424","stroke":"black","strokeWidth":1},{"srcDeviceId":"router_EED3B2","dstDeviceId":"router_D2F424","stroke":"black","strokeWidth":1},{"srcDeviceId":"router_EED3B2","dstDeviceId":"pc_08C80E","stroke":"black","strokeWidth":1},{"srcDeviceId":"router_D2F424","dstDeviceId":"pc_C98303","stroke":"black","strokeWidth":1},{"srcDeviceId":"router_88E8AA","dstDeviceId":"pc_077E39","stroke":"black","strokeWidth":1}]}';
            break;
        //**************
        case "DNS":
        case "IP/ICMP":
        case "ARP":
        case "TCP协议分析":
        case "HTTP":
        case "以太网帧分析":
        case "配置http服务器":
            jsonStr='{"devices":[{"id":"pc_171E3F","name":"WinXP","src":"images/device/WinXp_Icon.png","x":314,"y":161,"width":96,"height":73},{"id":"applicationServer_21C49E","name":"ApplicationServer","src":"images/device/Centos_Icon.png","x":537,"y":180,"width":62,"height":73}],"lines":[{"srcDeviceId":"pc_171E3F","dstDeviceId":"applicationServer_21C49E","stroke":"black","strokeWidth":1}]}';
            break;
        //****************
        case "路由表学习与配置":
            jsonStr='{"devices":[{"id":"pc_A7FDF0","name":"WinXP","src":"images/device/WinXp_Icon.png","x":294,"y":240,"width":96,"height":73},{"id":"router_AC0689","name":"ROUTER","src":"images/device/Router_Icon.png","x":480,"y":125,"width":78,"height":49},{"id":"applicationServer_E7A4BA","name":"ApplicationServer","src":"images/device/Centos_Icon.png","x":669,"y":236,"width":62,"height":73}],"lines":[{"srcDeviceId":"pc_A7FDF0","dstDeviceId":"router_AC0689","stroke":"black","strokeWidth":1},{"srcDeviceId":"router_AC0689","dstDeviceId":"applicationServer_E7A4BA","stroke":"black","strokeWidth":1}]}';
            break;
        //****************
        case "SMTP":
            jsonStr ='{"devices":[{"id":"switch_75C2EF","name":"Switch","src":"images/device/Switch_Icon.png","x":445,"y":257,"width":88,"height":47,"eth1":"x.x.x.x","eth2":"x.x.x.x","eth3":"x.x.x.x"},{"id":"applicationServer_BB2172","name":"ApplicationServer","src":"images/device/Centos_Icon.png","x":446,"y":97,"width":62,"height":73,"eth1":"x.x.x.x","eth2":"x.x.x.x","eth3":"x.x.x.x"},{"id":"pc_BE0344","name":"WinXP","src":"images/device/WinXp_Icon.png","x":270,"y":358,"width":96,"height":73,"eth1":"x.x.x.x","eth2":"x.x.x.x","eth3":"x.x.x.x"},{"id":"pc_E07D11","name":"WinXP","src":"images/device/WinXp_Icon.png","x":617,"y":363,"width":96,"height":73,"eth1":"x.x.x.x","eth2":"x.x.x.x","eth3":"x.x.x.x"}],"lines":[{"srcDeviceId":"pc_BE0344","dstDeviceId":"switch_75C2EF","stroke":"black","strokeWidth":1},{"srcDeviceId":"switch_75C2EF","dstDeviceId":"pc_E07D11","stroke":"black","strokeWidth":1},{"srcDeviceId":"applicationServer_BB2172","dstDeviceId":"switch_75C2EF","stroke":"black","strokeWidth":1}]}';
            break;
        case "ShareServer":
            jsonStr = '{"devices":[{"id":"shareserver","name":"SHARESERVER","src":"images/device/shareServer.png","x":380,"y":177,"width":78,"height":49,"eth1":"x.x.x.x","eth2":"x.x.x.x","eth3":"x.x.x.x"}],"lines":[]}';
            break;
        case "Extranet":
            jsonStr = '{"devices":[{"id":"extranet","name":"EXTRANET","src":"images/device/Extranet.png","x":380,"y":177,"width":78,"height":49,"eth1":"x.x.x.x","eth2":"x.x.x.x","eth3":"x.x.x.x"}],"lines":[]}';
            break;
    }
    topology.load(jsonStr);
}
function deleteDevice(){
    $('#infoDialog').dialog('close');
    topology.deleteCurrentObject();
}