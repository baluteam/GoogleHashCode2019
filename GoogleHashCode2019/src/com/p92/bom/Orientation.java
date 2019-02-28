/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.p92.bom;

/**
 *
 * @author Balu_ADMIN
 */
public enum Orientation {
    H("Horizontal"),
    V("Vertical");
    
    private final String ORIENTATION;
    
    private Orientation(String orientation) {
        this.ORIENTATION = orientation;
    }
    
    public String toPrettyString() {
        return ORIENTATION;
    }
    
    public static Orientation getOrientation(String input) {
        if(H.toString().equals(input)) {
            return H;
        }
        else if(V.toString().equals(input)) {
            return V;
        }
        throw new IllegalArgumentException("No orientation found for: " + input);
    }
}
