/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.DAL;

import mytunes.BE.Song;
import mytunes.BE.SongLibrary;
import mytunes.GUI.Controller.FXMLDocumentController;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import mytunes.BE.Playlist;

/**
 *
 * @author Desmoswal
 */
public class TextFileHandler extends FileManager
{
    
    public TextFileHandler()
    {
        //fileName = fileName + ".txt";
    }
    
    @Override
    public void saveAll(List<Song> songList)
    {
        String csvString = "";
        for (Song song : songList)
        {
            //csvString += customer.getName() + "," + customer.getEmail() + String.format("%n"); //name + email + format according to system and localization !!
            csvString += song.getPath() + "," + song.getArtist() + "," + song.getTitle() + "," + song.getGenre() + "," + song.getTime() + "," + song.getUUID() + String.format("%n");
        }
        
        try(BufferedWriter bw = new BufferedWriter(new FileWriter("SongLibrary.txt")))
        {
            bw.write(csvString);
        } 
        
        catch (IOException ex)
        {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public List<Song> getAll()
    {
        List<Song> songlist = new ArrayList();
        
        try(BufferedReader br = new BufferedReader(new FileReader("SongLibrary.txt")))
        {
            Scanner scanner = new Scanner(br);
            
            while(scanner.hasNext())
            {
                //Gets next line in file
                String line = scanner.nextLine();
                //Separates line into array by comma
                //fields[0] is name
                //fields[1] is email
                String[] fields = line.split(",");
                songlist.add(new Song(fields[0].trim(), fields[1].trim(), fields[2], fields[3], fields[4],fields[5].trim()));
                
                
            }
        }
        
        catch(IOException ioe)
        {
            System.out.println("Something went horribly wrong..");
        }
        
        System.out.println(songlist);
        return songlist;
    }
    
    @Override
    public void savePlaylists(List<Playlist> playList)
    {
        String csvString = "";
        for (Playlist playlist : playList)
        {
            //csvString += customer.getName() + "," + customer.getEmail() + String.format("%n"); //name + email + format according to system and localization !!
            //csvString += song.getPath() + "," + song.getArtist() + "," + song.getTitle() + "," + song.getGenre() + "," + song.getTime() + "," + song.getUUID() + String.format("%n");
            csvString += playlist.getName();
            for(Song song : playlist.getSongs()) {
                csvString += "," + song.getUUID(); 
            }
            csvString += String.format("%n");
        }
        
        try(BufferedWriter bw = new BufferedWriter(new FileWriter("PlaylistLibrary.txt")))
        {
            bw.write(csvString);
        } 
        
        catch (IOException ex)
        {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public List<Playlist> getPlaylists()
    {
        SongLibrary libSong = SongLibrary.getInstance();
        List<Playlist> playList = new ArrayList<>();
        
        try(BufferedReader br = new BufferedReader(new FileReader("PlaylistLibrary.txt")))
        {
            Scanner scanner = new Scanner(br);
            
            while(scanner.hasNext())
            {
                //Gets next line in file
                String line = scanner.nextLine();
                //Separates line into array by comma
                //fields[0] is name
                //fields[1] is email
                String[] fields = line.split(",");
                
                //songlist.add(new Song(fields[0].trim(), fields[1].trim(), fields[2], fields[3], fields[4],Integer.parseInt(fields[5].trim())));
                if(fields.length == 1) {
                    playList.add(new Playlist(fields[0],new ArrayList<>()));
                } else {
                    ArrayList<Song> songsInPlaylist = new ArrayList<>();
                    for (int i = 0; i < fields.length; i++) {
                        if(i!=0 && !libSong.getSongList().isEmpty()) {
                            for (Song song : libSong.getSongList()) {
                                if(song.getUUID() == fields[i]) {
                                    songsInPlaylist.add(song);
                                }
                            }
                            
                        }
                    }
                    playList.add(new Playlist(fields[0],songsInPlaylist));
                }
            }
        }
        
        catch(IOException ioe)
        {
            System.out.println("Something went horribly wrong..");
        }
        
        System.out.println(playList);
        return playList;
    }
}
