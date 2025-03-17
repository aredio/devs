package br.com.rarp.view.scnComponents;

import br.com.rarp.model.Leito;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;

public class ImageCard extends BorderPane {
	private SimpleStringProperty label = new SimpleStringProperty();
	private SimpleStringProperty pathImage = new SimpleStringProperty();
	private Leito leito;
	
	public ImageCard() {
		super();
		ImageView img = new ImageView();
		img.setFitHeight(40);
		img.setFitWidth(40);
		setCenter(img);
		
		Label lbl = new Label("1");
		lbl.setFont(Font.font(14));
		lbl.setPrefWidth(48);
		lbl.setAlignment(Pos.CENTER);
		setBottom(lbl);
		
		label.addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				((Label) getBottom()).setText(newValue);
			}
		});
		
		pathImage.addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				((ImageView) getCenter()).setImage(new Image(newValue));
			}
		});
		
		setPrefWidth(48);
		setPrefHeight(60);
	}

	public SimpleStringProperty getPathImage() {
		return pathImage;
	}

	public void setPathImage(SimpleStringProperty pathImage) {
		this.pathImage = pathImage;
	}

	public Leito getLeito() {
		return leito;
	}

	public void setLeito(Leito leito) {
		this.leito = leito;
		label.set(leito.getNumero()+"");
	}
	
	@Override
	public boolean equals(Object obj) {
		try {
			return leito.equals(((ImageCard) obj).getLeito());
		} catch (Exception e) {
			return false;
		}
	}
	
}
