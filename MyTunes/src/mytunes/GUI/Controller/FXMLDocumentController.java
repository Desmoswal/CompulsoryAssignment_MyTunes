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
    //All of the FXML variables in not more than 140 lines :)
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
    private AnchorPane pane;
    @FXML
    private ImageView playImage;
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
    private SongLibrary libSong; //the SongLibrary which is the virtual copy of our textfile
    private PlaylistLibrary libPl; //the PlaylistLibrary is also the virtual copy of our textfile
    private Song nowPlaying = null; //this is for displaying artist/title, and to check the last item of the actual playlist
    SongManager manager = new SongManager(); //the BLL layer
    
    /*
    * These should have been the variables of metadata but it gets emptied after run() is finished inside readMetadata()
    */
    private String currTitle;
    private String currAlbum;
    private String currArtist;
    private String currGenre;
    private String currDuration;
    private String currFullMetadata;
    private ObservableList<String> ol = FXCollections.observableArrayList();
    
    private ObservableList<Song> songlist; //the main "songlist" which contains the SongLibrary (and used for fill the library when it's being read from file.)
    private ObservableList<Playlist> playlistlist; //the main list of playlists which contains the PlaylistLibrary (and used for fill the library when it's being read from file.)
    private final Object obj= new Object(); //just to have an object which we can wait with (this is for readMetadata() to sync threads)
    private MediaPlayer player; //our main mediaplayer.
    private MediaPlayer loadPlayer; //our "loading" mediaplayer. the songs are getting loaded into this and gets its properties set, then it gets assigned to the main player.
    private TableView<Song> playingFrom; //to know which table are the songs being played from
    private String uriString; //this is also used in readMetadata()
    private ArrayList<Song> currentPlaylist = new ArrayList<>(); //the current playlist. this is being constructed on every new songstart so it will play the playlist's songs or the library songs
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
       //creating the one and only instance of the libraries and getting them
       //now we can get the libaries from everywhere using SongLibrary.getInstance();
       SongLibrary.createInstance();
       PlaylistLibrary.createInstance();
       libSong = SongLibrary.getInstance();
       libPl = PlaylistLibrary.getInstance();
       
       setTableProperties(); //assign column properties to tables
       
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
    }
    
    /**
         * Getting metadata from a temporary mediaplayer
         * by syncing threads of the app and the mediaplayer.
         * You can choose to get every data one by one,
         * using title, album, artist.. etc. 
         * OR using fullMetadata which gets everything.
         * fullMetadata has raw metadata, look out for that.
         * 
         * This is putting the newly opened song in the library. (Because we cannot get the metadata from anywhere else.)
    **/
    private void readMetadata(String uriString)
    {
        //-----Reading Metadata--------
        
        MediaPlayer tempPlayer = new MediaPlayer(new Media(uriString));
        tempPlayer.setOnReady(new Runnable() {

            @Override
            public void run() {
                currArtist =(String) tempPlayer.getMedia().getMetadata().get("artist");
                currTitle =(String) tempPlayer.getMedia().getMetadata().get("title");
                currAlbum =(String) tempPlayer.getMedia().getMetadata().get("album");
                currGenre = (String) tempPlayer.getMedia().getMetadata().get("genre");
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
                        libSong.addSong(new Song(uriString, currArtist, currTitle, currGenre, songTime));
                        updateSongTable();
                    } catch(InterruptedException ex) {
                        Thread.currentThread().interrupt();
                        System.out.println("InterruptedException in readmetadata.");
                    }
                    obj.notify();// the loop has to wait unitl we are able to get the media metadata thats why use .wait() and .notify() to synce the two threads(main thread and MediaPlayer thread)
                }

                System.out.println("--------New Metadata info--------");
                System.out.println(currArtist);
                System.out.println(currTitle);
                System.out.println(currAlbum);
                System.out.println(currGenre);
                System.out.println(currFullMetadata);
                System.out.println("--------End of new metadata-------");
            }
        });
    }
    
    /** Opens a filechooser by pressing New button, then calls readMetadata() to put in the library. **/
    @FXML
    private void openFile(ActionEvent event)
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("MP3 Files", "*.mp3"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));
        fileChooser.setTitle("Open Music File");

        File files = fileChooser.showOpenDialog(stage);
        uriString = files.toURI().toString();
        System.out.println(uriString);

        readMetadata(uriString);
    }
    
    /** This should have been the searchbutton action...
     * Not implemented yet.
     **/
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

    
    /** This should open the NewSongPopup, but we didn't implement it
     * because of saving problems.
     **/
    private void openNewSong(ActionEvent event) throws IOException {
        /**
         * POPUPS
         */
        // Fetches primary stage and gets loader and loads FXML file to Parent
        Stage primStage = (Stage)tblAllSongs.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/mytunes/GUI/View/NewSongPopup.fxml"));
        Parent root = loader.load();
        
        // Fetches controller from view
        NewSongPopupController controller = loader.getController();
                
        // Sets new stage as modal window
        Stage stageView = new Stage();
        stageView.setScene(new Scene(root));
        
        stageView.initModality(Modality.WINDOW_MODAL);
        stageView.initOwner(primStage);
        
        stageView.show();
    }
    
    /** Opens EditSongPopup **/
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
        
        // Fetches controller from view
        EditSongPopupController controller = loader.getController();
        controller.setController(this);
        
        
        // Sets new stage as modal window
        Stage stageView = new Stage();
        stageView.setScene(new Scene(root));
        
        stageView.initModality(Modality.WINDOW_MODAL);
        stageView.initOwner(primStage);
        
        //-------------------------
        Song selectedSong = tblAllSongs.getSelectionModel(
                            ).getSelectedItem();
        controller.fillFields(selectedSong.getArtist(),selectedSong.getTitle(),selectedSong.getGenre(), selectedSong.getUUID());
        

        controller.setSelected(selectedSong);
        stageView.show();
    }
    
    /** Opens DeleteSongPopup **/
    @FXML
    private void openDeleteSong(ActionEvent event) throws IOException {
        /**
         * POPUPS
         */
        // Fetches primary stage and gets loader and loads FXML file to Parent
        Stage primStage = (Stage)tblAllSongs.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/mytunes/GUI/View/DeleteSongPopup.fxml"));
        Parent root = loader.load();
        
        // Fetches controller from view
        DeleteSongPopupController controller = loader.getController();
        controller.setSelected(tblAllSongs.getSelectionModel().getSelectedItem());
        controller.setController(this);
        
        
        // Sets new stage as modal window
        Stage stageView = new Stage();
        stageView.setScene(new Scene(root));
        
        stageView.initModality(Modality.WINDOW_MODAL);
        stageView.initOwner(primStage);
        
        stageView.show();
    }
    
    /** Opens EditPlaylistPopup **/
    @FXML
    private void openEditPlaylist(ActionEvent event) throws IOException {
        /**
         * POPUPS
         */
        // Fetches primary stage and gets loader and loads FXML file to Parent
        Stage primStage = (Stage)tblAllSongs.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/mytunes/GUI/View/EditPlaylistPopup.fxml"));
        Parent root = loader.load();
        
        // Fetches controller from view
        EditPlaylistPopupController controller = loader.getController();
        
        controller.setSelected(tblAllPlaylists.getSelectionModel().getSelectedItem());
        controller.setController(this);
        
        // Sets new stage as modal window
        Stage stageView = new Stage();
        stageView.setScene(new Scene(root));
        
        stageView.initModality(Modality.WINDOW_MODAL);
        stageView.initOwner(primStage);
        
        stageView.show();
    }
    
    /** Opens DeletePlaylistPopup **/
    @FXML
    private void openDeletePlaylist(ActionEvent event) throws IOException {
        /**
         * POPUPS
         */
        // Fetches primary stage and gets loader and loads FXML file to Parent
        Stage primStage = (Stage)tblAllSongs.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/mytunes/GUI/View/DeletePlaylistPopup.fxml"));
        Parent root = loader.load();
        
        // Fetches controller from view
        DeletePlaylistPopupController controller = loader.getController();
        controller.setSelectedPlaylist(tblAllPlaylists.getSelectionModel().getSelectedItem());
        controller.setController(this);
        
        // Sets new stage as modal window
        Stage stageView = new Stage();
        stageView.setScene(new Scene(root));
        
        stageView.initModality(Modality.WINDOW_MODAL);
        stageView.initOwner(primStage);
        
        stageView.show();
    }
    
    /** Opens newPlaylistPopup **/
    @FXML
    private void openNewPlaylist(ActionEvent event) throws IOException {
        /**
         * POPUPS
         */
        // Fetches primary stage and gets loader and loads FXML file to Parent
        Stage primStage = (Stage)tblAllSongs.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/mytunes/GUI/View/NewPlaylistPopup.fxml"));
        Parent root = loader.load();
        
        // Fetches controller from view
        NewPlaylistPopupController controller = loader.getController();
        controller.setController(this);
        
        // Sets new stage as modal window
        Stage stageView = new Stage();
        stageView.setScene(new Scene(root));
        
        stageView.initModality(Modality.WINDOW_MODAL);
        stageView.initOwner(primStage);
        
        stageView.show();
    }

    /** Moves song one up in the actual playlist **/
    @FXML
    private void moveSongUp(ActionEvent event) {
        if(!tblCurPlaylist.getSelectionModel().isEmpty())
        {
            Song selectedSong = tblCurPlaylist.getSelectionModel().getSelectedItem();
            Playlist selectedPlaylist = tblAllPlaylists.getSelectionModel().getSelectedItem();
            int currentUp = selectedPlaylist.getSongs().indexOf(selectedSong);
            if(currentUp > 0) {
                Song toMove = selectedPlaylist.getSongs().get(currentUp);
                Song temp = selectedPlaylist.getSongs().get(selectedPlaylist.getSongs().indexOf(selectedSong) - 1);
                selectedPlaylist.getSongs().set(currentUp - 1, toMove);
                selectedPlaylist.getSongs().set(currentUp, temp);
            }
            updatePlaylistTable();
            tblAllPlaylists.getSelectionModel().select(selectedPlaylist);
            tblCurPlaylist.setItems(FXCollections.observableArrayList((tblAllPlaylists.getSelectionModel().getSelectedItem()).getSongs()));
            tblCurPlaylist.refresh();
        }
    }
    
    /** Moves song one down in the actual playlist. **/
    @FXML
    private void moveSongDown(ActionEvent event) {
        if(!tblCurPlaylist.getSelectionModel().isEmpty())
        {
            Song selectedSong = tblCurPlaylist.getSelectionModel().getSelectedItem();
            Playlist selectedPlaylist = tblAllPlaylists.getSelectionModel().getSelectedItem();
            int currentUp = selectedPlaylist.getSongs().indexOf(selectedSong);
            if(currentUp+1 <= selectedPlaylist.getSongs().size()) {
                Song toMove = selectedPlaylist.getSongs().get(currentUp);
                Song temp = selectedPlaylist.getSongs().get(selectedPlaylist.getSongs().indexOf(selectedSong) + 1);
                selectedPlaylist.getSongs().set(currentUp + 1, toMove);
                selectedPlaylist.getSongs().set(currentUp, temp);
            }
            updatePlaylistTable();
            tblAllPlaylists.getSelectionModel().select(selectedPlaylist);
            tblCurPlaylist.setItems(FXCollections.observableArrayList((tblAllPlaylists.getSelectionModel().getSelectedItem()).getSongs()));
            tblCurPlaylist.refresh();
        }
    }
    
    /** Deletes selected song from actual playlist **/
    @FXML
    private void deleteSongFromPlaylist(ActionEvent event) {
        if(!tblCurPlaylist.getSelectionModel().isEmpty())
        {
            Song selectedSong = tblCurPlaylist.getSelectionModel().getSelectedItem();
            Playlist selectedPlaylist = tblAllPlaylists.getSelectionModel().getSelectedItem();
            selectedPlaylist.removeSong(selectedSong);
            
            updatePlaylistTable();
            tblAllPlaylists.getSelectionModel().select(selectedPlaylist);
            tblCurPlaylist.setItems(FXCollections.observableArrayList((tblAllPlaylists.getSelectionModel().getSelectedItem()).getSongs()));
            tblCurPlaylist.refresh();
        }
    }
    
    /** Adds selected song from library to actual playlist **/
    @FXML
    private void addSongToPlaylist(ActionEvent event) {
        if(!tblAllSongs.getSelectionModel().isEmpty() && !tblAllPlaylists.getSelectionModel().isEmpty())
        {
            Playlist selectedPlaylist = tblAllPlaylists.getSelectionModel().getSelectedItem();
            Song selectedSong = tblAllSongs.getSelectionModel().getSelectedItem();
            selectedPlaylist.addSong(selectedSong);
            System.out.println(selectedPlaylist.getSongs());
            updatePlaylistTable();
            tblAllPlaylists.getSelectionModel().select(selectedPlaylist);
            tblCurPlaylist.setItems(FXCollections.observableArrayList((tblAllPlaylists.getSelectionModel().getSelectedItem()).getSongs()));
            tblCurPlaylist.refresh();
        }
    }
    
    /** Assigns all table column properties **/
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
    
    /**
     * THIS IS THE ULTIMATE SOLUTION FOR OUR PROBLEMS
     * CODING LEVEL OVER 9000
     * 
     * This is how it works and that's it
     */
    @FXML
    public void updateSongTable() {

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
    
    /**
     * THIS IS THE ULTIMATE SOLUTION FOR OUR PROBLEMS
     * CODING LEVEL OVER 9000
     * 
     * This is how it works and that's it
     */
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
        
        /*if(!tblAllPlaylists.getSelectionModel().isEmpty()) {
            tblCurPlaylist.setItems(FXCollections.observableArrayList(tblAllPlaylists.getSelectionModel().getSelectedItem().getSongs()));
        }*/
    }
    
    /**
     * Binding duration counter to GUI. Thank you Jeppe!
     */
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
    
    /**
     * Modified Jeppe's code to bind duration to the slider to move it.
     */
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
    
    /**
     * seeks the player
     * @param event 
     */
    @FXML
    private void seekTime(MouseEvent event) {
        Duration seektime = Duration.millis(player.getMedia().getDuration().toMillis()*(slideTime.getValue()/100));
        player.seek(seektime);
        bindPlayerToSlider();
    }
    
    /**
     * unbinds the slider when it's clicked so you can move it to seek
     * @param event 
     */
    @FXML
    private void unBindSlider(MouseEvent event) {
        slideTime.valueProperty().unbind();
    }
    
    /**
     * The main play() method.
     * This is being called from the playbutton and also from doubleclick.
     * This binds and assigns the GUI and writes nowPlaying to gui.
     */
    private void play(Song toPlay) {
        nowPlaying = toPlay;
        player.play();
        lblNowPlaying.setText(nowPlaying.getArtist() + " - " + nowPlaying.getTitle());
        
        bindPlayerToLabel();
        bindPlayerToSlider();
    }

    /**
     * The playbutton's method. Basically same as the doubleclick.
     * @param event 
     */
    @FXML
    private void playMusic(ActionEvent event)
    {
        if(loadPlayer != null) {
            setPlayerProperties(loadPlayer);
        }
        
        if(player == null) {
            player = loadPlayer;
            
            currentPlaylist = new ArrayList<>();
            for(Song song : playingFrom.getItems()) {
                currentPlaylist.add(song);
            }
            
            int start = currentPlaylist.indexOf(playingFrom.getSelectionModel().getSelectedItem());
            play(currentPlaylist.get(start));
        }
        
        if(player.getStatus().equals(READY)) {
            
            currentPlaylist = new ArrayList<>();
            for(Song song : playingFrom.getItems()) {
                currentPlaylist.add(song);
            }

            int start = currentPlaylist.indexOf(playingFrom.getSelectionModel().getSelectedItem());
            play(currentPlaylist.get(start));
        } else if(player.getStatus().equals(PAUSED)) {
            player.play();
        } else if(player.getStatus().equals(PLAYING)) {
            player.pause();
        }
    }
    
    /**
     * This method is checking the single and doubleclick on the Library's table.
     * On selection, it gets the selected song ready to play. And it may clear the other neighboor table so we won't have two songs
     * to play together.
     * On doubleclick, it assigns loadPlayer to player and starts playing.
     * @param event
     * @throws IOException 
     */
    @FXML
    private void mousePressedOnTableView(MouseEvent event) throws IOException
    {
        if(event.isPrimaryButtonDown())
        {
            if(!tblAllSongs.getSelectionModel().isEmpty())
            {
                playingFrom = tblAllSongs;
                loadPlayer = null;
                loadPlayer = new MediaPlayer(new Media((tblAllSongs.getSelectionModel().getSelectedItem()).getPath()));
                setPlayerProperties(loadPlayer);
                loadPlayer.setOnEndOfMedia(new Runnable() {
                    public void run()
                    {
                        if(currentPlaylist.indexOf(nowPlaying) != currentPlaylist.size() - 1)
                        {
                            Song nextsong = currentPlaylist.get(currentPlaylist.indexOf(nowPlaying) + 1);
                            loadPlayer = new MediaPlayer(new Media(nextsong.getPath()));
                            setPlayerProperties(loadPlayer);
                            player.stop();
                            player.dispose();
                            player = null;
                            nowPlaying = null;
                            player = loadPlayer;
                            
                            play(nextsong);
                        }
                    }
                });
            }
            if(event.getClickCount() == 1 && !tblAllSongs.getSelectionModel().isEmpty()) {
                tblCurPlaylist.getSelectionModel().clearSelection();
            }
            
            if(event.getClickCount() == 2)
            {
                if(player == null)
                {
                    player = loadPlayer;
                    
                    currentPlaylist = new ArrayList<>();
                    for(Song song : tblAllSongs.getItems()) {
                        currentPlaylist.add(song);
                    }

                    int start = currentPlaylist.indexOf(tblAllSongs.getSelectionModel().getSelectedItem());
                    play(currentPlaylist.get(start));
                }
                
                if(player.getStatus().equals(PAUSED) || player.getStatus().equals(PLAYING))
                {
                    player.stop();
                    player.dispose();
                    nowPlaying = null;
                    player = null;
                    player = loadPlayer;
                    
                    currentPlaylist = new ArrayList<>();
                    for(Song song : tblAllSongs.getItems()) {
                        currentPlaylist.add(song);
                    }
                    
                    int start = currentPlaylist.indexOf(tblAllSongs.getSelectionModel().getSelectedItem());
                    play(currentPlaylist.get(start));
                }
            }
        }
    }
    
    /**
     * This opens the selected playlist's content in the middle tableview.
     * @param event 
     */
    @FXML
    private void mousePressedOnPlaylist(MouseEvent event) {
        if(event.isPrimaryButtonDown()) {
            if(event.getClickCount() == 1) {
                if(!tblAllPlaylists.getSelectionModel().isEmpty()) {
                    Playlist selectedPlaylist = tblAllPlaylists.getSelectionModel().getSelectedItem();
                    tblCurPlaylist.setItems(FXCollections.observableArrayList(selectedPlaylist.getSongs()));
                }
            }
        }
    }
    
    /**
     * Basically the same as mousePressedOnTableView. Operates with the actual playlist's table (middle table).
     * @param event
     * @throws IOException 
     */
    @FXML
    private void mousePressedOnCurPlaylist(MouseEvent event) throws IOException
    {
        if(event.isPrimaryButtonDown())
        {
            if(!tblCurPlaylist.getSelectionModel().isEmpty())
            {
                playingFrom = tblCurPlaylist;
                loadPlayer = null;
                loadPlayer = new MediaPlayer(new Media((tblCurPlaylist.getSelectionModel().getSelectedItem()).getPath()));
                setPlayerProperties(loadPlayer);
                loadPlayer.setOnEndOfMedia(new Runnable() {

                    public void run()
                    {
                        if(currentPlaylist.indexOf(nowPlaying) != currentPlaylist.size() - 1)
                        {
                            Song nextsong = currentPlaylist.get(currentPlaylist.indexOf(nowPlaying) + 1);
                            loadPlayer = new MediaPlayer(new Media(nextsong.getPath()));
                            setPlayerProperties(loadPlayer);
                            player.stop();
                            player.dispose();
                            player = null;
                            nowPlaying = null;
                            player = loadPlayer;
                            
                            play(nextsong);
                        }
                    }
                }
);
            }
            if(event.getClickCount() == 1 && !tblCurPlaylist.getSelectionModel().isEmpty())
                tblAllSongs.getSelectionModel().clearSelection();
            if(event.getClickCount() == 2)
            {
                if(player == null)
                {
                    player = loadPlayer;
                    
                    currentPlaylist = new ArrayList<>();
                    for(Song song : tblCurPlaylist.getItems()) {
                        currentPlaylist.add(song);
                    }
                    int start = currentPlaylist.indexOf(tblCurPlaylist.getSelectionModel().getSelectedItem());
                    play(currentPlaylist.get(start));
                }
                
                if(player.getStatus().equals(PAUSED) || player.getStatus().equals(PLAYING))
                {
                    player.stop();
                    player.dispose();
                    nowPlaying = null;
                    player = null;
                    player = loadPlayer;
                    
                    currentPlaylist = new ArrayList<>();
                    for(Song song : tblCurPlaylist.getItems()) {
                        currentPlaylist.add(song);
                    }
                    int start = currentPlaylist.indexOf(tblCurPlaylist.getSelectionModel().getSelectedItem());
                    play(currentPlaylist.get(start));
                }
            }
        }
    }
    
    /** This will be used in search to set the radio button's group. **/
    private void setToggleGroupForRadioButtons() {
        ToggleGroup toggleGroup = new ToggleGroup();
        rbTitle.setToggleGroup(toggleGroup);
        rbArtist.setToggleGroup(toggleGroup);
    }
    private ObservableList<String> filteredList = FXCollections.observableArrayList();
    
    /**
     * This will be used in search later.
     * @param event 
     */
    @FXML
    private void searchClick(ActionEvent event)
    {
        if (rbTitle.isSelected())
        {
            
            //colAllSongsTitle.getCellData(nowPlaying)
        }
    
    }
    
    /**
     * Sets the players volume to the slider's value when the slider is being dragged.
     * @param event 
     */
    @FXML
    private void volumeSlider(MouseEvent event) {
        player.setVolume(slideVol.getValue()/100);
    }
    
    /**
     * Mutes/unmutes the player and changes the image of the button.
     * @param event 
     */
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
    
    /**
     * Sets the given MediaPlayer properties to change play/pause images and assign currently loaded song's duration to the label.
     * @param toSet 
     */
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
        toSet.setOnStalled(new Runnable() {
            public void run() {
                Image play = new Image("http://kivulallo.ddns.net/assignment/play.png");
                playImage.setImage(play);
            }
        });
    }
    
    /**
     * Jumps to next song in the current playlist (if is there any more)
     * @param event 
     */
    @FXML
    private void nextSong(ActionEvent event)
    {
        if(currentPlaylist != null) {
            if(currentPlaylist.indexOf(nowPlaying) != currentPlaylist.size() - 1) {
                Song nextsong = currentPlaylist.get(currentPlaylist.indexOf(nowPlaying) + 1);
                loadPlayer = new MediaPlayer(new Media(nextsong.getPath()));
                setPlayerProperties(loadPlayer);
                player.stop();
                player.dispose();
                player = null;
                nowPlaying = null;
                player = loadPlayer;
                
                play(nextsong);
            }
        }
    }
    
    /**
     * Jump back one song in the current playlist if is there any before.
     * @param event 
     */
    @FXML
    private void prevSong(ActionEvent event)
    {
        if(currentPlaylist != null) {
            if(currentPlaylist.indexOf(nowPlaying) > 0) {
                Song nextsong = currentPlaylist.get(currentPlaylist.indexOf(nowPlaying) - 1);
                loadPlayer = new MediaPlayer(new Media(nextsong.getPath()));
                setPlayerProperties(loadPlayer);
                player.stop();
                player.dispose();
                player = null;
                nowPlaying = null;
                player = loadPlayer;
                
                play(nextsong);
            }
        }
    }
}