package g21f7853_task2;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class App {
    public static void main(String[] args) {
        try {
            List<String> validFields = Arrays.asList("name", "postalZip", "region", "country", "address", "list");
            List<String> fieldsToOutput = readUserSelectedFields(validFields);
            File inputFile = new File("src/main/resources/data.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("record");
            JSONArray recordsArray = new JSONArray();
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    JSONObject recordObject = new JSONObject();
                    for (String field : fieldsToOutput) {
                        Node fieldNode = eElement.getElementsByTagName(field).item(0);
                        if (fieldNode != null) {
                            String fieldValue = fieldNode.getTextContent();
                            recordObject.put(field, fieldValue);
                        }
                    }
                    recordsArray.put(recordObject);
                }
            }
            System.out.println(recordsArray.toString());
        } catch (ParserConfigurationException | SAXException | IOException | JSONException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static List<String> readUserSelectedFields(List<String> validFields) {
        List<String> fieldsToOutput = new ArrayList<String>();
        try {
            System.out.print("Enter comma-separated fields to output: ");
            String input = System.console().readLine();
            String[] inputFields = input.split(",");
            for (String inputField : inputFields) {
                String field = inputField.trim();
                if (validFields.contains(field)) {
                    fieldsToOutput.add(field);
                } else {
                    System.err.println("Error: Invalid field: " + field);
                }
            }
        } catch (NullPointerException e) {
            System.err.println("Error: Console not available. Exiting.");
            System.exit(1);
        }
        return fieldsToOutput;
    }
}