/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.GUI.Controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import mytunes.BE.*;
import mytunes.BLL.SongManager;

/**
 *
 * @author Desmoswal
 */
public class FXMLDocumentController implements Initializable
{

    @FXML
    private Label labelcount;
    @FXML
    private TableColumn<Playlist, String> colPlaylistsTitle;
    @FXML
    private TableColumn<Playlist, String> colPlaylistsSongs;
    @FXML
    private TableColumn<Playlist, String> colPlaylistsTime;
    @FXML
    private Slider slideVol;
    @FXML
    private Button btnPlay;
    @FXML
    private Button btnPrev;
    @FXML
    private Button btnNext;
    @FXML
    private TableView<SongLibrary> tblCurPlaylist;
    @FXML
    private TableColumn<Song, String> colCurPlaylistTitle;
    @FXML
    private TableColumn<Song, String> colCurPlaylistTime;
    @FXML
    private TableView<Song> tblAllSongs;
    @FXML
    private TableColumn<Song, String> colAllSongsArtist;
    @FXML
    private TableColumn<Song, String> colAllSongsTitle;
    @FXML
    private TableColumn<Song, String> colAllSongsGenre;
    @FXML
    private TableColumn<Song, String> colAllSongsTime;
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
    private MenuItem itemFolder;
    @FXML
    private MenuItem itemFile;

    Stage stage;
    
    private SongLibrary lib = new SongLibrary();
    
    SongManager manager = new SongManager();
    
    String currTitle;
    String currAlbum;
    String currArtist;
    String currFullMetadata;
    
    
    private ObservableList<String> ol = FXCollections.observableArrayList();
    private final Object obj= new Object();

    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
       fillLibTable();
    }
    
    @FXML
    private void openFolder(ActionEvent event)
    {
    
    }
    
    @FXML
    private void openFile(ActionEvent event)
    {
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
        
        readMetadata(uriString);
        
        lib.addSong(new Song(uriString, currArtist, currTitle, "", "0"));
        System.out.println(currArtist + "openfile");
 
        
        fillLibTable();
    }
    
    @FXML
    private void handleButtonAction(ActionEvent event) throws IOException
    {
        System.out.println("You clicked me!");

        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        System.out.println("Current relative path is: " + s);
 
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
        
        /**
         * POPUPS
         */
        // Fetches primary stage and gets loader and loads FXML file to Parent
        Stage primStage = (Stage)tblAllSongs.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/mytunes/GUI/View/EditSongPopup.fxml"));
        Parent root = loader.load();
        
        // Fetches controller from patient view
        EditSongPopupController controller = 
                loader.getController();
        
        //controller.setPatient(patient);
        
        // Sets new stage as modal window
        Stage stagePatientView = new Stage();
        stagePatientView.setScene(new Scene(root));
        
        stagePatientView.initModality(Modality.WINDOW_MODAL);
        stagePatientView.initOwner(primStage);
        
        stagePatientView.show();
        
    }

    @FXML
    private void readMetadata(String uriString)
    {
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
        
        MediaPlayer player = new MediaPlayer(new Media(uriString));
        player.setOnReady(new Runnable() {

        @Override
        public void run() {
            currArtist =(String) player.getMedia().getMetadata().get("artist");
            ol.add(currArtist);
            synchronized(obj){//this is required since mp.setOnReady creates a new thread and our loopp  in the main thread
                obj.notify();// the loop has to wait unitl we are able to get the media metadata thats why use .wait() and .notify() to synce the two threads(main thread and MediaPlayer thread)
            }
            
            System.out.println(currArtist + "aaaaaaaaaaa");
        }
    });
        /*
        Media m = new Media(uriString);
        Media media = new Media(uriString);
        media.getMetadata().addListener((MapChangeListener<String, Object>) change -> 
                {
                    // code to process metadata attribute change.
                    currTitle = (String) m.getMetadata().get("title");
                    currAlbum = (String) m.getMetadata().get("album");
                    currArtist = (String) m.getMetadata().get("artist");
                    currFullMetadata = (String)m.getMetadata().toString();
                    
                    //Actually have no clue what this supposed to be.. 
                    //Maybe for every mediafile in the folder ??
                    //ObservableList<String> medialist = FXCollections.observableArrayList();
                    //     medialist.addAll(medialist);
                    //System.out.println(medialist);

                    System.out.println(currTitle);
                    System.out.println(currAlbum);
                    System.out.println(currArtist);
                    System.out.println(currFullMetadata);
                    
                                
        });*/
    }

    @FXML
    private void handleEditSongButtonAction(ActionEvent event)
    {
        
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
    
    @FXML
    public void fillLibTable() {
        ObservableList<Song> songlist = FXCollections.observableArrayList(lib.getSongList());
        //ObservableList<Song> songlist = FXCollections.observableArrayList() ;
        //tblAllSongs.setItems();
        
            colAllSongsArtist.setCellValueFactory(new PropertyValueFactory("artist"));
            colAllSongsTitle.setCellValueFactory(new PropertyValueFactory("title"));
            tblAllSongs.setItems(songlist);
            
    
            
            //colAllSongsGenre.setCellValueFactory(new PropertyValueFactory("genre"));
        List<Song> songList = new ArrayList(tblAllSongs.getItems());
        manager.saveAll(songList);
    }
}