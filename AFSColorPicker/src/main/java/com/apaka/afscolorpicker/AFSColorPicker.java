package com.apaka.afscolorpicker;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Point;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextPaint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class AFSColorPicker extends Dialog implements View.OnClickListener, View.OnTouchListener{
    private int DEFAULT_COLOR = Color.parseColor("#FF6600");
    private int DEFAULT_BLACK_COLOR = Color.parseColor("#000000");
    private int DEFAULT_WHITE_COLOR = Color.parseColor("#FFFFFF");
    private int DEFAULT_TRANSPERENT_COLOR = Color.parseColor("#00000000");
    private int COLRO_FORMAT = 0;
    private int SELECTED_COLOR = DEFAULT_COLOR;
    private Context context;
    private View alpha,gradient,picker,preview,gradientLens,alphaLens,pickerLens;
    private ColorChooserListener cclistener;
    private Button confirm,cancel;
    private Point pickerPoints;
    private Point gradientPoints;
    private Point alphaPoints;
    private EditText aCode,rCode,gCode,bCode,hexaCode;
    private TextView rView,gView,bView,dtitle;
    private ImageButton changeCode;

    public AFSColorPicker(Context ctx){
        super(ctx);
        this.context = ctx;
        this.cclistener = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.color_chooser_layout);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(lp);
        initialize();
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        //code here
        //that you can add a flag that you can call windowManager.addView now.
    }
    public void initialize(){

        dtitle = findViewById(R.id.dtitle);
        alpha = findViewById(R.id.alphaChooser);
        gradient = findViewById(R.id.gradientChooser);
        picker = findViewById(R.id.colorPicker);
        preview = findViewById(R.id.colorPreview);
        confirm = findViewById(R.id.confirmChooser);
        cancel = findViewById(R.id.cancelChooser);
        aCode = findViewById(R.id.aCode);
        rCode = findViewById(R.id.rCode);
        gCode = findViewById(R.id.gCode);
        bCode = findViewById(R.id.bCode);
        hexaCode = findViewById(R.id.hexaCode);
        rView = findViewById(R.id.rView);
        gView = findViewById(R.id.gView);
        bView = findViewById(R.id.bView);
        changeCode = findViewById(R.id.changeCode);

        gradientLens = findViewById(R.id.gradientLens);
        alphaLens = findViewById(R.id.alphaLens);
        pickerLens = findViewById(R.id.pickerLens);

        // initialize color palete
        preview.setBackgroundColor(DEFAULT_COLOR);
        setGradientColor(picker,new int[]{
                Color.parseColor("#FA0101"),
                Color.parseColor("#FAFA03"),
                Color.parseColor("#03FF03"),
                Color.parseColor("#01FBFB"),
                Color.parseColor("#0202FA"),
                Color.parseColor("#FB02FB"),
                Color.parseColor("#FF0004"),
        });
        setRadialGradientColor(gradient,new int[]{DEFAULT_WHITE_COLOR, DEFAULT_COLOR});
        setGradientColor(alpha,new int[]{DEFAULT_TRANSPERENT_COLOR,DEFAULT_COLOR,});
        Log.d("HHHHHHH:",gradient.getHeight()+"");


        // to execute loadcontent method after some delay!!!.
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadContents();
            }
        }, 50);
    }

    public void loadContents(){
        pickerPoints = new Point(2, 2);
        gradientPoints = new Point(2,gradient.getHeight()-2);
        alphaPoints = new Point(alpha.getWidth()-2,2);

        // add event to the components|elements
        picker.setOnTouchListener(this);
        gradient.setOnTouchListener(this);
        alpha.setOnTouchListener(this);
        confirm.setOnClickListener(this);
        cancel.setOnClickListener(this);
        changeCode.setOnClickListener(this);

        firstSelectedColor();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.confirmChooser) {
            cclistener.onColorSelected(SELECTED_COLOR);
            dismiss();
        } else if (id == R.id.cancelChooser) {
            cclistener.onColorNotChosen(DEFAULT_COLOR);
            dismiss();
        } else if (id == R.id.changeCode) {
            toggleCodeView();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent ev) {
        // TODO Auto-generated method stub
        int evX = (int) ev.getX();
        int evY = (int) ev.getY();
        if(evX>0 && evY>0) {
            Bitmap imgbmp = captureLayout(v, -1, -1);
            int id = v.getId();
            if (id == R.id.colorPicker) {
                if (evX < imgbmp.getWidth() && evY < imgbmp.getHeight()) {
                    pickerPoints = new Point(evX, 2);
                    cclistener.onPaletteColorChosen(ev, imgbmp.getPixel(evX, 2));
                    firstSelectedColor();
                }
                return true;
            } else if (id == R.id.gradientChooser) {
                if (evX < imgbmp.getWidth() && evY < imgbmp.getHeight()) {
                    gradientPoints = new Point(evX, ((evY < 0) ? 2 : evY));
                    cclistener.onColorChosen(ev, imgbmp.getPixel(evX, evY));
                    secondSelectedColor();
                }
                return true;
            } else if (id == R.id.alphaChooser) {
                if (evX < imgbmp.getWidth() && evY < imgbmp.getHeight()) {
                    alphaPoints = new Point(evX, 2);
                    cclistener.onAlphaChosen(ev, imgbmp.getPixel(evX, 2));
                    finalSelectedColor();
                }
                return true;
            }
            imgbmp.recycle();
        }

        return true;
    }

    private void setGradientColor(View tv, int[] colors){
        GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,colors);
        tv.setBackgroundDrawable(gd);
    }

    private void setRadialGradientColor(View tv, int[] colors){
        GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,colors);
        GradientDrawable gd2 = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,new int[]{DEFAULT_BLACK_COLOR,DEFAULT_TRANSPERENT_COLOR});
        LayerDrawable ld = new LayerDrawable(new Drawable[]{gd,gd2});
        tv.setBackgroundDrawable(ld);
    }

    private Bitmap captureLayout(View v, int w, int h){
        w = ((w<0)?v.getWidth():w);
        h = ((h<0)?v.getHeight():h);
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Drawable bgDrawable = v.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(canvas);

        v.draw(canvas);
        return bitmap;
    }

    private void firstSelectedColor(){
        Bitmap pickerBMP = captureLayout(picker,-1,-1);
        int pickerPXL = pickerBMP.getPixel(pickerPoints.x,pickerPoints.y);
        setRadialGradientColor(gradient, new int[]{DEFAULT_WHITE_COLOR, pickerPXL, });

        int left = (pickerPoints.x - Math.round(pickerLens.getWidth()/2));
        pickerLens.setX(left);

        secondSelectedColor();
        finalSelectedColor();
    }

    private void secondSelectedColor(){
        Bitmap gradientBMP = captureLayout(gradient,-1,-1);
        int gradientPXL = gradientBMP.getPixel(gradientPoints.x, gradientPoints.y);
        setGradientColor(alpha, new int[]{DEFAULT_TRANSPERENT_COLOR, gradientPXL,});

        int left = (gradientPoints.x - Math.round(gradientLens.getWidth()/2));
        int top = (gradientPoints.y - Math.round(gradientLens.getHeight()/2));
        gradientLens.setX(left);
        gradientLens.setY(top);

        finalSelectedColor();
    }

    private void finalSelectedColor(){
        Bitmap alphBMP = captureLayout(alpha,-1,-1);
        int alphaPXL = alphBMP.getPixel(alphaPoints.x,alphaPoints.y);
        preview.setBackgroundColor(alphaPXL);
        SELECTED_COLOR = alphaPXL;

        int a = Color.alpha(alphaPXL);
        int r = Color.red(alphaPXL);
        int g = Color.green(alphaPXL);
        int b = Color.blue(alphaPXL);
        String hex = String.format("#%02x%02x%02x", r, g, b, a);
        float[] hsv = new float[3];
        Color.RGBToHSV(r,g,b, hsv);

        hexaCode.setText(hex);
        aCode.setText(String.valueOf(a));

        int left = (alphaPoints.x - Math.round(alphaLens.getWidth()/2));
        alphaLens.setX(left);

        setRGBandHSVCode();
    }

    private void toggleCodeView(){
        if(COLRO_FORMAT>0)
            COLRO_FORMAT = 0;
        else
            COLRO_FORMAT = 1;

        setRGBandHSVCode();
    }

    private void setRGBandHSVCode(){
        int r = Color.red(SELECTED_COLOR);
        int g = Color.green(SELECTED_COLOR);
        int b = Color.blue(SELECTED_COLOR);
        float[] hsv = new float[3];
        Color.RGBToHSV(r,g,b, hsv);

        if(COLRO_FORMAT==0){
            rView.setText("R");
            gView.setText("G");
            bView.setText("B");

            rCode.setText(String.valueOf(r));
            gCode.setText(String.valueOf(g));
            bCode.setText(String.valueOf(b));
        }
        else if(COLRO_FORMAT==1){
            rView.setText("H");
            gView.setText("S");
            bView.setText("V");

            rCode.setText(String.valueOf(hsv[0]));
            gCode.setText(String.valueOf(hsv[1]));
            bCode.setText(String.valueOf(hsv[2]));
        }
    }

    // setters
    public void setTitle(String str){
        dtitle.setText(str);
    }

    public void setColor(int clr){
        SELECTED_COLOR = clr;
    }

    public void setColorPicker(int clr){
        // for picker
        Bitmap pickerBMP = captureLayout(picker,-1,-1);
        Point xy = getPositionOfColor(pickerBMP,clr);
        int pickerPXL = pickerBMP.getPixel(xy.x,xy.y);
        setRadialGradientColor(gradient, new int[]{DEFAULT_WHITE_COLOR, pickerPXL, });

        int left = (xy.x - Math.round(pickerLens.getWidth()/2));
        pickerLens.setX(left);
    }

    // getters
    public String getTitle(){
        return dtitle.getText().toString();
    }

    public int getDefaultColor(){
        return SELECTED_COLOR;
    }

    public Point getPositionOfColor(Bitmap bitmap,int color_to_find){
        Point xy = new Point(0,0);
        int total_width = bitmap.getWidth();
        int total_height = bitmap.getHeight();
        for (int y = 0; y < total_height; y++) {
            for (int x = 0; x < total_width; x++) {
                int pixel = bitmap.getPixel(x,y);
                //Reading colors
                int redValue = Color.red(pixel);
                int blueValue = Color.blue(pixel);
                int greenValue = Color.green(pixel);
                //finally creating the color for pixel
                int pixel_color = Color.rgb(redValue, blueValue, greenValue);
                if (pixel_color == color_to_find){
                    xy= new Point(x,y);
                    break;
                }
            }
        }
        return xy;
    }

    // custom event listener methodology
    public interface ColorChooserListener{
        // These methods are the different events and need to pass relevant arguments with the event
        public void onColorSelected(int clr);
        public void onAlphaChosen(MotionEvent event, int clr);
        public void onColorChosen(MotionEvent event, int clr);
        public void onPaletteColorChosen(MotionEvent event, int clr);
        public void onColorNotChosen(int clr);
    }

    public void setColorChooseListener(ColorChooserListener listener) {
        this.cclistener = listener;
    }

}
