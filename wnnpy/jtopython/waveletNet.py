import math
import random
from .wnn import WNN


class WaveletNet(WNN):
    def __init__(self, waveloncount):
            super().__init__(waveloncount)


    def initialise(self, a, b):
        n = int(math.log(self.waveloncount)/math.log(2))
        self.trans_min = a - 0.1 * (b - a)
        self.trans_max = b + 0.1 * (b - a)

        self.dil_min = 0.01 * (b - a)
        
        self.initComplete(a, b, n)
        
        while (self.count < self.waveloncount):
            t_i = a + (b - a) * random.random();
            l_i = 0.5 * (b - a) * math.pow(2, -n);
            self.addWavelon(0.0, t_i, l_i)
            
            
    def initComplete(self,  u, v, level): 
        t_i = 0.5 * (u + v);
        l_i = 0.5 * (v - u);
        self.addWavelon(0.0, t_i, l_i)
        if (level <= 1):
            return
        else:
            self.initComplete(u, t_i, level - 1)
            self.initComplete(t_i, v, level - 1)
        
        

    def learn(self, training, samples, iterations, gamma):
        sum = 0
        for i in range(samples):
            sum += training[i][1];
        self.y_bar = sum / samples;
        for j in range(iterations): 
            for k in range(samples):
                u_k = training[k][0];
                f_u_k = training[k][1];
                e_k = self.run(u_k) - f_u_k
                self.y_bar -= gamma * e_k
                for i in range(self.waveloncount):
                    psi = self.wavelons[i].fireGD(u_k)
                    dpsi_du = self.wavelons[i].derivGD(u_k)
                    w_i = self.weights[i]
                    t_i = self.wavelons[i].translation
                    l_i = self.wavelons[i].dilation
                    dc_dt = gamma * e_k * w_i * math.pow(l_i, -1) * dpsi_du
                    dc_dl = gamma * e_k * w_i * (u_k - t_i) * math.pow(l_i, -2) * dpsi_du
                    self.weights[i] -= gamma * e_k * psi
                    if (t_i + dc_dt < self.trans_min):
                        self.wavelons[i].translation = self.trans_min
                    elif (t_i + dc_dt > self.trans_max):
                        self.wavelons[i].translation = self.trans_max;
                    else:
                        self.wavelons[i].translation = t_i + dc_dt
                    if (l_i + dc_dl < self.dil_min):
                        self.wavelons[i].dilation = self.dil_min
                    else:
                        self.wavelons[i].dilation=l_i + dc_dl                   
            print('waveletNet '+ str(j))
       


    def run(self, input):
        output = self.y_bar
        for i in range(0,self.waveloncount):
            output += self.weights[i] * self.wavelons[i].fireGD(input)
        return output
