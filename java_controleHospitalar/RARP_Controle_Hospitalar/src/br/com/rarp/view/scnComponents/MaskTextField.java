package br.com.rarp.view.scnComponents;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

public class MaskTextField extends TextField {
	private SimpleBooleanProperty cnpj = new SimpleBooleanProperty();
	private SimpleBooleanProperty cep = new SimpleBooleanProperty();
	private SimpleBooleanProperty email = new SimpleBooleanProperty();
	private SimpleBooleanProperty telefone = new SimpleBooleanProperty();
	private SimpleBooleanProperty cpf = new SimpleBooleanProperty();

	public MaskTextField() {
		super();
		
		cpf.addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(newValue) {
					email.set(oldValue);
					cep.set(oldValue);
					telefone.set(oldValue);
					cnpj.set(oldValue);
					
			        setOnKeyTyped((KeyEvent event) -> {
			            if(!"0123456789".contains(event.getCharacter())){
			                event.consume();
			            }

			            if(event.getCharacter().trim().length()==0){ // apagando
			                if(getString().length()==4){
			                    setText(getString().substring(0,3));
			                    positionCaret(getString().length());
			                }
			                if(getString().length()==8){
			                    setText(getString().substring(0,7));
			                    positionCaret(getString().length());
			                }
			                if(getString().length()==12){
			                    setText(getString().substring(0,11));
			                    positionCaret(getString().length());
			                }
			            }else{ // escrevendo

			                if(getString().length()==14) event.consume();

			                if(getString().length()==3){
			                    setText(getString()+".");
			                    positionCaret(getString().length());
			                }
			                if(getString().length()==7){
			                    setText(getString()+".");
			                    positionCaret(getString().length());
			                }
			                if(getString().length()==11){
			                    setText(getString()+"-");
			                    positionCaret(getString().length());
			                }

			            }
			        });

			        setOnKeyReleased((KeyEvent evt) -> {

			            if(!getString().matches("\\d.-*")){
			                setText(getString().replaceAll("[^\\d.-]", ""));
			                positionCaret(getString().length());
			            }
			        });
				}
			}
		});

		cnpj.addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (newValue) {
					cep.set(oldValue);
					telefone.set(oldValue);
					email.set(oldValue);
					cpf.set(oldValue);
					
					setOnKeyTyped((KeyEvent event) -> {
						if ("0123456789".contains(event.getCharacter()) == false) {
							event.consume();
						}

						if (event.getCharacter().trim().length() == 0) { // apagando

							if (getString().length() == 3) {
								setText(getString().substring(0, 2));
								positionCaret(getString().length());
							}
							if (getString().length() == 7) {
								setText(getString().substring(0, 6));
								positionCaret(getString().length());
							}
							if (getString().length() == 11) {
								setText(getString().substring(0, 10));
								positionCaret(getString().length());
							}
							if (getString().length() == 16) {
								setText(getString().substring(0, 15));
								positionCaret(getString().length());
							}

						} else { // escrevendo

							if (getString().length() == 18)
								event.consume();

							if (getString().length() == 2) {
								setText(getString() + ".");
								positionCaret(getString().length());
							}
							if (getString().length() == 6) {
								setText(getString() + ".");
								positionCaret(getString().length());
							}
							if (getString().length() == 10) {
								setText(getString() + "/");
								positionCaret(getString().length());
							}
							if (getString().length() == 15) {
								setText(getString() + "-");
								positionCaret(getString().length());
							}

						}
					});
					setOnKeyReleased((KeyEvent evt) -> {

						if (!getString().matches("\\d./-*")) {
							setText(getString().replaceAll("[^\\d./-]", ""));
							positionCaret(getString().length());
						}
					});
				}

			}

		});

		cep.addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (newValue) {
					cnpj.set(oldValue);
					telefone.set(oldValue);
					email.set(oldValue);
					cpf.set(oldValue);
					
					setOnKeyTyped((KeyEvent event) -> {
						if ("0123456789".contains(event.getCharacter()) == false) {
							event.consume();
						}

						if (event.getCharacter().trim().length() == 0) { // apagando

							if (getString().length() == 6) {
								setText(getString().substring(0, 5));
								positionCaret(getString().length());
							}

						} else { // escrevendo

							if (getString().length() == 9)
								event.consume();

							if (getString().length() == 5) {
								setText(getString() + "-");
								positionCaret(getString().length());
							}

						}
					});
					setOnKeyReleased((KeyEvent evt) -> {

						if (!getString().matches("\\d-*")) {
							setText(getString().replaceAll("[^\\d-]", ""));
							positionCaret(getString().length());
						}
					});
				}
			}

		});

		telefone.addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (newValue) {
					cnpj.set(oldValue);
					cep.set(oldValue);
					email.set(oldValue);
					cpf.set(oldValue);
					
					setOnKeyTyped((KeyEvent event) -> {
						if ("0123456789".contains(event.getCharacter()) == false) {
							event.consume();
						}

						if (event.getCharacter().trim().length() == 0) { // apagando

							if (getString().length() == 10 && getString().substring(9, 10).equals("-")) {
								setText(getString().substring(0, 9));
								positionCaret(getString().length());
							}
							if (getString().length() == 9 && getString().substring(8, 9).equals("-")) {
								setText(getString().substring(0, 8));
								positionCaret(getString().length());
							}
							if (getString().length() == 4) {
								setText(getString().substring(0, 3));
								positionCaret(getString().length());
							}
							if (getString().length() == 1) {
								setText("");
							}

						} else { // escrevendo

							if (getString().length() == 14)
								event.consume();

							if (getString().length() == 0) {
								setText("(" + event.getCharacter());
								positionCaret(getString().length());
								event.consume();
							}
							if (getString().length() == 3) {
								setText(getString() + ")" + event.getCharacter());
								positionCaret(getString().length());
								event.consume();
							}
							if (getString().length() == 8) {
								setText(getString() + "-" + event.getCharacter());
								positionCaret(getString().length());
								event.consume();
							}
							if (getString().length() == 9 && getString().substring(8, 9) != "-") {
								setText(getString() + "-" + event.getCharacter());
								positionCaret(getString().length());
								event.consume();
							}
							if (getString().length() == 13 && getString().substring(8, 9).equals("-")) {
								setText(getString().substring(0, 8) + getString().substring(9, 10) + "-"
										+ getString().substring(10, 13) + event.getCharacter());
								positionCaret(getString().length());
								event.consume();
							}

						}

					});
					setOnKeyReleased((KeyEvent evt) -> {

						if (!getString().matches("\\d()-*")) {
							setText(getString().replaceAll("[^\\d()-]", ""));
							positionCaret(getString().length());
						}
					});
				}
			}

		});

		email.addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (newValue) {
					cnpj.set(oldValue);
					cep.set(oldValue);
					telefone.set(oldValue);
					cpf.set(oldValue);
					
					setOnKeyTyped((KeyEvent event) -> {
						if ("ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz._-@"
								.contains(event.getCharacter()) == false) {
							event.consume();
						}

						if ("@".equals(event.getCharacter()) && getString().contains("@")) {
							event.consume();
						}

						if ("@".equals(event.getCharacter()) && getString().length() == 0) {
							event.consume();
						}
					});
				}
			}

		});
	}
	
	private String getString() {
		if(getText() != null)
			return getText();
		return "";
	}

	public boolean getCnpj() {
		return cnpj.get();
	}

	public void setCnpj(boolean cnpj) {
		this.cnpj.set(cnpj);
	}

	public boolean getCep() {
		return cep.get();
	}

	public void setCep(boolean cep) {
		this.cep.set(cep);
	}

	public boolean getEmail() {
		return email.get();
	}

	public void setEmail(boolean email) {
		this.email.set(email);
	}

	public boolean getTelefone() {
		return telefone.get();
	}

	public void setTelefone(boolean telefone) {
		this.telefone.set(telefone);
	}
	
	public String getValue() {
		String value = getString();
		if(cnpj.get() || telefone.get() || cep.get()) {
			value = value.replaceAll("[\\D]", "");
		} 
		return value;
	}
	
	public void setValue(String value) {
		setText(value);
	}

	public boolean getCpf() {
		return cpf.get();
	}

	public void setCpf(boolean cpf) {
		this.cpf.set(cpf);
	}

}
