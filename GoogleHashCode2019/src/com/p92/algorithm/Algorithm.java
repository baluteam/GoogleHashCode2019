package com.p92.algorithm;

import com.p92.io.FileIO;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This utility class is responsible for calculating the solution.
 * 
 * @author Balu_ADMIN
 */
public final class Algorithm {
    private static final Logger LOGGER = Logger.getLogger(Algorithm.class.getName());
    
    /**
     * No need for having instances of this class. It is bound to solve an exact problem in a single threaded app. No generic implementation is needed here.
     */
    private Algorithm() {}
    
    /**
     * Calculate the solution of the given problem.
     * 
     * @param dataRows
     * @return even if the dataRows input is null, an empty list will be returned at least
     */
/*    public static List<SolutionWrapper> calculate(List<InputBOM> dataRows) {
        iterationLog = new StringBuilder();
        List<SolutionWrapper> result = new ArrayList<>();
        if(dataRows == null) {
            return result;
        }
        for(InputBOM dataRow : dataRows) {
            double minimumNumberOFSecondsNeededToEndTheGame = calculateMinNumberOfSecondsNeeded(dataRow);
            SolutionWrapper solution = new SolutionWrapper(dataRow, minimumNumberOFSecondsNeededToEndTheGame);
            String msg = solution.toExpectedOutputFormat() + FileIO.LINE_SEPARATOR;
            LOGGER.log(Level.INFO, msg);
            iterationLog.append(msg);
            result.add(solution);
        }
        return result;
    }
*/    

}
