package ua.kiev.prog;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

// C -> HTTP -> HTTP -> D
// WebSockets


public class GetThread implements Runnable {
    private final Gson gson;
    private int n;
    private String login;
    private static String filesStorage;

    public GetThread(String login, String filesStorage) {
        this.login = login;
        GetThread.filesStorage = filesStorage;
        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    }

    private static int parseUsers(InputStream is) throws Exception {
        Gson gsonB = new GsonBuilder().create();
        byte[] buf =  Utils.inputStreamToArray(is);
        String strBuf = new String(buf, StandardCharsets.UTF_8);
        JsonUsers list = gsonB.fromJson(strBuf, JsonUsers.class);
        if (list == null) {
            return 404;
        }
        for (User u : list.getList()) {
            System.out.println(u);
        }

        return 200;
    }

    private static int parseFile(InputStream is, String fileName, String login) throws Exception{

        File dir = new File(GetThread.filesStorage + login);
        if(!dir.exists()) {
            dir.mkdir();
        }
        String filePath = dir.getAbsolutePath() + "/" +  fileName;
        try(OutputStream os = new FileOutputStream(new File(filePath))) {
            byte[] buf = new byte[1024];
            for(int len = is.read(buf); len > 0; len = is.read(buf)) {
                os.write(buf, 0, len);
            }
        }
        return 200;
    }

    public static int sendRequest(String type, String value, String login) throws IOException {
        int res = 200;
        String link = Utils.getURL() + "/request?type=" + type + "&value=" + value + "&user=" + login;
        try {
            URL url = new URL(link);
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            try(InputStream is = http.getInputStream()) {
                res = type.equals("getfile") ? parseFile(is, value, login) : parseUsers(is);
                res = res != 200 ? res : http.getResponseCode();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            res = 405;
        }
        return res;
    }

    @Override
    public void run() {
        try {
            for( ;!Thread.interrupted(); Thread.sleep(500)) {
                URL url = new URL(Utils.getURL() + "/get?fromMsg=" + n + "&receiver=" + login);
                HttpURLConnection http = (HttpURLConnection) url.openConnection();

                try(InputStream is = http.getInputStream()) {
                    byte[] buf = Utils.inputStreamToArray(is);
                    String strBuf = new String(buf, StandardCharsets.UTF_8);
                    JsonMessages list = gson.fromJson(strBuf, JsonMessages.class);
                    if (list == null) {
                        continue;
                    }
                    for (Message m : list.getList()) {
                        System.out.println(m);
                        if(m.isFile()) {
                            System.out.println("Load file!");
                            sendRequest("getfile", m.getText(), m.getFrom());
                        }
                        if(m.getNumber() > n){
                            n = m.getNumber();
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
