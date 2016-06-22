/*************************************************************
* File : WNN.java *
*************************************************************
* Contains the WNN superclass. *
* This implements the methods needed to set and retrieve *
* the network weights and wavelet coefficients. *
*************************************************************
* Author : David C Veitch *
*************************************************************/
package waveletNN;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javax.swing.JOptionPane;

public class WNN {
	protected int wavelonCount;
	protected double y_bar = 0.0;
	protected double[] weights;
	protected Wavelon[] wavelons;
	protected int count = 0;

	/* Constructor to set up the network */
	public WNN(int wavelonCount) {
		this.wavelonCount = wavelonCount;
		this.wavelons = new Wavelon[wavelonCount];
		this.weights = new double[wavelonCount];
	}

	/*
	 * Method to initialise a wavelon, if there is one uninitialised
	 */
	protected void addWavelon(double w, double t, double l) {
		if (this.count < this.wavelonCount) {
			this.wavelons[this.count] = new Wavelon(t, l);
			this.weights[this.count] = w;
			this.count++;
		} else {
			JOptionPane.showMessageDialog(null, "Number of wavelons has been exceeded!", "Initialisation Error",
					JOptionPane.ERROR_MESSAGE);
			System.exit(-1);
		}
	}

	/* Methods to return the network parameters */
	public double getYBar() {
		return this.y_bar;
	}

	public double[] getWeights() {
		return this.weights;
	}

	public double[] getTranslations() {
		int i;
		double[] trans = new double[this.wavelonCount];
		for (i = 0; i < this.wavelonCount; i++) {
			trans[i] = this.wavelons[i].getTranslation();
		}
		return trans;
	}

	public double[] getDilations() {
		int i;
		double[] dils = new double[this.wavelonCount];
		for (i = 0; i < this.wavelonCount; i++) {
			dils[i] = this.wavelons[i].getDilation();
		}
		return dils;
	}

	/*
	 * Method to print the network parameters to the specified file
	 */
	public void outputParameters(String filename)

	throws IOException {
		int i;
		PrintWriter fileOutput = new PrintWriter(new FileWriter(filename));
		double[] translations = this.getTranslations();
		double[] dilations = this.getDilations();
		fileOutput.println(this.y_bar);
		for (i = 0; i < this.wavelonCount; i++) {
			fileOutput.println(this.weights[i] + " " + translations[i] + " " + dilations[i]);
		}
		fileOutput.close();
	}
}