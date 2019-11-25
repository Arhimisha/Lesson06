package innopolis.stc21.MA;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class ConfigReader {

    private static final int DEFAULT_PORT = 8000;
    private static final String DEFAULT_IP = "127.0.0.1";
    private static final String DEFAULT_CONFIG_FILE= "src\\main\\resources\\MySocket.config";

    static int getPortFromConfig() {
        try {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = documentBuilder.parse(DEFAULT_CONFIG_FILE);

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
    public static String getIpFromConfig() {
        try {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = documentBuilder.parse(DEFAULT_CONFIG_FILE);

            Node node = document.getDocumentElement();
            NodeList elements = node.getChildNodes();
            for (int i = 0; i < elements.getLength(); i++) {
                Node element = elements.item(i);
                if (element.getNodeType() != Node.TEXT_NODE && element.getNodeName().equals("IP")) {
                    return element.getChildNodes().item(0).getTextContent();
                }
            }
        } catch (Throwable e) {
            return DEFAULT_IP;
        }
        return DEFAULT_IP;
    }

}
