import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by 23878410v on 08/02/17.
 */
public class ServerData {
    private ArrayList<Socket> listSockets = new ArrayList<>();

    public ServerData() {

    }

    public ArrayList<Socket> getListSockets() {
        return listSockets;
    }

    public void Log(String type, String client, String request, String response){
        File log = new File("log.xml");
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc;
            Element rootElement;
            if (!log.exists()) {
                doc = docBuilder.newDocument();
                rootElement = doc.createElement("log");
                doc.appendChild(rootElement);
            } else {
                doc = docBuilder.parse(log);
                rootElement = doc.getDocumentElement();
            }

            Element connection = doc.createElement("connection");
            connection.setAttribute("type", type);
            connection.appendChild(createElement(doc, "IPAddress", client));
            connection.appendChild(createElement(doc, "request", request));
            connection.appendChild(createElement(doc, "response", response));

            rootElement.appendChild(connection);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(log);
            transformer.transform(source, result);

        }catch (IOException e){
            e.printStackTrace();
        }catch (SAXException e){
            e.printStackTrace();
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        }
    }

    public Element createElement(Document doc, String label, String value){
        Element element = doc.createElement(label);
        element.appendChild(doc.createTextNode(value));
        return element;
    }

}
