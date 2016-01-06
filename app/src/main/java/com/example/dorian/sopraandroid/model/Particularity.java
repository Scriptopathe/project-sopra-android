package com.example.dorian.sopraandroid.model;

import org.jdom2.Element;

/**
 * Created by dorian on 06/01/16.
 */
public class Particularity {
    private int id;
    private String name;

    public Particularity (Element particularity) {
        this.id = Integer.parseInt(particularity.getAttributeValue("id"));
        this.name = particularity.getChildText("Name");
    }

    public String getName() {
        return this.name;
    }
    public int getId() {
        return this.id;
    }

    @Override
    public String toString() {
        String ret;
        ret = "Site avec l'id :"+Integer.toString(this.id)+"\n";
        ret += "\tNom : "+this.name+"\n";
        return ret;
    }
}
