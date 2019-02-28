package com.p92.parser;

import com.p92.bom.ExpectedOutputFormat;
import com.p92.bom.Photo;
import com.p92.io.FileIO;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This utility class handles the parsing of the input string content.
 * 
 * @author Balu_ADMIN
 */
public class InputParser {
    /**
     * The expected data separator of the input format
     */
    private static final String DATA_SEPARATOR = ExpectedOutputFormat.DATA_SEPARATOR;
    /**
     * The expected separator of the input data rows
     */
    private static final String LINE_SEPARATOR = FileIO.LINE_SEPARATOR;
    /**
     * Private constructor for disabling creating instances of this utility class.
     */
    private InputParser() {}
    
    /**
     * This method knows what to expect as the input format and how to parse it and return the business object models
     * 
     * @param inputFileContent
     * @return
     * @throws AssertionError (is enabled in the project as VM option -enableassertions)
     * @throws NullPointerException
     * @throws NumberFormatException 
     */
    public static List<Photo> parse(String inputFileContent) throws AssertionError, NullPointerException, NumberFormatException {
        List<Photo> photos = new ArrayList<>();
        assert (inputFileContent != null && !inputFileContent.equals("")) : "The input file is null or empty";
        String[] lines = inputFileContent.trim().split(LINE_SEPARATOR); //remove white spaces from the beginning and from the end and split the data rows
        assert (0 < lines.length) : "The input file had no data in it";
        Photo photo;
        int numberOfPhotosInInput = Integer.parseInt(lines[0].trim());
        assert (lines.length - 1 == numberOfPhotosInInput) : ((lines.length - 1) + " input photo lines, but " + numberOfPhotosInInput + " were expected.");
        for(int i=1; i<lines.length; i++) {
            String[] fields = lines[i].trim().split(DATA_SEPARATOR);
            int numberOfTagsInInput = Integer.parseInt(fields[1].trim());
            assert (fields.length - 1 == numberOfTagsInInput) : ((fields.length - 1) + " input tags, but " + numberOfTagsInInput + " were expected.");
            photo = new Photo((i-1), fields[0], Arrays.copyOfRange(fields, 2, fields.length));
            photos.add(photo);
        }
        return photos;
    }
}
