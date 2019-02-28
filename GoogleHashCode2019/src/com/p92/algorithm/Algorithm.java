package com.p92.algorithm;

import com.p92.bom.Photo;
import com.p92.bom.SlideShow;
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
     * @param photos
     * @return 
     */
    public static SlideShow calculate(List<Photo> photos) {
        SlideShow slideShow = new SlideShow();
        return slideShow;
    }
    

}
