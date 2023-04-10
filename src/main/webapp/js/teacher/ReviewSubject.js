$(document).ready(function(){
	//绑定 查询 按钮 开始
	$("#reviewsubjectdiv input[value='查询']").click(function(){
		var $teachertb=$("#reviewsubjectdiv [name='teachertb']");
		var $errmsg=$("#reviewsubjectdiv [name='errmsg0']");
    	var $loadmsg=$("#reviewsubjectdiv [name='loadingimg']");
    	var tid=$("#tid").val();
    	
		$.getJSON("../SubjectServlet?mode=getSubjectsReviewedByTid",{tid:tid},function call(data){
			if(data==null){
	       		alert("请求异常！请与系统管理员联系，确定服务器是否运行正常！1");
	       		$loadmsg.hide();
				$teachertb.empty();
	            return;
	       	}
			if(data.errmsg!=""){
				$errmsg.text(data.errmsg);
				$loadmsg.hide();
				$teachertb.empty();
				return;
	       	}
			var reviewsubjectsarr=eval(data.result);
			if(data.result==""){
				$errmsg.text("查询结果数：0");
				$teachertb.empty();
				$loadmsg.hide();
				return;
			}
			$errmsg.text("查询结果数："+reviewsubjectsarr.length);	
			$teachertb.empty();
			$.each(reviewsubjectsarr,function(index,item){
				var xh=index+1;
				var subname=item.subname;
				var subid=item.subid;
				var subopinion=item.reviewopinion;
				if(subopinion==undefined) subopinion="";
				var simsubcount=item.simsubcount;
				
				var ishistorysub="";//是否历史课题
				if(simsubcount>0){
					ishistorysub="<span class='flag'>[历史课题]&nbsp;&nbsp;</span>";
				}
				$teachertb.append("<tr>"+
						"<td>"+xh+"</td>"+
						"<td>"+ishistorysub+"<a href='SubjectViewForReview.jsp?subid="+subid+"' target='_blank'>"+subname+"</a></td>"+
						"<td width=300px><span>"+subopinion+"</span><textarea rows='2' style='display:none'>"+subopinion+"</textarea><span style='display:none'>"+subid+"</span></td>"+
					  "</tr>");
			});
			//设置 修改  保存  按钮有效
			$("#reviewsubjectdiv input[value='修改']").show();
			$("#reviewsubjectdiv input[value='保存']").hide();
			$("#reviewsubjectdiv input[value='返回']").hide();
			//
			$teachertb.tbhighligt();//高亮显示当前行
			$loadmsg.hide();
		});
	});
	// 绑定 查询 按钮 结束
	$("#reviewsubjectdiv input[value='查询']").trigger("click");
	// 绑定 修改 按钮 开始
	$("#reviewsubjectdiv input[value='修改']").click(function(){
		$textformodify=$("#reviewsubjectdiv textarea");
		$textformodify.each(function(){
			$thistext=$(this);
			$textprev=$thistext.prev();
			$thistext.text($textprev.text());
			$thistext.show();
			$textprev.hide();
		});
		$("#reviewsubjectdiv [name='errmsg1']").text("当前可修改审核意见。");
		$(this).hide();
		$(this).next().show();//保存
		$(this).next().next().show();//返回
	});
	// 绑定 修改 按钮 结束
	// 绑定 保存 按钮 开始
	$("#reviewsubjectdiv input[value='保存']").click(function(){
		var $errmsg1=$("#reviewsubjectdiv [name='errmsg1']");
    	var $loadmsg1=$("#reviewsubjectdiv [name='loadingimg1']");
		var subreviewedarr=new Array();
		var index0=0;
		$inputformodify=$("#reviewsubjectdiv textarea").each(function(){
			$thistextarea=$(this);
			var opinion=$thistextarea.val();//修改后的审核意见
			if(opinion==undefined) opinion="";
			var opinionprev=$thistextarea.prev().text();//修改前的审核意见
			if(opinionprev==undefined) opinionprev="";
			if(opinion==opinionprev) return;//跳出本次循环
			///前后意见不同时，则继续
			var subid=$thistextarea.next().text();
			var reviewitem={subid:subid,reviewopinion:opinion};
			subreviewedarr[index0]=reviewitem;
			index0=index0+1;
		});
		if(subreviewedarr.length==0){
			$errmsg1.text("当前审核意见没有发生任何变化，请修改！");
			return;
		}
		//将javascript数组转换成json字符串
		var subreviewedjson=$.toJSON(subreviewedarr);
		var tid=$("#tid").val();
		$errmsg1.text("正在保存，请稍后……");
		$loadmsg1.show();
		$.getJSON("../SubjectServlet?mode=modifyReviewOpinionBytid",{tid:tid,subreviewedjson:subreviewedjson},function call(data){
			if(data==null){
	       		alert("请求异常！请与系统管理员联系，确定服务器是否运行正常！1");
	       		$loadmsg1.hide();
	            return;
	       	}
			if(data.errmsg!=""){
				$errmsg1.text("错误："+data.errmsg);
				$loadmsg1.hide();
				return;
	       	}
			$errmsg1.text("保存成功！");
			$loadmsg1.hide();
		});
	});
	// 绑定 保存 按钮 结束
	
	// 绑定 返回 按钮 开始
	$("#reviewsubjectdiv input[value='返回']").click(function(){
		$inputformodify=$("#reviewsubjectdiv textarea");
		$inputformodify.prev().show();
		$inputformodify.hide();
		//显示 修改 按钮
		var $thisinput=$(this);
		$thisinput.prev().hide();
		$thisinput.prev().prev().show();
		$thisinput.hide();
		$("#reviewsubjectdiv [name='errmsg1']").text("");
		$("#reviewsubjectdiv input[value='查询']").trigger("click");
	});
	// 绑定 返回 按钮 结束
});
