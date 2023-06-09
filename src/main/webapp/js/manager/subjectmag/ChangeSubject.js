$(document).ready(function(){
	//查询文档状态
	$("#changesubjectsdiv form[name='changesubjectform']").ajaxForm({
		beforeSubmit: function(){
			$stuid=$("#changesubjectsdiv input[name='stuid']");
			if($stuid.val()==""){
				alert("学号不能为空！");
				$stuid.focus();
				return false;
			}
			$tutorid=$("#changesubjectsdiv input[name='tutorid']");
			if($tutorid.val()==""){
				alert("新教师编号不能为空！");
				$tutorid.focus();
				return false;
			}
			$loadingimg=$("#changesubjectsdiv img[name='loadingimg']");
			$errmsg=$("#changesubjectsdiv span[name='errmsg0']");
			$loadingimg.show();
			$errmsg.text("正在更改，请稍候……");
		},
		dataType: 'json',
		success:   function(data){
			$loadingimg=$("#changesubjectsdiv img[name='loadingimg']");
			$errmsg=$("#changesubjectsdiv span[name='errmsg0']");
			if(data==null){
		   		alert("请求异常！请与系统管理员联系，确定服务器是否运行正常！1");
		   		$loadingimg.hide();
		        return;
		   	}
			if(data.errmsg!=""){
	       		alert("更换失败："+data.errmsg);
	       		$loadingimg.hide();
	       		return;
	        }
			$loadingimg.hide();
			$errmsg.text("");
			$("#changesubjectsdiv input[name='stuid']").val("");
			$("#changesubjectsdiv input[name='tutorid']").val("");
			alert("更换指导教师成功！");
		}
	});
});
