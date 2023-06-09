$(document).ready(function(){
	//初始化专业、班级级联菜单
	var $specseldesigndoc=$("#viewdesigndocsdiv select[name='specid']");
	code_getspeciality_select($specseldesigndoc);//专业列表
	$specseldesigndoc.change(function(){
		var specid=$(this).val();
		if(specid=="") return;
		var $classsel=$("#viewdesigndocsdiv select[name='classname']");
		$classsel.empty();
		code_getclass_select(specid,$classsel);//班级列表
	});
	//查询文档状态
	$("#viewdesigndocsdiv form[name='designdocform']").ajaxForm({
		beforeSubmit: function(){
			$spec=$("#viewdesigndocsdiv select[name='specid']");
			if($spec.val()==""){
				alert("专业不能为空，请选择！");
				$spec.focus();
				return false;
			}
			$loadingimg=$("#viewdesigndocsdiv img[name='loadingimg']");
			$errmsg=$("#viewdesigndocsdiv span[name='errmsg0']");
			$loadingimg.show();
			$errmsg.text("正在查询，请稍候……");
		},
		dataType: 'json',
		success:   showdesigndoc
	});
	//绑定 导出盲审意见 按钮操作
	$("#viewdesigndocsdiv input[name='exportreviewpaperopinion']").click(function(){
		$loadingimg=$("#viewdesigndocsdiv img[name='loadingimg']");
		$errmsg=$("#viewdesigndocsdiv span[name='errmsg0']");
		
		$spec=$("#viewdesigndocsdiv select[name='specid']");
		if($spec.val()==""){
			alert("专业不能为空，请选择！");
			$spec.focus();
			return;
		}
		var specid=$spec.val();
		var classname=$("#viewdesigndocsdiv [name='classname']").val();
		var sname=$("#viewdesigndocsdiv [name='sname']").val();
		$errmsg.text("正在导出盲审意见，请稍候……");
		$loadingimg.show();
		$.getJSON("../ExcelServlet?mode=exportBlindReviewContentBySpec",{specid:specid,classname:classname,sname:sname},function call(data){ 
			if(data==null){
		   		alert("ExcelServlet请求异常！[1]");
		   		$loadingimg.hide();
		        return;
		   	}
			if(data.errmsg!=""){
        		$errmsg.text("导出盲审意见失败："+data.errmsg);
        		$loadingimg.hide();
        		return;
        	}
        	$errmsg.html("导出成功！<a href='../FileDownloadServlet0?tempfilename=reviewpaper.xls' title='点击打开/保存文件'>盲审意见</a>");
        	$loadingimg.hide();
        });
	});
	//绑定 导出进程表 按钮操作
	$("#viewdesigndocsdiv input[name='exportprogresstable']").click(function(){
		$loadingimg=$("#viewdesigndocsdiv img[name='loadingimg']");
		$errmsg=$("#viewdesigndocsdiv span[name='errmsg0']");
		
		$spec=$("#viewdesigndocsdiv select[name='specid']");
		if($spec.val()==""){
			alert("专业不能为空，请选择！");
			$spec.focus();
			return;
		}
		var specid=$spec.val();
		var classname=$("#viewdesigndocsdiv [name='classname']").val();
		var sname=$("#viewdesigndocsdiv [name='sname']").val();
		$errmsg.text("正在导出进程表，请稍候……");
		$loadingimg.show();
		$.getJSON("../ExcelServlet?mode=exportProgressTableBySpec",{specid:specid,classname:classname,sname:sname},function call(data){ 
			if(data==null){
		   		alert("ExcelServlet请求异常！[1]");
		   		$loadingimg.hide();
		        return;
		   	}
			if(data.errmsg!=""){
        		$errmsg.text("导出进程表失败："+data.errmsg);
        		$loadingimg.hide();
        		return;
        	}
        	$errmsg.html("导出成功！<a href='../FileDownloadServlet0?tempfilename=progresstable.xls' title='点击打开/保存文件'>进程表</a>");
        	$loadingimg.hide();
        });
	});
});
//逐行显示设计文档
function showdesigndoc(data){
	$tbody=$("#viewdesigndocsdiv tbody[name='studocs']");
	$loadingimg=$("#viewdesigndocsdiv img[name='loadingimg']");
	$errmsg=$("#viewdesigndocsdiv span[name='errmsg0']");
	if(data==null){
   		alert("请求异常！请与系统管理员联系，确定服务器是否运行正常！1");
   		$loadingimg.hide();
   		$tbody.empty();
        return;
   	}
	if(data.errmsg!=""){
       		$errmsg.text("查询失败："+data.errmsg);
       		$loadingimg.hide();
       		$tbody.empty();
       		return;
    }
    if(data.result==""){
		$loadingimg.hide();
		$errmsg.text("查询结果数：0");
		$tbody.empty();
		return;
	}
    var studocarr=eval(data.result);
    $tbody.empty();
    $.each(studocarr,function(index,item){
    	var xh=index+1;
    	var validweekupnum=item.validweekupnum;
    	var weekupnumstr="";
    	if(validweekupnum<8){
    		weekupnumstr="<label class='flagmin'>"+validweekupnum+"</label>";
    	}else{
    		weekupnumstr="<label>"+validweekupnum+"</label>";
    	}
      	$tbody.append("<tr><td>"+xh+"</td><td>"+item.classname+"</td><td>"+item.stuid+"</td>"
                     +"<td>"+item.sname+"</td>"
                     +"<td>"+docstatustip(item.paperblindstatus)+"</td>"+
                     "<td><input type='hidden' value='paper'>"+docstatustip(item.paperstatus)+"</td>"+
                     "<td><input type='hidden' value='translation'>"+docstatustip(item.translationstatus)+"</td>"+
                     "<td><input type='hidden' value='sourcecode'>"+docstatustip(item.sourcecodestatus)+"</td>"+
                     "<td>"+weekupnumstr+"</td>"+
                     "</tr>");
    });
    
    $tbody.tbhighligt();//高亮显示当前行
    
    $loadingimg.hide();
	$errmsg.text("查询结果数："+studocarr.length);
	//绑定撤销归档操作
	$tbody.find("label[name='cancelfiling']").click(function(){
		if(confirm("确定要撤销归档？")==false) return;
		$this=$(this);
		var stuid=$this.parent().siblings().eq(2).text();
		var doctype=$this.prev().val();
		//文档状态改为“已上传”
		$.getJSON("../SubSubmitServlet?mode=submitDocForTea",{stuid:stuid,doctype:doctype,status:'1'},function call(data){ 
			if(data==null){
		   		alert("请求异常！请与系统管理员联系，确定服务器是否运行正常！1");
		        return;
		   	}
			if(data.errmsg!=""){
	    		alert(data.errmsg);
	    		return;
	    	}
			alert("已成功撤销归档！");
			$this.parent().html("<span class='flag'>已上传</span>");
		});
	});
}
//文档状态提示
function docstatustip(docstatus){
	var statusstr=docstatus;
	if(!(docstatus=="已归档"||docstatus=="已盲审")){
		statusstr="<span class='flag'>"+docstatus+"</span>";
	}else if(docstatus=="已归档"){
		statusstr="<label name='cancelfiling' title='点击可撤销归档'>已归档</label>";
	}
	return statusstr;
}