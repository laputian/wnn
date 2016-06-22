import numpy as np


def testfunc(x):
    return 10 * np.sin(2 * np.pi * x / 32)

def gaussian():
    return np.random.normal(0.0, 2)

def save_records(max , filename='training.txt'):
    out_file = open(filename,"w")
    for _ in range(max*10 + 1):
        x = max * np.random.rand()
        out_file.write(str(x)+" "+str(testfunc(x)+gaussian())+"\n")
    out_file.close()

save_records(64)

def testfuncNoise(filename='training.txt'):
    data = np.loadtxt(filename, skiprows=1)
    return data[data[:,0].argsort()]

import matplotlib.pyplot as plt

def plot():
    u = np.arange(0, 64, 0.1)
    plt.figure(1)
    plt.axis([0, 64, -15, 15])
    data=testfuncNoise()
    plt.plot(data[:,0], data[:,1])
    plt.show()


plot()
