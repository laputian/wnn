package waveletNN;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

public class FillTrainingFile {
	
	private static Random r = new Random();
	private static double stdev = 1.5;

	public static void main(String[] args) throws IOException {
		int i;
		int numSamples = 640;
		PrintWriter fileOutput = new PrintWriter(new FileWriter("data/training.txt"));
		fileOutput.println(numSamples);
		for (i = 0; i < numSamples; i++) {
			double x = 64 *  r.nextDouble();
			fileOutput.println(Double.toString(x)+" "+Double.toString(testDataFunc(x)));
		}
		fileOutput.close();
		
	}
	
	private static double testDataFunc(double x){
		
		return 10*Math.sin(2*Math.PI*x/32) + r.nextGaussian()*stdev;
	}
	
private static double testDataFunc1(double x){
		
	int i = (int) x;
	if (i*5 % 23 > 3)
		return 10.0;
	else if (i*4 %3 ==1 )
		return -2.0;
	else
		return 10*Math.sin(x/4); 
				
	}

private static double testDataFunc2(double x){
	
	double noise =  r.nextGaussian()*stdev;
	if (x > 35)
		return 5.0 + noise;
	else if (x <20)
		return -3+ + noise;
	else
		return 9 + noise; 
				
	}
}