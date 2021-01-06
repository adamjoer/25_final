package game;

/**
 * StringRef class is intended to make a objects with a reference string for calling in various code.
 * The checkReference compares a given reference with the object's REFERENCE String and returns true if they are the same.
 * The OUTPUT_STRING is the string correct reference should be "translated" to.
 */
public class StringRef {
    private final String reference;
    private final String outputString;

    /**
     * Associates a reference string, used in code, with a unique string that will be output in GUI.
     * This class is used to keep track of translatable text in GUI, so no strings are hardcoded in text.
     *
     * @param reference    The word by which this string will be referenced by in code
     * @param outputString The string that will be associated with the reference word
     */
    public StringRef(String reference, String outputString) {
        this.reference = reference;
        this.outputString = outputString;
    }

    /**
     * Check whether a reference matches with this output string
     *
     * @param compareString The word that will be compared against this Objects reference
     * @return True if the two strings are the same, false otherwise
     */
    public boolean checkReference(String compareString) {
        return reference.equals(compareString);
    }

    public String getString() {
        return outputString;
    }
}