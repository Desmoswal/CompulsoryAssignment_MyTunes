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
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author Desmoswal
 */
public class FXMLDocumentController implements Initializable
{

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

        String uriString = new File("D:\\GitHub\\School\\CompulsoryAssignment_MyTunes\\MyTunes\\sound1.mp3").toURI().toString();
        MediaPlayer player = new MediaPlayer(new Media(uriString));
        player.play();

        //-------------
        /*
      Media m = new Media(uriString);
      String title = (String)m.getMetadata().get("title");
      String album = (String)m.getMetadata().get("album");
      String artist = (String)m.getMetadata().get("artist");
      String asd = (String)m.getMetadata().toString();
      ObservableList<String> medialist = FXCollections.observableArrayList();
              medialist.addAll(medialist);
        System.out.println(medialist);
          System.out.println(title);
          System.out.println(album);
          System.out.println(artist);
          System.out.println(asd);
        //}*/
        File folder = new File("D:/GitHub/School/CompulsoryAssignment_MyTunes/MyTunes/");
        File[] listOfFiles = folder.listFiles();

        for (File file : listOfFiles)
        {
            if (file.isFile())
            {
                System.out.println(file.getName());
            }
        }
        
        
    }

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        // TODO
    }
    

    @FXML
    private void handleEditSongButtonAction(ActionEvent event)
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("MP3 Files", "*.mp3"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));
        fileChooser.setTitle("Open Music File");
        fileChooser.showOpenDialog(stage);
    }
    
    public void handleSavePlaylistAction(ActionEvent e) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Playlist");
            //System.out.println(playlist.getId());
            File file = fileChooser.showSaveDialog(stage);
/*            if (file != null) {
                try {
                    ImageIO.write(SwingFXUtils.fromFXImage(pic.getImage(),
                        null), "png", file);
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
            }*/
        }
}
