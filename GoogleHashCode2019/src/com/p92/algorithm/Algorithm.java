package com.p92.algorithm;

import com.p92.bom.Photo;
import com.p92.bom.Slide;
import com.p92.bom.SlideShow;
import com.p92.bom.WithTags;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
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
        List<Slide> verticalSlides = calculateRandomPairingOfVerticalPhotos(onlyVerticalPhotos);
        List<Slide> horizontalSlides = SlideShow.getAllHorizontalPhotosAsSlide(photos);
        int numberOfSlides = verticalSlides.size() + horizontalSlides.size();
        List<Slide> allSlides = new ArrayList<>(verticalSlides); //vertical + horizontal
        allSlides.addAll(horizontalSlides);
        List<List<Integer>> slidesScoreMatrix = new LinkedList<>(); //vertical + horizontal
//        int[][] slidesScoreMatrix = new int[numberOfSlides][numberOfSlides]; 
        //calculate the scores for the whole matrix even if there will be duplicates
        int maxScore = -1;
        int maxRowIndex = 0;
        int maxColumnIndex = 0;
        System.out.println("----------------- STARTING TO CREATE SCORE MATRIX -----------------");
        for(int columnIndex=0; columnIndex<numberOfSlides; columnIndex++) {
            slidesScoreMatrix.add(new LinkedList<>());
            //System.out.println("Calculating column: " + columnIndex + " / " + numberOfSlides);
            System.out.println(columnIndex);
            for(int rowIndex=0; rowIndex<numberOfSlides; rowIndex++) {
                int transitionScore = -1;
                if(columnIndex == rowIndex) {
                    transitionScore = 0; //the same photo has a score of 0
                }
                else if(rowIndex < columnIndex) { //do not calculate if we already calculated it
                    transitionScore = slidesScoreMatrix.get(rowIndex).get(columnIndex);
                    //if(slidesScoreMatrix.get(rowIndex).get(columnIndex) == null) {
                    //    throw new RuntimeException(columnIndex + " " + rowIndex + " was null");
                    //}
                }
                else {
                    transitionScore = SlideShow.calculateTransitionScore(allSlides.get(rowIndex), allSlides.get(columnIndex));
                }
                if(maxScore < transitionScore) {
                    maxScore = transitionScore;
                    maxRowIndex = rowIndex;
                    maxColumnIndex = columnIndex;
                }
                slidesScoreMatrix.get(columnIndex).add(rowIndex, transitionScore);
            }
            //printScoreMatrix(allSlides, slidesScoreMatrix);
        }
        //printScoreMatrix(allSlides, slidesScoreMatrix);
        System.out.println("Max score=" + maxScore + " from listElement(" + maxColumnIndex + ") transition(PHOTO_ID:" + allSlides.get(maxColumnIndex).getPretyId() + ") to listElement(" + maxRowIndex + ") transition(PHOTO_ID:" + allSlides.get(maxRowIndex).getPretyId() + ")");
        //we got the best matching of vertical photos
        slideShow.addSlides(allSlides.get(maxRowIndex), allSlides.get(maxColumnIndex));
        Set<Integer> alreadyUsedSlideIndices = new HashSet<>();
        alreadyUsedSlideIndices.add(maxRowIndex);
        alreadyUsedSlideIndices.add(maxColumnIndex);
        //we have the first 2 slides with the highest transition as start. Now check from this second slide, what is the next best transition and continue it
        boolean hasMorePhotosForTransition = true;
        System.out.println("----------------- STARTING TO CREATE SLIDESHOW USING BEST TRANSITIONS -----------------");
        while(hasMorePhotosForTransition) {
            //System.out.println(slideShow.toString()); //Photo ID-s in the current slide show
            System.out.println(alreadyUsedSlideIndices.size() + " / " + allSlides.size());
            int transitionFromSlideIndex = maxRowIndex;
            maxColumnIndex = 0;
            maxRowIndex = 0;
            hasMorePhotosForTransition = false;
            for(int columnIndex=1; columnIndex<numberOfSlides; columnIndex++) {
                if(!alreadyUsedSlideIndices.contains(columnIndex)) {
                    hasMorePhotosForTransition = true;
                    if(slidesScoreMatrix.get(maxRowIndex).get(maxColumnIndex) <= slidesScoreMatrix.get(transitionFromSlideIndex).get(columnIndex)) {
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
    
    private static void printScoreMatrix(List<? extends WithTags> allSlides, List<List<Integer>> slidesScoreMatrix) {
        final String TAB = "\t\t";
        System.out.print(TAB);
        for(int columnIndex=0; columnIndex<slidesScoreMatrix.size(); columnIndex++) {
            if(columnIndex == 0) {
                for(int rowIndex=0; rowIndex<slidesScoreMatrix.get(columnIndex).size(); rowIndex++) {
                    System.out.print(allSlides.get(rowIndex).getPretyId() + TAB);
                }
                System.out.println();
            }
            System.out.print(allSlides.get(columnIndex).getPretyId() + TAB);
            for(int rowIndex=0; rowIndex<slidesScoreMatrix.get(columnIndex).size(); rowIndex++) {
                System.out.print(slidesScoreMatrix.get(columnIndex).get(rowIndex) + TAB);
            }
            System.out.println();
        }
        System.out.println();
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
    
    private static List<Slide> calculateRandomPairingOfVerticalPhotos(List<Photo> onlyVerticalPhotos) {
        //we can calculate the best scoring vertical photo matches, still they can score worse with the horizontal photos
        List<Slide> verticalPhotoSlides = new ArrayList<>();
        Collections.shuffle(onlyVerticalPhotos);
        Slide slide;
        for(int i=0; i<onlyVerticalPhotos.size(); i+=2) {
            if(i+1 < onlyVerticalPhotos.size()) { //if there is a next pair still
                slide = new Slide(onlyVerticalPhotos.get(i), onlyVerticalPhotos.get(i + 1));
                verticalPhotoSlides.add(slide);
            }
        }
        System.out.println("There were " + onlyVerticalPhotos.size() + " vertical photos, which were made into " + verticalPhotoSlides.size() + " slides.");
        return verticalPhotoSlides;
    }
    
    //TODO implement real calculation
    private static List<Slide> calculateBestPairingOfVerticalPhotos(List<Photo> onlyVerticalPhotos) {
        List<Slide> verticalPhotoSlides = new ArrayList<>();
        //onlyVerticalPhotos.stream().forEach(p -> System.out.println(p));
        List<List<Integer>> slidesScoreMatrix = new LinkedList<>();
        //calculate the scores for all of the vertical photos matrice even if there will be duplicates
        int maxScore = -1;
        int maxRowIndex = 0;
        int maxColumnIndex = 0;
        for(int columnIndex=0; columnIndex<onlyVerticalPhotos.size(); columnIndex++) {
            slidesScoreMatrix.add(new LinkedList<>());
            for(int rowIndex=0; rowIndex<onlyVerticalPhotos.size(); rowIndex++) {
                int transitionScore = SlideShow.calculateTransitionScore(onlyVerticalPhotos.get(rowIndex), onlyVerticalPhotos.get(columnIndex));
                if(maxScore < transitionScore) {
                    maxScore = transitionScore;
                    maxRowIndex = rowIndex;
                    maxColumnIndex = columnIndex;
                }
                //System.out.println("Max score=" + maxScore + " from listElement(" + maxColumnIndex + ") transition(PHOTO_ID:" + onlyVerticalPhotos.get(maxColumnIndex).getId() + ") to listElement(" + maxRowIndex + ") transition(PHOTO_ID:" + onlyVerticalPhotos.get(maxRowIndex).getId() + ")");
                slidesScoreMatrix.get(columnIndex).add(rowIndex, transitionScore);
            }
        }
        //System.out.println("Max score=" + maxScore + " from listElement(" + maxColumnIndex + ") transition(PHOTO_ID:" + onlyVerticalPhotos.get(maxColumnIndex).getId() + ") to listElement(" + maxRowIndex + ") transition(PHOTO_ID:" + onlyVerticalPhotos.get(maxRowIndex).getId() + ")");
        //printScoreMatrix(onlyVerticalPhotos, slidesScoreMatrix);
        //we got the best matching of vertical photos
        Slide slide = new Slide(onlyVerticalPhotos.get(maxColumnIndex), onlyVerticalPhotos.get(maxRowIndex));
        verticalPhotoSlides.add(slide);
        //calculate the next best match for 2 vertical photos
        
        //TODO implement real calculation
        
        for(int i=0; i<onlyVerticalPhotos.size(); i++) {
            if(i+1 < onlyVerticalPhotos.size()) {
                //if there is a next pair still
                slide = new Slide(onlyVerticalPhotos.get(i), onlyVerticalPhotos.get(i + 1));
                verticalPhotoSlides.add(slide);
            }
        }
        return verticalPhotoSlides;
    }
    
    
}
