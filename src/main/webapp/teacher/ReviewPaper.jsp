<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<!-- 显示待盲审的论文 -->
<input type="button" value="查询" name="searchbtn"/>
<img alt="" src="../images/loading.gif" name="loadingimg" style="display:none">
<span name="errmsg0" class="flag"></span>
<table class="mtable2">
	<tr><th>序号</th><th>论文</th><th>评阅状态</th><th>操作</th></tr>
	<tbody name="teachertb"></tbody>
</table>
<!-- 评阅论文窗口 -->
<div name="reviewpaperwin" class="win">
	<div  class="titlebar">
		<span class="title">毕业设计评阅人评审表（盲审）</span>
		<span class="close" onclick='hideDialog("#reviewpaperdiv [name=\"reviewpaperwin\"]")' title="关闭">X</span>
	</div>  
 	<div class="content"> 
 		<form name="reviewpaperform"  method="post">
			<table class="mtable2">
				<tr align="center"><td colspan=4><span name="subname"></span></td><br></tr>
				<tr align="center" style="font-weight:bold"><td style="width:120px">评审内容</td><td style="width:300px">具 体 要 求</td><td style="width:40px">分值</td><td style="width:10px">评分</td></tr>
	 			<tr align="center">
	 				<td>选题意义</td>
	 				<td>选题的理论意义或实际应用价值，<br>选题的专业性与新颖性</td>
	 				<td>0－2</td>
	 				<td><input type="text" name="significance" value="" class="text" style="width:40px"/></td>
	 			</tr>
	 			<tr align="center">
	 				<td>毕业设计内容</td>
	 				<td>设计内容的难易程度、工作量；</br>方案设计的科学性和合理性；</br>实验真实性和正确性；</br>结论合理性、结果的应用价值；</br>学生掌握基础理论、专业知识、基本工程方法和技能情况；</br>学生分析问题和解决问题的能力</td>
	 				<td>0－10</td>
	 				<td><input type="text" name="designcontent" value="" class="text" style="width:40px"/></td>
	 			</tr>
	 			<tr align="center">
	 				<td>设计说明书撰写能力</td>
	 				<td>概念清晰与分析、设计严谨的程度；</br>术语准确性，文字通顺程度；</br>写作规范性及文字表达能力</td>
	 				<td>0－3</td>
	 				<td><input type="text" name="composeability" value="" class="text" style="width:40px"/></td>
	 			</tr>
	 			<tr align="center">
	 				<td>文献查阅外文翻译</td>
	 				<td>文献查阅的广度和深度，</br>对学科或行业领域知识的了解程度，</br>对文献资料的掌握及综述能力；</br>外文翻译准确性、表述水平及写作规范性</td>
	 				<td>0－3</td>
	 				<td><input type="text" name="translationlevel" value="" class="text" style="width:40px"/></td>
	 			</tr>
	 			<tr align="center">
	 				<td>创新性</td>
	 				<td>对前人工作有一定的改进或突破，或有独特见解。</td>
	 				<td>0－2</td>
	 				<td><input type="text" name="innovative" value="" class="text" style="width:40px"/></td>
	 			</tr>
	 			<tr><td colspan=3 align="right">评阅人评分（保留1位小数）合计：</td><td align="center"><input type="text" name="sumgrade" disabled="disabled" style="width:40px"/></td></tr>
	 			<tr>
	 				<td>评阅结果（请在相应项上划“√”）</td>
					<td colspan=3>
						<span class="flag" name="pass" style="display:none">√</span>评审通过[16,20]&nbsp;&nbsp;&nbsp;&nbsp;
						<span class="flag" name="updatepass" style="display:none">√</span>修改后通过[12,16) &nbsp;&nbsp;&nbsp;&nbsp;
						<span class="flag" name="unpass" style="display:none">√</span>不通过[0,12)
					</td>
				</tr>
	 			<tr><td colspan=4>评阅意见：</br><textarea rows=10 name="reviewopinion"></textarea></td></tr>
	 			<tr><td colspan=2 align="right">评阅日期(yyyy-mm-dd)：</td><td colspan=2><input type="text" name="reviewtime" value="" class="text"/></td></tr>
	 			<tr><td colspan=4 align="right">
	 				<input type="button" name="savebtn" value="保存"/>
	 				<input type="button" name="submitbtn" value="提交"/><input type="hidden" name="subid" value=""/>
	 				<br/>
	 				<span name="notice" class="flag">注意：点击“保存”可暂存评阅信息，以后还能修改；但点击“提交”后将不能再修改任何评阅信息。</span>
	 				</td>
	 			</tr>
	 		</table>
	 	</form>
 	</div>
</div>