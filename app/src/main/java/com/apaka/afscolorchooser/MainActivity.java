package com.apaka.afscolorchooser;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.apaka.afscolorpicker.AFSColorPicker;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn = findViewById(R.id.btn);
        TextView txt = findViewById(R.id.txt);
        AFSColorPicker apfck = new AFSColorPicker(MainActivity.this);
        apfck.setColor(Color.parseColor("#829384"));
        apfck.setColorChooseListener(new AFSColorPicker.ColorChooserListener() {
            @Override
            public void onColorSelected(int clr) {
                txt.setTextColor(clr);
            }

            @Override
            public void onAlphaChosen(MotionEvent event, int clr) {

            }

            @Override
            public void onColorChosen(MotionEvent event, int clr) {

            }

            @Override
            public void onPaletteColorChosen(MotionEvent event, int clr) {

            }

            @Override
            public void onColorNotChosen(int clr) {

            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                apfck.show();
            }
        });
    }
}