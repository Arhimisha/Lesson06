package innopolis.stc21.MA;

import static innopolis.stc21.MA.ConfigReader.getPortFromConfig;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class MySocket {

    public static void main(String[] args) {
        try {
            int port = getPortFromConfig();
            ServerSocket serverSocket = new ServerSocket(port);
            while (true) {
                try (Socket socket = serverSocket.accept()) {
                    InputStream inputStream = socket.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    System.out.println("clientRequest:");
                    StringBuffer clientRequest = new StringBuffer();
                    reader.lines().filter(l -> l != null).takeWhile(l -> !l.isEmpty()).forEachOrdered(line -> clientRequest.append(line + System.lineSeparator()));
                    String request = clientRequest.toString();
                    System.out.println(request);

                    String answer = getAnswer(request);
                    OutputStream outputStream = socket.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
                    System.out.println("answer:");
                    System.out.println(answer);
                    writer.write(answer);
                    writer.flush();
                    outputStream.flush();
                    socket.setSoTimeout(3);
                    //Thread.sleep(10000);
                    System.out.println("\nEnds of sleep\n");
//                    reader.close();
//                    writer.close();
                }
                // todo проверить цикличность
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getAnswer(String request) {
        StringBuffer answer = new StringBuffer();
        if (request.startsWith("GET")) {
            String body = getBody();
            answer.append("HTTP/1.1 200 Hello client\r\n");
            answer.append("Server: MySocket \r\n");
            answer.append("Content-Length: " + body.length() + "\r\n");
            answer.append("\r\n");
            answer.append(body);
        } else {
            answer.append("HTTP/1.1 404 Bad Request\r\n");
            answer.append("Server: MySocket \r\n");
            answer.append("Content-Length: 0\r\n");
            answer.append("\r\n");
        }
        return answer.toString();
    }


    private static String getBody() {
        StringBuffer body = new StringBuffer();
        body.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\"\n\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n");
        body.append("<html>");
        body.append("<head>Current folder contains:</head>");
        body.append("<body>");
        body.append(getContentsOfCurrentFolder());
        body.append("</body>");
        body.append("</html>");

        return body.toString();
    }

    private static String getContentsOfCurrentFolder() {
        StringBuffer buffer = new StringBuffer();
        // String absolutePath = new File(".").getAbsolutePath();
        //absolutePath = absolutePath.substring(0, absolutePath.length()-1);
        File currentFolder = new File("");


        return buffer.toString();
    }
}

// todo убрать println