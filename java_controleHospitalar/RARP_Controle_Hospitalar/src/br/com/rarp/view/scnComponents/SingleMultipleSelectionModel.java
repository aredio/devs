package br.com.rarp.view.scnComponents;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.MultipleSelectionModel;

public class SingleMultipleSelectionModel<T> extends MultipleSelectionModel<T> {
	
	private ObservableList<T> items = FXCollections.observableArrayList();
	private ObservableList<T> selectedItems = FXCollections.observableArrayList();

	public ObservableList<T> getItems() {
		return items;
	}

	public void setItems(ObservableList<T> items) {
		this.items = items;
	}

	@Override
	public ObservableList<Integer> getSelectedIndices() {
		ObservableList<Integer> result = FXCollections.observableArrayList();
		for (T t: selectedItems)
			result.add(items.indexOf(t));
		return result;
	}

	@Override
	public ObservableList<T> getSelectedItems() {
		return selectedItems;
	}

	@Override
	public void selectAll() {			
		selectedItems.setAll(items);
		if(items.size() > 0) {
			setSelectedItem(items.get(items.size() - 1));
			setSelectedIndex(items.size() - 1);
		}
	}

	@Override
	public void selectFirst() {
		if (items.size() > 0 && !selectedItems.contains(items.get(0)))  {
			selectedItems.add(items.get(0));
			setSelectedItem(items.get(0));
			setSelectedIndex(0);
		}
	}

	@Override
	public void selectIndices(int index, int... indices) {
		for (int i = 0; i < indices.length; i++) {
			if(i >= 0 && i < items.size() && !selectedItems.contains(items.get(i))) {
				selectedItems.add(items.get(i));
				setSelectedItem(items.get(i));
				setSelectedIndex(i);
			}
		}
	}

	@Override
	public void selectLast() {
		if (items.size() > 0) {
			selectedItems.add(items.get(items.size() - 1));
			setSelectedItem(items.get(items.size() - 1));
			setSelectedIndex(items.size() - 1);
		}
	}

	@Override
	public void clearAndSelect(int index) {
		if (index >= 0 && index < items.size()) {
			selectedItems.clear();
			selectedItems.add(items.get(index));
			setSelectedItem(items.get(index));
			setSelectedIndex(index);
		}
	}

	@Override
	public void clearSelection() {
		selectedItems.clear();
		setSelectedItem(null);
		setSelectedIndex(-1);
	}

	@Override
	public void clearSelection(int index) {
		if(index >= 0 && index < items.size()) {
			if(selectedItems.contains(items.get(index)))
				selectedItems.remove(items.get(index));
			if(getSelectedItem() != null && getSelectedItem().equals(items.get(index))) {
				setSelectedItem(null);
				setSelectedIndex(-1);
			}
		}
	}

	@Override
	public boolean isEmpty() {
		return selectedItems.size() > 0;
	}

	@Override
	public boolean isSelected(int index) {
		return index >= 0 && index < items.size() && selectedItems.contains(items.get(index));
	}

	@Override
	public void select(int index) {
		if(index >= 0 && index < items.size()) {
			if(!selectedItems.contains(items.get(index)))
				selectedItems.add(items.get(index));
			setSelectedItem(items.get(index));
			setSelectedIndex(index);
		}	
	}

	@Override
	public void select(T obj) {
		if(items.contains(obj) && !selectedItems.contains(obj))
			selectedItems.add(obj);
		setSelectedItem(obj);
		setSelectedIndex(items.indexOf(obj));
	}

	@Override
	public void selectNext() {
		if(items.indexOf(getSelectedItem()) + 1 >= 0 && items.indexOf(getSelectedItem()) + 1 < items.size()) {
			setSelectedItem(items.get(items.indexOf(getSelectedItem()) + 1));
			setSelectedIndex(items.indexOf(getSelectedItem()) + 1);
		}
	}

	@Override
	public void selectPrevious() {
		if(items.indexOf(getSelectedItem()) - 1 >= 0 && items.indexOf(getSelectedItem()) - 1 < items.size()) {
			setSelectedItem(items.get(items.indexOf(getSelectedItem()) - 1));
			setSelectedIndex(items.indexOf(getSelectedItem()) - 1);
		}
	}

	public void clearSelecteds() {
		clearSelection();
	}
}
