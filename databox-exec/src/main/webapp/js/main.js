var services;
    
function LoadXML(url) {
    var xhr = window.ActiveXObject ? new ActiveXObject("microsoft.xmlhttp") : new XMLHttpRequest(),dom;
    xhr.open("get", url, false);
    xhr.send(null);
    if (0 == xhr.status || 200 == xhr.status) {
        if (document.all && 0 == xhr.status) {//IE浏览器下非http的ajax请求xml文件无法生成XML的DOM对象，需要使用microsoft.xmldom重生生成
            dom = new ActiveXObject("microsoft.xmldom");
            dom.loadXML(xhr.responseText);
        }
        else dom = xhr.responseXML;
    }
    else aler('加载XML失败！' + xhr.responseText);
    if (dom != null){
    	return dom.firstChild;
    }
    return null;
}

function initServiceList(){
	services = LoadXML("./xml/serviceInfo.xml").children;
	var serviceContent ="";
	for(var i = 0; i < services.length;i++){
		serviceContent += "<li><a tooltip='"+services[i].getAttribute("desc") +"'>"+services[i].getAttribute("name")+"</a></li>";
	}
	$("#serviceInfo").html(serviceContent);
}

function initDataList(){
	dataList = LoadXML("./xml/dataInfo.xml").children;
	var dataContent ="";
	for(var i = 0; i < dataList.length;i++){
		//dataContent += "<li id="+i+" onclick = s("+i+")>"+dataList[i].getAttribute("name") + "<ul>";
		//dataContent += "<li><label  onclick = s("+i+")>"+dataList[i].getAttribute("name") + "</label><ul id="+i+" style=\"display:none\">";
		dataListName = dataList[i].getElementsByTagName('name')[0].textContent;
		dataContent += "<li><label  onclick = s("+i+")>" + dataListName + "</label><ul id="+i+" style=\"display:none\">";
		tables = dataList[i].getElementsByTagName('table');
		//serviceContent += "<li><a tooltip='"+services[i].getAttribute("desc") +"'>"+services[i].getAttribute("name")+"</a></li>";
		for(var j = 0; j < tables.length; j++){
			tableName = tables[j].getElementsByTagName('ch-name')[0].textContent;
			dataContent += "<li>"+ tableName +"</li>";
			//dataContent += "<li>"+tables[j].getAttribute("name")+"</li>";
		}
		dataContent += "</ul></li>";

	}
	$("#dataInfo").html(dataContent);
}


function s(itemid){
	if (document.getElementById(itemid).style.display == 'none') {
		document.getElementById(itemid).style.display = "block";
	} else {
		document.getElementById(itemid).style.display = "none";
		var divid = itemid + "id";
		document.getElementById(divid).remove();
	}
}

function init(){
	//window.location.href="./serviceInfo.xml"; 
	initServiceList();
	initDataList();
}

function getXML(name){
	var url = "GetXMLServlet";
	var result = [];
	$.ajax({
		data: "get",
		url: url,
		data: "xml=" + name,
		cache: false,
		async: false,
		success: function (data) {
			result = eval('(' + data + ')');;
		}
	});
	return result;
}

function initServiceList2(){
	var services = getXML("service").service;
	var serviceContent ="";
	for(var i = 0; i < services.length;i++){
		serviceContent += "<li><a tooltip='"+services[i]["description"]["description"] +"'>"+services[i]["name"]["name"]+"</a></li>";
	}
	$("#serviceInfo").html(serviceContent);
}

function initDataList2(){
	var dataList = getXML("data").data;
	var dataContent ="";
	for(var i = 0; i < dataList.length;i++){
		dataContent += "<li><label  onclick = s(\"data"+i+"\")>" + dataList[i]["name"]["name"] + "</label><ul id=\"data"+i+"\" style=\"display:none\">";
		var tables = dataList[i]["tables"]["table"];
		for(var j = 0; j < tables.length; j++){
			dataContent += "<li><label  onclick = s(\"table"+i+"-"+j+"\")>"+ tables[j]["ch-name"]["ch-name"] 
						+ "</label><ul id=\"table"+i+"-"+j+"\" style=\"display:none\">";
			var fields = tables[j]["fields"]["field"];
			var pk = tables[j]["pks"]["pks"]["pk"];
			var fks = !tables[j]["fks"] ? [] : tables[j]["fks"]["fk"];
			var fks_ch = [];
			var field_dict = {};
			for (var f = 0; f < fields.length; f++){
				field_dict[fields[f]["name"]["name"]] = fields[f]["ch-name"]["ch-name"];
			}
			for (var f = 0; f < fks.length; f++){
				fks_ch.push(field_dict[fks[f]["fk"]]);
			}
			dataContent += "<li>描述："+ tables[j]["description"]["description"] + "</li>";
			dataContent += "<li>主键："+ field_dict[pk] +"</li>";
			if(fks_ch.length > 0){
				dataContent += "<li>外键："+ fks_ch.toString() +"</li>";
			}
			dataContent += "</ul></li>";
		}
		dataContent += "</ul></li>";
	}
	$("#dataInfo").html(dataContent);
}

