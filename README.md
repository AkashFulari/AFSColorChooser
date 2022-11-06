# AFSColorChooser
Android Color Chooser Library

> Step 1. Add the JitPack repository to your build file
  Add it in your root build.gradle at the end of repositories:
```
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```
> Step 2. Add the dependency
```
dependencies {
	implementation 'com.github.AkashFulari:AFSColorChooser:1.0.0'
}
```

> Step 3. Use in your program
```
...
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn = findViewById(R.id.btn);
        TextView txt = findViewById(R.id.txt);
        AFSColorPicker apfck = new AFSColorPicker(MainActivity.this);
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
```
