package game;

/**
 * StringHandler makes an array of StringRefs in the constructor, from a specified XML file.
 */
public class StringHandler {

    private final StringRef[] STRING_REFS;

    /**
     * Creates and stores an array of stringRef objects,
     * so they can be looked up by their references
     *
     * @param filePath Filepath to XML-file containing the strings used in GUI
     */
    public StringHandler(String filePath) {
        STRING_REFS = Utility.stringRefGenerator(filePath);
    }

    /**
     * Looks up a reference in the stringRef array, as soon as one call of checkReference returns true,
     * the for loop breaks and returns the corresponding String.
     *
     * @param reference Unique reference/identifier.
     * @return The associated output string.
     */
    public String getString(String reference) {

        // Look over each stringRef and check if their reference matches the input reference
        for (StringRef stringRef : STRING_REFS) {
            if (stringRef.checkReference(reference)) {

                // If found, return that StringRef object's output string
                return stringRef.getString();
            }
        }

        // Error: The reference was not recognised and the argument must therefore be faulty
        throw new IllegalArgumentException(String.format("String reference '%s' not recognised.", reference));
    }
}