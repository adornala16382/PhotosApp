/**
 * Album initalization class 
 * @author Aditya Rangavajhala Aryan Dornala
 */
package model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Set;
/**
 * Creates the Album class to store Data about attributes for each album
 */
public class Album implements Serializable{

    private String name;

    private String owner;

    private ArrayList <Photo> photoSet;

    private int numPhotos;

    private Date earliestDate;

    private Date latestDate;

    private static final Date MAX_DATE = new Date(Long.MAX_VALUE);

    private static final Date MIN_DATE = new Date(0);
/**
 * Two argument constructor for album taking in a name and owner
 * @param name
 * @param owner
 */
    public Album(String name, String owner){
        this.name = name;
        this.owner = owner;
        this.photoSet = new ArrayList<>();
        this.earliestDate = MAX_DATE;
        this.latestDate = MIN_DATE;
        this.setNumPhotos();
    }
/**
 * Gets the name of the album
 * @return name
 */
    public String getName(){
        return name;
    }
/**
 * Sets the name of the album
 * @param newName is the String respresinting the name of the album
 */
    public void setName(String newName){
        name = newName;
    }
/**
 * Sets the number of photos that an album contains
 */
    private void setNumPhotos(){
        if(photoSet == null){
            numPhotos = 0;
        }
        else{
            numPhotos = photoSet.size();
        }
    }
    /**
     * Adds a photo to an album 
     * @param p is the photo to be added
     */

    public void addPhoto(Photo p){
        photoSet.add(p);
        setNumPhotos();
        setEarliestDate();
        setLatestDate();
    }
/**
 * Returns the list of photos an album has
 * @return photoSet
 */
    public ArrayList <Photo> getPhotoSet(){
        return photoSet;
    }
/**
 * returns the number of photos an Album contains
 * @return numPhotos
 */
    public int getNumPhotos(){
        return numPhotos;
    }
    /**
     * Finds the earliest date of a photo in the album
     * @return earliestDate
     */

    public Date getEarliestDate(){
        return earliestDate;
    }

    /**
     * Finds the latest date of a photo in the album
     * @return latestDate
     */
    public Date getLatestDate(){
        return latestDate;
    }

    /**
     * Computes the earliest date that an album has inside of it
     */
    private void setEarliestDate(){
        for(Photo curPhoto: photoSet){
            if(curPhoto.getDate().compareTo(earliestDate) < 0){
                earliestDate = curPhoto.getDate();
            }
        } 
    }
    /**
     * Computes the latest date an album has inside of it
     */

    private void setLatestDate(){
        for(Photo curPhoto: photoSet){
            if(curPhoto.getDate().compareTo(latestDate) > 0){
                latestDate = curPhoto.getDate();
            }
        } 
    }
    /**
     * toString method of an Album that prints its name and the date (earliest + latest)
     */

    public String toString(){
        if(earliestDate == MAX_DATE){
            return name + " " + "(" + numPhotos + ")"; 
        }
        String [] earliestArr = earliestDate.toString().split(" ");
        String [] latestArr = latestDate.toString().split(" ");
        ArrayList<String> newEarliestArr = new ArrayList<>();
        ArrayList<String> newLatestArr = new ArrayList<>();;
        for(int i=0; i < 3; i++){
            newEarliestArr.add(earliestArr[i]);
            newLatestArr.add(latestArr[i]);
        }
        return name + " " + "(" + numPhotos + ") " + String.join(" ", newEarliestArr) + " - " + String.join(" ", newLatestArr); 
    }

}
