package filter;

import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 每种角色用户只能访问本角色权限
 */
//
@WebFilter
        (
                filterName = "AuthorityFilter",
                servletNames = {"ReviewPaperServlet", "SubjectServlet", "SubSubmitServlet", "StusubServlet", "TeacherServlet", "UserServlet", "ClassServlet",
                        "WeekSumServlet", "StudentServlet", "SpecialityServlet", "SysarguServlet", "SyscodeServlet", "ExcelServlet", "FileDownloadServlet0"}
        )
public class AuthorityFilter implements Filter {
    Logger logger = Logger.getLogger(AuthorityFilter.class);

    private Map<String, String> authorityMap;

    public AuthorityFilter() {
        // TODO Auto-generated constructor stub
    }

    public void destroy() {
    }

    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httprequest = (HttpServletRequest) request;
        HttpServletResponse httpresponse = (HttpServletResponse) response;

        HttpSession session = httprequest.getSession();
        String usertype = (String) session.getAttribute("usertype");
        String userid = (String) session.getAttribute("userid");
        String mode = httprequest.getParameter("mode");//获得请求servlet的方法
        String url = httprequest.getRequestURI();
        String[] partinUrl = url.split("/");

        String curServletname = partinUrl[partinUrl.length - 1];
        String authorityname = usertype + "#" + curServletname + "#" + mode;

        if (usertype.equals("学生")) {
            if (authorityMap.get(usertype + "#" + curServletname) == null && authorityMap.get(authorityname) == null) {
                String errmsg = "无权访问";
                response.setCharacterEncoding("utf-8");
                response.getWriter().write("{\"result\":,\"errmsg\":\"" + errmsg + "\"}");
                return;
            }
        }
        logger.info("[" + userid + "]" + authorityname);
        chain.doFilter(request, response);
    }

    public void init(FilterConfig fConfig) throws ServletException {
        authorityMap = new HashMap<String, String>();
        //管理员权限有所有权限，无需限制
        //教师权限
//		 authorityMap.put("教师#ExcelServlet#exportProgressTableByStu", "");//导出进程表(学生个人)
//		 authorityMap.put("教师#ExcelServlet#exporttaskbook", "");//导出课题任务书
//		 authorityMap.put("教师#ExcelServlet#exporttaskbooksBytid", "");//导出指定教师全部任务书
//		 
//		 authorityMap.put("教师#ReviewPaperServlet#setReviewOpinion", "");//设置盲审意见
//		 authorityMap.put("教师#ReviewPaperServlet#getReviewOpinion", "");//查看盲审意见
//		 authorityMap.put("教师#ReviewPaperServlet#getPapersReviewedByTid", "");//得到教师盲审的论文信息
//		 authorityMap.put("教师#ReviewPaperServlet#getPaperReviewInfos", "");//按 学生 查询 详细的盲审信息
//		 authorityMap.put("教师#ReviewPaperServlet#cancelPaperReview", "");//撤销评阅
//		 
//		 authorityMap.put("教师#StudentServlet#get", "");//根据学号获得学生基本信息
//		 authorityMap.put("教师#StudentServlet#get", "");//根据学号获得学生基本信息
//		 
//		 authorityMap.put("教师#SubjectServlet#edt", "");//编辑课题（增加、修改）
//		 authorityMap.put("教师#SubjectServlet#modifybaseinfo", "");//仅修改课题基本信息（除课题编号、适应专业外）
//		 authorityMap.put("教师#SubjectServlet#get", "");//根据subid得到课题基本信息
//		 authorityMap.put("教师#SubjectServlet#gets", "");//根据tutorid得到课题列表

        //学生权限
        authorityMap.put("学生#ClassServlet#gets", "");
        authorityMap.put("学生#FileDownloadServlet0", "");
        authorityMap.put("学生#SpecialityServlet#gets", "");

        authorityMap.put("学生#StudentServlet#get", "");
        authorityMap.put("学生#StudentServlet#personaledt", "");

        authorityMap.put("学生#StusubServlet#submitpickresultfirst", "");
        authorityMap.put("学生#StusubServlet#getpickedsubsbystu", "");
        authorityMap.put("学生#StusubServlet#delsubsbystu", "");

        authorityMap.put("学生#SubjectServlet#get", "");
        authorityMap.put("学生#SubjectServlet#gets", "");
        authorityMap.put("学生#SubjectServlet#getsubsbyspec", "");
        authorityMap.put("学生#SubjectServlet#getsubsbyspec", "");

        authorityMap.put("学生#SubSubmitServlet#updateDoc", "");
        authorityMap.put("学生#SubSubmitServlet#getUploadstatus", "");
        authorityMap.put("学生#SubSubmitServlet#getUploadstatus", "");

        authorityMap.put("学生#SysarguServlet#getarguByname", "");
        authorityMap.put("学生#SysarguServlet#gets", "");

        authorityMap.put("学生#SyscodeServlet#getcodeByno", "");
        authorityMap.put("学生#SyscodeServlet#gets", "");

        authorityMap.put("学生#UserServlet#modifypwd", "");
        authorityMap.put("学生#UserServlet#get", "");
        authorityMap.put("学生#UserServlet#get", "");

        authorityMap.put("学生#WeekSumServlet#fillInWeekupForStu", "");
        authorityMap.put("学生#WeekSumServlet#getWeekupByWeek", "");

        authorityMap.put("学生#ReviewPaperServlet#getReviewOpinion", "");//查看盲审意见

    }
}