function init2(){
	initServiceList2();
	initDataList2();
}

function requestJson(url, param) {
	var result = [];
	var response = $.ajax({
		type: "get",
		url: url,
		data: param,
		dataType: "json",
		cache: false,
		async: false,
		success: function (data) {
			result = data;
		}
	});
	return response.resoponseText;
}

function upload() {
	alert("123");
		$.ajax({
			url: "UploadServlet",
			type: 'POST',
			cache: false,
			data: new FormData($('#taskUploadForm')),
			processData: false,
			contentType: false,
			//dataType:"json",
			success : function(data) {
				alert("success.");
			},
			error : function() {
				alert("error.");
			}
		});
}

function listAllTask() {
	$("#tasklist_content").empty();
	
	$.getJSON("ListTaskServlet", function(data, status, xhr){
		$.each(data, function(i, field) {
			var trTask = $("<tr></tr>").attr("id", field.taskGroupName + "_" + field.taskName);
			var tdTaskName = $("<td></td>").attr("style", "font-size:20px").text(field.taskName);
			var tdTaskGroupName = $("<td></td>").attr("style", "font-size:20px").text(field.taskGroupName);
			var tdStatus = $("<td></td>").attr("style", "font-size:20px").text("stop");
			var tdOperation = $("<td></td>").attr("style", "font-size:20px").html("<a href='javascript:void(0)' onclick='executeTask(\"" + field.taskGroupName + "\", \"" + field.taskName + "\")'>运行</a>  <a href='javascript:void(0)' onclick='getTaskResult(\"" + field.taskGroupName + "\", \"" + field.taskName + "\")'>结果</a>");
			trTask.append(tdTaskGroupName, tdTaskName, tdStatus, tdOperation);
			$("#tasklist_content").append(trTask);
		});
	});
}

function getUrlParam(name) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
    var r = window.location.search.substr(1).match(reg);  //匹配目标参数
	if (r != null) return unescape(r[2]); return null; //返回参数值
}

function checkUploadStatus() {
	var value = getUrlParam("status");
	if(value != null) {
		if(value == 0) {
			alert("上传成功！");
			$(window).attr("location", window.location.pathname);
		} else if(value == 1) {
			alert("服务器无法正常接收文件！");
			$(window).attr("location", window.location.pathname);
		} else if(value == 2) {
			alert("数据盒冲突的任务！");
			$(window).attr("location", window.location.pathname);
		} else if(value == 3) {
			alert("无法正常发布任务！");
			$(window).attr("location", window.location.pathname);
		}
	}
}

function getTaskResult(taskGroupName, taskName) {
	
	$.getJSON("GetResultServlet?taskGroupName=" + taskGroupName + "&taskName=" + taskName, function(data, status, xhr) {
		var res = "";
		
		if(data.result != null) {
			res = data.result;
		}
		var err = "";
		if(data.error != null)
			err = data.error;
		layer.open({
		  type: 1,
		  title: taskGroupName + "/" + taskName + " Output",
		  shadeClose: true,
		  shade: false,
		  maxmin: true, //开启最大化最小化按钮
		  area: ['800px', '400px'],
		  content: "<table height='100%' width='100%'><tr height='60%'><td width='100px' style='font-size:20px'>result</td><td style='font-size:20px'>"+res+"</td></tr><tr height='40%'><td style='font-size:20px'>error</td><td style='font-size:20px'>"+err+"</td></tr></table>"
		});
	});
}

function executeTask(taskGroupName, taskName) {
	$.getJSON("ExecuteServlet?taskGroupName=" + taskGroupName + "&taskName=" + taskName, function(data, status, xhr) {
		if(data.status == 0) {
			alert("成功启动任务！");
		} else {
			alert("无法启动任务！");
		}
	});
}

$(function() {
	function checkStatus() {
		$.getJSON("CheckStatusServlet", function(data, status, xhr) {
			$.each(data, function(idx, item){
				var id = "#" + item.taskGroupName + "_" + item.taskName;
				$(id).children().eq(2).text(item.status);
			});
		});
	}
	setInterval(checkStatus, 1000);
});

$(document).ready(function() {
	listAllTask();
	checkUploadStatus();
	init2();
});