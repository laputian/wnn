/*************************************************************
* File : Wavelon.java *
*************************************************************
* Contains a ‘Wavelon’ object. *
* This ‘Wavelon’ has associated ‘translation’ and *
* ‘dilation’ parameters. There are methods to adjust and *
* return these parameters. *
* There are two choices of activation function, the *
* ‘Gaussian Derivative’ or the ‘Battle-Lemarie’ wavelet. *
*************************************************************
* Author : David C Veitch *
*************************************************************/
package waveletNN;

public class Wavelon {
	private double translation;
	private double dilation;

	/* Constructor to initialise the private variables */
	public Wavelon(double translation, double dilation) {
		this.translation = translation;
		this.dilation = dilation;
	}

	/* Methods to change the private variables */
	public void setTranslation(double translation) {
		this.translation = translation;
	}

	public void setDilation(double dilation) {
		this.dilation = dilation;
	}

	/* Methods to return the private variables */
	public double getTranslation() {
		return this.translation;
	}

	public double getDilation() {
		return this.dilation;
	}

	/* Method to calculate the ‘Gaussian Derivative’ */
	public double fireGD(double input) {
		double u = (input - this.translation) / this.dilation;
		return -u * Math.exp(-0.5 * Math.pow(u, 2));
	}

	/*
	 * Method to calculate the ‘Gaussian 2nd Derivative’ used by the wavelet
	 * network learning algorithm
	 */
	public double derivGD(double input) {
		double u = (input - this.translation) / this.dilation;
		return Math.exp(-0.5 * Math.pow(u, 2)) * (Math.pow(u, 2) - 1);
	}

	/* Method to calculate the ‘Battle-Lemarie’ wavelet */
	public double fireLemarie(double input) {
		double u = this.dilation * input - this.translation;
		double y = 0.0;
		if (u >= -1 && u < 0) {
			y = 0.5 * Math.pow(u + 1, 2);
		} else if (u >= 0 && u < 1) {
			y = 0.75 - Math.pow(u - 0.5, 2);
		} else if (u >= 1 && u < 2) {
			y = 0.5 * Math.pow(u - 2, 2);
		} else
			y = 0.0;
		return Math.pow(this.dilation, 0.5) * y;
	}
}