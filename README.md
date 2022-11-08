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
	// alpha version
	implementation 'com.github.AkashFulari:AFSColorChooser:1.0'
	// earlier updated
	implementation 'com.github.AkashFulari:AFSColorChooser:1.0.0'
	// letest updated
	implementation 'com.github.AkashFulari:AFSColorChooser:2.0.0'
}
```

> Step 3. Create layout file
```
<?xml version="1.0" encoding="utf-8"?>
<androidx.coordinatorlayout.widget.CoordinatorLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">
    <LinearLayout
        android:gravity="center_vertical"
        android:orientation="horizontal"
        android:padding="20dp"
        android:layout_width="match_parent"
        android:layout_height="wrap_content">
        <TextView
            android:id="@+id/txt"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:textSize="15dp"
            android:textStyle="bold"
            android:text="COLOR SELECT"/>
        <Button
            android:id="@+id/btn"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="COLOR CHOOSER"
            android:layout_marginLeft="15dp"/>
    </LinearLayout>

</androidx.coordinatorlayout.widget.CoordinatorLayout>
```

> Step 4. Use into your Activity 
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
