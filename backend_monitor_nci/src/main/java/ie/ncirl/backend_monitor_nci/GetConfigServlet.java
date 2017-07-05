package ie.ncirl.backend_monitor_nci;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Administrativo on 04/07/2017.
 */

public class GetConfigServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        resp.setContentType("text/plain");
        resp.getWriter().println("Please use the form to POST to this url");
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        String appId      = req.getParameter("appId");
        String methodName = req.getParameter("methodName");
        String device     = req.getParameter("device");

        //TODO implement the logic here - STATISTICS

        resp.setStatus(200);

    }
}
