import math
import random
import numpy as np

noiseSigma = 1.0


def testfunc(x):
    return 10 * math.sin(2 * math.pi * x / 32)

def gaussian(noiseSigma = noiseSigma):
    return np.random.normal(0.0, noiseSigma)

def save_records(max , filename='training.txt'):
    out_file = open(filename,"w")
    for _ in range(max*10 + 1):
        x = random.randrange(0, max)
        out_file.write(str(x)+" "+str(testfunc(x)+gaussian())+"\n")
    out_file.close()

save_records(64)

def testfuncNoise(filename='training.txt'):
    data = np.loadtxt(filename, skiprows=1)
    return data[data[:,0].argsort()]

import matplotlib.pyplot as plt

def plotit():
    u = np.arange(0, 64, 0.1)
    plt.figure(1)
    plt.axis([0, 64, -15, 15])
    data=testfuncNoise()
    plt.plot(data[:,0], data[:,1])
    plt.show()


#plotit()
