package br.com.rarp.view.scnWait;

import java.net.URL;
import java.util.ResourceBundle;

import br.com.rarp.control.SistemaCtrl;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class WaiControler extends Application implements Initializable {
	
    @FXML
    private ProgressBar progress;

    @FXML
    private Label txtmensagem;

    private Stage stage;
    
    private Thread thread;

    private boolean fechar;
    public WaiControler() throws Exception {
		// TODO Auto-generated constructor stub
    	start(SistemaCtrl.getInstance().getStage());
    }
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}

	public void open() {
		
		
				if (stage != null) 
					if (!stage.isShowing())
					
						stage.show();
						
					
		
		
	}

	public void Close() {
		fechar = true;
		
		if (thread != null )
			thread.interrupt();
		
		if (stage != null)
			stage.hide();
		stage =null;
	}
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		this.stage = primaryStage;
		stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("Wai.fxml"))));
		stage.setResizable(false);
		stage.initStyle(StageStyle.UNDECORATED);
		stage.sizeToScene(); 
		//stage.setOnShowing(value);
		stage.setOnShowing(a ->{
            System.out.println("Showing");
            Task<String> close = new Task<String>() {

                @Override
                protected String call() throws Exception {
                    while (!fechar);
                    
                    return null;
                }
            };

            close.setOnSucceeded(c ->{
            	stage.close();
                //alert.hide();
            });
            new Thread(close).start();
        });
	}
	
	public Label getTxtmensagem() {
		return txtmensagem;
	}

	public void setTxtmensagem(Label txtmensagem) {
		this.txtmensagem = txtmensagem;
	}

}
