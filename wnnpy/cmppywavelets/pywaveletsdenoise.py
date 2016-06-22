import numpy as np
import pywt
import math

def denoise(data, wavelet, noiseSigma, level):
    size = len(data)
    coeffs = pywt.wavedec(data, wavelet=wavelet, level=level)
    threshold =  noiseSigma * math.sqrt(2 * math.log2(size))
    rec_coeffs = coeffs
    rec_coeffs[1:] = (pywt.threshold(i, value=threshold, mode="soft") for i in rec_coeffs[1:])
    return pywt.waverec(rec_coeffs, wavelet)
