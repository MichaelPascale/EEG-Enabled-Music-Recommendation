# EEG-Enabled-Music-Recommendation
Electroencephalogram enabled music reccomendation in Android.

Ashwin Nair, Harsh Makwana, Michael Pascale, and Ashwin Jagadeesha.

Final project for COMP.4600 Special Topics in Mobile Sensor Computing at the [University of Massachusetts Lowell](cs.uml.edu).

## Project Checkpoints

**MFCC:**
Looked into Spotify API; it does not give us audio to use. There are precomputed features but we will be extracting our own. Made a test app to explore using TarsosDSP to extract Mel Frequency Cepstral Coefficient features from audio files on Android. MFCC processes a Tarsos AudioEvent buffer signalled by AudioDispatcher. Unfortunately the Android version of the library can only read data from the microphone and not from files. Attempted to convert .mp3 to PCM using Android MediaCodec, however this is still not a usable format. Attempted the same with .wav but again Tarsos does not have AudioDispatcherFactory on Android.

4/24 - Switched to JavaMP3. Successfully opens document selector intent, loads audio file, and decodes to raw buffer array so that MFCC may be run.

**MUSE:**
Successfully connected Muse with bluetooth to get stable muse values TP9,AF7,AF8,TP10. Saved these values into CSV.Currently pursuing classification of these values into different emotions. Also trying to evaluate different other features of these values to generate a better feature set for emotion detection.

**LIBROSA:**
Have used librosa in python to get the zero crossing rate; spectral centroid, which indicates where the mass of the sound is located; spectral roll-off, which is frequency below which a specified percentage of the total spectral energy; and calculated MFCCs of the audio signal. Have used PyAudio to get the audio files.
