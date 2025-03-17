package br.com.rarp.view.scnComponents;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author fcsalgado
 * new AutoCompleteComboBoxListener<>(suaCombo);
 */
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.StringConverter;

public class AutoCompleteComboBoxListener<T> implements EventHandler<KeyEvent> {

	private ComboBox<T> comboBox;
	private ObservableList<T> data;
	private boolean moveCaretToPos = false;
	private boolean digited = false;
	private int caretPos;

	public AutoCompleteComboBoxListener(final ComboBox<T> comboBox) {
		this.comboBox = comboBox;
		new StringBuilder();
		data = comboBox.getItems();

		this.comboBox.setEditable(true);
		this.comboBox.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent t) {
				comboBox.hide();
			}
		});
		
		this.comboBox.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(newValue)
					comboBox.getEditor().selectAll();
			}
		});

		this.comboBox.setOnKeyReleased(AutoCompleteComboBoxListener.this);

		this.comboBox.setOnAction(t -> {
			boolean valido = false;
			if (digited) {
				for (int i = 0; i < data.size(); i++) {
					if (data.get(i).toString().toLowerCase().contains(
							AutoCompleteComboBoxListener.this.comboBox.getEditor().getText().toLowerCase())) {
						valido = true;
					}
					if (valido) {
						AutoCompleteComboBoxListener.this.comboBox.setValue(data.get(i));
						break;
					}
				}
				digited = false;
				if (!valido) {
					AutoCompleteComboBoxListener.this.comboBox.setItems(data);
					AutoCompleteComboBoxListener.this.comboBox.setValue(null);
				}
			}
		});
		comboBox.setConverter(new StringConverter<T>() {

			@Override
			public String toString(T arg0) {
				if (arg0 == null)
					return null;
				return arg0.toString();
			}

			@Override
			public T fromString(String string) {
				return comboBox.getSelectionModel().getSelectedItem();
			}
		});

	}

	@Override
	public void handle(KeyEvent event) {
		if (event.getCode() == KeyCode.UP) {
			caretPos = -1;
			moveCaret(comboBox.getEditor().getText().length());
			return;
		} else if (event.getCode() == KeyCode.DOWN) {
			if (!comboBox.isShowing()) {
				comboBox.show();
			}
			caretPos = -1;
			moveCaret(comboBox.getEditor().getText().length());
			return;
		} else if (event.getCode() == KeyCode.BACK_SPACE) {
			moveCaretToPos = true;
			caretPos = comboBox.getEditor().getCaretPosition();
		} else if (event.getCode() == KeyCode.DELETE) {
			moveCaretToPos = true;
			caretPos = comboBox.getEditor().getCaretPosition();
		}

		if (event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.LEFT || event.isControlDown()
				|| event.getCode() == KeyCode.HOME || event.getCode() == KeyCode.END
				|| event.getCode() == KeyCode.TAB) {
			return;
		}

		ObservableList<T> list = FXCollections.observableArrayList();
		if (data == null || data.size() == 0) {
			data = AutoCompleteComboBoxListener.this.comboBox.getItems();
		}
		for (int i = 0; i < data.size(); i++) {
			if (data.get(i).toString().toLowerCase()
					.contains(AutoCompleteComboBoxListener.this.comboBox.getEditor().getText().toLowerCase())) {
				list.add(data.get(i));
			}
		}
		String t = comboBox.getEditor().getText();
		comboBox.setItems(list);
		comboBox.getEditor().setText(t);
		if (!moveCaretToPos) {
			caretPos = -1;
		}
		moveCaret(t.length());
		if (!list.isEmpty()) {
			comboBox.show();
		}
		digited = true;
	}

	private void moveCaret(int textLength) {
		if (caretPos == -1) {
			comboBox.getEditor().positionCaret(textLength);
		} else {
			comboBox.getEditor().positionCaret(caretPos);
		}
		moveCaretToPos = false;
	}

}
