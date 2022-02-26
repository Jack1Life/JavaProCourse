package ua.kiev.prog;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.*;
import java.nio.charset.StandardCharsets;

@WebServlet(name = "requestServlet", value = "/request")
public class RequestsServlet extends HttpServlet {
    private UsersList usrList = UsersList.getInstance();

    private byte[] getFile(String name, String user) {
        File fl = new File(Message.FILES_STORAGE + user + "/" + name);
        int len = (int) fl.length();
        byte[] res = new byte[len];
        try(InputStream is = new FileInputStream(fl)) {
            is.read(res);
        } catch (IOException e) {
            e.printStackTrace();
            res = null;
        }
        return res;
    }

    private byte[] executeRequest(String type, String value, String user) {
        String res = null;
        if(type.equals("whoonline")) {
            res = usrList.getOnlineJSON();
        } else if(type.equals("setstatus")) {
            usrList.updateUser(user, value);
            res = usrList.getUserJSON(user);
        } else if(type.equals("getfile")) {
            return getFile(value, user);
        }
        return res != null ? res.getBytes(StandardCharsets.UTF_8) : null;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String user = req.getParameter("user");
        String reqType = req.getParameter("type");
        String reqValue = req.getParameter("value");
        resp.setContentType("application/json");

        byte[] answer = executeRequest(reqType, reqValue, user);
        if (answer != null) {
            OutputStream os = resp.getOutputStream();
            os.write(answer);
        }
    }
}
