package com.github.imjaspreet.keyboardsample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.github.imjaspreet.keyboardhelper.KeyboardHelper;

public class MainActivity extends AppCompatActivity implements KeyboardHelper.OnKeyboardToggleListener{

    private TextView textView;
    private KeyboardHelper keyboardHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.textView);
        keyboardHelper = new KeyboardHelper(this);
        keyboardHelper.setListener(this);
    }



    @Override
    protected void onDestroy() {
        keyboardHelper.destroy();
        super.onDestroy();
    }

    @Override
    public void onKeyboardShown(int keyboardSize) {
        textView.setText(String.format("Shown\nkeyboard size: %dpx", keyboardSize));
    }

    @Override
    public void onKeyboardClosed() {
        textView.setText("Closed");
    }
}
