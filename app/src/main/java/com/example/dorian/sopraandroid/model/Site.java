package com.example.dorian.sopraandroid.model;

import org.jdom2.Element;

/**
 * Created by dorian on 04/01/16.
 */
public class Site {
    private int id;
    private String name;
    private String address;

    public Site (Element site) {
        this.id = Integer.parseInt(site.getAttributeValue("id"));
        this.name = site.getChildText("Name");
        this.address = site.getChildText("Address");
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        String ret;
        ret = "Site avec l'id :"+Integer.toString(this.id)+"\n";
        ret += "\tNom : "+this.name+"\n";
        ret += "\tAdresse : "+this.address;
        return ret;
    }
}
