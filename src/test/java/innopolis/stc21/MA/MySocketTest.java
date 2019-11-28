package innopolis.stc21.MA;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.*;
import java.net.Socket;
import java.util.Objects;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import static org.junit.Assert.*;
import static innopolis.stc21.MA.ConfigReader.*;

public class MySocketTest {

    @BeforeClass
    public static void startServer(){
        Future<Void> task = new FutureTask<>(() -> {
            MySocket.main(new String[]{""});
            return null;
        });
        Thread server = new Thread((Runnable) task);
        server.setDaemon(true);
        server.start();
    }

    @Test
    public void checkGoodAnswer() {
        String serverAnswer = getServerAnswer("GET / HTTP/1.1\r\nHost: %s:%s\r\n\r\n");
        assertTrue(serverAnswer.startsWith("HTTP/1.1 200 Hello client"));
    }

    @Test
    public void checkBadAnswer() {
        String serverAnswer = getServerAnswer("NOT GET / HTTP/1.1\r\nHost: %s:%s\r\n\r\n");
        assertTrue(serverAnswer.startsWith("HTTP/1.1 404 Bad Request"));
    }

    private String getServerAnswer(String request) {
        final StringBuffer serverAnswer = new StringBuffer();
        final int port = getPortFromConfig();
        final String ip = getIpFromConfig();
        try (Socket socket = new Socket(ip, port);
             OutputStream outputStream = socket.getOutputStream();
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
             InputStream inputStream = socket.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        ) {
            writer.write(String.format(request, ip, port));
            writer.flush();
            reader.lines()
                    .filter(Objects::nonNull)
                    .takeWhile(l -> !l.isEmpty())
                    .forEachOrdered(line -> serverAnswer.append(line).append(System.lineSeparator()));
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.getMessage());
        } finally {
            return serverAnswer.toString();
        }
    }
}