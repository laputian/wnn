/*************************************************************
* File : Wavenet.java *
*************************************************************
* Contains a Wavenet subclass, which extends the WNN *
* superclass. *
* The wavelet coefficients are dyadic for the wavenet. *
* The learning algorithm adjusts the network weights only. *
* The wavelet coefficients are fixed at initialisation. *
*************************************************************
* Author : David C Veitch *
*************************************************************/
package waveletNN;

public class Wavenet extends WNN {
	private int t_0;
	private int t_K;

	/*
	 * Constructor to set the number of wavelons and the ranges of the
	 * translation parameter.
	 */
	public Wavenet(int t_low, int t_high) {
		/*
		 * Number of wavelons equals the number of integers in the interval
		 * [t_low, t_high].
		 */
		super(t_high - t_low + 1);
		this.t_0 = t_low;
		this.t_K = t_high;
	}

	/* Method to initialise the wavelons. */
	public void initialise(int M) {
		int t_i;
		for (t_i = this.t_0; t_i <= this.t_K; t_i++) {
			super.addWavelon(0.0, t_i, Math.pow(2, M));
		}
	}

	/*
	 * Method to perform the stochastic gradient learning algorithm on the
	 * training data for the given ‘learning iterations’ and ‘learning rate’
	 * (gamma) constants.
	 */
	public void learn(double[][] training, int samples, int iterations, double gamma) {
		int i, j, k;
		double u_k, f_u_k, e_k, phi;
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
				for (i = 0; i < super.wavelonCount; i++) {
					phi = super.wavelons[i].fireLemarie(u_k);
					/*
					 * The normalisation factor from the formula is taken care
					 * of within ‘psi’.
					 */
					super.weights[i] -= gamma * e_k * phi;
				}
			}
			System.out.println(j);
		}
	}

	/*
	 * Method to run the wavenet in its current configuration
	 */
	public double run(double input) {
		int i;
		double output = 0.0;
		for (i = 0; i < super.wavelonCount; i++) {
			output += super.weights[i] * super.wavelons[i].fireLemarie(input);
		}
		return output;
	}
}