package com.example.dorian.sopraandroid.model;

import org.jdom2.Element;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by amandine on 12/01/2016.
 */
public class Room {
    private int id;
    private String name;
    private String capacity;
    private ArrayList<Particularity> particularities;
    private ArrayList<Booking> bookings;

    public Room(Element roomSearchResult) {
        bookings = new ArrayList<Booking>();
        System.out.println("roomSearchResult  :  ROOT -->" + roomSearchResult.toString());
        setRoom(roomSearchResult.getChild("Room"));
        Element bookings = roomSearchResult.getChild("Bookings");
        addBooking(bookings.getChildren());
    }

    public void setRoom(Element room){
        List<Element> particularitiesElem;
        this.id = Integer.parseInt(room.getAttributeValue("id"));
        this.name = room.getChildText("Name");
        this.capacity = room.getChildText("Capacity");
 /*       Element particularitiesNode = room.getChild("Particularities");
        particularitiesElem = particularitiesNode.getChildren();
        for (int i = 0; i < particularitiesElem.size(); i++) {
            this.particularities.add(new Particularity(particularitiesElem.get(i)));
        }*/
    }

    public void addBooking(List<Element> bookings){
        System.out.println("bookings  :  ROOT -->" + bookings.toString());
        System.out.println("booking taille : " + bookings.size());
        int j=0;
        Iterator i = bookings.iterator();
        Element courantBooking;
        while (i.hasNext()){
            System.out.println(" j : " + j);
            courantBooking = (Element) i.next();
            Booking book = new Booking(courantBooking);
            this.bookings.add(book);
            System.out.println(" j : " + j);
            j++;
        }
    }

    /*
    List<Element> listOfRoomSearchResult = xmlfile.getChildren("RoomSearchResult");
                            Iterator i = listOfRoomSearchResult.iterator();
                            System.out.println("list of roomSearchResult : "+listOfRoomSearchResult.size());
                            int l =0;
                            while (i.hasNext() ) {
                                //liste temporaire pour les fils
                                List<String> tempList = new ArrayList<String>();

                                //On récupère l'element courant de RoomSearchResult
                                Element courantRoomSearch = (Element) i.next();

                                System.out.println("on sauvegarde la valeur de room");
                                //on sauvegarde la room
                                Room r = new Room(courantRoomSearch);

                                //Le nom de la salle devient un header pour l'affichage
                                this.listDataHeader.add(r.getName());

                                //on ajoute ses booking a la salle
                        //        Element courantBookings = courantRoomSearch.getChild("Bookings");
                        //        r.addBooking(courantBookings);

                                //les bookings de la salle deviennent des childen pour l'affichage
                       /*         ArrayList<Booking> bookings = r.getBookings();
                                for (int k=0; k<bookings.size(); k++) {
                                    tempList.add(bookings.get(k).getDay() + " from " + bookings.get(k).getStartTime() + " to " + bookings.get(k).getEndTime());
                                }*/

    public int getId(){
        return this.id;
    }

    public String getName(){
        return this.name;
    }

    public String getCapacity(){
        return this.capacity;
    }

    public ArrayList<Particularity> getParticularities(){
        return this.particularities;
    }

    public ArrayList<Booking> getBookings(){
        return this.bookings;
    }

}