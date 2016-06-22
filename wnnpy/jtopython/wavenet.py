import math
from .wnn import WNN


class Wavenet(WNN):
    def __init__(self, t_low, t_high):
        super().__init__(t_high - t_low + 1)
        self.t_0 = t_low
        self.t_K = t_high

    def initialise(self, M):
        for t_i in range(self.t_0, self.t_K +1):
            self.addWavelon(0.0, t_i, math.pow(2, M))

    def learn(self, training, samples, iterations, gamma):
        for j in range(iterations):
            for k in range(samples):
                u_k = training[k][0];
                f_u_k = training[k][1];
                e_k = self.run(u_k) - f_u_k
                for i in range(self.waveloncount):
                    phi = self.wavelons[i].fireLemarie(u_k)
                    self.weights[i] -= gamma * e_k * phi
            print('wavenet '+ str(j))

    def run(self, input):
        output = 0.0
        for i in range(0,self.waveloncount):
            output += self.weights[i] * self.wavelons[i].fireLemarie(input)
        return output
