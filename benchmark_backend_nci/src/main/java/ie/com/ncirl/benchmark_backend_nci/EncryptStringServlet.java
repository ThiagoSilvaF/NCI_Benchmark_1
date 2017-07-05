package ie.com.ncirl.benchmark_backend_nci;

        import java.io.IOException;
        import java.io.UnsupportedEncodingException;
        import java.security.MessageDigest;
        import java.security.NoSuchAlgorithmException;
        import org.apache.commons.codec.binary.Base64;


        import javax.servlet.http.*;


public class EncryptStringServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        resp.setContentType("text/plain");
        resp.getWriter().println("Please use the form to POST to this url");
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        String password = req.getParameter("text_param");
        resp.setContentType("text/plain");

        MessageDigest mdSha1 = null;
        try
        {
            mdSha1 = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e1) {
            e1.getMessage();
        }
        try {
            mdSha1.update(password.getBytes("ASCII"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] data = mdSha1.digest();
        StringBuffer sb = new StringBuffer();
        String hex=null;

        hex = Base64.encodeBase64String(data);

        sb.append(hex);
        //HashValue=sb.toString();


        resp.getWriter().println(sb.toString());
    }
}
