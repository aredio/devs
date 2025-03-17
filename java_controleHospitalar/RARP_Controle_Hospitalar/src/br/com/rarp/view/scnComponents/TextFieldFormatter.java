package br.com.rarp.view.scnComponents;

import javafx.scene.control.TextField;

import java.text.ParseException;

import javax.swing.text.MaskFormatter;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class TextFieldFormatter extends TextField{

    private final MaskFormatter mf;
	private SimpleStringProperty CaracteresValidos =  new SimpleStringProperty();
    private SimpleStringProperty mask =  new SimpleStringProperty();
    private SimpleBooleanProperty apagarEspacos = new SimpleBooleanProperty();

    public boolean getApagarEspacos() {
		return apagarEspacos.get();
	}

	public void setApagarEspacos(boolean apagarEspacos) {
		this.apagarEspacos.set(apagarEspacos) ;
	}

	public TextFieldFormatter() {
    	super();
        mf = new MaskFormatter();
        
        this.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub
				if (newValue.length() > oldValue.length())
				formatter();
			}
		});
    }

    public void formatter() {
        try {
            mf.setMask(mask.get());
        } catch (ParseException ex) {
            System.out.println(ex.getMessage());
        }

        mf.setValidCharacters(CaracteresValidos.get());
        mf.setValueContainsLiteralCharacters(false);
        String text = this.getText().replaceAll("[\\W]", "");
        int possicao = text.length();
        boolean repetir = true;
        while (repetir) {

            char ultimoCaractere;

            if (text.equals("")) {
                break;
            } else {
                ultimoCaractere = text.charAt(text.length() - 1);
            }

            try {
                text = mf.valueToString(text);
                repetir = false;
            } catch (ParseException ex) {
                text = text.replace(ultimoCaractere + "", "");
                repetir = true;
            }

        }

        this.setText(text);
        this.selectPositionCaret(possicao);
        

        if (!text.equals("")) {
            for (int i = 0; this.getText().charAt(i) != ' ' && i < this.getLength() - 1; i++) {
            	this.forward();
            }
        }
    }


    /**
     * @return the CaracteresValidos
     */
    public String getCaracteresValidos() {
        return CaracteresValidos.get();
    }

    /**
     * @param CaracteresValidos the CaracteresValidos to set
     */
    public void setCaracteresValidos(String CaracteresValidos) {
        this.CaracteresValidos.set(CaracteresValidos);
    }
    
    public String getMask() {
        return mask.get();
    }

    /**
     * @param mask the mask to set
     */
    public void setMask(String mask) {
        this.mask.set(mask);
    }
}
