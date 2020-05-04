#!/usr/bin/env python
# coding: utf-8

# In[1]:


import librosa
audio_path = r'C:\Users\harsh\Downloads\Music\ChillingMusic.wav'
x , sr = librosa.load(audio_path)
print(type(x), type(sr))


# In[2]:


librosa.load(audio_path, sr=44100)


# In[3]:


import IPython.display as ipd
ipd.Audio(r'C:\Users\harsh\Downloads\Music\ChillingMusic.wav')


# In[4]:


get_ipython().run_line_magic('matplotlib', 'inline')
import matplotlib.pyplot as plt
import librosa.display
plt.figure(figsize=(14, 5))
librosa.display.waveplot(x, sr=sr)


# In[5]:


X = librosa.stft(x)
Xdb = librosa.amplitude_to_db(abs(X))
plt.figure(figsize=(14, 5))
librosa.display.specshow(Xdb, sr=sr, x_axis='time', y_axis='hz')
plt.colorbar()


# In[6]:


librosa.display.specshow(Xdb, sr=sr, x_axis='time', y_axis='log')
plt.colorbar()


# In[7]:


librosa.output.write_wav('example.wav', x, sr)


# In[9]:


import numpy as np
sr = 22050 # sample rate
T = 5.0    # seconds
t = np.linspace(0, T, int(T*sr), endpoint=False) # time variable
x = 0.5*np.sin(2*np.pi*220*t)# pure sine wave at 220 Hz

ipd.Audio(x, rate=sr) # load a NumPy array

librosa.output.write_wav('tone_220.wav', x, sr)


# In[11]:


# Load the signal
x, sr = librosa.load(r'C:\Users\harsh\Downloads\Music\ChillingMusic.wav')
#Plot the signal:
plt.figure(figsize=(14, 5))
librosa.display.waveplot(x, sr=sr)


# In[12]:


# Zooming in
n0 = 9000
n1 = 9100
plt.figure(figsize=(14, 5))
plt.plot(x[n0:n1])
plt.grid()


# In[13]:


zero_crossings = librosa.zero_crossings(x[n0:n1], pad=False)
print(sum(zero_crossings))


# In[17]:


spectral_centroids = librosa.feature.spectral_centroid(x, sr=sr)[0]
spectral_centroids.shape


# In[18]:


frames = range(len(spectral_centroids))
t = librosa.frames_to_time(frames)


# In[23]:


import sklearn
def normalize(x, axis=0):
    return sklearn.preprocessing.minmax_scale(x, axis=axis)


# In[24]:


librosa.display.waveplot(x, sr=sr, alpha=0.4)
plt.plot(t, normalize(spectral_centroids), color='r')


# In[25]:


spectral_rolloff = librosa.feature.spectral_rolloff(x+0.01, sr=sr)[0]
librosa.display.waveplot(x, sr=sr, alpha=0.4)
plt.plot(t, normalize(spectral_rolloff), color='r')


# In[28]:


x, fs = librosa.load(r'C:\Users\harsh\Downloads\idk.wav')
librosa.display.waveplot(x, sr=sr)


# In[30]:


mfccs = librosa.feature.mfcc(x, sr=fs)
print (mfccs.shape)


# In[31]:


librosa.display.specshow(mfccs, sr=sr, x_axis='time')


# In[32]:


import sklearn
mfccs = sklearn.preprocessing.scale(mfccs, axis=1)
print(mfccs.mean(axis=1))
print(mfccs.var(axis=1))
librosa.display.specshow(mfccs, sr=sr, x_axis='time')


# In[36]:


x, sr = librosa.load(r'C:\Users\harsh\Downloads\lol.wav')
hop_length = 512
chromagram = librosa.feature.chroma_stft(x, sr=sr, hop_length=hop_length)
plt.figure(figsize=(15, 5))
librosa.display.specshow(chromagram, x_axis='time', y_axis='chroma', hop_length=hop_length, cmap='coolwarm')


# In[ ]:





# In[ ]:




