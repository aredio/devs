package br.com.rarp.utils;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.InputMismatchException;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.ButtonType;

public class Utilitarios {
	static boolean resultPergunta = false;
	
	private static EventHandler<KeyEvent> bloquear = new EventHandler<KeyEvent>() {
		
		@Override
		public void handle(KeyEvent event) {
			event.consume();
		}
	};
	
	public static double getPercentual(double valorTotal, double parteValor) {
		if(valorTotal > 0)
			return parteValor * 100 / valorTotal;
		else
			return 0;
	}

	public static void atencao(String message) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setContentText(message);
		alert.setHeaderText("Atenção");
		alert.showAndWait();
	}

	public static void erro(String message) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setContentText(message);
		alert.setHeaderText("Erro");
		alert.showAndWait();
	}

	public static void message(String message) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setContentText(message);
		alert.setHeaderText("Sucesso");
		alert.showAndWait();
	}

	public static String dateToStr(Date data) {
		return new SimpleDateFormat("dd/MM/yyyy").format(data);
	}
	
	public static Date localDateToDate(LocalDate localDate) {
		Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
		return Date.from(instant);
	}
	
	public static LocalDate dateToLocalDate(Date date) {
		return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}
	
	public static Date localDateTimeToDate(LocalDateTime localDateTime) {
		return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
	}
	
	public static LocalTime strToLocalTime(String value) {
		return LocalTime.parse(value);
	}
	
	public static LocalDateTime dateToLocalDateTime(Date date) {
		return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
	}
	
	public static LocalDate localDateTimeToLocalDate(LocalDateTime localDateTime) {
		return dateToLocalDate(localDateTimeToDate(localDateTime));
	}
	
	public static LocalDateTime localDateToLocalDateTime(LocalDate localDate) {
		return dateToLocalDateTime(localDateToDate(localDate));
	}

	public static String formatStringSQL(String sql) {
		sql = sql.replaceAll("\"", "");
		sql = sql.replaceAll("'", "");
		return sql;
	}

	public static boolean pergunta(String message) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setContentText(message);
		ButtonType btnSim = new ButtonType("Sim");
		ButtonType btnNao = new ButtonType("Não");
		alert.getButtonTypes().clear();
		alert.getButtonTypes().addAll(btnSim, btnNao);
		alert.showAndWait().ifPresent(b -> {
			if (b == btnSim) {
				resultPergunta = true;
			} else if (b == btnNao) {
				resultPergunta = false;
			}
		});
		return resultPergunta;
	}

	public static Double strToDouble(String value) {
		try {
			return Double.parseDouble(value);
		} catch (Exception e) {
			return 0.0;
		}
	}

	public static double strToDouble(String value, double defaultValue) {
		try {
			return Double.parseDouble(value);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public static int strToInt(String val, int valDefault) {
		try {
			return Integer.parseInt(val);
		} catch (Exception e) {
			return valDefault;
		}
	}

	public static int strToInt(String val) {
		try {
			return Integer.parseInt(val);
		} catch (Exception e) {
			return 0;
		}
	}

	public static Node getNodeById(Parent parent, String id) {
		if (parent != null)
			for (Node node : parent.getChildrenUnmodifiable()) {
				if (node != null && node.getId() != null && node.getId().equals(id))
					return node;
			}
		return null;
	}

	public static ArrayList<Node> getAllNodes(Parent root) {
		ArrayList<Node> nodes = new ArrayList<Node>();
		addAllDescendents(root, nodes);
		return nodes;
	}

	private static void addAllDescendents(Parent parent, ArrayList<Node> nodes) {
		for (Node node : parent.getChildrenUnmodifiable()) {
			nodes.add(node);
			if (node instanceof Parent)
				addAllDescendents((Parent) node, nodes);
		}
	}
	
	public static boolean isCPF(String CPF) {
        if (CPF.equals("00000000000") || CPF.equals("11111111111")
                || CPF.equals("22222222222") || CPF.equals("33333333333")
                || CPF.equals("44444444444") || CPF.equals("55555555555")
                || CPF.equals("66666666666") || CPF.equals("77777777777")
                || CPF.equals("88888888888") || CPF.equals("99999999999")
                || (CPF.length() != 11)) {
            return (false);
        }

        char dig10, dig11;
        int sm, i, r, num, peso;

        try {
            sm = 0;
            peso = 10;
            for (i = 0; i < 9; i++) {
                num = (int) (CPF.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso - 1;
            }

            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11)) {
                dig10 = '0';
            } else {
                dig10 = (char) (r + 48);
            }
            sm = 0;
            peso = 11;
            for (i = 0; i < 10; i++) {
                num = (int) (CPF.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso - 1;
            }

            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11)) {
                dig11 = '0';
            } else {
                dig11 = (char) (r + 48);
            }

            if ((dig10 == CPF.charAt(9)) && (dig11 == CPF.charAt(10))) {
                return (true);
            } else {
                return (false);
            }
        } catch (InputMismatchException erro) {
            return (false);
        }
    }

    public static boolean isCNPJ(String cnpj) {
        if (!cnpj.substring(0, 1).equals("")) {
            try {
                if (cnpj.equals("00000000000000") || cnpj.equals("11111111111111")
                        || cnpj.equals("22222222222222") || cnpj.equals("33333333333333")
                        || cnpj.equals("44444444444444") || cnpj.equals("55555555555555")
                        || cnpj.equals("66666666666666") || cnpj.equals("77777777777777")
                        || cnpj.equals("88888888888888") || cnpj.equals("99999999999999")
                        || (cnpj.length() != 14)) {
                    return false;
                }
                int soma = 0, dig;
                String cnpj_calc = cnpj.substring(0, 12);
                if (cnpj.length() != 14) {
                    return false;
                }
                char[] chr_cnpj = cnpj.toCharArray();
                /* Primeira parte */
                for (int i = 0; i < 4; i++) {
                    if (chr_cnpj[i] - 48 >= 0 && chr_cnpj[i] - 48 <= 9) {
                        soma += (chr_cnpj[i] - 48) * (6 - (i + 1));
                    }
                }
                for (int i = 0; i < 8; i++) {
                    if (chr_cnpj[i + 4] - 48 >= 0 && chr_cnpj[i + 4] - 48 <= 9) {
                        soma += (chr_cnpj[i + 4] - 48) * (10 - (i + 1));
                    }
                }
                dig = 11 - (soma % 11);
                cnpj_calc += (dig == 10 || dig == 11) ? "0" : Integer.toString(
                        dig);
                /* Segunda parte */
                soma = 0;
                for (int i = 0; i < 5; i++) {
                    if (chr_cnpj[i] - 48 >= 0 && chr_cnpj[i] - 48 <= 9) {
                        soma += (chr_cnpj[i] - 48) * (7 - (i + 1));
                    }
                }
                for (int i = 0; i < 8; i++) {
                    if (chr_cnpj[i + 5] - 48 >= 0 && chr_cnpj[i + 5] - 48 <= 9) {
                        soma += (chr_cnpj[i + 5] - 48) * (10 - (i + 1));
                    }
                }
                dig = 11 - (soma % 11);
                cnpj_calc += (dig == 10 || dig == 11) ? "0" : Integer.toString(
                        dig);
                return cnpj.equals(cnpj_calc);
            } catch (Exception e) {
                return false;
            }
        } else {
            return false;
        }
    }

	public static boolean isMaiorIdade(LocalDate dtNascimento) {
		LocalDate date = LocalDate.now();
		LocalDate date2 = dtNascimento.plusYears(18);
		return date.equals(date2) || !date.isBefore(date2);
	}

	public static EventHandler<KeyEvent> getBloquear() {
		return bloquear;
	}

	public static void setBloquear(EventHandler<KeyEvent> bloquear) {
		Utilitarios.bloquear = bloquear;
	}

}
