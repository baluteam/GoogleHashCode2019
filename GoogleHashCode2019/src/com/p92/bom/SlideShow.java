/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.p92.bom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
}
