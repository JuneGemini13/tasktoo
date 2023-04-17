package g21f7853_task2;

import java.io.File;
import java.io.FileWriter;
import java.util.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class App {

    public static void main(String[] args) {
        try {
            String[] fieldsToOutput = readUserSelectedFields();
            List<String> fieldsList = Arrays.asList(fieldsToOutput);
            File inputFile = new File("src/main/resources/data.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("record");
            JSONArray records = new JSONArray();
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    JSONObject record = new JSONObject();
                    for (String field : fieldsList) {
                        String value = eElement.getElementsByTagName(field).item(0).getTextContent();
                        record.put(field, value);
                    }
                    records.put(record);
                }
            }
            System.out.println(records.toString(4));
            FileWriter fileWriter = new FileWriter("src/main/resources/output.json");
            fileWriter.write(records.toString(4));
            fileWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String[] readUserSelectedFields() {
        String input = System.console().readLine("Enter comma-separated fields to output: ");
        return input.split(",");
    }
}

