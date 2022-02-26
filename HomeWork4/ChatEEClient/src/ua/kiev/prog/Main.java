package ua.kiev.prog;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		try {
			System.out.println("Enter your login: ");
			String login = scanner.nextLine();
	
			Thread th = new Thread(new GetThread(login, "./"));
			th.setDaemon(true);
			th.start();

            System.out.println("Enter your message: ");
			while (true) {
				String text = scanner.nextLine();
				if (text.isEmpty()) {
					continue;
				}

				int res = 200;
				Message m = new Message(login, text);
				if(m.isFile()) {
					res = m.sendFile(new File(m.getText()), login);
				} else if(m.getRequestType() == null) {
					res = m.send();
				} else {
					res = GetThread.sendRequest(m.getRequestType(), m.getText(), login);
				}
				if (res != 200) {
					System.out.println("HTTP error occured: " + res);
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			scanner.close();
		}
	}
}
