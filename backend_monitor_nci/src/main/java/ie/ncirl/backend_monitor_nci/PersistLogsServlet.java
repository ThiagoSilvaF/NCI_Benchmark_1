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
            java.util.logging.Logger.getLogger("todtm.getImage").warning("*********** funcionando normal ***********");
            GAEClient g = new GAEClient();
            java.util.logging.Logger.getLogger("todtm.getImage").warning("*********** instanciado ***********");
            DeviceLog device = null;

            device = new Gson().fromJson(param, DeviceLog.class);
            java.util.logging.Logger.getLogger("todtm.getImage").warning("*********** criou o json ***********");

            g.addProcessInfoToTheCloud(device);
            java.util.logging.Logger.getLogger("todtm.getImage").warning( "*********** rodou o cloud ***********");
        }catch (Exception ex){
            java.util.logging.Logger.getLogger("todtm.getImage").warning( "***********  ***********" + ex.getMessage());
            ex.printStackTrace();
            resp.setStatus(500);
            return;
        }

        resp.setStatus(200);
    }
}
