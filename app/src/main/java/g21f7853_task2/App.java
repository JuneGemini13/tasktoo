package g21f7853_task2;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class App {

    public static void main(String[] args) {
        try {
            List<String> fieldsToOutput = readUserSelectedFields();
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            RecordHandler recordHandler = new RecordHandler(fieldsToOutput);
            saxParser.parse("src/main/resources/data.xml", recordHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static List<String> readUserSelectedFields() {
        List<String> validFields = Arrays.asList("name", "postalZip", "region", "country", "address", "list");
        List<String> fieldsToOutput = new ArrayList<>();
        System.out.println("Enter comma-separated fields to output:");
        String input = System.console().readLine();
        String[] fields = input.split(",");
        for (String field : fields) {
            String trimmedField = field.trim();
            if (validFields.contains(trimmedField)) {
                fieldsToOutput.add(trimmedField);
            } else {
                System.out.println("Invalid field: " + trimmedField);
            }
        }
        return fieldsToOutput;
    }

    private static class RecordHandler extends DefaultHandler {
        private List<String> fieldsToOutput;
        private JSONObject currentRecord;
        private String currentTag;

        public RecordHandler(List<String> fieldsToOutput) {
            this.fieldsToOutput = fieldsToOutput;
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            if (qName.equalsIgnoreCase("record")) {
                currentRecord = new JSONObject();
            }
            currentTag = qName;
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            String value = new String(ch, start, length).trim();
            if (!value.isEmpty()) {
                if (fieldsToOutput.contains(currentTag)) {
                    currentRecord.put(currentTag, value);
                }
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            if (qName.equalsIgnoreCase("record")) {
                JSONArray recordArray = new JSONArray();
                recordArray.put(currentRecord);
                System.out.println(recordArray.toString());
            }
        }
    }

}
