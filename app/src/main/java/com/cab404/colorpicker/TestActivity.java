package com.cab404.colorpicker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by cab404 on 22.11.15.
 */
public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ccolorpicker_frame);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, new ColorPickerFragment())
                .commit();

    }
}
