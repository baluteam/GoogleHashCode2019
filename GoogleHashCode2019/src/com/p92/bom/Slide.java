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
public class Slide implements ExpectedOutputFormat {

    private final Photo[] photosInSlide;
    private final Set<String> tags = new HashSet<>();
    
    public Slide(Photo photo) {
        if(photo == null || !photo.isHorizontal()) {
            throw new IllegalArgumentException("Wrong photo type. Single photo slide can be only horizontal, but this is: " + photo);
        }
        photosInSlide = new Photo[1];
        photosInSlide[0] = photo;
        tags.addAll(Arrays.asList(photo.getTags()));
    }
    
    public Slide(Photo photo1, Photo photo2) {
        if(photo1 == null || photo2 == null || !photo1.isVertical() || !photo2.isVertical()) {
            throw new IllegalArgumentException("Wrong photo types. Duo photo slide can be only vetical, but these are:\n\t" + photo1 + "\n\t" + photo2);
        }
        photosInSlide = new Photo[2];
        photosInSlide[0] = photo1;
        photosInSlide[1] = photo2;
        tags.addAll(Arrays.asList(photo1.getTags()));
        tags.addAll(Arrays.asList(photo2.getTags()));
    }
    
    @Override
    public String toExpectedOutputFormat() {
        if(photosInSlide.length == 1) {
            return String.valueOf(photosInSlide[0].getId());
        }
        else if(photosInSlide.length == 2) {
            return photosInSlide[0].getId() + DATA_SEPARATOR + photosInSlide[1].getId();
        }
        throw new IllegalArgumentException("Wrong number of photos in a slide: " + photosInSlide.length);
    }
    
    public int getNumberOfPhotosInSlide() {
        return photosInSlide.length;
    }

    public Set<String> getTags() {
        return tags;
    }
}
