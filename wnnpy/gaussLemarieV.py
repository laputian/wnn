import numpy as np

def gaussFunc(u, filename='coeffs_g.txt', mean=0.0):
    coeffs = np.loadtxt(filename,skiprows=1)
    w = coeffs[:,0]
    t = coeffs[:,1]
    l= coeffs[:,2]
    wavelonCount = len(w)
    m = len(u)
    y = np.zeros(u.shape)
    for i in range(len(u)):
        y[i]=mean
        for j in range(wavelonCount):
            x = (u[i] - t[j]) / l[j]
            y[i] = y[i] + w[j] * -x * np.exp(-0.5*x**2)
    return y

def lemarieFunc(u,filename='coeffs_d.txt'):
    coeffs = np.loadtxt(filename,skiprows=1)
    w = coeffs[:,0]
    t = coeffs[:,1]
    l= coeffs[:,2]
    wavelonCount = len(w)
    m = len(u)
    y = np.zeros(u.shape)
    for i in range(len(u)):
        for j in range(wavelonCount):
            x = l[j]*u[i] - t[j]
            if x>=-1 and x<0:
                y[i] = y[i] + w[j] * 0.5*(x+1)**2
            elif x>=0 and x<1:
                y[i] = y[i] + w[j] * (0.75 - (x-0.5)**2)
            elif x>=1 and x<2:
                y[i] = y[i] + w[j] * 0.5*(x-2)**2
    return y


def testfuncNoise(filename='training.txt'):
    data = np.loadtxt(filename, skiprows=1)
    return data[data[:,0].argsort()]

import matplotlib.pyplot as plt

def plot(lemarie=False):
    u = np.arange(0, 64, 0.1)
    if lemarie:
        title="Lemarie"
        func = lemarieFunc
    else:
        title = "Gauss"
        func = gaussFunc
    plt.figure(1)
    plt.suptitle(title)
    plt.subplot(211)
    plt.title('Noisy')
    plt.axis([0, 64, -15, 15])
    data=testfuncNoise()
    plt.plot(data[:,0], data[:,1])

    plt.subplot(212)
    plt.title('Reconstructed')
    plt.axis([0, 64, -20, 20])
    plt.plot(u, func(u))
    plt.show()

def showplot(lemarie=False):
	plot(lemarie)
	
#showplot(True)
	
