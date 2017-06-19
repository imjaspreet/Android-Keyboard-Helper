# Android-Keyboard-Helper
Software keyboard helper for Android

## Usage
Make sure that you use adjustResize windowSoftInputMode in your Acitivty configuration in AndroidManifest.xml:

```xml
<activity
    android:name=".MainActivity"
    android:windowSoftInputMode="adjustResize" />
```  
Bind KeyboardHelper in your Activity.onCreate() method. To prevent memory leaks make sure to unbind it in onDestroy() method.
    
```java    
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
```
## Download

### Library dependency

```gradle
dependencies {
  compile 'com.github.imjaspreet.keyboardhelper:keyboardhelper:1.0.0'
}
```

## Thanks to
-https://github.com/AzimoLabs

## License

    Copyright (C) 2016 Azimo

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

