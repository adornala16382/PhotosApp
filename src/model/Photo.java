/**
 * Photo Class for photo object
 * @author Aditya Rangavajhala Aryan Dornala
 */
package model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Photo class that stores various attributes about Photos
 */
public class Photo implements Serializable {
    
    private String path;

    private String caption;

    private String name; 

    private Date date;

    private Map <String, ArrayList<String>> tags;
/**
 * 3-arg constructor for photo 
 * @param name is the name of the photo being displayed
 * @param path is the path associated with the photo
 * @param date is the date associated with the photo (last modified)
 */
    public Photo(String name, String path, Date date){
        this.name = name;
        this.path = path;
        this.date = date;  
        this.caption = "";
        this.tags = new HashMap<String, ArrayList<String>>();
    }
    /**
     * Returns the path of an object
     * @return path of the Photo object
     */

    public String getPath(){
        return path;
    }
    /**
     * Returns name of the object
     * @return name of the object
     */
    public String getName(){
        return this.name;
    }
    /**
     * Sets the name of the photo to the specified String
     * @param name is the name provided for the photo
     */
    public void setName(String name){
        this.name = name; 
    }
/**
 * Returns the String caption 
 * @return caption of the photo
 */
    public String getCaption(){
        return caption;
    }
    /**
     * Sets the caption of the photo to the given string
     * @param caption is the inputtd caption to be set
     */
    public void setCaption(String caption){
        this.caption = caption;
    }
/**
 * Returns the date of the photo object
 * @return the date of the photo
 * 
 */
    public Date getDate(){
        return date;
    }
/**
 * Returns the tags hashmap that has tag_name:tag_value
 * @return the hashmaps of tags
 */
    public Map<String, ArrayList<String>> getTags(){
        return tags;
    }
/**
 * Removes a tag from the hashmap
 * @param key is the tag_name
 * @param value is the tag_value
 */
    public void removeTag(String key, String value){
        tags.get(key).remove(value);

    }

    /**
     * Allows for user to set/add tags to a photo
     * @param tagName is the type of tag
     * @param tagValue is the value of the tag
     */
    public void setTags(String tagName, String tagValue){

         if(!tags.containsKey(tagName)){
            ArrayList<String> newArrayList = new ArrayList<String>();
            newArrayList.add(tagValue);
            tags.put(tagName, newArrayList);
        }
        else{
            tags.get(tagName).add(tagValue);
        }
        
    }
    /**
     * A more readable format of the date of the photo 
     * @return the date in string format
     */

    public String dateToString(){
       
        String [] earliestArr = date.toString().split(" ");
        ArrayList<String> newEarliestArr = new ArrayList<>();
        for(int i=0; i < 3; i++){
            newEarliestArr.add(earliestArr[i]);
        }
        return String.join(" ", newEarliestArr); 
    }


    /**
     * toString of photo
     * @return name of the photo 
     */
    public String toString(){
        return this.name;
    }

    
}
