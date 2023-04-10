package servlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.*;
import java.util.HashSet;

public class OnlineListener implements HttpSessionListener, ServletContextListener,
        HttpSessionAttributeListener {
    // 获取application对象
    ServletContext application = null;

    HttpSession session = null;

    HashSet<String> set = new HashSet<String>(); // 存放在线用户的用户名

    int count = 0;

    public void sessionCreated(HttpSessionEvent arg0) {
        //System.out.println("新的会话:");
    }

    public void sessionDestroyed(HttpSessionEvent arg0) {
        session = arg0.getSession();
        if (session.getAttribute("userid") != null) {
            //System.out.println("用户注销....");
            String userid = (String) session.getAttribute("userid");
            set = (HashSet<String>) application.getAttribute("registerUser");
            set.remove(userid);
            application.setAttribute("registerUser", set);
            count = (Integer) application.getAttribute("userCount");
            if (count > 0) {
                count--;
            }
            application.setAttribute("userCount", count);
        }

        if (session == null) {
            //System.out.println("用户超时....");
            count = (Integer) application.getAttribute("userCount");
            if (count > 0) {
                count--;
            }
            application.setAttribute("userCount", count);
        }
    }

    public void attributeAdded(HttpSessionBindingEvent arg0) {
        session = arg0.getSession();
        if (session.getAttribute("userid") != null) {
            String userid = (String) session.getAttribute("userid");
            set = (HashSet<String>) application.getAttribute("registerUser");
            // 新用户登陆
            if (!set.contains(userid)) {
                set.add(userid);
                count = (Integer) application.getAttribute("userCount");
                count++;
                application.setAttribute("userCount", count);
            }
        }
    }

    public void attributeRemoved(HttpSessionBindingEvent arg0) {
    }

    public void attributeReplaced(HttpSessionBindingEvent arg0) {
        // TODO Auto-generated method stub
    }

    public void contextDestroyed(ServletContextEvent arg0) {
        // TODO Auto-generated method stub
    }

    public void contextInitialized(ServletContextEvent arg0) {
        // tomcat启动时创建用于保存人数的application对象
        application = arg0.getServletContext();
        application.setAttribute("userCount", new Integer(0));// 总在线人数
        application.setAttribute("registerUser", new HashSet<String>());// 在线用户名
    }
}
