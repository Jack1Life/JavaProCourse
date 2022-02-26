package ua.kiev.prog;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class GetListServlet extends HttpServlet {
	
	private MessageList msgList = MessageList.getInstance();
	private UsersList usrList = UsersList.getInstance();

    @Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String fromStr = req.getParameter("fromMsg");
		String toUser = req.getParameter("receiver");
		int from = 0;

		usrList.updateUser(toUser);
		try {
			from = Integer.parseInt(fromStr);
			from =  (from < 0) ? 0 : from;
		} catch (Exception ex) {
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
		}

		resp.setContentType("application/json");

		String json = msgList.toJSON(from, toUser);
		if (json != null) {
			OutputStream os = resp.getOutputStream();
            byte[] buf = json.getBytes(StandardCharsets.UTF_8);
			os.write(buf);
		}
	}
}
