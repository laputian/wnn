import math
import numpy as np
from jtopython.wavenet import Wavenet
from jtopython.waveletNet import WaveletNet
from gaussLemarieV import showplot


def loadTraingData(filename='training.txt'):
    data = np.loadtxt(filename, skiprows=1)
    return data[data[:,0].argsort()]

def dyadic(data, gamma=0.05, learmingIterations=100, dyadicResolution=-2):
    domain_low = data[0,0]
    domain_high = data[-1,0]
    samples = len(data[:,0])
    t_low = int((domain_low - 1) * math.pow(2, dyadicResolution) - 1)
    t_high = int((domain_high + 1) * math.pow(2, dyadicResolution) + 1)
    wnn = Wavenet(t_low, t_high)
    print('t_low='+str(t_low)+ ' - t_high = '+str(t_high))
    wnn.initialise(dyadicResolution)
    wnn.learn(data, samples, learmingIterations, gamma)
    wnn.outputParameters("coeffs_d.txt")
    
def gaussian(data, gamma=0.05, learmingIterations=100, wavelon_nr=16):
    wln = WaveletNet(wavelon_nr)
    samples = len(data[:,0])
    domain_low = data[0,0]
    domain_high = data[-1,0]
    wln.initialise(domain_low, domain_high)
    wln.learn(data, samples, learmingIterations, gamma)
    wln.outputParameters("coeffs_g.txt")
    
def buildAndShow(lemarie=False):
    if lemarie: 
        dyadic(loadTraingData())
    else:
        gaussian(loadTraingData())
    showplot(lemarie) 
    
buildAndShow(lemarie=False)  



