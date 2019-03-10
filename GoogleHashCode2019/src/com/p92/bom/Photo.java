/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.p92.bom;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Balu_ADMIN
 */
public class Photo implements WithTags {
    private int id;
    private Orientation orientation;
    private Set<String> tags;

    public Photo(int id, Orientation orientation, String[] tags) {
        this.id = id;
        this.orientation = orientation;
        setTags(tags);
    }
    
    public Photo(int id, String orientation, String[] tags) {
        this.id = id;
        this.orientation = Orientation.getOrientation(orientation);
        setTags(tags);
    }

    @Override
    public String toString() {
        return "Photo{" + "id=" + id + ", orientation=" + orientation + ", tags=" + Arrays.toString(tags.toArray()) + '}';
    }
    
    @Override
    public String getPretyId() {
        return "ID(" + id + ")";
    }

    public int getId() {
        return id;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public Set<String> getTags() {
        return tags;
    }
    
    private void setTags(String[] tags) {
        this.tags = new HashSet<>(Arrays.asList(tags));
    }
    
    public boolean isHorizontal() {
        return (orientation != null && Orientation.H.equals(orientation));
    }
    
    public boolean isVertical() {
        return (orientation != null && Orientation.V.equals(orientation));
    }
}
