<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>通知发布查看页面</title>
<link rel="stylesheet" type="text/css" href="css/common.css" />
<link rel="stylesheet" type="text/css" href="css/divwin.css" />
<script src="js/jquery-1.4.2.js" type="text/javascript"></script>
<script src="js/jquery.form.js" type="text/javascript"></script>
<script src="js/jquery.tools.min.tabs.js" type="text/javascript"></script>
<script src="js/jquerywin.js" type="text/javascript"></script>
<script src="js/jquery.mask.js" type="text/javascript"></script>
<script src="js/jquery.table.js" type="text/javascript"></script>
<script src="js/com/code.js" type="text/javascript"></script>
<script src="js/jquery.json.js" type="text/javascript"></script>
<script src="js/jquery.common.js" type="text/javascript"></script>
<script type="text/javascript">
$(document).ready(function(){
$.getJSON("InformServlet?mode=gets&wyxinform=null",null,function call(data){
	if(data.errmsg!=="")
		{
		alert(data.errmsg);
		return;
	}
	//alert("11111111");
	var informs=eval(data.result);
	var $tbody=$("#infoshow");
	$.each(informs,function(index,item){
		 if(item.modifytime==="" || item.modifytime==null)
	     {
	     	modtime="未修改过";
	     }
	     else
	     {
	     	modtime=item.modifytime;
	     }
	     //alert("能进来吗？");
	     //   </div>
	     //alert(item.infid);
	     $tbody.append("<tr><td>"+item.title+"</td><td>"+item.releasetime+"</td><td>"+modtime+"</td><td><input type='button' value='查看详情' onclick='showall("+item.infid+")'/></td></tr>");			     
	});
});
})

function showall(id)
{
    //alert(id);
    showDialog("#showspan",'信息详情');
	$.getJSON("InformServlet?mode=get&infoid="+id+"",null,function call(data){
		//alert("能进来吗？");
		if(data.errmsg!="")
		{ 
			alert(data.errmsg);
			return;
		}
		//alert("1");
		var inform=eval(data.result);
		var informaffixs=new Array();
		informaffixs=inform.AffixFiles;
		//alert(informaffixs.length);
		var $showaffix=$("#showtable");
		$showaffix.empty();
		$("#showtitle").val(inform.title);
		$("#showcontent").val(inform.content);
		for(var i=0;i<informaffixs.length;i++)
		{
			var download=inform.affixpath+"/"+informaffixs[i].fileName;
			//inform.affixpath+"/"+informaffixs[i].fileName;<a href='"+download+"' target='_blank'></a>
			$showaffix.append("<tr><td align='left'>"+informaffixs[i].fileName+"</td><td align='right'><input type='button' value='下载'/></td></tr>");
			//alert("rrrr000");<input type='button' class='btn' value='下载'/>
		}
		$showaffix.find("input[value='下载']").click(function(){
			alert("hello");
			//$.getJSON("InformServlet?mode=get&infoid="+id+"",null,function call(data){
			$.getJSON("servlet/Download?downloadFileName="+download+"",null,function call(data){
				alert("成功");
			});
		});
	});
}

</script>
</head>
<body>
<div>
<table class="mtable2">
<tr><th>标题</th><th>发布时间</th><th>修改时间</th><th>详情</th></tr>
<tbody id="infoshow"></tbody>
</table>
</div>
<div id="showspan" class="win" style="top:100px;left:100;width:300">
标题：<input id="showtitle" type="text" readonly/><br>
通知内容：<input id="showcontent" type="text" readonly/><br>
附件：<table>
		<tr><th>附件名称</th><th>下载</th></tr>
		<tbody id="showtable"></tbody>
	</table><br>
     <input type="button" value="返回" onclick="hideDialog('#showspan')"/>
</div>
</body>
</html>