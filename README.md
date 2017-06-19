# Android-Keyboard-Helper
Software keyboard helper for Android

# Usage
Make sure that you use adjustResize windowSoftInputMode in your Acitivty configuration in AndroidManifest.xml:

```xml
<activity
    android:name=".MainActivity"
    android:windowSoftInputMode="adjustResize" />
```  
Bind KeyboardWatcher in your Activity.onCreate() method. To prevent memory leaks make sure to unbind it in onDestroy() method.
    
