package com.p92.bom;

/**
 * Interface which should return the expected output format
 * 
 * @author Balu_ADMIN
 */
public interface ExpectedOutputFormat {
    /**
     * The data separator between the fields.
     */
    public static final String DATA_SEPARATOR = "\t";
    /**
     * Maximum of 5 digits is allowed in the fractional part of the solution. The rest will be trimmed if there is any.
     */
    public static final int MAX_FRACTIONAL_PART = 5;
    /**
     * This method returns the string content in the expected output format, which should be writen into the solution output file.
     * 
     * @return 
     */
    public String toExpectedOutputFormat();
}
