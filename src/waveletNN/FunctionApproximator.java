/*************************************************************
* File : FunctionApproximator.java *
*************************************************************
* A Java Program that will approximate an unknown *
* function from a series of sample inputs and outputs for *
* that function, read from the file ‘training.txt’. *
* It achieves this via wavelet network learning. *
* It outputs the wavelet coefficients to the file *
* ‘coeffs.txt’. *
*************************************************************
* Author : David C Veitch *
*************************************************************/
package waveletNN;

import java.io.*;
import java.util.StringTokenizer;
import java.lang.Math;
import javax.swing.*;

public class FunctionApproximator {
	public static void main(String[] args) throws IOException {
		int dyadic, N, M, wavelons;
		int S = 300;
		double gamma;
		double[][] data;
		int samples = 0;
		double domain_low = 0, domain_high = 0;
		int t_low, t_high;
		String line;
		StringTokenizer tokens;
		String[] buttons = { "Wavelet Network", "Dyadic Wavenet" };
		BufferedReader fileInput = new BufferedReader(new FileReader("data/training.txt"));
		/*
		 * Read the number of training samples from the first line of file
		 * ‘training.txt’
		 */
		if ((line = fileInput.readLine()) != null) {
			S = Integer.parseInt(line);
		} else {
			JOptionPane.showMessageDialog(null, "Error reading the number of training samples", "File Read Error",
					JOptionPane.ERROR_MESSAGE);
			System.exit(-1);
		}
		data = new double[S][2];
		/*
		 * Read the file ‘training.txt’, for a maximum of ‘S’ training samples.
		 */
		while ((line = fileInput.readLine()) != null && samples < S) {
			tokens = new StringTokenizer(line, " ");
			/* Each line is of the form ‘u_k f(u_k)’ */
			if (tokens.countTokens() != 2) {
				JOptionPane.showMessageDialog(null, "Error on line " + (samples + 1), "File Read Error",
						JOptionPane.ERROR_MESSAGE);
				System.exit(-1);
			}
			/* The first value will be the sample input ‘u_k’ */
			data[samples][0] = Double.parseDouble(tokens.nextToken());
			/* The second value will be the sample output ‘f(u_k)’ */
			data[samples][1] = Double.parseDouble(tokens.nextToken());
			/*
			 * Initialise the domain ranges from the first lines values.
			 */
			if (samples == 0) {
				domain_low = data[samples][0];
				domain_high = data[samples][0];
			} else
				/*
				 * If necessary, adjust the domain of the function to be
				 * estimated.
				 */
				if (data[samples][0] < domain_low) {
				domain_low = data[samples][0];
			} else if (data[samples][0] > domain_high) {
				domain_high = data[samples][0];
			}
			samples++;
		}
		/* Prompt the user for the type of network to use. */
		dyadic = JOptionPane.showOptionDialog(null, "Select the type of WNN", "WNN Selection",
				JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, buttons, buttons[0]);
		/* Prompt the user for the following learning constants */
		N = Integer.parseInt(JOptionPane.showInputDialog("Enter the number of learning iterations:"));
		gamma = Double.parseDouble(JOptionPane.showInputDialog("Enter the learning rate:"));
		if (dyadic == 1) {
			M = Integer.parseInt(JOptionPane.showInputDialog("Enter the dyadic resolution:"));
			/*
			 * Calculate the range of the wavelet centres covering a
			 * neighbourhood of the sample domain
			 */
			t_low = (int) ((domain_low - 1) * Math.pow(2, M) - 1);
			t_high = (int) ((domain_high + 1) * Math.pow(2, M) + 1);
			/* Instantiate the wavenet */
			Wavenet wnn = new Wavenet(t_low, t_high);
			/* Initialise the wavenet for the given resolution ‘M’ */
			wnn.initialise(M);
			/* Perform the learning of the sampled data */
			wnn.learn(data, samples, N, gamma);
			/*
			 * Output the learned wavelet parameters to the specified file
			 */
			wnn.outputParameters("coeffs.txt");
			/* Mark for garbage collection */
			wnn = null;
		} else {
			wavelons = Integer.parseInt(JOptionPane.showInputDialog("Enter the number of wavelons:"));
			/* Instantiate the wavelet network */
			WaveletNet wnn = new WaveletNet(wavelons);
			/*
			 * Initialise the wavelet network for the given sample domain
			 */
			wnn.initialise(domain_low, domain_high);
			/* Perform the learning of the samples data */
			wnn.learn(data, samples, N, gamma);
			/*
			 * Output the learned wavelet parameters to the specified file
			 */
			wnn.outputParameters("coeffs.txt");
			/* Mark for garbage collection */
			wnn = null;
		}
		System.gc();
		System.exit(0);
	}
}