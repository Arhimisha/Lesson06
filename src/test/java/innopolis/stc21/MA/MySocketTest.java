package innopolis.stc21.MA;


import org.junit.Test;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Objects;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import static org.junit.Assert.*;
import static innopolis.stc21.MA.ConfigReader.*;

public class MySocketTest {

    @Test
    public void main() {
        Future<Void> task = new FutureTask<Void>(() -> {
            MySocket.main(new String[]{""});
            return null;
        });
        Thread server = new Thread((Runnable) task);
        server.setDaemon(true);
        server.start();

        int port = getPortFromConfig();
        String ip = getIpFromConfig();
        try (Socket socket = new Socket(ip, port)) {
            OutputStream outputStream = socket.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
            InputStream inputStream = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer serverAnswer = new StringBuffer();

            writer.write(String.format("GET / HTTP/1.1\r\nHost: %s:%s\r\n\r\n", ip, port));
            writer.flush();
            reader.lines().filter(Objects::nonNull).takeWhile(l -> !l.isEmpty()).forEachOrdered(line -> serverAnswer.append(line).append(System.lineSeparator()));
            assertTrue(serverAnswer.toString().startsWith("HTTP/1.1 200 Hello client"));

            //Cleaning reader
            while (reader.ready()) {
                reader.read();
            }
            serverAnswer.setLength(0);

            writer.write(String.format("NOT GET / HTTP/1.1\r\nHost: %s:%s\r\n\r\n", ip, port));
            writer.flush();
            reader.lines().filter(Objects::nonNull).takeWhile(l -> !l.isEmpty()).forEachOrdered(line -> serverAnswer.append(line).append(System.lineSeparator()));
            assertTrue(serverAnswer.toString().startsWith("HTTP/1.1 404 Bad Request"));

        } catch (UnknownHostException e) {
            e.printStackTrace();
            fail();
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }
}