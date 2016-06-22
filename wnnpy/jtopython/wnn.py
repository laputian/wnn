from .wavelon import Wavelon
import sys

class WNN:
    def __init__(self, waveloncount):
        self.waveloncount = waveloncount
        self.wavelons = []
        self.weights = []
        self.count = 0
        self.y_bar = 0.0

    def addWavelon(self, w, t, l):
        if self.count < self.waveloncount:
            self.wavelons.append(Wavelon(t, l))
            self.weights.append(w)
            self.count += 1
        else:
            print("Number of wavelons has been exceeded!")
            sys.exit(1)

    def getDilations(self):
        dils = []
        for i in range(self.waveloncount):
            dils.append(self.wavelons[i].dilation)
        return dils

    def getTranslations(self):
        trans = []
        for j in range(self.waveloncount):
            trans.append(self.wavelons[j].translation)
        return trans

    def outputParameters(self, filename='coeffs.txt'):
        out_file = open(filename, "w")
        out_file.write(str(self.y_bar) + "\n")
        translations = self.getTranslations()
        dilations = self.getDilations()
        for i in range(self.waveloncount):
            out_file.write(str(self.weights[i]) + " " + str(translations[i]) + " " + str(dilations[i])+ "\n")
        out_file.close()



