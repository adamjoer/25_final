package game;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import game.field.*;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import java.awt.Color;
import java.io.File;

public class Utility {

    /**
     * Generates an array of Field objects from an XML-file.
     *
     * @param filePath Filepath of the XML-file.
     * @return An array of objects inherited from Field with data from specified XML-file.
     */
    public static Field[] fieldGenerator(String filePath) {

        // Load XML-file and get a list of field data.
        NodeList fieldList = getXmlContent(filePath, "field");
        Field[] fields = new Field[fieldList.getLength()];

        try {

            // Go over each Node in the NodeList
            for (int i = 0; i < fieldList.getLength(); i++) {

                // Check if the node is an element
                if (fieldList.item(i).getNodeType() == Node.ELEMENT_NODE) {

                    // Convert the conde to an element
                    Element ele = (Element) fieldList.item(i);

                    // Extract general data from XML-elements
                    String fieldType = getString(ele, "fieldType");
                    String title = getString(ele, "title");
                    String subText = getString(ele, "subText");
                    String description = getString(ele, "description");

                    // Extract potentially additional data and initialise object based on the field type
                    switch (fieldType) {
                        case "Property":
                            Color color = new Color(getInt(ele, "color"));
                            int value = getInt(ele, "value");
                            int relatedPropertyPosition = getInt(ele, "relatedPropertyPosition");
                            fields[i] = new Property(title, subText, description, i, value, color, relatedPropertyPosition);
                            break;

                        case "Chance":
                            fields[i] = new Chance(title, subText, description, i);
                            break;

                        case "Jail":
                            int bail = getInt(ele, "fine");
                            int goToJailPosition = getInt(ele, "goToJailPosition");
                            fields[i] = new Jail(title, subText, description, i, bail, goToJailPosition);
                            break;

                        case "GoToJail":
                            int jailPosition = getInt(ele, "jailPosition");
                            fields[i] = new GoToJail(title, subText, description, i, jailPosition);
                            break;

                        case "Parking":
                            fields[i] = new Parking(title, subText, description, i);
                            break;

                        case "Go":
                            int reward = getInt(ele, "reward");
                            fields[i] = new Go(title, subText, description, i, reward);
                            break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Return array of Field objects
        return fields;
    }

    /**
     * Generates an array of stringRef objects from an XML-file.
     *
     * @param filePath Filepath of the XML-file.
     * @return An array of StringRef objects with data from specified XML-file.
     */
    public static StringRef[] stringRefGenerator(String filePath) {

        // Load XML-file and get a list of string data.
        NodeList stringRefList = getXmlContent(filePath, "stringRef");
        StringRef[] stringRefs = new StringRef[stringRefList.getLength()];

        try {

            // Go over each Node int the NodeList
            for (int i = 0; i < stringRefList.getLength(); i++) {

                // Check if node is an element
                if (stringRefList.item(i).getNodeType() == Node.ELEMENT_NODE) {

                    // Convert node to an element
                    Element ele = (Element) stringRefList.item(i);

                    // Initialise stringRef object with data from file
                    stringRefs[i] = new StringRef(getString(ele, "reference"), getString(ele, "outputString"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Return array of stringRef objects
        return stringRefs;
    }

    /*
     * Extracts a boolean from an XML element
     * @param ele An XML element extracted from a document
     * @param tag The XML tag to extract a boolean value from
     * @return The boolean value in that tag

    private static boolean getBool (Element ele, String tag){
        boolean bool = false;
        try{
            bool = Boolean.parseBoolean(ele.getElementsByTagName(tag).item(0).getTextContent());
        } catch (Exception e){
            e.printStackTrace();
        }
        return bool;
    }
    */

    /**
     * Extracts a String from an XML element.
     *
     * @param ele An XML element extracted from a document
     * @param tag The XML tag to extract a String from
     * @return The String in that XML-tag
     */
    private static String getString(Element ele, String tag) {
        String str;
        try {
            str = ele.getElementsByTagName(tag).item(0).getTextContent();
        } catch (NullPointerException e) {
            str = " ";
        }
        return str;
    }

    /**
     * Extracts an integer from an XML element.
     * Can also decode decimal, hexadecimal, and octal numbers.
     *
     * @param ele An XML element extracted from a document
     * @param tag The XML tag to extract an integer value from
     * @return The integer requested
     */
    private static int getInt(Element ele, String tag) {
        int n = Integer.MAX_VALUE;
        try {
            n = Integer.decode(ele.getElementsByTagName(tag).item(0).getTextContent());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return n;
    }

    /**
     * Imports data from an XML file to a NodeList and returns the NodeList.
     *
     * @param filePath Path to the XML file to read
     * @param mainTag  The primary XML element tag
     * @return A NodeList with each element in the XML file
     */
    private static NodeList getXmlContent(String filePath, String mainTag) {
        // TODO: This method needs some comments so we can explain what it does

        NodeList nodeList = new NodeList() {
            @Override
            public Node item(int index) {
                return null;
            }

            @Override
            public int getLength() {
                return 0;
            }
        };
        try {
            File file = new File(filePath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newDefaultInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document document = dBuilder.parse(file);
            document.getDocumentElement().normalize();
            nodeList = document.getElementsByTagName(mainTag);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return nodeList;
    }
}
