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

import android.app.Activity;
import android.content.ContentResolver;
import android.media.AudioFormat;
import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.net.Uri;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.PipeDecoder;
import be.tarsos.dsp.io.TarsosDSPAudioFormat;
import be.tarsos.dsp.io.TarsosDSPAudioInputStream;
import be.tarsos.dsp.io.UniversalAudioInputStream;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.mfcc.MFCC;

import fr.delthas.javamp3.Sound;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void openButton(View v) {
        try {
            // Open the audio file.
            InputStream audioFileStream = getResources().openRawResource(R.raw.voila);
            ((TextView) findViewById(R.id.textView5)).setText("res/viola.mp3");

            // Import with JavaMP3
            Sound sound = new Sound(new BufferedInputStream(audioFileStream));

            ByteArrayOutputStream outputStream  = new ByteArrayOutputStream();

            int length = sound.decodeFullyInto(outputStream);

            ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

            String info = sound.getSamplingFrequency() + "hz, " + (sound.isStereo() ? 2 : 1) + " channels.";
            ((TextView) findViewById(R.id.textView11)).setText(info);

            // From JavaMP3 getAudioFormat(). We can't use javax classes in Android...
            TarsosDSPAudioFormat audioFormat =
              new TarsosDSPAudioFormat(sound.getSamplingFrequency(), 16, sound.isStereo() ? 2 : 1, true, false);

            //AudioEvent audioEvent = new AudioEvent(audioFormat);
            //byte[] bb = audioEvent.getByteBuffer();
            //ArrayList<Byte> bytes = new ArrayList<Byte>();


            UniversalAudioInputStream stream = new UniversalAudioInputStream(inputStream, audioFormat);

            // Reccomended buffer size from TarsosDSP docs.
            AudioDispatcher d = new AudioDispatcher(stream, 1024, 512);
            MFCC mfcc = new MFCC(1024, sound.getSamplingFrequency());
            d.addAudioProcessor(mfcc);

            ((TextView) findViewById(R.id.textView7)).setText("Processing...");
            d.run();

            float result[] = mfcc.getMFCC();
            float r = mean(result);

            ((TextView) findViewById(R.id.textView7)).setText(Float.toString(r));

            //AudioDispatcher d = AudioDispatcherFactory.fromPipe()
            // AudioProcessor p = new AudioProcessor() {
            //  @Override
            //  public boolean process(AudioEvent aev){
            //     return true;
            //  }

            //   @Override
            //   public void processingFinished() {

            //   }
            //};*/
        } catch (Exception e) {
            Log.d("***ERROR!!!***", e.getMessage());
            ((TextView) findViewById(R.id.textView9)).setText(e.getMessage());
        }
    }
    public static float mean(float[] m) {
        float total = 0;

        for (int i = 0; i < m.length; i++)
            total += m[i];
        return total / m.length;
    }

}
