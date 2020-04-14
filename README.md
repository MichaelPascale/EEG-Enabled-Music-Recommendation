# EEG-Enabled-Music-Recommendation
Electroencephalogram enabled music reccomendation in Android.

Ashwin Nair, Harsh Makwana, Michael Pascale, and Ashwin Jagadeesha.

Final project for COMP.4600 Special Topics in Mobile Sensor Computing at the [University of Massachusetts Lowell](cs.uml.edu).

## Project Starter

**MFCC:** Looked into Spotify API; it does not give us audio to use. There are precomputed features but we will be extracting our own. Made a test app to explore using TarsosDSP to extract Mel Frequency Cepstral Coefficient features from audio files on Android. MFCC processes a Tarsos AudioEvent buffer signalled by AudioDispatcher. Unfortunately the Android version of the library can only read data from the microphone and not from files. Attempted to convert .mp3 to PCM using Android MediaCodec, however this is still not a usable format. Attempted the same with .wav but again Tarsos does not have AudioDispatcherFactory on Android.

**MUSE:**
