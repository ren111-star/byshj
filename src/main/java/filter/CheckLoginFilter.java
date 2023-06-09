package filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/*用于检测用户是否登录的过滤器
 *配置参数
 *checkSessionKey 需检查的在Session中保存的关键字
 *redirectURL如果用户未登录，则重定向到指定的页面，URL不包括ContextPath
 *notCheckURLList不做检查的URL列表，以分号分开，并且URL中不包括ContextPath
 * */
public class CheckLoginFilter implements Filter {
    protected FilterConfig filterConfig = null;
    private String redirectURL = null;
    private List notCheckURLList = new ArrayList();
    private String sessionKey = null;

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession();
        if (sessionKey == null) {
            filterChain.doFilter(request, response);
            return;
        }
        if ((!checkRequestURLIntNotFilterList(request)) && session.getAttribute(sessionKey) == null) {
            response.sendRedirect(request.getContextPath() + "/" + redirectURL);
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    public void destroy() {
        notCheckURLList.clear();
    }

    private boolean checkRequestURLIntNotFilterList(HttpServletRequest request) {
        String uri = request.getServletPath() + (request.getPathInfo() == null ? "" : request.getPathInfo());
        return notCheckURLList.contains(uri);
    }

    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
        redirectURL = filterConfig.getInitParameter("redirectURL");
        sessionKey = filterConfig.getInitParameter("checkSessionKey");
        String notCheckURLListStr = filterConfig.getInitParameter("notCheckURLList");
        if (notCheckURLListStr != null) {
            StringTokenizer st = new StringTokenizer(notCheckURLListStr, ";");
            notCheckURLList.clear();
            while (st.hasMoreTokens()) {
                notCheckURLList.add(st.nextToken());
            }
        }
    }
}
