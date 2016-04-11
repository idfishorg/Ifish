package com.panfishingllc.ifish;

/**
 * Created by jing on 4/10/2016.
 */
public class Species {
    private int id;
    private String name;
    private int thumbnail;

    public Species(int id, String name, int thumbnail) {
        this.id = id;
        this.name = name;
        this.thumbnail = thumbnail;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setId(int id) {
        this.id = id;
        return;
    }

    public void setName(String name) {
        this.name = name;
        return;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
        return;
    }
}
