import pywt
import numpy as np
from fillTrainingFile import testfuncNoise, noiseSigma
from cmppywavelets.pywaveletsdenoise import denoise


funcdata = testfuncNoise()

retfunc = denoise(funcdata[:,1], wavelet = 'db8', noiseSigma=noiseSigma, level=None)

import matplotlib.pyplot as plt

plt.figure(1)
plt.subplot(211)
plt.title('Noisy')
plt.axis([0, 64, -15, 15])
plt.plot(funcdata[:,0], funcdata[:,1])
plt.subplot(212)
plt.title('Reconstructed')
plt.axis([0, 64, -15, 15])
plt.plot(np.arange(0, 64, 0.1), retfunc)
plt.show()
