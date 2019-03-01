package com.p92.algorithm;

import com.p92.bom.Photo;
import com.p92.bom.Slide;
import com.p92.bom.SlideShow;
import com.p92.io.FileIO;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
        calculateWithScoreMatrix(slideShow, photos);
        return slideShow;
    }
    
    /**
     * Just a test method without any logic to test the expected output format for the example input
     * @param slideShow
     * @param photos 
     */
    private static void testCalculate(SlideShow slideShow, List<Photo> photos) {
        Slide s1 = new Slide(photos.get(0));
        Slide s2 = new Slide(photos.get(1), photos.get(2));
        Slide s3 = new Slide(photos.get(3));
        slideShow.addSlides(s1,s2,s3);
    }
    
    private static void calculateWithScoreMatrix(SlideShow slideShow, List<Photo> photos) {
        List<Photo> onlyVerticalPhotos = SlideShow.getAllVerticalPhotos(photos);
        List<Slide> verticalSlides = calculateBestPairingOfVerticalPhotos(onlyVerticalPhotos);
        List<Slide> horizontalSlides = SlideShow.getAllHorizontalPhotosAsSlide(photos);
        int numberOfSlides = verticalSlides.size() + horizontalSlides.size();
        List<Slide> allSlides = new ArrayList<>(verticalSlides); //vertical + horizontal
        allSlides.addAll(horizontalSlides); 
        int[][] slidesScoreMatrix = new int[numberOfSlides][numberOfSlides]; //vertical + horizontal
        //calculate the scores for the whole matrix even if there will be duplicates
        for(int columnIndex=0; columnIndex<numberOfSlides; columnIndex++) {
            for(int rowIndex=0; rowIndex<numberOfSlides; rowIndex++) {
                slidesScoreMatrix[columnIndex][rowIndex] = SlideShow.calculateTransitionScore(allSlides.get(rowIndex), allSlides.get(columnIndex));
            }
        }
        //printScoreMatrix(allSlides, slidesScoreMatrix);
        //get the max score from every row and remove that index
        Set<Integer> alreadyUsedSlideIndices = new HashSet<>();
        int maxColumnIndex = 0;
        int maxRowIndex = 0;
        //search only in the upper triangle of the matrix
        for(int columnIndex=1; columnIndex<numberOfSlides; columnIndex++) {
            for(int rowIndex=0; rowIndex<columnIndex; rowIndex++) {
                //slidesScoreMatrix[rowIndex][columnIndex] = 99; //you can use a custom value and then pretty print to see if we are really working in the upper triangle
                if(slidesScoreMatrix[maxRowIndex][maxColumnIndex] < slidesScoreMatrix[rowIndex][columnIndex]) {
                    maxRowIndex = rowIndex;
                    maxColumnIndex = columnIndex;
                }
            }
        }
        //printScoreMatrix(allSlides, slidesScoreMatrix);
        //System.out.printf("slidesScoreMatrix[%d][%d] = %d\n", maxRowIndex, maxColumnIndex, slidesScoreMatrix[maxRowIndex][maxColumnIndex]);
        // ID(1_2) --> ID(3)
        slideShow.addSlides(allSlides.get(maxRowIndex), allSlides.get(maxColumnIndex));
        alreadyUsedSlideIndices.add(maxRowIndex);
        alreadyUsedSlideIndices.add(maxColumnIndex);
        //we have the first 2 slides with the highest transition as start. Now check from this second slide, what is the next best transition and continue it
        boolean hasMorePhotosForTransition = true;
        while(hasMorePhotosForTransition) {
            int transitionFromSlideIndex = maxRowIndex;
            maxColumnIndex = 0;
            maxRowIndex = 0;
            hasMorePhotosForTransition = false;
            for(int columnIndex=1; columnIndex<numberOfSlides; columnIndex++) {
                if(!alreadyUsedSlideIndices.contains(columnIndex)) {
                    hasMorePhotosForTransition = true;
                    if(slidesScoreMatrix[maxRowIndex][maxColumnIndex] <= slidesScoreMatrix[transitionFromSlideIndex][columnIndex]) {
                        maxRowIndex = transitionFromSlideIndex;
                        maxColumnIndex = columnIndex;
                    }
                }
            }
            if(hasMorePhotosForTransition) {
                slideShow.addSlide(allSlides.get(maxColumnIndex));
                alreadyUsedSlideIndices.add(maxColumnIndex);
            }
        }
    }
    
    private static void printScoreMatrix(List<Slide> allSlides, int[][] slidesScoreMatrix) {
        final String TAB = "\t\t";
        System.out.print(TAB);
        for(int columnIndex=0; columnIndex<slidesScoreMatrix.length; columnIndex++) {
            if(columnIndex == 0) {
                for(int rowIndex=0; rowIndex<slidesScoreMatrix[columnIndex].length; rowIndex++) {
                    System.out.print(allSlides.get(rowIndex).getPretyId() + TAB);
                }
                System.out.println();
            }
            System.out.print(allSlides.get(columnIndex).getPretyId() + TAB);
            for(int rowIndex=0; rowIndex<slidesScoreMatrix[columnIndex].length; rowIndex++) {
                System.out.print(slidesScoreMatrix[columnIndex][rowIndex] + TAB);
            }
            System.out.println();
        }
        System.out.println();
    }
    
    private static List<Slide> calculateBestPairingOfVerticalPhotos(List<Photo> onlyVerticalPhotos) {
        //TODO implement real calculation
        List<Slide> verticalPhotoSlides = new ArrayList<>();
        for(int i=0; i<onlyVerticalPhotos.size(); i++) {
            if(i+1 < onlyVerticalPhotos.size()) {
                //if there is a next pair still
                Slide slide = new Slide(onlyVerticalPhotos.get(i), onlyVerticalPhotos.get(i + 1));
                verticalPhotoSlides.add(slide);
            }
        }
        return verticalPhotoSlides;
    }
}
