/*************************************************************
* File : WaveletNet.java *
*************************************************************
* Contains a Wavelet Network subclass, which extends the *
* WNN superclass. *
* This Wavelet Network adjusts its wavelet coefficients by *
* learning from a set of training data. *
*************************************************************
* Author : David C Veitch *
*************************************************************/
package waveletNN;

public class WaveletNet extends WNN {
	private double trans_min;
	private double trans_max;
	private double dil_min;

	public WaveletNet(int wavelonCount) {
		super(wavelonCount);
	}

	/* Method to initialise the network */
	public void initialise(double a, double b) {
		/*
		 * ‘n’ is the number of complete resolution levels that can be
		 * initialised for the given wavelon count.
		 */
		int n = (int) (Math.log(super.wavelonCount) / Math.log(2));
		double t_i, l_i;
		/*
		 * Set the range of the translation parameters to be 20% larger than
		 * D=[a,b]
		 */
		this.trans_min = a - 0.1 * (b - a);
		this.trans_max = b + 0.1 * (b - a);
		/* Set the minimum dilation value */
		this.dil_min = 0.01 * (b - a);
		/*
		 * Initialise the wavelons within the complete resolution levels.
		 */
		this.initComplete(a, b, n);
		/*
		 * Initialise the remaining wavelons at random within the highest
		 * resolution level.
		 */
		while (super.count < super.wavelonCount) {
			t_i = a + (b - a) * Math.random();
			l_i = 0.5 * (b - a) * Math.pow(2, -n);
			super.addWavelon(0.0, t_i, l_i);
		}
	}

	/*
	 * Recursive method to initialise the wavelons within the complete
	 * resolution levels
	 */
	private void initComplete(double u, double v, int level) {
		double t_i = 0.5 * (u + v);
		double l_i = 0.5 * (v - u);
		super.addWavelon(0.0, t_i, l_i);
		if (level <= 1) {
			return;
		} else {
			this.initComplete(u, t_i, level - 1);
			this.initComplete(t_i, v, level - 1);
		}
	}

	/*
	 * Method to perform the stochastic gradient learning algorithm on the
	 * training data for the given ‘learning iterations’ and ‘learning rate’
	 * (gamma) constants.
	 */
	public void learn(double[][] training, int samples, int iterations, double gamma) {
		int i, j, k;
		double sum = 0;
		double u_k, f_u_k, e_k, psi, dpsi_du, w_i, l_i, t_i, dc_dt, dc_dl;
		/* y_bar is set to be the mean of the training data. */
		for (i = 0; i < samples; i++) {
			sum += training[i][1];
		}
		super.y_bar = sum / samples;
		for (j = 0; j < iterations; j++) {
			for (k = 0; k < samples; k++) {
				/*
				 * For each training sample, calculate the current ‘error’ in
				 * the network, and then update the network weights according to
				 * the stochastic gradient procedure.
				 */
				u_k = training[k][0];
				f_u_k = training[k][1];
				e_k = this.run(u_k) - f_u_k;
				super.y_bar -= gamma * e_k;
				for (i = 0; i < super.wavelonCount; i++) {
					psi = super.wavelons[i].fireGD(u_k);
					dpsi_du = super.wavelons[i].derivGD(u_k);
					w_i = super.weights[i];
					t_i = super.wavelons[i].getTranslation();
					l_i = super.wavelons[i].getDilation();
					dc_dt = gamma * e_k * w_i * Math.pow(l_i, -1) * dpsi_du;
					dc_dl = gamma * e_k * w_i * (u_k - t_i) * Math.pow(l_i, -2) * dpsi_du;
					super.weights[i] -= gamma * e_k * psi;
					/* Apply the constraints to the adjustable parameters */
					if (t_i + dc_dt < this.trans_min) {
						super.wavelons[i].setTranslation(this.trans_min);
					} else if (t_i + dc_dt > this.trans_max) {
						super.wavelons[i].setTranslation(this.trans_max);
					} else {
						super.wavelons[i].setTranslation(t_i + dc_dt);
					}
					if (l_i + dc_dl < this.dil_min) {
						super.wavelons[i].setDilation(dil_min);
					} else {
						super.wavelons[i].setDilation(l_i + dc_dl);
					}
				}
			}
			System.out.println(j);
		}
	}

	/*
	 * Method to run the wavelet network in its current configuration
	 */
	public double run(double input) {
		int i;
		double output = super.y_bar;
		for (i = 0; i < super.wavelonCount; i++) {
			output += super.weights[i] * super.wavelons[i].fireGD(input);

		}
		return output;
	}
}
