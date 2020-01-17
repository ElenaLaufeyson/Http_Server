package serverSide;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.simple.JSONObject;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private Socket socket;
    private InputStream input = null;

    public ClientHandler(Socket socket) throws IOException {
        this.socket = socket;
        input = socket.getInputStream();
    }

    private void send(String ans) throws IOException {
        new PrintStream(socket.getOutputStream(), true, "UTF-8").println(ans);
    }

    private String prepareAnswer(int year) {
        JSONObject answer = new JSONObject();
        int code;
        String message;
        if (year == -1) {
            code = 401;
            message = "invalid parameter";
        } else if (year == -2 ) {
            code = 402;
            message ="invalid year";
        } else if (year < 100 ){
            code = 200;
            message = (year % 4 != 0) ?
                    "13/09/" + year : "14/09/" + year;
        }else if (year < 400 ){
            code = 200;
            message = (year % 4 != 0 || year % 100 == 0 ) ?
                    "13/09/" + year % 100 : "14/09/" + year % 100;
        } else {
            code = 200;
            message = (year % 4 != 0 || year % 100 == 0 && year % 400 != 0) ?
                    "13/09/" + year % 100 : "14/09/" + year % 100;

        }
        answer.put("errorCode", code);
        answer.put("dataMessage", message);
        return StringEscapeUtils.unescapeJava(answer.toJSONString());
    }

    private String readHeader() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        StringBuilder builder = new StringBuilder();
        String line;
        while (true) {
            line = reader.readLine();
            if (line == null || line.isEmpty()) {
                break;
            }
            builder.append(line + System.getProperty("line.separator"));
        }
        return builder.toString();
    }

    private int getYear(String header) {
        int year;
        String params = header.substring(header.indexOf("?") + 1,
                header.indexOf(" ", header.indexOf("?") + 1));
        int paramIndex = params.indexOf("=");
        if (paramIndex == -1 || !params.substring(0, paramIndex).equals("year")) {
            return -1;
        }
        try {
            year = Integer.parseInt(params.substring(paramIndex + 1));
            return year;
        } catch (NumberFormatException e) {
            return -2;
        }
    }

    @Override
    public void run() {
        try {
            send(prepareAnswer(getYear(readHeader())));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
