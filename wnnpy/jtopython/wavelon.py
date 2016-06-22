import math

class Wavelon:

    def __init__(self, translation, dilation):
        self.dilation = dilation
        self.translation = translation


    def  fireGD(self, input):
        u = (input - self.translation) / self.dilation;
        return -u * math.exp(-0.5 * math.pow(u, 2));


    def   derivGD(self, input):
        u = (input - self.translation) / self.dilation
        return math.exp(-0.5 * math.pow(u, 2)) * (math.pow(u, 2) - 1)



    def fireLemarie(self, input):
        u = self.dilation * input - self.translation
        y = 0.0
        if (u >= -1 and u < 0):
            y = 0.5 * math.pow(u + 1, 2)
        elif (u >= 0 and u < 1):
            y = 0.75 - math.pow(u - 0.5, 2)
        elif (u >= 1 and u < 2):
            y = 0.5 * math.pow(u - 2, 2)
        else:
            y = 0.0
        return math.pow(self.dilation, 0.5) * y

