package innopolis.stc21.MA;

import static innopolis.stc21.MA.ConfigReader.getPortFromConfig;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;

public class MySocket {

    public static void main(String[] args) {
        try {
            int port = getPortFromConfig();
            ServerSocket serverSocket = new ServerSocket(port);
            while (true) {
                try (Socket socket = serverSocket.accept()) {
                    while (!socket.isClosed()) {
                        InputStream inputStream = socket.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                        StringBuffer clientRequest = new StringBuffer();
                        reader.lines().filter(Objects::nonNull).takeWhile(l -> !l.isEmpty()).forEachOrdered(line -> clientRequest.append(line).append(System.lineSeparator()));
                        String request = clientRequest.toString();
                        if(0 == request.length()){
                            socket.close();
                            break;
                        }
                        System.out.println("\nClient Request:");
                        System.out.println(request);

                        String answer = getAnswer(request);
                        OutputStream outputStream = socket.getOutputStream();
                        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
                        System.out.println("\nAnswer:");
                        System.out.println(answer);
                        writer.write(answer);
                        writer.flush();
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Socket is close");
        }
    }

    private static String getAnswer(String request) {
        StringBuffer answer = new StringBuffer();
        if (request.startsWith("GET")) {
            String body = getBody();
            answer.append("HTTP/1.1 200 Hello client\r\n");
            answer.append("Server: MySocket \r\n");
            answer.append("Content-Length: ").append(body.length()).append("\r\n");
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
        return getInternalContent(new File("").getAbsoluteFile()).toString();
    }

    private static StringBuffer getInternalContent(File currentFile) {
        StringBuffer listOfFiles = new StringBuffer();
        if (currentFile != null) {
            File[] files = currentFile.getAbsoluteFile().listFiles();
            for (File file : files) {
                listOfFiles.append(file.getAbsolutePath()).append("<br>").append(System.lineSeparator());
                if (file.isDirectory()) {
                    listOfFiles.append(getInternalContent(file));
                }
            }
        }
        return listOfFiles;
    }
}