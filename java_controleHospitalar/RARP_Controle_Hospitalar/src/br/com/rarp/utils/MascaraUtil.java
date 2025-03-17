package br.com.rarp.utils;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public enum MascaraUtil {
	;

	public static void somenteNumeros(final TextField textField) {
		textField.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observableValue, String antigo, String novo) {
				if (!novo.isEmpty()) {
					try {
						char[] t = novo.toCharArray();
						String novo2 = "";
						for (char i : t) {

							if (i != '(' && i != ')' && i != '-' && i != '/') {
								novo2 += i;
							}
						}

						Double.parseDouble(novo2);
					} catch (NumberFormatException e) {
						desfazAlteracao(antigo, textField);
					}
				}
			}
		});
	}

	private static void desfazAlteracao(String antigo, TextField textField) {
		if (antigo != null && !antigo.isEmpty()) {
			textField.setText(antigo);
		} else {
			textField.clear();
		}
	}

	public static void addTextLimiter(final TextField tf, final int maxLength) {
		tf.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(final ObservableValue<? extends String> ov, final String oldValue,
					final String newValue) {
				if (tf.getText().length() > maxLength) {
					String s = tf.getText().substring(0, maxLength);
					tf.setText(s);
				}
			}
		});
	}

	public static void addBarraData(final DatePicker data, final int maxLength) {
		data.promptTextProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(final ObservableValue<? extends String> ov, final String oldValue,
					final String newValue) {
				if (data.getPromptText().length() > maxLength) {
					String s = data.getPromptText().substring(0, maxLength);
					data.setPromptText(s);
				} else {

					// String value = tf.getText();
					// value = value.replaceAll("[^0-9]", "");
					// value = value.replaceFirst("(\\d{2})(\\d)", "$1/$2");
					// value = value.replaceFirst("(\\d{2})\\/(\\d{2})(\\d)",
					// "$1/$2/$3");
					// tf.setText(value);
					// positionCaret(tf);

					if (data != null) {
						if (data.getPromptText().length() < newValue.length()) {
							if (data.getPromptText().length() == 2) {
								data.setPromptText(data.getPromptText() + "/");
							}

							if (maxLength > 5) {
								if (data.getPromptText().length() == 5) {
									data.setPromptText(data.getPromptText() + "/");
								}
							}
						}
					}
				}
			}
		});
	}

	public static void addBarraCelular(final TextField tf, final int maxLength) {
		tf.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(final ObservableValue<? extends String> ov, final String oldValue,
					final String newValue) {
				if (tf.getText().length() > maxLength) {
					String s = tf.getText().substring(0, maxLength);
					tf.setText(s);
				} else {
					if (oldValue.length() < newValue.length()) {
						if (tf.getText().length() == 1) {
							String s = tf.getText().toString();

							tf.setText("(" + s);
						}
						if (tf.getText().length() == 3) {
							String s = tf.getText().toString();
							tf.setText(s + ")");
						}

						if (tf.getText().length() == 8) {
							tf.setText(tf.getText() + "-");
						}
					}
				}
			}
		});
	}

	public static void somenteNumerosPreco(final TextField textField) {
		textField.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observableValue, String antigo, String novo) {
				if (!novo.isEmpty()) {
					try {
						if (novo.equals("R$")) {

						} else {

							char[] t = novo.toCharArray();
							String novo2 = "";
							for (char i : t) {

								if (i != 'R' && i != '$' && i != ',') {
									novo2 += i;
								}
							}

							Double.parseDouble(novo2);
						}
					} catch (NumberFormatException e) {
						desfazAlteracao(antigo, textField);
					}
				}
			}
		});
	}

	public static void addVigula(final TextField tf, final int maxLength) {
		tf.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(final ObservableValue<? extends String> ov, final String oldValue,
					final String newValue) {
				if (tf.getText().length() > maxLength) {
					String s = tf.getText().substring(0, maxLength);
					tf.setText(s);
				} else {

					char[] t = tf.getText().toCharArray();

					String aki = "";
					for (char d : t) {
						if (d != ',' && d != '.') {

							aki += d;
						}
					}

					char[] r = aki.toCharArray();

					String a = "";
					int d = tf.getText().length() - 4;

					if (r.length > 4) {
						for (int i = 0; i < r.length; i++) {

							if (i == 0) {
								a += "R";
							} else {
								if (i == 1) {
									a += "$";
								} else {

									if (i == d) {
										a += String.valueOf(r[i]) + ",";
									} else {
										a += String.valueOf(r[i]);
									}
								}
							}
						}
						tf.setText(a);
					}

				}
			}
		});
	}

	@SuppressWarnings("unused")
	private static void positionCaret(final TextField textField) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				// Posiciona o cursor sempre a direita.
				textField.positionCaret(textField.getText().length());
			}
		});
	}
}
