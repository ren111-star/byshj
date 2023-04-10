$(document).ready(function(){
	$("#searchinformform input[value='发布通知']").click(function(){
		showDialog("#informedt",'发布通知');
	});
	var options_inform={
		beforeSubmit:  function(){
			var $title=$("#informform input[name='title']");
			if($title.val()=="" || $title.val()==null)
			{
				alert("标题不能为空，请选择！");
				$title.focus();
				return false;
			}
			$loadingimg=$("#informform img[name='loadingimg']");
			$errmsg=$("#informform span[name='errmsg']");
			$loadingimg.show();
			$errmsg.text("正在提交，请稍候……");
		    },
		dataType: 'json',
		success: function()
							{
								//alert("你已成功发布通知！");
								$("#title").val("");
								$("#informcontent").val("");
								$errmsg.text("");
								$loadingimg.hide();
								$("#signid").val(null);
								$("#files_list").empty();
								hideDialog('#informedt');
								//document.getElementById("wyxlist").style.display="block";
								refresh();
							}
	};
	/*查询通知ajax表单提交绑定*/
	$("#searchinformform").ajaxForm({dataType:'json',success:searchSuccess});
});
function searchSuccess(data) 
{
	 $codetb=$("#syscodemagui [name='codetb']");
	 $errmsg=$("#syscodemagui [name='errmsg0']");
	 $loadmsg=$("#syscodemagui [name='loadingimg']");
	if(data.errmsg!=""){
		$errmsg.text("错误："+data.errmsg);
		$loadmsg.hide();
		$codetb.empty();
		return;
	}
	/*
	if(data.result==""){
		$errmsg.text("查询结果数：0");
		$codetb.empty();
		$loadmsg.hide();
		return;
	}
	*/
	var infoarr=eval(data.result);
    var $tbody=$("#informtb");
    var modtime="";
    $tbody.empty();
    $.each(infoarr,function(index,item){
	     var xh=index+1;
	     if(item.modifytime==null){
	     	modtime="&nbsp;";
	     }
	     else{
	     	modtime=item.modifytime;
	     }
	     $tbody.append("<tr><td>"+xh+"</td><td>"+item.title+"</td><td>"+item.viewtimes
	     		+"</td><td>"+item.releasetime+"</td><td>"+modtime+"</td><td><input type='hidden' value='"+item.infid+"'/><input type='button' class='btn' value='修改'/>&nbsp;&nbsp;<input type='button' class='btn' value='删除'/></td></tr>");
	     }
	  );
    //为表单中的按钮绑定方法
   $tbody.find("input[value='修改']").click(function(){
  		var infid=$(this).siblings().eq(0).val();
  		$("#infid").val(infoid);
  		$("#affixlist").show();
  		$.getJSON("../InformServlet?mode=get",{infid:infid},function call(data){   
        		if(data.errmsg!=""){
      				alert(data.errmsg);
      				return;
      			}
      			var inform=eval(data.result);
      			$("#title").val(inform.title);
      			$("#informcontent").val(inform.content);
      			var arrfiles=new Array();
      			arrfiles=inform.AffixFiles;
      			var path=inform.affixpath;
      			//该方法顺便实现了删除后的页面刷新工作
      			showaffixlist(arrfiles,path);
   		});
   		showDialog("#informedt",'修改通知');
  });  
   $tbody.find("input[value='删除']").click(function(){
  		var infid=$(this).siblings().eq(0).val();
  		if(!confirm("确定要删除吗？"))return;
  		$.getJSON("../InformServlet?mode=del",{infid:infid},function call(data){
  			if(data.errmsg!=""){
  				alert(data.errmsg);
  				return;
  			}
  			//删除成功后，刷新通知列表
  			$("#searchinformform").submit();
  		});
  });
}
//王宜晓的修改按钮操作中的显示附件
function showaffixlist(arrfiles,path)
{
	//alert(arrfiles.length);
	$("#affixlisttb").empty();
	var delaffixname=new Array();
	var arr=new Array();
	var p=0;
	var j=0;
	for(var i=0;i<arrfiles.length;i++)
	{
		var wyx=arrfiles[i].fileName;
		$("#affixlisttb").append("<tr><td>"+wyx+"</td><td><input type='text' value='"+wyx+"' style='display:none'/><input type='button' class='btn' value='删除该附件'/></td></tr>");
	};
	$("#affixlisttb").find("input[value='删除该附件']").click(function(){
			       				//alert("能进来吗？");
			       				delaffixname[j]=$(this).siblings().eq(0).val();
			       				var uploadpath=path+"/"+delaffixname[j];
			       				$.getJSON("../InformServlet?mode=delfile&uploadpath="+uploadpath+"",null);
			       				j++;
			       				for(var k=0;k<j;k++)
			       				{
			       					for(i=0;i<arrfiles.length;i++)
			       					{
			       						if(delaffixname[k]!=arrfiles[i].fileName)
			       						{
			       							//alert(arrfiles[i].fileName);
			       							arr[p]=arrfiles[i];
			       							p++;
			       						}
			       					}
			       				}
			       				showaffixlist(arr,path);
			       			});
}