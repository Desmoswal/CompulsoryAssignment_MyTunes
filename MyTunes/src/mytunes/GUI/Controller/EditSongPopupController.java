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
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import mytunes.BE.Song;
import mytunes.BE.SongLibrary;

/**
 * FXML Controller class
 *
 * @author Kristof
 */
public class EditSongPopupController implements Initializable {

    @FXML
    private TextField txtTitle;
    @FXML
    private TextField txtArtist;
    @FXML
    private TextField txtGenre;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnCancel;

    
    private Stage curStage;
    private String uuid;
    
    private SongLibrary libSong;
    
    private Song selectedSong;
    
    private FXMLDocumentController controller;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        libSong = SongLibrary.getInstance();
    }    
    
    @FXML
    public void fillFields(String artist, String title, String genre, String uuid)
    {
        txtArtist.setText(artist);
        txtTitle.setText(title);
        txtGenre.setText(genre);
        this.uuid = uuid;
    }

    @FXML
    private void saveSong(ActionEvent event) 
    {
        curStage = (Stage) btnSave.getScene().getWindow();
        selectedSong.setArtist(txtArtist.getText());
        selectedSong.setTitle(txtTitle.getText());
        selectedSong.setGenre(txtGenre.getText());
        controller.updateSongTable();
        curStage.close();
    }

    @FXML
    private void cancel(ActionEvent event) 
    {
        curStage = (Stage) btnCancel.getScene().getWindow();
        curStage.close();
    }
    
    @FXML
    public void setSelected(Song song)
    {
        this.selectedSong = song;
    }
    
    @FXML
    public void setController(FXMLDocumentController controller)
    {
        this.controller = controller;
    }
}
