package br.com.rarp.model.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import br.com.rarp.utils.Utilitarios;

public class TypedProperties extends Properties {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3656689727223274529L;

	public String getProperty(String key, String defaultValue) {
		return super.getProperty(key, defaultValue);
	}
	
	public Integer getProperty(String key, Integer defaultValue) {
		String property = super.getProperty(key, "a");
		try {
			return Integer.valueOf(property);
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}
	
	public Boolean getProperty(String key, Boolean defaultValue) {
		String property = super.getProperty(key, "a");
		try {
			return Boolean.valueOf(property);
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}
	
	public Double getProperty(String key, Double defaultValue) {
		String property = super.getProperty(key, "a");
		try {
			return Double.valueOf(property);
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}
	
	public Date getProperty(String key, Date defaultValue) {
		String property = super.getProperty(key, "a");
		try {
			return new SimpleDateFormat().parse(property);
		} catch (ParseException e) {
			return defaultValue;
		}
	}
	
	public Object setProperty(String key, String value) {
		return super.setProperty(key, value);
	}
	
	public Object setProperty(String key, Integer value) {
		return super.setProperty(key, String.valueOf(value));
	}
	
	public Object setProperty(String key, Double value) {
		return super.setProperty(key, String.valueOf(value));
	}
	
	public Object setProperty(String key, Boolean value) {
		return super.setProperty(key, String.valueOf(value));
	}
	
	public Object setProperty(String key, Date value) {
		return super.setProperty(key, Utilitarios.dateToStr(value));
	}
}
