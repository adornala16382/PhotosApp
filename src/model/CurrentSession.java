/**
 * CurrentSesion class 
 * @author Aditya Rangavajhala Aryan Dornala
 */

package model;

import java.io.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import app.PhotosLibrary;

import java.nio.file.Files;
import java.nio.file.attribute.FileTime;
/**
 * Maintains session variables that can be accessed in other class
 */

public class CurrentSession implements Serializable{
    

    public static final String storeDir = "src/dat";

    public static final String storeFile = "currentSession.dat";
    
    private Map<String, Map<String, Album>> usersToAlbums;

    private String currentUser;

    private Album selectedAlbum;

    private Photo selectedPhoto;

    /**
     * No-arg constructor for CurrentSession
     */
    public CurrentSession(){
        usersToAlbums = new HashMap<>();
    }
    /**
     * returns the hashmap of users to albums
     * @return userToAlbums
     */
    public Map<String, Map<String, Album>> getUsersToAlbums(){
        return usersToAlbums;
    }
/**
 * Returns the currentUser
 * @return currentUser
 */
    public String getCurrentUser(){
        return currentUser;
    }
/**
 * Returns the arrayList of Albums for a user
 * @return albumList
 */
    public ArrayList<String> getUserAlbums(){
  
        ArrayList<String> albumsList = new ArrayList<String>();
        for(String albumName: usersToAlbums.get(currentUser).keySet()){
            albumsList.add(albumName);
        }
        return albumsList;
    }
/**
 * sets the current user
 * @param user
 */
    public void setCurrentUser(String user){
        currentUser = user;
    }
/**
 * Sets the selected Album
 * @param a
 */
    public void setSelectedAlbum(Album a){
        selectedAlbum = a;
    }
/**
 * Returns the selected Album
 * @return selectedAlbum
 */
    public Album getSelectedAlbum(){
        return selectedAlbum;
    }
/**
 * Sets the selected Photo
 * @param a is photo being set as selcted
 */
    public void setSelectedPhoto(Photo a){
        selectedPhoto = a;
    }
/**
 * Returns the current selected Photo
 * @return selectedPhoto
 */
    public Photo getSelectedPhoto(){
        return selectedPhoto;
    }
/**
 * Writes data to the dat file
 * @param session
 * @throws IOException
 */
    public static void writeApp(CurrentSession session) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(
            new FileOutputStream(storeDir + File.separator + storeFile));
        oos.writeObject(session);
    }
/**
 * Reads values from the dat file
 * @return
 * @throws IOException
 * @throws ClassNotFoundException
 */
    public static CurrentSession readApp() throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(
        new FileInputStream(storeDir + File.separator + storeFile));
        PhotosLibrary.session = (CurrentSession)ois.readObject();
        return PhotosLibrary.session;
    }
/**
 * Saves changed values to the dat file
 * @throws IOException
 */

    public static void save() throws IOException{
        PhotosLibrary.session.writeApp(PhotosLibrary.session);
    }
}
