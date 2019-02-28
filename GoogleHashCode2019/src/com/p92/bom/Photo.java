/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.p92.bom;

import java.util.Arrays;

/**
 *
 * @author Balu_ADMIN
 */
public class Photo {
    private int id;
    private Orientation orientation;
    private String[] tags;

    public Photo(int id, Orientation orientation, String[] tags) {
        this.id = id;
        this.orientation = orientation;
        this.tags = tags;
    }
    
    public Photo(int id, String orientation, String[] tags) {
        this.id = id;
        this.orientation = Orientation.getOrientation(orientation);
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "Photo{" + "id=" + id + ", orientation=" + orientation + ", tags=" + Arrays.toString(tags) + '}';
    }
    
    
}
