/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.p92.main;

import com.p92.algorithm.Algorithm;
import com.p92.bom.Photo;
import com.p92.bom.SlideShow;
import com.p92.gui.IterationDisplayGUI;
import com.p92.io.FileIO;
import com.p92.parser.InputParser;
import java.util.List;

/**
 *
 * @author Balu_ADMIN
 */
public class Main {
    public static void main(String[] args) {
        FileIO fileIO = new FileIO();
        String fileContent = fileIO.loadUsingGUI();
        List<Photo> photos = null;
        try {
            photos = InputParser.parse(fileContent);
        }
        catch(AssertionError e) {
            fileIO.displayError(e.getMessage());
        }
        IterationDisplayGUI.displayLoadingIndicator();
//        for(Photo photo : photos) {
//            System.out.println(photo);
//        }
        SlideShow slideShow = Algorithm.calculate(photos);
        fileIO.saveSolution(slideShow.getSlides());
        IterationDisplayGUI.hideLoadingIndicator();
    }
}
