package net.chordata.audiofeatures;
/*
    Audio Feature Extraction
    Spring 2020 Final Project Starter for COMP.4600 by Michael Pascale.

    Referenced:
    https://developer.android.com/guide/topics/providers/document-provider#addt-resources-samples
    https://developer.android.com/reference/android/media/MediaCodec
    https://developer.android.com/training/basics/intents/result
    https://youtu.be/HDJP0yj04n8
    https://github.com/JorenSix/TarsosDSP
    https://github.com/anhlt25897/AndroidMFCC-DTW
    https://0110.be/tags/TarsosDSP
 */
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.io.TarsosDSPAudioFormat;
import be.tarsos.dsp.io.UniversalAudioInputStream;
import be.tarsos.dsp.mfcc.MFCC;

import fr.delthas.javamp3.Sound;

public class MainActivity extends AppCompatActivity {

    private int n_mfccs = 12;

    private int[] ResourceMP3sToProcess = {
            R.raw.classical01, R.raw.classical02, R.raw.classical03, R.raw.classical04, R.raw.classical05,
            R.raw.classical06, R.raw.classical07, R.raw.classical08, R.raw.classical09, R.raw.classical10,
            R.raw.jazz01, R.raw.jazz02, R.raw.jazz03, R.raw.jazz04, R.raw.jazz05,
            R.raw.jazz06, R.raw.jazz07, R.raw.jazz08, R.raw.jazz09, R.raw.jazz10,
            R.raw.metal01, R.raw.metal02, R.raw.metal03, R.raw.metal04, R.raw.metal05,
            R.raw.metal06, R.raw.metal07, R.raw.metal08, R.raw.metal09, R.raw.metal10,
            R.raw.pop01, R.raw.pop02, R.raw.pop03, R.raw.pop04, R.raw.pop05,
            R.raw.pop06, R.raw.pop07, R.raw.pop08, R.raw.pop09, R.raw.pop10,
            R.raw.rock01, R.raw.rock02, R.raw.rock03, R.raw.rock04, R.raw.rock05,
            R.raw.rock06, R.raw.rock07, R.raw.rock08, R.raw.rock09, R.raw.rock10
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void openButton(View v) {
        new Thread() {
            public void run() {
                for (int track = 0; track < ResourceMP3sToProcess.length; ++track) {
                    try {
                        update_ui_text(R.id.StatusText, "Decoding MP3 File...");

                        // Open the audio file.
                        int resource = ResourceMP3sToProcess[track];
                        update_ui_text(R.id.FileInfo, getResources().getResourceName(resource));
                        InputStream audioFileStream = getResources().openRawResource(resource);

                        // Decode the file with JavaMP3.
                        final Sound sound = new Sound(new BufferedInputStream(audioFileStream));
                        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                        int length = sound.decodeFullyInto(outputStream);
                        int samples = length / 2;

                        // Display some information about the sound.
                        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

                        String info = sound.getSamplingFrequency() + "hz, " + (sound.isStereo() ? 2 : 1) + " channel, " + samples + " samples.";
                        update_ui_text(R.id.SoundInfo, info);

                        // From JavaMP3 getAudioFormat(). We can't use javax classes in Android...
                        TarsosDSPAudioFormat audioFormat =
                                new TarsosDSPAudioFormat(sound.getSamplingFrequency(), 16, sound.isStereo() ? 2 : 1, true, false);

                        // Make an AudioInputStream from the decoded audio stream.
                        UniversalAudioInputStream stream = new UniversalAudioInputStream(inputStream, audioFormat);

                        update_ui_text(R.id.StatusText, "Processing...");
                        // Reccomended buffer size from TarsosDSP docs.
                        AudioDispatcher d = new AudioDispatcher(stream, 2048, 512);

                        /* int samplesPerFrame, float sampleRate, int amountOfCepstrumCoef, int amountOfMelFilters, float lowerFilterFreq, float upperFilterFreq */
                        // 20 coefficients for windows of 2048 samples to match data set.
                        final MFCC mfcc = new MFCC(2048, sound.getSamplingFrequency(), n_mfccs, 35, 100, 9000);

                        //final List<float[]> results = new ArrayList<>();

                        d.addAudioProcessor(new AudioProcessor() {

                            int m = 0;
                            // Create a list for each MFCC. 2-dim array of mfcc x window.
                            List<List<Float>> list_mfccs = new ArrayList<List<Float>>(n_mfccs);

                            @Override
                            public boolean process(AudioEvent a) {
                                update_ui_text(R.id.StatusText, "Processing " + (++m) + "...");
                                if (!mfcc.process(a)) return false;

                                // results.add(mfcc.getMFCC());
                                float[] res = mfcc.getMFCC();

                                // String str = Arrays.toString(res);
                                // str = str.substring(1, str.length()-1);
                                // writeToFile(str +"\n", "mfccs.csv");
                                for (int i = 0; i < n_mfccs; ++i) {
                                    if (i >= list_mfccs.size())
                                        list_mfccs.add(new ArrayList<Float>());

                                    list_mfccs.get(i).add(res[i]);
                                }

                                return true;
                            }

                            @Override
                            public void processingFinished() {
                                String result = new String();
                                String head = new String();
                                for (int i = 0; i < n_mfccs; ++i) {
                                    List<Float> list = list_mfccs.get(i);
                                    Float[] arr = new Float[list.size()];
                                    list.toArray(arr);

                                    double mea = mean(arr);
                                    double std = stdev(arr);
                                    double min = minimum(arr);
                                    double max = maximum(arr);
                                    double skw = skew(arr);
                                    double kur = kurt(arr);

                                    result += (result.isEmpty() ? "" : ",") + mea + "," + std + "," + min + "," + max + "," + skw + "," + kur;
                                    head += (head.isEmpty() ? "" : ",") + "mea" + i + "," + "std" + i + "," + "min" + i + "," + "max"
                                            + i + "," + "skw" + i + "," + "kur" + i;
                                }
                                writeToFile(result + "\n", "mfccs.csv", head);
                                update_ui_text(R.id.MFCCList, result);
                            }
                        });

                        d.run();

                        update_ui_text(R.id.StatusText, "Done.");
                    } catch (Exception e) {
                        update_ui_text(R.id.StatusText, "Error.");
                        e.getStackTrace()[0].getLineNumber();
                        update_ui_text(R.id.ExceptionText, e.getMessage());
                    }
                }
            }
        }.start();


    }

    public void update_ui_text(int res, String msg) {
        final String str = msg;
        final int r = res;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((TextView) findViewById(r)).setText(str);
            }
        });
    }

    public void writeToFile(String toWrite, String filename, String head) {
        File file = new File(getExternalFilesDir(null), filename);
        FileOutputStream os;
        try {
            if (!file.exists()) {
                file.createNewFile();
                toWrite = head+"\n"+toWrite;
            }
            os = new FileOutputStream(file, true);

            os.write(toWrite.getBytes());
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


/***** FEATURES *****/
// Adapted from code provided for COMP.4600.

    public static double sum(Float[] a) {
        double total = 0;
        for (int i = 0; i < a.length; i++)
            total += a[i];
        return total;
    }
    public static double mean(Float[] a) {
        return sum(a) / a.length;
    }

    public static double stdev(Float a[]){
        if(a == null || a.length == 0) return 0.0;
        double s = variance(a);
        s = Math.sqrt(s);
        return s;
    }

    public static double variance(Float a[]){
        if(a == null || a.length == 0) return 0.0;
        int length = a.length;
        double average = 0, s = 0;
        average = mean(a);
        for (int i = 0; i<length; i++)
        {
            s = s + Math.pow(a[i] - average, 2);
        }
        s = s / length;
        return s;
    }
    
    public static double minimum(Float a[]){
        if(a == null || a.length == 0) return 0.0;
        int length = a.length;
        double m = a[0];
        for (int i = 1; i < length; i++)
            m = a[i]<m?a[i]:m;
        return m;
    }

    public static double maximum(Float a[]){
        if(a == null || a.length == 0) return 0.0;

        int length = a.length;
        double m = a[0];
        for (int i = 1; i<length; i++)
            m = a[i]<m ? m : a[i];
        return m;
    }

    public static double skew(Float[] a){
        if(a == null || a.length == 0) return 0.0;
        double mean = mean(a);
        double dev = stdev(a);
        double sum=0;
        for(int i=0;i<a.length;i++)
            sum+= Math.pow((a[i]-mean)/dev,3);
        return sum/a.length;
    }

    public static double kurt(Float[] a){
        if(a == null || a.length == 0) return 0.0;
        double mean = mean(a);
        double dev = stdev(a);
        double sum=0;
        for(int i=0;i<a.length;i++)
            sum+= Math.pow((a[i]-mean)/dev,4);
        return sum/a.length-3;
    }
}
