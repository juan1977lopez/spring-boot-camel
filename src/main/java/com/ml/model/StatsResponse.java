package com.ml.model;

public class StatsResponse {
	
	private double countMutant;
	private double countHuman;
	private double ratio;
	
	public double getCountMutant() {
		return countMutant;
	}

	public void setCountMutant(double countMutant) {
		this.countMutant = countMutant;
	}

	public double getCountHuman() {
		return countHuman;
	}

	public void setCountHuman(double countHuman) {
		this.countHuman = countHuman;
	}

	public double getRatio() {
		return ratio;
	}

	public void setRatio(double ratio) {
		this.ratio = ratio;
	}
}
