package innopolis.stc21.MA;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class MySocket {
    private static final int DEFAULT_PORT = 8000;

    public static void main(String[] args) {

        int port = getPortFromConfig();

        try {
            ServerSocket serverSocket = new ServerSocket(port);
            while (true) {
                Socket socket = serverSocket.accept();

                InputStream inputStream = socket.getInputStream();
                OutputStream outputStream = socket.getOutputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                //Writer writer = new OutputStreamWriter(outputStream);
                System.out.println("clientRequest:");
                String clientRequest = "";
                while (!clientRequest.equals("\r\n")) {
                    clientRequest = reader.readLine();// +System.lineSeparator();

                    System.out.println( clientRequest);
                }
                socket.close();
                // todo проверить цикличность
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int getPortFromConfig() {
        try {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = documentBuilder.parse("src\\main\\resources\\MySocket.config");

            Node node = document.getDocumentElement();
            NodeList elements = node.getChildNodes();
            for (int i = 0; i < elements.getLength(); i++) {
                Node element = elements.item(i);
                if (element.getNodeType() != Node.TEXT_NODE && element.getNodeName().equals("PORT")) {
                    return Integer.parseInt(element.getChildNodes().item(0).getTextContent());
                }
            }
        } catch (Throwable e) {
            return DEFAULT_PORT;
        }
        return DEFAULT_PORT;
    }
}
