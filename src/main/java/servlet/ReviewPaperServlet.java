package servlet;

import bean.ReviewPaperBean;
import bpo.ReviewPaperBpo;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ReviewPaperServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        response.setHeader("Cache-Control", "no-cache");
        String result = "";
        String errmsg = "";
        String mode = request.getParameter("mode");
        ReviewPaperBpo reviewpaperbpo = new ReviewPaperBpo();

        if (mode.equals("assignPaperToTeaForReview")) {//为教师分配盲审论文
            try {
                String resulttemp = reviewpaperbpo.assignPaperToTeaForReview();
                Gson gson = new Gson();
                result = gson.toJson(resulttemp);
            } catch (Exception e) {
                errmsg = e.getMessage();
            }
        } else if (mode.equals("setReviewOpinion")) {//设置盲审意见
            try {
                ReviewPaperBean reviewpaper = this.getReviewPaper(request);
                reviewpaperbpo.setReviewOpinion(reviewpaper);
            } catch (Exception e) {
                errmsg = e.getMessage();
            }
        } else if (mode.equals("getReviewOpinion")) {//查看盲审意见
            String subid = request.getParameter("subid");
            try {
                ReviewPaperBean resulttemp = reviewpaperbpo.getReviewOpinion(subid);
                Gson gson = new Gson();
                result = gson.toJson(resulttemp);
            } catch (Exception e) {
                errmsg = e.getMessage();
            }
        } else if (mode.equals("getAllpapernum")) {//查询教师指导论文数目
            Gson gson = new Gson();
            try {
                result = gson.toJson(reviewpaperbpo.getAllpapernum());
            } catch (Exception e) {
                errmsg = e.getMessage();
            }
        } else if (mode.equals("getPapersReviewedByTid")) {//得到教师盲审的论文信息
            String tid = request.getParameter("tid");
            Gson gson = new Gson();
            try {
                result = gson.toJson(reviewpaperbpo.getPapersReviewedByTid(tid));
            } catch (Exception e) {
                errmsg = e.getMessage();
            }
        } else if (mode.equals("getPaperReviewInfos")) {//按 学生 查询 详细的盲审信息
            String specid = request.getParameter("specid");
            String classname = request.getParameter("classname");
            String sname = request.getParameter("sname");

            Gson gson = new Gson();
            try {
                result = gson.toJson(reviewpaperbpo.getPaperReviewInfos(specid, classname, sname));
            } catch (Exception e) {
                errmsg = e.getMessage();
            }
        } else if (mode.equals("cancelPaperReview")) {//撤销评阅
            String subid = request.getParameter("subid");
            try {
                reviewpaperbpo.cancelPaperReview(subid);
            } catch (Exception e) {
                errmsg = e.getMessage();
            }
        } else {
            errmsg = mode + "未定义";
        }
        if (result.equals("")) {
            response.getWriter().write("{\"errmsg\":\"" + errmsg + "\"}");
        } else {
            response.getWriter().write("{\"result\":" + result + ",\"errmsg\":\"" + errmsg + "\"}");
        }
    }

    private ReviewPaperBean getReviewPaper(HttpServletRequest request) throws Exception {//增加新课题
        ReviewPaperBean reviewpaper = new ReviewPaperBean();
        try {
            //取得课题基本信息
            reviewpaper.setSubid(request.getParameter("subid"));
            reviewpaper.setSignificance(Float.valueOf(request.getParameter("significance")));
            reviewpaper.setDesigncontent(Float.valueOf(request.getParameter("designcontent")));
            reviewpaper.setComposeability(Float.valueOf(request.getParameter("composeability")));
            reviewpaper.setTranslationlevel(Float.valueOf(request.getParameter("translationlevel")));
            reviewpaper.setInnovative(Float.valueOf(request.getParameter("innovative")));
            reviewpaper.setReviewopinion(request.getParameter("reviewopinion"));
            reviewpaper.setReviewtime(request.getParameter("reviewtime"));
            reviewpaper.setSubmitflag(request.getParameter("submitflag"));

        } catch (Exception e) {
            throw e;
        }
        return reviewpaper;
    }

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);

    }
}
