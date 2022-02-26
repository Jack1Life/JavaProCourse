package ua.kiev.prog;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class Message {
	private static String FILE_MARKER = "#file";
	private static String[] REQUSTS = {
			"#whoonline",
			"#setstatus",

	};
	private Date date = new Date();
	private String from;
	private String to;
	private String text;
	private int number;
	private boolean file;
	private String requestType;

	public Message(String from, String text) {
		this.file = false;
		this.from = from;
		this.text = externParameters(text);
	}

	private String externParameters(String text) {
		String message = text;
		this.requestType = null;

		int toIndx = message.indexOf('@');
		if(toIndx >= 0) {
			this.to = message.substring(toIndx + 1).split(" ")[0];
			message = message.replace("@" + this.to + " ", "").trim();
		}
		if(message.indexOf(Message.FILE_MARKER) >= 0) {
			this.file = true;
			message = message.replace(Message.FILE_MARKER, "").trim();
		}
		for(String req : Message.REQUSTS) {
			if (message.indexOf(req) >= 0) {
				this.requestType = req.substring(1).trim();
				message = message.replace(req, "").trim();
				break;
			}
		}
		return message;
	}

	public String toJSON() {
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
		return gson.toJson(this);
	}
	
	public static Message fromJSON(String s) {
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
		return gson.fromJson(s, Message.class);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder().append("[").append(date)
				.append(", From: ").append(from).append(", To: ").append(to == null ? "all" : to)
				.append("] ");
		if(file) {
			sb = sb.append("New file sent: ");
		}
		return sb.append(text).toString();

	}

	public int send() throws IOException {
		URL obj = new URL(Utils.getURL() + "/add");
		HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/json");
		conn.setDoOutput(true);

		try (OutputStream os = conn.getOutputStream()) {
			String json = toJSON();
			os.write(json.getBytes(StandardCharsets.UTF_8));
			return conn.getResponseCode(); // 200?
		}
	}

	public int sendFile(File fl, String login) throws IOException {
		int resCode = 200;
		String url = Utils.getURL() + "/add?file="+ fl.getName() +"&user=" + login;
		System.out.println("sendFile: " + this.text);
		if(!fl.exists()) {
			System.out.println("File not found!");
			return 400;
		}
		URL obj = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Connection", "Keep-Alive");
		conn.setRequestProperty("Cache-Control", "no-cache");
		conn.setRequestProperty("Content-Type", URLConnection.guessContentTypeFromName(fl.getName()));
		conn.setDoOutput(true);
		try (OutputStream os = conn.getOutputStream(); InputStream is = new FileInputStream(fl)) {
			byte[] bytes = Utils.inputStreamToArray(is);
			os.write(bytes);
			resCode = conn.getResponseCode();
		}
		if(resCode == 200) {
			this.text = fl.getName();
			resCode = this.send();
		}
		return resCode;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public boolean isFile() {
		return file;
	}

	public void setFile(boolean file) {
		this.file = file;
	}
}
