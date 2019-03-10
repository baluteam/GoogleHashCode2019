/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.p92.bom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Balu_ADMIN
 */
public class SlideShow {
    private final List<Slide> slides;

    public SlideShow() {
        this.slides = new ArrayList<>();
    }
    
    public void addSlide(Slide slide) {
        slides.add(slide);
    }
    
    public void addSlides(Slide... slides) {
        this.slides.addAll(Arrays.asList(slides));
    }

    public List<Slide> getSlides() {
        return slides;
    }
    
    /**
     * Returns the score for the transition between two given slides
     * 
     * @param s1
     * @param s2
     * @return 
     */
    public static int calculateTransitionScore(WithTags s1, WithTags s2) {
        //intersection = common values
        Set<String> intersection = new HashSet<>(s1.getTags()); // use the copy constructor
        intersection.retainAll(s2.getTags());
        int intersectionValue = intersection.size();
        //only in s1
        Set<String> onlyInS1 = new HashSet<>(s1.getTags()); // use the copy constructor
        onlyInS1.removeAll(s2.getTags());
        int onlyInS1Value = onlyInS1.size();
        //only in s2
        Set<String> onlyInS2 = new HashSet<>(s2.getTags()); // use the copy constructor
        onlyInS2.removeAll(s1.getTags());
        int onlyInS2Value = onlyInS2.size();
        return Math.min(Math.min(onlyInS1Value, onlyInS2Value), intersectionValue);
    }
    
    public static List<Photo> getAllHorizontalPhotos(final List<Photo> allOfTheOriginalPhotos) {
        List<Photo> onlyHorizontalPhotos = new ArrayList<>();
        for(Photo photo : allOfTheOriginalPhotos) {
            if(photo.isHorizontal()) {
                onlyHorizontalPhotos.add(photo);
            }
        }
        return onlyHorizontalPhotos;
    }
    
    public static List<Photo> getAllVerticalPhotos(final List<Photo> allOfTheOriginalPhotos) {
        List<Photo> onlyVerticalPhotos = new ArrayList<>();
        for(Photo photo : allOfTheOriginalPhotos) {
            if(photo.isVertical()) {
                onlyVerticalPhotos.add(photo);
            }
        }
        return onlyVerticalPhotos;
    }
    
    public static List<Slide> getAllHorizontalPhotosAsSlide(final List<Photo> allOfTheOriginalPhotos) {
        List<Slide> onlyHorizontalPhotosSlide = new ArrayList<>();
        for(Photo photo : allOfTheOriginalPhotos) {
            if(photo.isHorizontal()) {
                onlyHorizontalPhotosSlide.add(new Slide(photo));
            }
        }
        return onlyHorizontalPhotosSlide;
    }

    @Override
    public String toString() {
        return Arrays.toString(slides.toArray());
    }
}
