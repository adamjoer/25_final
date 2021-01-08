package game;


import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import game.field.*;
import chance.card.*;
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
        Field[] fieldArr = new Field[fieldList.getLength()];
        try {

            // Extract data from each fieldList element and create Field objects for the Field[].
            for (int i = 0; i < fieldList.getLength(); i++) {

                Node field = fieldList.item(i);

                if (field.getNodeType() == Node.ELEMENT_NODE) {

                    Element ele = (Element) field;
                    Color color = new Color(getInt(ele,"color"));
                    String fieldType = getString(ele,"fieldType");
                    String title = getString(ele, "title");
                    String subText = getString(ele, "subText");
                    String description = getString(ele, "description");




                    switch (fieldType){
                        case "Property":
                            // int cost, buildingCost, pawnValue, relatedProperties,nextRelatedProperty;

                            int cost = getInt(ele,"cost");
                            int pawnValue = getInt(ele, "pawnValue");
                            int relatedProperties = getInt(ele, "relatedProperties");
                            int nextRelatedProperty = getInt(ele, "nextRelatedProperty");

                            int[] rentLevels = getIntArray(ele,"rentLevels","level");
                            String subType = getString(ele, "subType");


                            switch (subType){
                                case "Street":
                                    int buildingCost = getInt(ele, "buildingCost");

                                    fieldArr[i] = new Street(title, subText, description, i, color, cost, pawnValue,
                                            rentLevels, relatedProperties, nextRelatedProperty, buildingCost);
                                    break;

                                case "Shipping":

                                    fieldArr[i] = new Shipping(title, subText, description, i, color, cost,
                                            pawnValue, rentLevels, relatedProperties, nextRelatedProperty);
                                    break;

                                case "Brewery":
                                    fieldArr[i] = new Brewery(title, subText, description, i, color, cost,
                                            pawnValue, rentLevels, relatedProperties, nextRelatedProperty);
                                    break;
                            }
                            break;

                        case "Chance":
                            fieldArr[i] = new Chance(title, subText, description, i, color);

                            break;
                        case "Jail":
                            int bail = getInt(ele,"bail");

                            fieldArr[i] = new Jail(title, subText, description, i, color, bail);

                            break;
                        case "GoToJail":
                            int jailPosition = getInt(ele,"jailPosition");

                            fieldArr[i] = new GoToJail(title, subText, description, i, color, jailPosition);

                            break;
                        case "Parking":
                            fieldArr[i] = new Parking(title, subText, description, i, color);

                            break;
                        case "Start":
                            fieldArr[i] = new Start(title, subText, description, i, color);

                            break;
                        case "TaxField":
                            int fine = getInt(ele,"fine");

                            fieldArr[i] = new TaxField(title, subText, description, i, color, fine);

                            break;
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        // Return array of Field objects
        return fieldArr;
    }
    /**
     * Generates an array of ChanceCard objects from an XML-file.
     *
     * @param filePath Filepath of the XML-file.
     * @return An array of objects inherited from ChanceCard with data from specified XML-file.
     */
    public static ChanceCard[] chanceCardGenerator(String filePath){
        // Load XML-file and get a list of field data.
        NodeList chanceCardList = getXmlContent(filePath, "chanceCard");
        int arrayLength = 0;
        try {
            for (int i = 0; i < chanceCardList.getLength(); i++) {
                Node chanceCard = chanceCardList.item(i);

                if (chanceCard.getNodeType() == Node.ELEMENT_NODE) {
                    Element ele = (Element) chanceCard;
                    arrayLength += getInt(ele,"duplicates");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        ChanceCard[] chanceCards = new ChanceCard[arrayLength];
        try {

            // Extract data from each fieldList element and create Field objects for the Field[].
            for (int i = 0; i < chanceCardList.getLength(); i++) {

                Node chanceCard = chanceCardList.item(i);

                if (chanceCard.getNodeType() == Node.ELEMENT_NODE) {

                    Element ele  = (Element) chanceCard;
                    String cardText = getString(ele,"cardText");
                    String cardType = getString(ele, "cardType");
                    int duplicates = getInt(ele,"duplicates");
                    int amount;

                    for (int j = 0; j < duplicates; j++) {
                        switch (cardType) {
                            case "HouseTax":
                                int houseTax = getInt(ele, "houseTax");
                                int hotelTax = getInt(ele, "hotelTax");
                                chanceCards[i+j] = new HouseTax(cardText,houseTax,hotelTax);
                                break;

                            case "BankTransaction":
                                amount = getInt(ele,"amount");
                                chanceCards[i+j] = new BankTransaction(cardText,amount);
                                break;

                            case "CashFromPlayer":
                                amount = getInt(ele,"amount");
                                chanceCards[i+j] = new CashFromPlayer(cardText,amount);
                                break;

                            case "MovePlayer":
                                int increment = getInt(ele, "increment");
                                chanceCards[i+j] = new MovePlayer(cardText,increment);
                                break;

                            case "Lottery":
                                int threshold = getInt(ele,"threshold");
                                amount = getInt(ele,"amount");
                                chanceCards[i+j] = new Lottery(cardText,amount,threshold);
                                break;

                            case "MovePlayerToTile":
                                int destination = getInt(ele, "destination");
                                chanceCards[i+j] = new MovePlayerToTile(cardText,destination);
                                break;
                        }
                    }
                    // Correcting the index for number of duplicates created in the array.
                    i += duplicates-1;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return chanceCards;
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

            // Go over each Node in the NodeList
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
     * @param index The index for the requested tag (in case of multiple uses of the tag).
     * @return The integer requested
     */
    private static int getInt(Element ele, String tag, int index) {
        int n = Integer.MAX_VALUE;
        try {
            n = Integer.decode(ele.getElementsByTagName(tag).item(index).getTextContent());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return n;
    }
    // Variation of getInt without the use of index (for unique tags in the element).
    private static int getInt(Element ele, String tag){
        return getInt(ele,tag,0);
    }

    /**
     * Extracts an integer array from XML elements with the same tag - number of elements specified by another XML element.
     * @param countTag The tag for the XML element, which holds the tag for the number of ints to extract from the XML doc.
     * @param tag The tag that holds the requested ints for the array.
     * @return An integer array with the integers from the elements in the XML doc.
     */
    private static int[] getIntArray(Element ele, String countTag, String tag){
        int count = getInt(ele,countTag);
        int[] intArr = new int[count];
        for (int i = 0; i < count; i++) {
            intArr[i] = getInt(ele,tag,i);
        }
        return intArr;
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
