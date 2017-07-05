/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Servlet Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloWorld
*/

package ie.ncirl.backend_monitor_nci;

import java.io.IOException;

import javax.servlet.http.*;

public class SyncDeviceServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        resp.setContentType("text/plain");
        resp.getWriter().println("Please use the form to POST to this url");
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        String param = req.getParameter("param");
        resp.setContentType("text/plain");
        if (param == null) {
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        }
        resp.setStatus(HttpServletResponse.SC_OK);
       // resp.set

    }
}
