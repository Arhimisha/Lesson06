package innopolis.stc21.MA;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class ConfigReader {

    private static final int DEFAULT_PORT = 8000;
    private static final String DEFAULT_IP = "127.0.0.1";
    private static final String DEFAULT_CONFIG_FILE = "src/main/resources/Config.xml";

    static int getPortFromConfig() {
        String setting = getSettingFromConfig("PORT");
        if (setting == null || setting.trim().isEmpty()) {
            return DEFAULT_PORT;
        }
        return Integer.parseInt(setting);
    }

    static String getIpFromConfig() {
        String setting = getSettingFromConfig("IP");
        if (setting == null || setting.trim().isEmpty()) {
            return DEFAULT_IP;
        }
        return setting;
    }

    private static String getSettingFromConfig(String nameOfSetting) {
        try {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = documentBuilder.parse(DEFAULT_CONFIG_FILE);

            Node node = document.getDocumentElement();
            NodeList elements = node.getChildNodes();
            for (int i = 0; i < elements.getLength(); i++) {
                Node element = elements.item(i);
                if (element.getNodeType() != Node.TEXT_NODE && element.getNodeName().equals(nameOfSetting)) {
                    return element.getChildNodes().item(0).getTextContent();
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
