package org.odk.collect.android.activities.engineclass;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.jsibbold.zoomage.ZoomageView;

import org.odk.collect.android.R;

public class pruebazoom extends AppCompatActivity {
    private static final String TAG = pruebazoom.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.zoomprueba);
        ZoomageView imageZoom = (ZoomageView)findViewById(R.id.myZoomageView);
        //imageZoom.setImageBitmap();
    }
}
