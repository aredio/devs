package br.com.rarp.view.scnComponents;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

public class CurrencyTextField extends TextField {
	public CurrencyTextField() {
		
		setOnKeyTyped(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if(!"0123456789-,".contains(event.getCharacter()))
					event.consume();
				
				if((event.getCharacter().contains(",") && getText().contains(","))
					|| (event.getCharacter().contains("-") && getText().contains("-"))
					|| (event.getCharacter().contains("-") && getCaretPosition() != 0))
					event.consume();
			}
		});
		
		focusedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(newValue) {
					setText(removeMask(getText()));
				} else {
					setText(putMask(getText()));
				}
			}
		});
	}
	
	private String removeMask(String text) {
		return text.replaceAll("\\.","").replace("R", "").replace("$", "").trim();
	}
	
	private String putMask(String text) {
		if(text.equals(""))
			text = "0";
		if(text.contains(","))
			text = text.replace(",", ".");
		return String.format("R$ %,.2f", Double.parseDouble(text));
	}
	
	public double getValue() {
		return Double.parseDouble(getText().replaceAll("\\.","").replace(",",".").replace("R", "").replace("$", "").trim());
	}
	
	public void setValue(double value) {
		setText(putMask(value + ""));
	}
}
