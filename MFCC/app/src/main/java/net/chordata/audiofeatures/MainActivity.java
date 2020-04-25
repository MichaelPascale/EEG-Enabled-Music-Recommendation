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
        // Create an Android Storage Access Framework client.
        Intent i = new Intent();
        i.setAction(Intent.ACTION_OPEN_DOCUMENT);

        // Select the intent category.
        i.addCategory(Intent.CATEGORY_OPENABLE);

        // Set MIME type of file to look for.
        i.setType("audio/*");

        // Start the dialogue.
        startActivityForResult(i, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            String path = uri.getPath();
            ((TextView) findViewById(R.id.textView5)).setText(path);
            Log.d("MFCC APP INFO", "Made it to line 82");
            try {
                // Open the audio file.
                InputStream audioFileStream = getBaseContext().getContentResolver().openInputStream(uri);
                Log.d("MFCC APP INFO", "Made it to line 86");
                // Create an extractor to get file metadata.
                //MediaExtractor extractor = new MediaExtractor();
                //extractor.setDataSource(this, uri, null);
                //extractor.selectTrack(0);

                // Get the MIME type of the file.
                //MediaFormat format = extractor.getTrackFormat(0);

                // Initialize a codec object for the file type.
                //MediaCodec codec = MediaCodec.createDecoderByType(format.getString(MediaFormat.KEY_MIME));
                //codec.configure(format, null, null, 0);
                //codec.start();

               // codec.getInputBuffer(0);

                //ByteBuffer fileBuffer = new ByteBuffer();
                ///extractor.readSampleData(fileBuffer, 0);

                // Queue onto codec
                // Get from coded
                //File f = new File(path);
                //f.length();
                //FileReader fr = new FileReader(f);
                //fr.read();
                Log.d("MFCC APP INFO", "Made it to line 111");
                /* Begin JavaMP3 */
                //Sound sound = new Sound(new BufferedInputStream(new FileInputStream(f)));
                ContentResolver cr = getApplicationContext().getContentResolver();
                Sound sound = new Sound(new BufferedInputStream(audioFileStream));
                Log.d("SoundInfo", "SOUND INFO" + Integer.toString(sound.getSamplingFrequency()));
                ((TextView) findViewById(R.id.textView11)).setText(Integer.toString(sound.getSamplingFrequency()));
                /* ***** END JavaMP3 ***** */
                //TarsosDSPAudioFormat audioFormat =
                      //  new TarsosDSPAudioFormat(/*rate, ssize, channels, frameSize, frameRate, t/f bigendian*/);
                //AudioEvent audioEvent = new AudioEvent(audioFormat);
               // byte[] bb = audioEvent.getByteBuffer();

                // Run MFCC on buffered data.
                //ArrayList<Byte> bytes = new ArrayList<Byte>();
                ////while ( extractor.readSampleData());
                ////Log.d("********", Integer.toString(extractor.g))
                ////MFCC mfcc = new MFCC(/*sampleFrameSize, rate*/)
                       // be.tarsos.dsp.io.PipeDecoder

                //PipeDecoder decoder = new PipeDecoder();
                //InputStream stream = decoder.getDecodedStream(uri.getPath(), 1, 1, -1);

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
    }

}
