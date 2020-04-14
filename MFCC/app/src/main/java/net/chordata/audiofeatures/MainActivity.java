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
import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.net.Uri;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import be.tarsos.dsp.io.PipeDecoder;
import be.tarsos.dsp.mfcc.MFCC;

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
            ((TextView) findViewById(R.id.textView5)).setText(uri.toString());
            String path = uri.getPath();

            try {
                // Open the audio file.
                InputStream audioFileStream = getBaseContext().getContentResolver().openInputStream(uri);

                // Create an extractor to get file metadata.
                MediaExtractor extractor = new MediaExtractor();
                extractor.setDataSource(this, uri, null);
                extractor.selectTrack(0);

                // Get the MIME type of the file.
                MediaFormat format = extractor.getTrackFormat(0);

                // Initialize a codec object for the file type.
                MediaCodec codec = MediaCodec.createDecoderByType(format.getString(MediaFormat.KEY_MIME));
                codec.configure(format, null, null, 0);
                codec.start();

                ByteBuffer fileBuffer = new ByteBuffer();
                extractor.readSampleData(fileBuffer, 0);

                // Queue onto codec
                // Get from coded
                // Run mfcc on buffered data.

                MFCC mfcc = new MFCC(/*sampleFrameSize, rate*/)
            } catch (Exception e) {

            }
        }
    }

}
