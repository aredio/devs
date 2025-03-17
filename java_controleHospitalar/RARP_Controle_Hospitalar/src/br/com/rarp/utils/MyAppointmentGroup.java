package br.com.rarp.utils;
import javafx.scene.paint.Paint;
import jfxtras.scene.control.agenda.Agenda.AppointmentGroup;

public class MyAppointmentGroup implements AppointmentGroup {
	
	private String description;
	private String styleClass;
	private Paint paint;

	public MyAppointmentGroup(String styleClass, String paint) {
		setDescription(paint);
		setStyleClass(styleClass);
		setPaint(paint);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj != null)
			return ((MyAppointmentGroup) obj).getStyleClass().equals(styleClass);
		else
			return false;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return description;
	}

	@Override
	public void setDescription(String s) {
		description = s;
	}

	@Override
	public String getStyleClass() {
		// TODO Auto-generated method stub
		return styleClass;
	}

	@Override
	public void setStyleClass(String s) {
		styleClass = s;
	}

	public Paint getPaint() {
		return paint;
	}

	public void setPaint(String s) {
		this.paint = Paint.valueOf(s);
	}
	
	@Override
	public String toString() {
		return description;
	}

}
