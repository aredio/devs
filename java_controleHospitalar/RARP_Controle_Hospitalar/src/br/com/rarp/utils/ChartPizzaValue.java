package br.com.rarp.utils;

public class ChartPizzaValue {
	private String label;
	private Integer value;
	private String legend;
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public Integer getValue() {
		return value;
	}
	public void setValue(Integer value) {
		this.value = value;
	}
	public String getLegend() {
		return legend;
	}
	public void setLegend(String legend) {
		this.legend = legend;
	}
	@Override
	public boolean equals(Object obj) {
		try {
			return ((ChartPizzaValue) obj).getLegend().equals(legend);
		} catch (Exception e) {
			return false;
		}
	}
}
