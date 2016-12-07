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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Kristof
 */
public class NewSongPopupController implements Initializable {

    @FXML
    private Button btnCreate;
    @FXML
    private Button btnCancel;
    @FXML
    private Label labPath;
    @FXML
    private Label labTime;
    @FXML
    private TextField txtGenre;
    @FXML
    private TextField txtArtist;
    @FXML
    private TextField txtTitle;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void createSong(ActionEvent event) {
    }

    @FXML
    private void cancel(ActionEvent event) {
    }
    
}
