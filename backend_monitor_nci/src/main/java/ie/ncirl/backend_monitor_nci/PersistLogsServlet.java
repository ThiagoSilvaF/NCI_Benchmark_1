package ie.ncirl.backend_monitor_nci;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Administrativo on 03/07/2017.
 */

public class PersistLogsServlet  extends HttpServlet {

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
        Long ret = 0L;

        try {
            GAEClient g = new GAEClient();
            DeviceLog device = null;

            device = new Gson().fromJson(param, DeviceLog.class);

            g.addProcessInfoToTheCloud(device);
        }catch (Exception ex){
            ex.printStackTrace();
            resp.setStatus(500);
            return;
        }

        resp.setStatus(200);
    }
}
