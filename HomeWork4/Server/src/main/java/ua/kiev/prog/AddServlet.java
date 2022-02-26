package ua.kiev.prog;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class AddServlet extends HttpServlet {
	private MessageList msgList = MessageList.getInstance();

    private void saveFile(String userName, String fileName, InputStream file) {
        File dir = new File(Message.FILES_STORAGE + userName);
        if(!dir.exists()) {
            dir.mkdir();
        }
        File fl = new File(dir.getAbsolutePath() + "/" + fileName);
        if(fl.exists()) {
            return;
        }

        try{
            fl.createNewFile();
            FileOutputStream fos = new FileOutputStream(fl);
            byte[] buf = new byte[20024];
            for(int len = file.read(buf); len > 0; len = file.read(buf)){
                fos.write(buf, 0, len);
            }
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String fileName = req.getParameter("file");
        String user = req.getParameter("user");
        if(fileName != null) {
            saveFile(user, fileName, req.getInputStream());
        } else {
            byte[] buf = requestBodyToArray(req);
            String bufStr = new String(buf, StandardCharsets.UTF_8);
            Message msg = Message.fromJSON(bufStr);
            if (msg != null)
                msgList.add(msg);
            else
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
	}

	private byte[] requestBodyToArray(HttpServletRequest req) throws IOException {
        InputStream is = req.getInputStream();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[10240];
        for(int r = is.read(buf); r > -1; r = is.read(buf)) {
            if (r > 0) {
                bos.write(buf, 0, r);
            }
        }
        return bos.toByteArray();
    }
}
