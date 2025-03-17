package br.com.rarp.view.scnSplash;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class SplashController extends Application implements Initializable {

	private Stage stage;
	private static Integer count = 1;

	private static int progress = 0;


	@FXML
	private ProgressBar pgsSplash;
	
    @FXML
    private ImageView img;
    
    @FXML
    private AnchorPane pnlPrincipal;

	@Override
	public void start(Stage stage) throws Exception {
		stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("Splash.fxml"))));
		this.stage = stage;
	}

	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public void next() {
		if (progress < 100)
			progress = progress + (100 / count);
	}

	@SuppressWarnings("static-access")
	public void abrir(Integer count) throws Exception {
		if (count > 0)
			this.count = count;
		start(new Stage());
		stage.setResizable(false);
		stage.show();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		pnlPrincipal.heightProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				img.setFitHeight(pnlPrincipal.getHeight());
			}
		});
		
		pnlPrincipal.widthProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				img.setFitWidth(pnlPrincipal.getWidth());
			}
		});
	}
	
}
