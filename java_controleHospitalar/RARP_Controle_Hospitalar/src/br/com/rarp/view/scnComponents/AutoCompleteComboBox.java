package br.com.rarp.view.scnComponents;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.ListChangeListener;
import javafx.scene.control.ComboBox;

public class AutoCompleteComboBox<T> extends ComboBox<T> {

	private boolean acceptEmptyValue;
	private String emptyString = "<Nenhum>";
	public String getEmptyString() {
		return emptyString;
	}

	public void setEmptyString(String emptyString) {
		this.emptyString = emptyString;
	}

	private class EmptyObject {
		@Override
		public String toString() {
			return emptyString;
		}
	}
	
	private EmptyObject emptyValue = new EmptyObject();

	@SuppressWarnings("unchecked")
	public AutoCompleteComboBox() {			
		new AutoCompleteComboBoxListener<>(this);
		
		getItems().addListener(new ListChangeListener<T>() {

			@Override
			public void onChanged(Change<? extends T> c) {
				if(acceptEmptyValue && !getItems().contains(emptyValue)) {
					List<T> data = new ArrayList<>();
					data.add((T) emptyValue);
					data.addAll(getItems());
					getItems().setAll(data);
					getSelectionModel().select((T) emptyValue);
				}
			}
		});

	}

	public boolean isAcceptEmptyValue() {
		return acceptEmptyValue;
	}

	@SuppressWarnings("unchecked")
	public void setAcceptEmptyValue(boolean acceptEmptyValue) {
		this.acceptEmptyValue = acceptEmptyValue;
		if (acceptEmptyValue) {
			getItems().add((T) emptyValue);
			getSelectionModel().select((T) emptyValue);
		}
			
	}
	
	public T getSelectedValue() {
		return getValue().equals(emptyValue) ? null : getValue();
	}
}
