/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

//---------------------
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.MenuButton;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author Desmoswal
 */
public class FXMLDocumentController implements Initializable
{

    @FXML
    private Label labelcount;
    @FXML
    private TableColumn<?, ?> colPlaylistsTitle;
    @FXML
    private TableColumn<?, ?> colPlaylistsSongs;
    @FXML
    private TableColumn<?, ?> colPlaylistsTime;
    @FXML
    private Slider slideVol;
    @FXML
    private Button btnPlay;
    @FXML
    private Button btnPrev;
    @FXML
    private Button btnNext;
    @FXML
    private TableView<?> tblCurPlaylist;
    @FXML
    private TableColumn<?, ?> colCurPlaylistTitle;
    @FXML
    private TableColumn<?, ?> colCurPlaylistTime;
    @FXML
    private TableView<String> tblAllSongs;
    @FXML
    private TableColumn<?, ?> colAllSongsArtist;
    @FXML
    private TableColumn<?, ?> colAllSongsTitle;
    @FXML
    private TableColumn<?, ?> colAllSongsGenre;
    @FXML
    private TableColumn<?, ?> colAllSongsTime;
    @FXML
    private Button btnNewPlaylist;
    @FXML
    private Button btnEditPlaylist;
    @FXML
    private Button btnDeletePlaylist;
    @FXML
    private Button btnAddToPlaylist;
    @FXML
    private Button btnMoveUp;
    @FXML
    private Button btnMoveDown;
    @FXML
    private Button btnDeleteFromPlaylist;
    @FXML
    private Button btnEditSong;
    @FXML
    private Button btnDeleteSong;
    @FXML
    private Slider slideTime;
    @FXML
    private TextField txtSearch;
    @FXML
    private Button btnSearch;
    @FXML
    private ComboBox<?> menuNew;

    Stage stage;

    //@FXML
    //private Label label;
    @FXML
    private void handleButtonAction(ActionEvent event)
    {
        System.out.println("You clicked me!");

        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        System.out.println("Current relative path is: " + s);
        
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("MP3 Files", "*.mp3"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));
        fileChooser.setTitle("Open Music File");

        File files = fileChooser.showOpenDialog(stage);
        String uriString = files.toURI().toString();//(s + "\\sound1.mp3").toURI().toString();
        System.out.println(uriString);
        MediaPlayer player = new MediaPlayer(new Media(uriString));
        player.play();

        //-----Reading Metadata--------
        /**
         * First it adds a listener to the media
         * then it gets every single detail of
         * the media file.
         * You can choose to get every data one by one,
         * using title, album, artist.. etc. 
         * OR using fullMetadata which gets everything.
         * fullMetadata has raw metadata, look out for that.
         */
        Media m = new Media(uriString);
        Media media = new Media(uriString);
        media.getMetadata().addListener((MapChangeListener<String, Object>) change -> 
                {
                    // code to process metadata attribute change.
                    String title = (String) m.getMetadata().get("title");
                    String album = (String) m.getMetadata().get("album");
                    String artist = (String) m.getMetadata().get("artist");
                    String fullMetadata = (String)m.getMetadata().toString();
                    
                    //Actually have no clue what this supposed to be.. 
                    //Maybe for every mediafile in the folder ??
                    //ObservableList<String> medialist = FXCollections.observableArrayList();
                    //     medialist.addAll(medialist);
                    //System.out.println(medialist);
                    
                    System.out.println(title);
                    System.out.println(album);
                    System.out.println(artist);
                    System.out.println(fullMetadata);
        });
        
        //Prints out the files in the folder
        File folder = new File(s);
        File[] listOfFiles = folder.listFiles();

        for (File file : listOfFiles)
        {
            if (file.isFile())
            {
                System.out.println(file.getName());
            }
        }
        
        labelcount.textProperty().bind(
                player.currentTimeProperty().asString());
        
    }

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        // TODO
    }

    @FXML
    private void handleEditSongButtonAction(ActionEvent event)
    {
        //Filechooser with extension filters for selecting music
        //Maybe later folders too
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("MP3 Files", "*.mp3"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));
        fileChooser.setTitle("Open Music File");
        
        fileChooser.showOpenDialog(stage);
    }

    public void handleSavePlaylistAction(ActionEvent e)
    {
        //Saves a .playlist file, supposed to save every detail of the playlists
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Playlist");

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Playlist Files", "*.playlist"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));

        //System.out.println(playlist.getId());
        File file = fileChooser.showSaveDialog(stage);

        if (file != null)
        {
            try
            {
                file.createNewFile();
            } catch (IOException ex)
            {
                System.out.println(ex.getMessage());
            }
        }
    }
}
