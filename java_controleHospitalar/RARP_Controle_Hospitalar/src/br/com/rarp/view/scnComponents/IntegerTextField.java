package br.com.rarp.view.scnComponents;

import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

public class IntegerTextField extends TextField {
	
	public IntegerTextField() {
		this.setOnKeyTyped(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				switch (event.getCharacter()) {
				case "+":
					event.consume();
					try {
						setText(String.valueOf(Integer.parseInt(getText()) + 1));
					} catch (NumberFormatException e1) {
						setText("1");
					}
					break;
				case "-":
					event.consume();
					try {
						setText(String.valueOf(Integer.parseInt(getText()) - 1));
					} catch (NumberFormatException e1) {
						setText("-1");
					}
					break;
				default:
					try {
						Integer.parseInt(event.getCharacter());
					} catch (NumberFormatException e) {
						if(!event.getCharacter().contains("-"))
							event.consume();
					}
					break;
				}
			}
		});
	}
	
	public int getValue() {
		try {
			return Integer.parseInt(getText());
		} catch (NumberFormatException e) {
			return 0;
		}
	}
	
	public void setValue(int value) {
		setText(value+"");
	}
}
