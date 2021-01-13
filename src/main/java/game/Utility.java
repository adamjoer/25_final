package game;


import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import game.chance.card.*;
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

        // Load XML-file and get a NodeList of field data.
        NodeList fieldList = getXmlContent(filePath, "field");
        Field[] fieldArr = new Field[fieldList.getLength()];
        try {

            // Extract data from each fieldList element and create Field objects for the Field[].

            for (int i = 0; i < fieldList.getLength(); i++) {

                Node field = fieldList.item(i);

                if (field.getNodeType() == Node.ELEMENT_NODE) {

                    // After ensuring that the field Node is an Element Node, it gets attributes shared by all field data.
                    Element ele = (Element) field;
                    Color color = new Color(getInt(ele, "color"));
                    String fieldType = getString(ele, "fieldType");
                    String title = getString(ele, "title");
                    String subText = getString(ele, "subText");
                    String description = getString(ele, "description");


                    /*
                     Switch on fieldType to get particular values for the specific fieldType.
                     The Field[] is then updated with a new instance of the specific fieldType determined by the switch.
                     */

                    switch (fieldType) {
                        case "Property":
                            int cost = getInt(ele, "cost");
                            int pawnValue = getInt(ele, "pawnValue");
                            int relatedProperties = getInt(ele, "relatedProperties");
                            int nextRelatedProperty = getInt(ele, "nextRelatedProperty");

                            int[] rentLevels = getIntArray(ele, "rentLevels", "level");
                            String subType = getString(ele, "subType");


                            switch (subType) {
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
                            int bail = getInt(ele, "bail");

                            fieldArr[i] = new Jail(title, subText, description, i, color, bail);

                            break;
                        case "GoToJail":
                            int jailPosition = getInt(ele, "jailPosition");

                            fieldArr[i] = new GoToJail(title, subText, description, i, color, jailPosition);

                            break;
                        case "Parking":
                            fieldArr[i] = new Parking(title, subText, description, i, color);

                            break;
                        case "Start":
                            fieldArr[i] = new Start(title, subText, description, i, color);

                            break;
                        case "TaxField":
                            int fine = getInt(ele, "fine");

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

    public static ChanceCard[] chanceCardGenerator(String filePath) {

        // Load XML-file and get a NodeList of chanceCard data.
        NodeList chanceCardList = getXmlContent(filePath, "chanceCard");

        // Get the number of elements in the array by counting chanceCard duplicates specified by the duplicate tag.
        int arrayLength = 0;
        try {
            for (int i = 0; i < chanceCardList.getLength(); i++) {
                Node chanceCard = chanceCardList.item(i);

                if (chanceCard.getNodeType() == Node.ELEMENT_NODE) {
                    Element ele = (Element) chanceCard;
                    arrayLength += getInt(ele, "duplicates");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        int cardCounter = 0;
        //Create a ChanceCard[] to add ChanceCards to.
        ChanceCard[] chanceCards = new ChanceCard[arrayLength];
        try {

            // Extract data from each chanceCardList element and create ChanceCard objects for the ChanceCard[].
            for (int i = 0; i < chanceCardList.getLength(); i++) {

                Node chanceCard = chanceCardList.item(i);

                if (chanceCard.getNodeType() == Node.ELEMENT_NODE) {


                    // After ensuring that the chanceCard Node is an Element Node, it gets attributes shared by all ChanceCard data.
                    Element ele = (Element) chanceCard;
                    String cardText = getString(ele, "cardText");
                    String cardType = getString(ele, "cardType");
                    int duplicates = getInt(ele, "duplicates");
                    int amount;

                    /*
                     Switch on cardType to get particular values for the specific cardType.
                     The ChanceCard[] is then updated with a new instance of the specific cardType determined by the switch.
                     If the specific card is meant to have duplicates, the process is repeated for the number of duplicates.
                     */
                    for (int j = 0; j < duplicates; j++) {
                        switch (cardType) {
                            case "HouseTax":
                                int houseTax = getInt(ele, "houseTax");
                                int hotelTax = getInt(ele, "hotelTax");
                                chanceCards[cardCounter] = new HouseTax(cardText, houseTax, hotelTax);
                                break;

                            case "BankTransaction":
                                amount = getInt(ele, "amount");
                                chanceCards[cardCounter] = new BankTransaction(cardText, amount);
                                break;

                            case "CashFromPlayer":
                                amount = getInt(ele, "amount");
                                chanceCards[cardCounter] = new CashFromPlayer(cardText, amount);
                                break;

                            case "MovePlayer":
                                int increment = getInt(ele, "increment");
                                chanceCards[cardCounter] = new MovePlayer(cardText, increment);
                                break;

                            case "Lottery":
                                int threshold = getInt(ele, "threshold");
                                amount = getInt(ele, "amount");
                                String successText = getString(ele, "successText");
                                String failText = getString(ele, "failText");
                                chanceCards[cardCounter] = new Lottery(cardText, amount, threshold, successText, failText);
                                break;

                            case "MovePlayerToTile":
                                int destination = getInt(ele, "destination");
                                chanceCards[cardCounter] = new MovePlayerToTile(cardText, destination);
                                break;

                            case "MoveToNearestShipping":
                                int[] shippingLocations = getIntArray(ele, "shippingLocations", "location");
                                boolean forward = getBool(ele, "forward");
                                boolean doubleRent = getBool(ele, "doubleRent");

                                chanceCards[cardCounter] = new MoveToNearestShipping(cardText, shippingLocations, forward, doubleRent);
                                break;

                            case "GoToJailCard":
                                int jailPosition = getInt(ele, "jailPosition");
                                chanceCards[cardCounter] = new GoToJailCard(cardText, jailPosition);
                                break;

                            case "OutOfJailCard":
                                chanceCards[cardCounter] = new OutOfJailCard(cardText);
                        }
                        cardCounter++;
                    }
                    // Correcting the index for number of duplicates created in the array.
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

    /**
     * shuffleIntArray utilizes removeFromArray to remove a random element from the int array given and
     * addToArray to apply the same random element to the end of the array.
     *
     * The method is recursive, such that it keeps on removing elements in a random order, until there is only one
     * element left in the initial array. It then appends the single element arrays in the random order they were
     * extracted. This guarantees a shuffled array. As the method picks out by index rather than by value, it ensures
     * that the recursion is well defined.
     *
     * shuffleDeck creates an int array with the same length as BASE_DECK and makes it so the each element of the int
     * array corresponds to its index. The int array is then shuffled with shuffleIntArray and used as a shuffle
     * reference for the drawPile so that each element in the drawPile has a reference to a different element in the
     * BASE_DECK.
     *
     * @param array The int array to shuffle
     * @return A shuffled int array with the same elements as the argument given, in a random order.
     */

    public static int[] shuffleIntArray(int[] array) {

        if (array.length == 1) return array;

        int index = (int) Math.round((Math.random() * (array.length - 1)));
        int[] arrayWithoutIndex = removeFromArray(array, index);
        return addToArray(shuffleIntArray(arrayWithoutIndex), array[index]);
    }

    public static int[] removeFromArray(int[] array, int index) {

        // Copy existing array into temporary array
        int[] temp = array;

        // Change array length
        array = new int[array.length - 1];

        // Copy temporary array into new array, leaving out the element at specified index
        System.arraycopy(temp, 0, array, 0, index);
        System.arraycopy(temp, index + 1, array, index, array.length - index);

        // Return new, shorter array
        return array;
    }

    public static Property[] removeFromArray(Property[] array, int index) {

        // Copy existing array into temporary array
        Property[] temp = array;

        // Change array length
        array = new Property[array.length - 1];

        // Copy temporary array into new array, leaving out the element at specified index
        System.arraycopy(temp, 0, array, 0, index);
        System.arraycopy(temp, index + 1, array, index, array.length - index);

        // Return new, shorter array
        return array;
    }

    public static String[] removeFromArray(String[] array, int index) {

        // Copy existing array into temporary array
        String[] temp = array;

        // Change array length
        array = new String[array.length - 1];

        // Copy temporary array into new array, leaving out the element at specified index
        System.arraycopy(temp, 0, array, 0, index);
        System.arraycopy(temp, index + 1, array, index, array.length - index);

        // Return new, shorter array
        return array;
    }

    public static Color[] removeFromArray(Color[] array, int index) {

        // Copy existing array into temporary array
        Color[] temp = array;

        // Change array length
        array = new Color[array.length - 1];

        // Copy temporary array into new array, leaving out the element at specified index
        System.arraycopy(temp, 0, array, 0, index);
        System.arraycopy(temp, index + 1, array, index, array.length - index);

        // Return new, shorter array
        return array;
    }

    /**
     * addToArray creates a new int array with one more place and fills in that place with a given integer.
     *
     * @param array  The array to add a place to.
     * @param insert The value to assign to the created place.
     * @return The array with the appended int place (placed at the last index).
     */

    public static int[] addToArray(int[] array, int insert) {

        // Copy existing array into temporary array
        int[] temp = array;

        // Change array length
        array = new int[array.length + 1];

        // Copy temporary array into new array
        System.arraycopy(temp, 0, array, 0, temp.length);

        // Add specified element to end of new array
        array[array.length - 1] = insert;

        // Return new, longer array
        return array;
    }

    public static Property[] addToArray(Property[] array, Property insert) {

        // Copy existing array into temporary array
        Property[] temp = array;

        // Change array length
        array = new Property[array.length + 1];

        // Copy temporary array into new array
        System.arraycopy(temp, 0, array, 0, temp.length);

        // Add specified element to end of new array
        array[array.length - 1] = insert;

        // Return new, longer array
        return array;
    }

    public static String[] addToArray(String[] array, String insert) {

        // Copy existing array into temporary array
        String[] temp = array;

        // Change array length
        array = new String[array.length + 1];

        // Copy temporary array into new array
        System.arraycopy(temp, 0, array, 0, temp.length);

        // Add specified element to end of new array
        array[array.length - 1] = insert;

        // Return new, longer array
        return array;
    }

    public static Street[] addToArray(Street[] array, Street insert) {
        // Copy existing array into temporary array
        Street[] temp = array;

        // Change array length
        array = new Street[array.length + 1];

        // Copy temporary array into new array
        System.arraycopy(temp, 0, array, 0, temp.length);

        // Add specified element to end of new array
        array[array.length - 1] = insert;

        // Return new, longer array
        return array;
    }

    public static boolean isInArray(int[] array, int search) {

        for (int i : array) {
            if (i == search) return true;
        }

        return false;
    }

    /**
     * Extracts a boolean from an XML element
     *
     * @param ele An XML element extracted from a document
     * @param tag The XML tag to extract a boolean value from
     * @return The boolean value in that tag
     */
    private static boolean getBool(Element ele, String tag) {
        boolean bool = false;
        try {
            bool = Boolean.parseBoolean(ele.getElementsByTagName(tag).item(0).getTextContent());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bool;
    }


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
     * @param ele   An XML element extracted from a document
     * @param tag   The XML tag to extract an integer value from
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

    /**
     * Variation of getInt without the use of index (for unique tags in the element).
     *
     * @param ele An XML element extracted from a document
     * @param tag The XML tag to extract an integer value from
     * @return The integer requested
     */
    private static int getInt(Element ele, String tag) {
        return getInt(ele, tag, 0);
    }

    /**
     * Extracts an integer array from XML elements with the same tag - number of elements specified by another XML element.
     *
     * @param countTag The tag for the XML element, which holds the tag for the number of ints to extract from the XML doc.
     * @param tag      The tag that holds the requested ints for the array.
     * @return An integer array with the integers from the elements in the XML doc.
     */
    private static int[] getIntArray(Element ele, String countTag, String tag) {
        int count = getInt(ele, countTag);
        int[] intArr = new int[count];
        for (int i = 0; i < count; i++) {
            intArr[i] = getInt(ele, tag, i);
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

        // Create an empty NodeList to contain XML data in.

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

        // try-catch for loading the given file and creating the environment to extract data for the NodeList.

        try {

            // Import the file specified by the filePath as an abstract file.

            File file = new File(filePath);

            // Create a DocumentBuilderFactory and make a new instance of DocumentBuilder.

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newDefaultInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            /*
             DocumentBuilder parses the abstract file to the Document type, and prepares the document so that
             the NodeList can be extracted (in document order).
             */

            Document document = dBuilder.parse(file);
            document.getDocumentElement().normalize();
            nodeList = document.getElementsByTagName(mainTag);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return nodeList;
    }
}
