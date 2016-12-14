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
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.concurrent.TimeUnit;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.binding.StringBinding;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import static javafx.scene.media.MediaPlayer.Status.PAUSED;
import static javafx.scene.media.MediaPlayer.Status.PLAYING;
import static javafx.scene.media.MediaPlayer.Status.READY;
import static javafx.scene.media.MediaPlayer.Status.UNKNOWN;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
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
    private TableView<Song> tblCurPlaylist;
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
    @FXML
    private AnchorPane pane;
    @FXML
    private ImageView playImage;
    @FXML
    private MenuButton menuButton;
    @FXML
    private Button btnMute;
    @FXML
    private TableView<Playlist> tblAllPlaylists;
    @FXML
    private ImageView imgMute;
    @FXML
    private Label lblNowPlaying;
    @FXML
    private RadioButton rbTitle;
    @FXML
    private RadioButton rbArtist;
    @FXML
    private Label lblDuration;

    Stage stage;
    
    private SongLibrary libSong;
    private PlaylistLibrary libPl;
    
    
    private Song nowPlaying = null;
    
    SongManager manager = new SongManager();
    
    String currTitle;
    String currAlbum;
    String currArtist;
    String currGenre;
    String currDuration;
    String currFullMetadata;
    
    private ObservableList<String> ol = FXCollections.observableArrayList();
    
    private ObservableList<Song> songlist;
    private ObservableList<Playlist> playlistlist;
    private final Object obj= new Object();
    
    Path curRelPath = Paths.get("");
    
    private MediaPlayer player;
    private MediaPlayer loadPlayer;
    String uriString;
    URI uri;
    private ArrayList<Song> currentPlaylist;
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
       SongLibrary.createInstance();
       PlaylistLibrary.createInstance();
       libSong = SongLibrary.getInstance();
       libPl = PlaylistLibrary.getInstance();
       
       setTableProperties();
       
       //-------Setting up Songlist
       songlist = FXCollections.observableArrayList(manager.getAll());
       
        for (Song song : songlist)
        {
            libSong.addSong(song);
        }
        
       songlist = FXCollections.observableArrayList(libSong.getSongList());
       tblAllSongs.setItems(songlist);
       
       //------Setting up Playlist
       playlistlist = FXCollections.observableArrayList(manager.getPlaylists());
       
       for(Playlist playlist : playlistlist)
       {
           libPl.addPlaylist(playlist);
       }
       
       playlistlist = FXCollections.observableArrayList(libPl.getPlaylists());
       tblAllPlaylists.setItems(playlistlist);
       
       slideVol.setValue(100);
       //manager.getAll();
    }
    
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
        MediaPlayer tempPlayer = new MediaPlayer(new Media(uriString));
        tempPlayer.setOnReady(new Runnable() {

        @Override
        public void run() {
            currArtist =(String) tempPlayer.getMedia().getMetadata().get("artist");
            currTitle =(String) tempPlayer.getMedia().getMetadata().get("title");
            currAlbum =(String) tempPlayer.getMedia().getMetadata().get("album");
            currGenre = (String) tempPlayer.getMedia().getMetadata().get("genre");
            /*double tmpCurrMinToSec = Math.floor(player.getMedia().durationProperty().getValue().toMinutes() * 60 +); //Convert Floored minutes to seconds
            double currDurationMinutes = Math.floor(player.getMedia().durationProperty().getValue().toMinutes()); 
            double currDurationSeconds = Math.floor(player.getMedia().durationProperty().getValue().toSeconds()) - tmpCurrMinToSec;
            String currDuration = currDurationMinutes + ":" + currDurationSeconds;*/
            currFullMetadata =(String) tempPlayer.getMedia().getMetadata().toString();
            String songTime = String.format("%d:%d",
                    TimeUnit.MILLISECONDS.toMinutes((long) tempPlayer.getMedia().getDuration().toMillis()),
                    TimeUnit.MILLISECONDS.toSeconds((long) tempPlayer.getMedia().getDuration().toMillis()) - TimeUnit.MINUTES.toSeconds(
                    TimeUnit.MILLISECONDS.toMinutes((long)tempPlayer.getMedia().getDuration().toMillis())));
            
            
            ol.add(currArtist);
            ol.add(currTitle);
            ol.add(currAlbum);
            ol.add(currFullMetadata);
            synchronized(obj){//this is required since mp.setOnReady creates a new thread and our loopp  in the main thread
                try {
                    obj.wait(10);
                    //System.out.println(currArtist + "afterwait");
                    libSong.addSong(new Song(uriString, currArtist, currTitle, currGenre, songTime));
                    updateSongTable();
                    } catch(InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    System.out.println("nem");
                }
                obj.notify();// the loop has to wait unitl we are able to get the media metadata thats why use .wait() and .notify() to synce the two threads(main thread and MediaPlayer thread)
            }
            
            System.out.println("--------New Metadata info--------");
            System.out.println(currArtist);
            System.out.println(currTitle);
            System.out.println(currAlbum);
            System.out.println(currGenre);
//            System.out.println(currDuration);
            System.out.println(currFullMetadata);
            System.out.println("--------End of new metadata-------");
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
        uriString = files.toURI().toString();//(s + "\\sound1.mp3").toURI().toString();
        System.out.println(uriString);
        //player = new MediaPlayer(new Media(uriString));
        //player.play();
        
        readMetadata(uriString);
        //lib.addSong(new Song(uriString, currArtist, currTitle, "", "0"));
        //updateSongTable();
    }
    
    @FXML
    private void handleButtonAction(ActionEvent event) throws IOException
    {
        
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
    }

    

    private void openNewSong(ActionEvent event) throws IOException {
        /**
         * POPUPS
         */
        // Fetches primary stage and gets loader and loads FXML file to Parent
        Stage primStage = (Stage)tblAllSongs.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/mytunes/GUI/View/NewSongPopup.fxml"));
        Parent root = loader.load();
        
        // Fetches controller from patient view
        NewSongPopupController controller = loader.getController();
        
        //controller.setPatient(patient);
        
        // Sets new stage as modal window
        Stage stagePatientView = new Stage();
        stagePatientView.setScene(new Scene(root));
        
        stagePatientView.initModality(Modality.WINDOW_MODAL);
        stagePatientView.initOwner(primStage);
        
        stagePatientView.show();
    }
    
    @FXML
    private void openEditSong(ActionEvent event) throws IOException
    {
        /**
         * POPUPS
         */
        // Fetches primary stage and gets loader and loads FXML file to Parent
        Stage primStage = (Stage)tblAllSongs.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/mytunes/GUI/View/EditSongPopup.fxml"));
        Parent root = loader.load();
        
        // Fetches controller from patient view
        EditSongPopupController controller = loader.getController();
        controller.setController(this);
        
        //controller.setPatient(patient);
        
        // Sets new stage as modal window
        Stage stagePatientView = new Stage();
        stagePatientView.setScene(new Scene(root));
        
        stagePatientView.initModality(Modality.WINDOW_MODAL);
        stagePatientView.initOwner(primStage);
        
        //-------------------------
        Song selectedSong = tblAllSongs.getSelectionModel(
                            ).getSelectedItem();
        controller.fillFields(selectedSong.getArtist(),selectedSong.getTitle(),selectedSong.getGenre(), selectedSong.getUUID());
        

        controller.setSelected(selectedSong);
        stagePatientView.show();
    }
    
    @FXML
    private void openDeleteSong(ActionEvent event) throws IOException {
        /**
         * POPUPS
         */
        // Fetches primary stage and gets loader and loads FXML file to Parent
        Stage primStage = (Stage)tblAllSongs.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/mytunes/GUI/View/DeleteSongPopup.fxml"));
        Parent root = loader.load();
        
        // Fetches controller from patient view
        DeleteSongPopupController controller = loader.getController();
        controller.setSelected(tblAllSongs.getSelectionModel().getSelectedItem());
        controller.setController(this);
        
        //controller.setPatient(patient);
        
        // Sets new stage as modal window
        Stage stagePatientView = new Stage();
        stagePatientView.setScene(new Scene(root));
        
        stagePatientView.initModality(Modality.WINDOW_MODAL);
        stagePatientView.initOwner(primStage);
        
        stagePatientView.show();
    }
    
    @FXML
    private void openEditPlaylist(ActionEvent event) throws IOException {
        /**
         * POPUPS
         */
        // Fetches primary stage and gets loader and loads FXML file to Parent
        Stage primStage = (Stage)tblAllSongs.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/mytunes/GUI/View/EditPlaylistPopup.fxml"));
        Parent root = loader.load();
        
        // Fetches controller from patient view
        EditPlaylistPopupController controller = loader.getController();
        
        controller.setSelected(tblAllPlaylists.getSelectionModel().getSelectedItem());
        controller.setController(this);
        
        //controller.setPatient(patient);
        
        // Sets new stage as modal window
        Stage stagePatientView = new Stage();
        stagePatientView.setScene(new Scene(root));
        
        stagePatientView.initModality(Modality.WINDOW_MODAL);
        stagePatientView.initOwner(primStage);
        
        stagePatientView.show();
    }
    
    @FXML
    private void openDeletePlaylist(ActionEvent event) throws IOException {
        /**
         * POPUPS
         */
        // Fetches primary stage and gets loader and loads FXML file to Parent
        Stage primStage = (Stage)tblAllSongs.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/mytunes/GUI/View/DeletePlaylistPopup.fxml"));
        Parent root = loader.load();
        
        // Fetches controller from patient view
        DeletePlaylistPopupController controller = loader.getController();
        controller.setSelectedPlaylist(tblAllPlaylists.getSelectionModel().getSelectedItem());
        controller.setController(this);
        
        //controller.setPatient(patient);
        
        // Sets new stage as modal window
        Stage stagePatientView = new Stage();
        stagePatientView.setScene(new Scene(root));
        
        stagePatientView.initModality(Modality.WINDOW_MODAL);
        stagePatientView.initOwner(primStage);
        
        stagePatientView.show();
    }
    
    @FXML
    private void openNewPlaylist(ActionEvent event) throws IOException {
        /**
         * POPUPS
         */
        // Fetches primary stage and gets loader and loads FXML file to Parent
        Stage primStage = (Stage)tblAllSongs.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/mytunes/GUI/View/NewPlaylistPopup.fxml"));
        Parent root = loader.load();
        
        // Fetches controller from patient view
        NewPlaylistPopupController controller = loader.getController();
        controller.setController(this);
        
        //controller.setPatient(patient);
        
        // Sets new stage as modal window
        Stage stagePatientView = new Stage();
        stagePatientView.setScene(new Scene(root));
        
        stagePatientView.initModality(Modality.WINDOW_MODAL);
        stagePatientView.initOwner(primStage);
        
        stagePatientView.show();
    }

    @FXML
    private void moveSongUp(ActionEvent event) {
        
    }
    
    @FXML
    private void moveSongDown(ActionEvent event) {
        
    }
    
    @FXML
    private void deleteSongFromPlaylist(ActionEvent event) {
        
    }
    
    @FXML
    private void addSongToPlaylist(ActionEvent event) {
        if(!tblAllSongs.getSelectionModel().isEmpty() && !tblAllPlaylists.getSelectionModel().isEmpty()) {
           Playlist selectedPl =  tblAllPlaylists.getSelectionModel().getSelectedItem();
           Song selectedSong = tblAllSongs.getSelectionModel().getSelectedItem();
            selectedPl.addSong(selectedSong);
            updatePlaylistTable();
        }
    }
    
    @FXML
    public void setTableProperties() {
        colAllSongsArtist.setCellValueFactory(new PropertyValueFactory("artist"));
        colAllSongsTitle.setCellValueFactory(new PropertyValueFactory("title"));
        colAllSongsGenre.setCellValueFactory(new PropertyValueFactory("genre"));
        colAllSongsTime.setCellValueFactory(new PropertyValueFactory("time"));
        
        colPlaylistsTitle.setCellValueFactory(new PropertyValueFactory("name"));
        colPlaylistsSongs.setCellValueFactory(new PropertyValueFactory("size"));
        colPlaylistsTime.setCellValueFactory(new PropertyValueFactory("alltime"));
        
        colCurPlaylistTitle.setCellValueFactory(new PropertyValueFactory("title"));
        colCurPlaylistTime.setCellValueFactory(new PropertyValueFactory("time"));
    }
    
    @FXML
    public void updateSongTable() {
        /**
         * THIS IS THE ULTIMATE SOLUTION FOR OUR PROBLEMS
         * CODING LEVEL OVER 9000
         */
        songlist = FXCollections.observableArrayList(libSong.getSongList());
        manager.saveAll(songlist);
        
        for (Song song : songlist)
        {
            libSong.removeSong(song);
        }
        
        songlist = FXCollections.observableArrayList(manager.getAll());
        for(Song song : songlist)
        {
            libSong.addSong(song);
        }
        manager.saveAll(songlist);
        songlist = FXCollections.observableArrayList(libSong.getSongList());
        tblAllSongs.setItems(songlist);
        manager.saveAll(songlist);
    }
    
    @FXML
    public void updatePlaylistTable()
    {
        playlistlist = FXCollections.observableArrayList(libPl.getPlaylists());
        manager.savePlaylists(playlistlist);
        for(Playlist playlist : playlistlist) {
            libPl.removePlaylist(playlist);
        }
        playlistlist = FXCollections.observableArrayList(manager.getPlaylists());
        for(Playlist playlist : playlistlist) {
            libPl.addPlaylist(playlist);
        }
        manager.savePlaylists(playlistlist);
        playlistlist = FXCollections.observableArrayList(libPl.getPlaylists());
        tblAllPlaylists.setItems(playlistlist);
        manager.savePlaylists(playlistlist);
        
        if(!tblAllPlaylists.getSelectionModel().isEmpty()) {
            tblCurPlaylist.setItems(FXCollections.observableArrayList(tblAllPlaylists.getSelectionModel().getSelectedItem().getSongs()));
        }
    }
    
    private void bindPlayerToLabel()
    {
        labelcount.textProperty().bind(
        new StringBinding()
                {
                    {
                        super.bind(player.currentTimeProperty());
                    }
                    @Override
                    protected String computeValue()
                    {
                    String form = String.format("%d:%d",
                            TimeUnit.MILLISECONDS.toMinutes((long) player.getCurrentTime().toMillis()),
                            TimeUnit.MILLISECONDS.toSeconds((long) player.getCurrentTime().toMillis()) - 
                            TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(
                                    (long)player.getCurrentTime().toMillis()
                                )
                            )
                        );
                        return form;
                    }
                }
        );
        
    }
    
    private void bindPlayerToSlider() {
        slideTime.valueProperty().bind(
            new ObjectBinding<Double>() {
            {
                super.bind(player.currentTimeProperty());
            }
                @Override
                protected Double computeValue() {
                    return player.getCurrentTime().toSeconds()/player.getMedia().getDuration().toSeconds()*100;
                }
            });
    }
    
    @FXML
    private void seekTime(MouseEvent event) {
        Duration seektime = Duration.millis(player.getMedia().getDuration().toMillis()*(slideTime.getValue()/100));
        player.seek(seektime);
        bindPlayerToSlider();
    }
    
    @FXML
    private void unBindSlider(MouseEvent event) {
        slideTime.valueProperty().unbind();
    }
    
    @FXML
    public void fillPlaylistTable() {
        tblAllPlaylists.setItems(playlistlist);
        //manager.saveAll(playlistlist);
    }
    
    
    private void play(Song toPlay) {
        nowPlaying = toPlay;
        player.play();
        lblNowPlaying.setText(nowPlaying.getArtist() + " - " + nowPlaying.getTitle());
        
        bindPlayerToLabel();
        bindPlayerToSlider();
    }

    @FXML
    private void playMusic(ActionEvent event)
    {
        if(player == null) {
            player = loadPlayer;
        }
        
        if(player.getStatus().equals(READY)) {
            play(tblAllSongs.getSelectionModel().getSelectedItem());
        } else if(player.getStatus().equals(PAUSED)) {
            player.play();
        } else if(player.getStatus().equals(PLAYING)) {
            player.pause();
        }
    }
    
    @FXML
    private void mousePressedOnTableView(MouseEvent event) throws IOException
    {
        if(event.isPrimaryButtonDown()) {
            if(!tblAllSongs.getSelectionModel().isEmpty()) {
                loadPlayer = null;
                loadPlayer = new MediaPlayer(new Media(tblAllSongs.getSelectionModel().getSelectedItem().getPath()));
                setPlayerProperties(loadPlayer);
            }
            
            if(event.getClickCount()==1) {
                if(!tblAllSongs.getSelectionModel().isEmpty()) {
                    tblCurPlaylist.getSelectionModel().clearSelection();
                } else if(!tblCurPlaylist.getSelectionModel().isEmpty()) {
                    tblAllSongs.getSelectionModel().clearSelection();
                }
            }
            
            // Check double-click left mouse button
            if(event.getClickCount()==2){
                if(player == null) {
                     player = loadPlayer;
                     play(tblAllSongs.getSelectionModel().getSelectedItem());
                }
                
                if(player.getStatus().equals(PAUSED) || player.getStatus().equals(PLAYING)) {
                    player.stop();
                    player.dispose();
                    nowPlaying = null;
                    player = null;
                    player = loadPlayer;

                    play(tblAllSongs.getSelectionModel().getSelectedItem());
                }
                
                
            }
        }  
    }
    
    @FXML
    private void mousePressedOnPlaylists(MouseEvent event)
    {
        if(event.isPrimaryButtonDown()) {
            if(!tblAllSongs.getSelectionModel().isEmpty()) {
                Playlist selectedPlaylist = tblAllPlaylists.getSelectionModel().getSelectedItem();
                updatePlaylistTable();
                tblCurPlaylist.setItems(FXCollections.observableArrayList(selectedPlaylist.getSongs()));
                tblCurPlaylist.refresh();
            }
        }  
    }
    
    @FXML
    private void mousePressedOnCurPlaylist(MouseEvent event) throws IOException
    {
        if(event.isPrimaryButtonDown()) {
            if(!tblCurPlaylist.getSelectionModel().isEmpty()) {
                loadPlayer = null;
                loadPlayer = new MediaPlayer(new Media(tblCurPlaylist.getSelectionModel().getSelectedItem().getPath()));
                setPlayerProperties(loadPlayer);
            }
            
            if(event.getClickCount()==1) {
                if(!tblCurPlaylist.getSelectionModel().isEmpty()) {
                    tblAllSongs.getSelectionModel().clearSelection();
                }
            }
            
            // Check double-click left mouse button
            if(event.getClickCount()==2){
                currentPlaylist = (ArrayList<Song>)tblCurPlaylist.getItems();
                if(player == null) {
                     player = loadPlayer;
                     play(tblCurPlaylist.getSelectionModel().getSelectedItem());
                }
                
                if(player.getStatus().equals(PAUSED) || player.getStatus().equals(PLAYING)) {
                    player.stop();
                    player.dispose();
                    nowPlaying = null;
                    player = null;
                    player = loadPlayer;

                    play(tblAllSongs.getSelectionModel().getSelectedItem());
                }
                
                
            }
        }  
    }
    @FXML
    public void printLib()
    {
        System.out.println(libSong.getSongList());
    }
    
    private void setToggleGroupForRadioButtons() {
        ToggleGroup toggleGroup = new ToggleGroup();
        rbTitle.setToggleGroup(toggleGroup);
        rbArtist.setToggleGroup(toggleGroup);
    }
    private ObservableList<String> filteredList = FXCollections.observableArrayList();
    
    @FXML
    private void searchClick(ActionEvent event)
    {
        if (rbTitle.isSelected())
        {
            
            //colAllSongsTitle.getCellData(nowPlaying)
        }
    
    }
    
    @FXML
    private void volumeSlider(MouseEvent event) {
        player.setVolume(slideVol.getValue()/100);
    }
    
    @FXML
    private void muteButtonAction(ActionEvent event) {
        Image mute = new Image("http://kivulallo.ddns.net/assignment/mute.png");
        Image volume = new Image("http://kivulallo.ddns.net/assignment/volume.png");
        if(player.isMute() == false) {
            imgMute.setImage(mute);
            player.setMute(true);
        } else if(player.isMute() == true) {
            imgMute.setImage(volume);
            player.setMute(false);
        }
    }
    
    private void setPlayerProperties(MediaPlayer toSet) {
        toSet.setOnPaused(new Runnable() {
            public void run() {
                Image play = new Image("http://kivulallo.ddns.net/assignment/play.png");
                playImage.setImage(play);
            }
        });
        toSet.setOnPlaying(new Runnable() {
            public void run() {
                Image pause = new Image("http://kivulallo.ddns.net/assignment/pause.png");
                playImage.setImage(pause);

            }
        });
        toSet.setOnReady(new Runnable() {
           public void run() {
                lblDuration.setText(
                    String.format("%d:%d",
                    TimeUnit.MILLISECONDS.toMinutes((long) toSet.getMedia().getDuration().toMillis()),
                    TimeUnit.MILLISECONDS.toSeconds((long) toSet.getMedia().getDuration().toMillis()) - TimeUnit.MINUTES.toSeconds(
                    TimeUnit.MILLISECONDS.toMinutes(
                    (long)toSet.getMedia().getDuration().toMillis())))
                );
           } 
        });
    }
}