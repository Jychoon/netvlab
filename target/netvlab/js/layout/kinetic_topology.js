/** 
类名：网络拓扑图
描述：网络拓扑图主类
constructor:
{
	container: "container",
    width: 578,
    height: 200,
    backgroundImage:"../images/background.png",
    connectorImage:"../images/connector.png",
    toolbar:{
     	container:'toolbar',
     	data:[
	    	{id:'router',name:'路由器',image:'Router_Icon_128x128.png',width:32,height:32}
	    ]
    },
    popmenu:{
     	container:'mm',
     	data:[
	    	{id:'menu_1',name:'删除连接线',onclick:function(evt,instance){instance.deleteCurrentLine()},filter:"line"},
	    	{id:'menu_2',name:'删除设备',onclick:function(evt,instance){instance.deleteCurrentLine(),filter:"device"}},
	    	{id:'menu_1',name:'保存',onclick:function(evt,instance){alert("save")},filter:"all"}
	    ]
    }
}
*/

$('#frame11').onload = function(){
    $('body').onkeydown = function(){
        $('#frame11').focus();
    };
};
var bobo = null;
var device2Url = '';
var debugMode=true;
//主类
Kinetic.Topology = Kinetic.Class.extend({
	config:null,
	stage:null,
	layer:null,
	messageLayer:null,
	debugMode:false,
	toolbar:null,
	backGround:null,
	connector:null,
	devices:null,
	lines:null,
	loading:null,
	popmenu:null,
	currentObject:null,
	init: function(config) {
		this.config=config;
		this.devices=new Array();
		this.lines=new Array();
		this.toolbar=new Kinetic.Topology.Toolbar({
			toolbar:this.config.toolbar
		});
		
		this.stage = new Kinetic.Stage({
          container: config.container,
          width: config.width,
          height: config.height
        });
        this.layer = new Kinetic.Layer();
        this.stage.add(this.layer);
        this.messageLayer = new Kinetic.Layer();
        this.stage.add(this.messageLayer);
        this.backGround=new Kinetic.Topology.Background({
        	topology:this,
		    backgroundImage:this.config.backgroundImage
        });
        $('#'+config.container).get(0).topology=this;
        $('#'+config.container).droppable({//定义绘图区的事件
				onDragEnter:function(){
					$(this).addClass('over');
				},
				onDragLeave:function(){
					$(this).removeClass('over');
				},
				onDrop:function(e,source){
					var position = $("#"+$(source).attr("id")+"_proxy").position();
					$(this).removeClass('over');
					var img=$("img",$(source));
					var device=new Kinetic.Topology.Device({
						topology:this.topology,
						data:{id:img.attr("id")+"_"+sequence.nextVal(),name:img.attr("title"),src:img.attr("src"),
							//x:this.topology.getStage().getWidth()/2,y:this.topology.getStage().getHeight() / 2,width:'auto',height:'auto'}
							x:position.left-$("#"+this.topology.config.toolbar.container).width(),y:position.top,width:'auto',height:'auto'}
					});
					this.topology.addDevice(device);
				}
			});
		this.connector=new Kinetic.Topology.Device.Connector({
			topology:this,
			src:this.config.connectorImage
		}); 
		this.loading=false;
		this.layer.on("mouseup", function(e) {
	      
        });
    },
    containLine:function(srcDevice,dstDevice){
    	var line=this.stage.get("#"+srcDevice.getId()+"_"+dstDevice.getId());
    	if(line.length>0)
    	{
    		return true;
    	}
    	line=this.stage.get("#"+dstDevice.getId()+"_"+srcDevice.getId());
    	if(line.length>0)
    	{
    		return true;
    	}
    	return false;
    },
    deleteCurrentObject:function(){
    	this.currentObject.remove();
    },
    //点击设备全屏访问后新开远程tab
    visitCurrentObject:function(devName){
        url = this.currentObject.id;
        json = JSON.parse(JSON.stringify(device2Url));
        var id = json[url];
		$.ajax({
			type:"post",
            dataType:"json",
			data:{
				"deviceID":url,
                "serverID":id
			},
			url:"/netvlab/ajax/topo_getConsole",
			success:function(data){
                var returnJsonData = JSON.parse(JSON.stringify(data));
                var consoleURL = returnJsonData["consoleURL"];
                window.open(consoleURL,"_blank");
			},
			error:function(){
				alert('资源分配中，请耐心等待！');
			}
		});
    },
    //IP信息弹窗
    showIP:function(devName){
    	//首先获取网卡信息
    	url = this.currentObject.id;
        json = JSON.parse(JSON.stringify(device2Url));
        var id = json[url];
		$.ajax({
			type:"post",
            dataType:"json",
			data:{
                "deviceID":url,
                "serverID":id
			},
			url:'/netvlab/ajax/topo_getServerIp',
			success:function(data){
				var interfaces = data["interfaces"];
				console.log(interfaces)
                var ipInfo = '<fieldset><legend>原IP</legend><table class="basicInfoTable">'
                    +'<tr><th>设备名称:</th><td>'+url;
                ipInfo +='<tr><th>网卡:</th><td><select id="selectIP">';
                for (var i=0;i<interfaces.length;i++)
                {
                   ipInfo +='<option value="'+i+'">网卡'+i+'</option>'
                }
                ipInfo += '</select></td></tr><tr><th>IP地址:</th><td id= "interfaceIP">'
                         +interfaces[0]["fixedIps"][0]["ipAddress"]+'</td></tr>';
                ipInfo += '</td></tr></table></fieldset>';
                $('#changIPDialog').show();
                $("#ipInfo").html(ipInfo);
		            
                $("#selectIP").change(function() {
                    var selectVal = $("#selectIP").val();
                    console.log(selectVal);
                    $("#interfaceIP").html(interfaces[selectVal]["fixedIps"][0]["ipAddress"]);
                });

				$('#changIPDialog').dialog({
		            title: 'IP信息', 
		            width: 400,   
		            //height: 500,
		            closed: false,   
		            cache: false,
		            draggable:false,      
		            modal: true,
		            buttons:[
		                {
                            text:'取消',
                            handler:function(){
                                $('#changIPDialog').dialog('close');
                            }
		                }
		            ]
		        });
			},
			error:function(){
				alert('服务器内部错误，请进入虚拟机查看IP.');
			}
		});	
    },
    //修改IP Ajax
    ipChange:function(portID,ip,serverId,netID){
    	var inputs = {
            	"portID":portID,
            	"ip":ip,
            	"serverID":serverId,
            	"netID":netID
            };
		$.ajax({
			type:'POST',
			data:{
				dictData:JSON.stringify(inputs)
			},
			url:'../ajax/ChangeIpAction',
			success:function(data){
				if(data["result"] == 1){
					$('#changIPDialog').dialog('close');
					$.messager.show({
						title:'友情提示',
						msg:'IP修改成功!',
						timeout:3000,
						showType:'slide'
					});
				}else{
					 $.messager.alert("操作提示", "IP冲突或网段不正确，请重新输入IP地址！","warning");
				}
			},
			error:function(){
				alert('IP修改失败');
			}
		});	
    },
    getCurrentObject:function(){
    	return this.currentObject;
    },
    setCurrentObject:function(obj){
    	this.currentObject=obj;
    },
    getDevices:function(){
    	return this.devices;
    },
    addDevice:function(device){
    	this.devices.push(device);
    },
    getLines:function(){
    	return this.lines;
    },
    addLine:function(line){
    	this.lines.push(line);
    },
    getStage:function(){
    	return this.stage;
    },
    getLayer:function(){
    	return this.layer;
    },
    getConfig:function(){
    	return this.config;
    },
    writeMessage:function(message) {
    	if(this.debugMode)
    	{
	        var context = this.messageLayer.getContext();
	        this.messageLayer.clear();
	        context.font = "18pt Calibri";
	        context.fillStyle = "black";
	        context.fillText(message, 10, 25);
        }
    },
    getFitDevice:function(x,y){
    	for(var i=0;i<this.devices.length;i++)
    	{
    		var fireRange={
    			x:this.devices[i].deviceImage.getX(),
    			y:this.devices[i].deviceImage.getY(),
    			width:this.devices[i].deviceImage.getWidth(),
    			height:this.devices[i].deviceImage.getHeight()
    		};
    		if((x>=fireRange.x&&x<=fireRange.x+fireRange.width)
    		&&(y>=fireRange.y&&y<=fireRange.y+fireRange.height))
	    	{
	    		return this.devices[i];
	    	}
    	}
    	return null;
    },
    toJson:function(){
    	var jsonObj={
    		devices:[],
    		lines:[]
    	};
    	for(var i=0;i<this.devices.length;i++)
    	{
    		var device=this.devices[i];
    		var data=device.getConfig().data;
    		data.eth1 = device.deviceImage.attrs.eth1;
    		data.eth2 = device.deviceImage.attrs.eth2;
    		data.eth3 = device.deviceImage.attrs.eth3;
    		jsonObj.devices.push(data);
    	}
    	for(var i=0;i<this.lines.length;i++)
    	{
    		var line=this.lines[i];
    		var config=line.getConfig();
    		var srcDeviceId=config.srcDevice.getId();
    		var dstDeviceId=config.dstDevice.getId();
    		var stroke=config.stroke;
    		jsonObj.lines.push({
    			srcDeviceId:srcDeviceId,
    			dstDeviceId:dstDeviceId,
    			srcIf:config.srcnic,
    			dstIf:config.dstnic,
    			srcip:config.srcip,
    			dstip:config.dstip,
    			subnet_cidr:config.subnet,
    			stroke:stroke,
    			strokeWidth:config.strokeWidth
    		});
    	}
    	return JSON.stringify(jsonObj);
    },
    clear:function(){
		for(var i=0;i<this.devices.length;i++)
    	{//删除所有设备
    		var device=this.devices[i];
    		this.layer.remove(device.deviceImage);
    	}
    	for(var i=0;i<this.lines.length;i++)
    	{//删除所有线
    		var line=this.lines[i];
    		this.layer.remove(line.lineObject);
    	}
		this.layer.draw();
		this.devices=[];
		this.lines=[];
    },
    load:function(jsonStr){
    	this.loading=true;
    	if(jsonStr!=null&&jsonStr.length>0)
    	{
    		this.clear();
    		var jsonObj=JSON.parse(jsonStr);
    		var deviceMap=[];
			for(var i=0;i<jsonObj.devices.length;i++)
	    	{
	    		var data=jsonObj.devices[i];
	    		var device=new Kinetic.Topology.Device({
						topology:this,
						data:data
					});
				this.addDevice(device);
				deviceMap[device.getId()]=device;
	    	}
	    	this.loadLineAsync(this,jsonObj);
    	}
    },
    loadLineAsync:function(instance,jsonObj){
    	var flag=true;
    	for(var i=0;i<instance.devices.length;i++)
    	{
    		if(instance.devices[i].getId()==null)
    		{
    			flag=false;
    			break;
    		}
    	}
    	instance.writeMessage(flag);
    	if(flag)
    	{//设备都已绘制完毕，可以绘线了
    		this.loading=false;
    		this.fitSizeAuto();
    		for(var i=0;i<jsonObj.lines.length;i++)
	    	{
	    		var line=jsonObj.lines[i];
	    		var srcDevice=instance.getDeviceById(line.srcDeviceId);
	    		var dstDevice=instance.getDeviceById(line.dstDeviceId);
	    		if(srcDevice!=null&&dstDevice!=null)
	    		{
	    			new Kinetic.Topology.Line({
		      			topology:instance,
						srcDevice:srcDevice,
						dstDevice:dstDevice,
						stroke:line.stroke,
						strokeWidth:line.strokeWidth
		      		});
	    		}
	    	}
	    	
    	}
    	else
    	{
    		setTimeout(function(){instance.loadLineAsync(instance,jsonObj);},300);
    	}
    },
    getDeviceById:function(deviceId)
    {
    	for(var i=0;i<this.devices.length;i++)
    	{
    		var device=this.devices[i];
    		if(device.getId()==deviceId)
    		{
    			return device;
    		}
    	}
    	return null;
    },
    resize:function(width,height){
		this.stage.setSize(width, height);
        this.stage.draw();
        //每一次调整画布大小，都要重新记录一下LineImageData，不然mounseover事件无法正确激发
        for(var i=0;i<this.lines.length;i++)
        {
        	this.lines[i].lineObject.saveImageData();
        }
        this.getLayer().draw();
    },
    fitSizeAuto:function(){
    }
});
/**
类名：工具栏
描述：工具栏主类
constructor:
{
	topology:topology,
    backgroundImage:"../images/background.png",
}
*/
Kinetic.Topology.Background = Kinetic.Class.extend({
	config:null,
	init: function(config) {
		this.config=config;
		this.draw();
    },
    getConfig:function(){
    	return this.config;
    },
    draw:function(){
    	var imageObj = new Image();
    	var instance=this;
        imageObj.onload = function() {
		    var node = new Kinetic.Shape({
		          drawFunc: function(context){
		          	var pattern = context.createPattern(imageObj, "repeat");
		            context.rect(0, 0, instance.config.topology.getStage().getWidth(), instance.config.topology.getStage().getHeight());
	          		context.fillStyle = pattern;
		            context.fill();
		          }
		        });
	         instance.config.topology.getLayer().add(node);
	         instance.config.topology.getLayer().draw();
	         node.moveToBottom();
	         instance.config.topology.getLayer().draw();
        };
        imageObj.src =instance.config.backgroundImage;
    }
});
/**
类名：设备
描述：设备主类
constructor:
{
	topology:topology,
    data:{
    	id:"router",name:"路由器",src:"../images/router.png",x:0,y:0,width:'auto',height:'auto'
    }
    callbackObj:..
}
*/
Kinetic.Topology.Device = Kinetic.Class.extend({
    id:null,
    config:null,
	lines:null,
	deviceImage:null,
	fireRange:null,
	init: function(config) {
		this.config=config;
		this.draw();
		this.lines=new Array();
    },
    getConfig:function(){
    	return this.config;
    },
    getLines:function(){
    	return this.lines;
    },
    addLine:function(line){
    	this.lines.push(line);
    },
    getDeviceImage:function(){
    	return this.deviceImage;
    },
    getId:function(){
    	return this.id;
    },
    remove:function(){
    	if(this.deviceImage!=null)
    	{
    		if(this.lines!=null&&this.lines.length>0)
    		{
    			for(var i=0;i<this.lines.length;i++)
    			{
    				this.lines[i].remove();//删除与设备相关联的线
    			}
    		}
    		var devices=this.config.topology.getDevices();
    		if(devices!=null&&devices.length>0)
    		{//删除拓扑图中注册的设备信息
    			var another=[];
    			for(var i=0;i<devices.length;i++)
    			{
    				if(devices[i].getId()!=this.getId())
    				{
    					another.push(devices[i]);
    				}
    			}
    			this.config.topology.devices=another;
    		}
    		this.config.topology.getLayer().remove(this.deviceImage);//删除设备图片
    		this.config.topology.getLayer().draw();
    		this.config.topology.connector.hide();
    		this.lines=null;
    	}
    	
    },
    checkRange:function(x,y){
    	//this.config.topology.writeMessage("x: " + x + ", y: " + y);
    	var device=this.deviceImage;
    	this.fireRange={ 
            x: device.getX()+device.getWidth()/4,
            y: device.getY()+device.getHeight()/4,
            width: device.getWidth()/2,
            height: device.getHeight()/2
    	};
    	if((x>=this.fireRange.x&&x<=this.fireRange.x+this.fireRange.width)
    		&&(y>=this.fireRange.y&&y<=this.fireRange.y+this.fireRange.height))
    	{
    		return true;
    	}
    	else
    	{
    		return false;
    	}
    },
    syncConfig:function(){
    	this.config.data.id=this.deviceImage.getId();
    	this.config.data.x=this.deviceImage.getX();
    	this.config.data.y=this.deviceImage.getY();
    	this.config.data.width=this.deviceImage.getWidth();
    	this.config.data.height=this.deviceImage.getHeight();
    },
    draw:function(){
    	var imageObj = new Image();
    	var config=this.config;
    	var instance=this;
    	imageObj.onload = function() {
    		 var initData={
	            x: config.data.x,
	            y: config.data.y ,
	            image: imageObj,
	            id:config.data.id,
	            name:config.data.name,
	            draggable:true,
	            eth1:'x.x.x.x',
	            eth2:'x.x.x.x',
	            eth3:'x.x.x.x'
	          };
	         
	         if(config.data.width!='auto')
	         {
	         	initData.width=config.data.width;
	         }
	         if(config.data.height!='auto')
	         {
	         	initData.height=config.data.height;
	         }
    		 var device = new Kinetic.Image(initData);
	         config.topology.getLayer().add(device);
	         config.topology.getLayer().draw();
	         instance.id=initData.id;
	         instance.deviceImage=device;
	         instance.syncConfig();
	         instance.bindEvent();
	         config.topology.fitSizeAuto();
	         if(instance.config.callbackObj)
	         {
	         	instance.config.callbackObj.callback(instance);
	         }
    	};
    	imageObj.src=config.data.src;
    },
    bindEvent:function(){
    	var config=this.config;
		var instance=this;
		this.deviceImage.on("click", function(evt) {
			console.log("click");
            document.body.style.cursor = "pointer";
            if (evt.button==0) {
                var infoContent = '<fieldset><legend>基本信息</legend><table class="basicInfoTable">'+'<tr><th>设备名称:</th><td>'+instance.deviceImage.attrs.id;
                infoContent += '</td></tr></table></fieldset>'+'<fieldset><legend>基本操作</legend><div class="basicOp">';
                if(device2Url == null){
                	device2Url = '';
                }
                if (device2Url=='' || this.getId()=='shareserver' || this.getId()=='extranet') {
                    infoContent += '<a href=\"javascript:void(0);\" onclick=\"deleteDevice();\">删除设备</a>';
                } else {
                    var visit = '<a href=\"javascript:void(0);\" onclick=\"topology.visitCurrentObject(\''+instance.deviceImage.attrs.id+'\');\">全屏访问</a>';
                    //+'</div></fieldset>';
                    var chaIp = '<a href=\"javascript:void(0);\" onclick=\"topology.showIP(\''+instance.deviceImage.attrs.id+'\');\">IP信息</a></div></fieldset>';
                    infoContent += visit;
                    infoContent += chaIp;
                }
                $("#infoDialog").show();
                $("#infoContent").html(infoContent);
                $('#infoDialog').dialog({
                    title: '设备信息',
                    width: 400,
                    //height: 500,
                    closed: false,
                    cache: false,
                    draggable:false,
                    modal: true,
                    buttons:[{
                      text:'确定',
                      handler:function(){$('#infoDialog').dialog('close');}
                    },{
                      text:'取消',
                      handler:function(){$('#infoDialog').dialog('close');}
                    }]
                });
            }
        });
        /////////////////////////////////////ctt-codeEnd///////////////////////////////////////////////////////////////
    	this.deviceImage.on("mouseover", function(evt) {
	          document.body.style.cursor = "pointer";
	          var shape = evt.shape;
              // shape.style.backgroundColor = "#f40";
              // this.deviceImage.addClass('bbbb');
	          //shape.setStroke("gray");
              // this.setFill("blue");
	          //shape.setStrokeWidth(0.7);
	          shape.getLayer().draw();
	          instance.config.topology.setCurrentObject(instance);

        });
        this.deviceImage.on("mouseout", function(evt) {
           document.body.style.cursor = "default";
           var shape = evt.shape;
           shape.setStroke("white");
           shape.setStrokeWidth(0.1);
	       shape.getLayer().draw();
        });

        this.deviceImage.on("dragstart", function(evt) {
        	  console.log("dragstart");
        	  config.topology.connector.hide();
	          var shape = evt.shape;
	          shape.setShadow({
		            color: "black",
		            blur: 10,
		            offset: [10, 10],
		            alpha: 0.5
		      });
		      shape.getLayer().draw();
		      //与其相关的线隐藏
		      instance.config.topology.writeMessage(instance.getLines().length);
		      for(var i=0;i<instance.getLines().length;i++)
		      {
		      		instance.getLines()[i].hide();
		      }
		 });
		 this.deviceImage.on("dragend", function(evt) {
			 console.log("dragend");
	          var shape = evt.shape;
	          shape.setShadow({
		           		color: "white",
			            blur: 0,
			            offset: [0, 0],
			            alpha: 1
			          });
			  shape.getLayer().draw();
			  //与其相关的线重绘后显示
			  for(var i=0;i<instance.getLines().length;i++)
		      {
		      		instance.getLines()[i].redraw();
		      		instance.getLines()[i].show();
		      }
		      instance.syncConfig();
		      instance.config.topology.fitSizeAuto();
		 });
		 this.deviceImage.on("mousemove", function(evt) {
		 	  var shape = evt.shape;
	          var mousePos = config.topology.getStage().getMousePosition();
	          var x = mousePos.x ;
	          var y = mousePos.y;
	          if(instance.checkRange(x,y))
	          {//显示连接器
	          		config.topology.connector.move(instance);
	          		config.topology.connector.show();
	          }
	          else
	          {//隐藏连接器
	          		config.topology.connector.hide();
	          }
	          //config.topology.writeMessage( "x: " + (x) + ", y: " + (y)+"|left:"+shape.getX()+"top:"+shape.getY());
	          
        });
    }
});
/**
类名：设备连接器
描述：设备连接器主类
constructor:
{
	topology:topology,
	src:connectorImage
}
*/
Kinetic.Topology.Device.Connector = Kinetic.Class.extend({
	config:null,
	device:null,
	connectorImage:null,
	isHide:false,
	onDrag:false,
	joinLine:null,
	init: function(config) {
		this.config=config;
		this.draw();
    },
    getConfig:function(){
    	return this.config;
    },
    getDevice:function(){
    	return this.device;
    },
    setDevice:function(device){
    	this.device=device;
    },
    getConnectorImage:function(){
    	return this.connectorImage;
    },
    move:function(device){
    	if(!this.onDrag)
    	{
    		this.device=device;
	    	var shape=device.getDeviceImage();
	    	var x=shape.getX()+shape.getWidth()/2-5;
	    	var y=shape.getY()+shape.getHeight()/2-5;
	    	this.connectorImage.setX(x);
	    	this.connectorImage.setY(y);
	    	this.connectorImage.moveToTop();
	    	this.config.topology.getLayer().draw();
    	}
    },
    show:function(){
    	if(this.isHide&&!this.onDrag)
    	{
    		this.isHide=false;
	    	this.connectorImage.show();
	    	this.connectorImage.setDraggable(true);
	    	this.config.topology.getLayer().draw();
    	}
    },
    hide:function(){
    	if(!this.isHide&&!this.onDrag)
    	{
    		this.isHide=true;
	    	this.connectorImage.hide();
	    	this.connectorImage.setDraggable(false);
	    	this.config.topology.getLayer().draw();
    	}
    },
    clear:function(){
    	this.connectorImage.clear();
    	this.config.topology.getLayer().draw();
    },
    restore:function(){
    	this.draw();
    },
    draw:function(){
    	var imageObj = new Image();
    	var config=this.config;
    	var instance=this;
    	imageObj.onload = function() {
    		var initData={
	            x: 5,
	            y: 5 ,
	            image: imageObj,
	            id:"connector",
	            name:"连接器",
	            draggable:false
	          };
    		 var connector = new Kinetic.Image(initData);
    		 
	         config.topology.getLayer().add(connector);
	         config.topology.getLayer().draw();
	         instance.connectorImage=connector;
	         instance.hide();
	         instance.bindEvent();
    	};
    	imageObj.src=config.src;
    },
    bindEvent:function(){
    	var instance=this;
    	var config=instance.config;
    	this.connectorImage.on("mouseover", function(evt) {
    		document.body.style.cursor = "pointer";
	        var shape = evt.shape;
	        shape.getLayer().draw();
    		instance.show();
        });
        this.connectorImage.on("mouseout", function(evt) {
        	document.body.style.cursor = "default";
            var shape = evt.shape;
	        shape.getLayer().draw();
        	instance.hide();
        });
        this.connectorImage.on("dragstart", function(evt) {
	          
	          instance.onDrag=true;
	          var device=instance.getDevice();
	          var shape=device.getDeviceImage();
	    	  var x=shape.getX()+shape.getWidth()/2;
	    	  var y=shape.getY()+shape.getHeight()/2;
	    	  
	    	  var connector = evt.shape;
	          instance.joinLine=new Kinetic.Line({
		          points: [x, y, connector.getX(), connector.getY()],
		          stroke: "green",
		          strokeWidth: 1,
		          lineJoin: "round",
		          dashArray: [33, 10]
		        });
		      connector.getLayer().add(instance.joinLine);
		      connector.getLayer().draw();
		 });
		 this.connectorImage.on("dragend", function(evt) {
	          var shape = evt.shape;
	          var dstDevice=instance.config.topology.getFitDevice(shape.getX(),shape.getY());
	          //如果连接器的终点是空白区域，就在终点位置建立一个与源设备一样的结点，并把它设置为终结点
	          instance.onDrag=false;
	          var connector_x=shape.getX();
	          var connector_y=shape.getY();
	          instance.move(instance.getDevice());
	          instance.hide();
	          shape.getLayer().remove(instance.joinLine);
		      shape.getLayer().draw();
		      instance.joinLine=null;
	          if(dstDevice==null)
	          {
	          		var originalDevice=instance.getDevice();//源结点
	          		var config=originalDevice.getConfig().data;
	          		var dstDeviceId=config.id.substring(0,config.id.lastIndexOf("_"))+"_"+sequence.nextVal();
	          		dstDevice=new Kinetic.Topology.Device({
						topology:instance.config.topology,
						data:{id:dstDeviceId,name:config.name,src:config.src,
							x: connector_x,y:connector_y,width:'auto',height:'auto'},
						callbackObj:{instance:instance,
							callback:function(dstDevice){
								new Kinetic.Topology.Line({
					      			topology:this.instance.config.topology,
									srcDevice:this.instance.getDevice(),
									dstDevice:dstDevice,
									stroke:'black',
									strokeWidth:1
					      		});
							}
						}
					});
					instance.config.topology.addDevice(dstDevice);
	          }
	          else
	          {
	          		  if(dstDevice!=null&&dstDevice.getId()!=instance.getDevice().getId()&&!instance.config.topology.loading)
				      {//连线
				      		if(!instance.config.topology.containLine(instance.getDevice(),dstDevice))
				      		{
					      		var line=new Kinetic.Topology.Line({
					      			topology:instance.config.topology,
									srcDevice:instance.getDevice(),
									dstDevice:dstDevice,
									stroke:'black',
									strokeWidth:1
					      		});
				      		}
				      }
	          }
		      
		 });
		 this.connectorImage.on("dragmove", function(evt) {
		 	  var connector = evt.shape;
	          var device=instance.getDevice();
	          var shape=device.getDeviceImage();
	    	  var x=shape.getX()+shape.getWidth()/2;
	    	  var y=shape.getY()+shape.getHeight()/2;
	    	  //config.topology.writeMessage(x+","+y+","+connector.getX()+","+connector.getY()+","+instance.onDrag);
	          if(instance.joinLine!=null)
	          {
	          		instance.joinLine.setPoints([x,y,connector.getX(),connector.getY()]);
	          		connector.getLayer().draw();
	          }
         });
    }
});
/**
类名：连接线
描述：连接线主类
constructor:
{
	topology:topology,
	srcDevice:device,
	dstDevice:device,
	stroke:'blue',
	strokeWidth:1
}
*/
Kinetic.Topology.Line = Kinetic.Class.extend({
	config:null,
	lineObject:null,
	init: function(config) {
		this.config=config;
		this.draw();
		this.bindEvent();
    },
    getConfig:function(){
    	return this.config;
    },
    getSrcDevice:function(){
    	return this.config.srcDevice;
    },
    getDstDevice:function(){
    	return this.config.dstDevice;
    },
    hide:function(){
    	this.lineObject.hide();
    	this.config.topology.getLayer().draw();
    },
    show:function(){
    	this.lineObject.show();
    	this.config.topology.getLayer().draw();
    },
    redraw:function(){
    	var srcElement=this.config.srcDevice.getDeviceImage();
    	var x1=srcElement.getX()+srcElement.getWidth()/2;
    	var y1=srcElement.getY()+srcElement.getHeight()/2;
    	var dstElement=this.config.dstDevice.getDeviceImage();
    	var x2=dstElement.getX()+dstElement.getWidth()/2;
    	var y2=dstElement.getY()+dstElement.getHeight()/2;
    	this.lineObject.setPoints([x1, y1, x2, y2]);
    	this.config.topology.getLayer().draw();
    	this.lineObject.saveImageData();
    },
    remove:function(){
    	if(this.lineObject!=null)
    	{
    		var lines=this.config.topology.getLines();
    		if(lines!=null&&lines.length>0)
    		{//删除拓扑图中注册的线信息
    			var another=[];
    			for(var i=0;i<lines.length;i++)
    			{
    				if(lines[i].lineObject.getId()!=this.lineObject.getId())
    				{
    					another.push(lines[i]);
    				}
    			}
    			this.config.topology.lines=another;
    		}
    		this.config.topology.getLayer().remove(this.lineObject);
    		this.lineObject.clearImageData();
    		this.config.topology.getLayer().draw();
    	}
    },
    draw:function(){
    	var srcElement=this.config.srcDevice.getDeviceImage();
    	var x1=srcElement.getX()+srcElement.getWidth()/2;
    	var y1=srcElement.getY()+srcElement.getHeight()/2;
    	var dstElement=this.config.dstDevice.getDeviceImage();
    	var x2=dstElement.getX()+dstElement.getWidth()/2;
    	var y2=dstElement.getY()+dstElement.getHeight()/2;
    	this.lineObject = new Kinetic.Line({
		          points: [x1, y1, x2, y2],
		          stroke: this.config.stroke,
		          strokeWidth: this.config.strokeWidth,
		          lineCap: "round",
		          lineJoin: "round",
		          draggable: false,
		          detectionType: "pixel",
		          id:this.config.srcDevice.getId()+"_"+this.config.dstDevice.getId()
		        });
		this.config.topology.getLayer().add(this.lineObject);
		this.lineObject.moveToBottom();
		this.lineObject.moveUp();
		this.config.topology.getLayer().draw();
		this.lineObject.saveImageData();
		this.config.srcDevice.addLine(this);
		this.config.dstDevice.addLine(this);
		this.config.topology.addLine(this);
    },
    bindEvent:function(){
    	var instance=this;
        this.lineObject.on("click", function(evt) {
            document.body.style.cursor = "pointer";
            $("#infoDialog").show();
            if (evt.button==0) {
               var infoContent = '<fieldset id="srcEth" style="float:left;width:250px;height:100px;margin:10px 10px;"><legend>源设备</legend><table class="basicInfoTable">'
                    +'<tr><th>名称:</th><td>'+instance.config.srcDevice.id+'</td></tr>';
               if(instance.config.srcDevice.deviceImage.attrs.name == 'ROUTER'){
            	   infoContent +='<tr><th>网卡:</th><td><select><option value="1">网卡1</option>'
            		   +'<option value="2">网卡2</option><option value="3">网卡3</option></select></td></tr>'
               }else if(instance.config.srcDevice.deviceImage.attrs.name != 'Switch'){
            	   infoContent +='<tr><th>网卡:</th><td><select><option value="1">网卡1</option></select></td></tr>'
               }
               infoContent +='</table></fieldset>'
                    +'<fieldset id="dstEth" style="float:left;width:250px;height:100px;margin:10px 10px;"><legend>目的设备</legend><table class="basicInfoTable">'
                    +'<tr><th>名称:</th><td>'+instance.config.dstDevice.id+'</td></tr>';
               if(instance.config.dstDevice.deviceImage.attrs.name == 'ROUTER'){
            	   infoContent +='<tr><th>网卡:</th><td><select><option value="1">网卡1</option>'
            		   +'<option value="2">网卡2</option><option value="3">网卡3</option></select></td></tr>'
               }else if(instance.config.dstDevice.deviceImage.attrs.name != 'Switch'){
            	   infoContent +='<tr><th>网卡:</th><td><select><option value="1">网卡1</option></select></td></tr>'
               }
               infoContent +='</table></fieldset>';
               if(instance.config.subnet == undefined)
            	{
            	   //infoContent +='<div style="padding-left: 123px;font-weight: bold;">子网网段(如10.0.1.0/24)<input type="text" id="subnet" class="subnet" /></div>';
            	   infoContent +='<div style="padding-left: 123px;font-weight: bold;">子网网段(格式:X.X.X.0/24)<input type="text"  id="subnet" class="subnet" /></div>';
            	}
               else{
            	   infoContent +='<div style="padding-left: 123px;font-weight: bold;">子网网段(格式:X.X.X.0/24)<input type="text" id="subnet" class="subnet" value ="'+instance.config.subnet+'"/></div>';   
               }
               
            if (device2Url=='') {
                    infoContent += '<fieldset><legend>基本操作</legend><div class="basicOp">'
                    +'<a href="javascript:void(0);" onclick="deleteDevice();">删除</a>';
                }
            $("#infoContent").html(infoContent);
            $('#infoDialog').dialog({   
                title: '连线信息', 
                width: 600,
                //height: 300,
                closed: false,   
                cache: false, 
                draggable:false,    
                modal: true,
                buttons:[{
                    text:'确定',
                    handler:function(){
                		if($('#srcEth select').val() == '1'){
                			instance.config.srcDevice.deviceImage.attrs.eth1 = $('#srcEth input').val();
                			instance.config.srcnic = 'eth1';
                			instance.config.srcip = instance.config.srcDevice.deviceImage.attrs.eth1;
                		}else if($('#srcEth select').val() == '2'){
                			instance.config.srcDevice.deviceImage.attrs.eth2 = $('#srcEth input').val();
                			instance.config.srcnic = 'eth2';
                			instance.config.srcip = instance.config.srcDevice.deviceImage.attrs.eth2;
                		}else if($('#srcEth select').val() == '3'){
                			instance.config.srcDevice.deviceImage.attrs.eth3 = $('#srcEth input').val();
                			instance.config.srcnic = 'eth3';
                			instance.config.srcip = instance.config.srcDevice.deviceImage.attrs.eth3;
                		}
                		if($('#dstEth select').val() == '1'){
                			instance.config.dstDevice.deviceImage.attrs.eth1 = $('#dstEth input').val();
                			instance.config.dstnic = 'eth1';
                			instance.config.dstip = instance.config.dstDevice.deviceImage.attrs.eth1;
                		}else if($('#dstEth select').val() == '2'){
                			instance.config.dstDevice.deviceImage.attrs.eth2 = $('#dstEth input').val();
                			instance.config.dstnic = 'eth2';
                			instance.config.dstip = instance.config.dstDevice.deviceImage.attrs.eth2;
                		}else if($('#dstEth select').val() == '3'){
                			instance.config.dstDevice.deviceImage.attrs.eth3 = $('#dstEth input').val();
                			instance.config.dstnic = 'eth3';
                			instance.config.dstip = instance.config.dstDevice.deviceImage.attrs.eth3;
                		}
                		
                		//判断用户输入的子网网段格式(X.X.X.X/24)是否正确
                		var InputSubnet=$('#subnet').val();
                		var flagSubnetFormat=true;
                		var j=0;
                		
                		for(var i=0;i<InputSubnet.length;i++){
                			if(j>4){
            					flagSubnetFormat=false;
            					break;
            				}
                			if(InputSubnet.charAt(i)>='0' && InputSubnet.charAt(i)<='9'){
                				if(j == 3 && InputSubnet.charAt(i) != '0'){
                					flagSubnetFormat=false;
                					break;
                				}
                			}else if(InputSubnet.charAt(i)=='.' && j<3){
                				j++;
                				if(i==0){
                					flagSubnetFormat=false;
                					break;
                				}else if(InputSubnet.charAt(i-1)=='.'){
                					flagSubnetFormat=false;
                					break;
                				}
                			}else if(InputSubnet.charAt(i)=='/' && j==3 && InputSubnet.charAt(i-1)!='.'){
                				j++;
                			}else{
                				flagSubnetFormat=false;
                				break;
                			}
                		}
                		
                		if(j!=4){
                			flagSubnetFormat=false;
                		}else if(j==4){
                			i=InputSubnet.length-1;
                    		if(InputSubnet.charAt(i)!='4'||InputSubnet.charAt(i-1)!='2'||InputSubnet.charAt(i-2)!='/'){
                    			flagSubnetFormat=false;
                    		}
                		}
                		
                		if(InputSubnet == '192.168.1.0/24'){
                			flagSubnetFormat=false;
                		}
                		
                		if(flagSubnetFormat == true){
                			instance.config.subnet = $('#subnet').val();
                    		$('#infoDialog').dialog('close');
                		}else{
                			alert("子网网段格式不正确！");
                		}
                	}
                },{
                    text:'取消',
                    handler:function(){$('#infoDialog').dialog('close');}
                }]
            });  
            }
            
        });
        /////////////////////////////////////ctt-codeEnd///////////////////////////////////////////////////////////////
        this.lineObject.on("mouseover", function(evt) {
            document.body.style.cursor = "pointer";
            var shape = evt.shape;
            shape.setStrokeWidth(shape.getStrokeWidth()+2);
            instance.config.topology.getLayer().draw();
            instance.lineObject.saveImageData();
	        instance.config.topology.setCurrentObject(instance);
        });
        this.lineObject.on("mouseout", function(evt) {
            document.body.style.cursor = "default";
            var shape = evt.shape;
            shape.setStrokeWidth(shape.getStrokeWidth()-2);
            instance.config.topology.getLayer().draw();
            instance.lineObject.saveImageData();
	        instance.config.topology.writeMessage("mouseover");
        });
    }
});
/**
类名：工具栏
描述：工具栏主类
constructor:
{
    toolbar:{
     	container:'toolbar',
     	data:[
	    	{id:'router',name:'路由器',image:'Router_Icon_128x128.png',width:32,height:32}
	    ]
    }
}
*/
Kinetic.Topology.Toolbar = Kinetic.Class.extend({
	config:null,
	init: function(config) {
		this.config=config;
		$("#"+this.config.toolbar.container).html(this.getHtml());
		for(var i=0,n=this.config.toolbar.data.length;i<n;i++)
    	{
    		var data=this.config.toolbar.data[i];
    		var toolkit=new Kinetic.Topology.Toolbar.Toolkit({
    			container:this.config.toolbar.container+'_topology',
				data:data
    		});
    	}
    },
    getConfig:function(){
    	return this.config;
    },
    getHtml:function(){
    	var html= "<div id='"+this.config.toolbar.container+"_topology' >";
    	html+="</div>";
		return html;
    }
});
/**
类名：工具
描述：工具主类
constructor:
{
	container:'toolbar_topology',
	data:{id:'router',name:'路由器',image:'Router_Icon_128x128.png',width:32,height:32}
}
*/
Kinetic.Topology.Toolbar.Toolkit = Kinetic.Class.extend({
	config:null,
	init: function(config) {
		this.config=config;
		var container=$("#"+this.config.container);
    	container.append(this.getHtml());
    	this.bindEvent();
    },
    getConfig:function(){
    	return this.config;
    },
    getHtml:function(){
    	var html="<div id='"+this.config.data.id+"_div'>" +
    			"<img id='"+this.config.data.id+"' src='"+this.config.data.image+"' width='"+this.config.data.width+"px' height='"+this.config.data.height+"px' style='padding:2px;position:relative;z-index:100;border:solid white 1px;vertical-align: middle;margin-left: 10px;' title='"+this.config.data.name+"'/>" +
    			"<span style='margin-left:5px;'>"+this.config.data.name+"</span>"+
    			"</div>";
    	return html;
    },
    bindEvent:function(){
    	$("#"+this.config.data.id+"_div").draggable({//可托动
				revert:true,
				proxy: function(source){
					var cloneObj=$(source).clone().attr("id",$(source).attr("id")+"_proxy");
					var p=cloneObj.insertAfter(source);
					return p;
				}
			});
    	//定义工具栏图片的鼠标事件
	   $("#"+this.config.data.id).mouseover(function(){
	   		$(this).css("border","solid black 1px");
	   });
	   $("#"+this.config.data.id).mouseout(function(){
	   		$(this).css("border","solid white 1px");
	   });
    }
});

/**
类名：菜单
描述：菜单主类
constructor:
*/
Kinetic.Topology.Menu = Kinetic.Class.extend({
	config:null,
	init: function(config) {
		this.config=config;
    },
    getConfig:function(){
    	return this.config;
    },
    getHtml:function(){
    }
});
/**
类名：菜单项
描述：菜单项主类
constructor:
*/
Kinetic.Topology.Menu.Item = Kinetic.Class.extend({
	config:null,
	init: function(config) {
		this.config=config;
    },
    getConfig:function(){
    	return this.config;
    },
    getHtml:function(){
    }
});

/**
类名：序列
描述：序列主类
constructor:
*/
Kinetic.Topology.Sequence = Kinetic.Class.extend({
	seq:0,
	init: function() {
    },
    nextVal:function(){
    	return Math.uuid();
    }
});

var sequence=new Kinetic.Topology.Sequence({});

//test()
function test(){
	alert('111');
	$('#frame11').onkeydown = function(){
		$('#frame11').focus();
	}
}