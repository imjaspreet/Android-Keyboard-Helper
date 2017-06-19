package com.github.imjaspreet.keyboardhelper;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import java.lang.ref.WeakReference;

/**
 * Created by jaspreet on 16/06/17.
 */

public class KeyboardHelper {

    private WeakReference<Activity> activityRef;
    private WeakReference<View> rootViewRef;
    private WeakReference<OnKeyboardToggleListener> onKeyboardToggleListenerRef;
    private ViewTreeObserver.OnGlobalLayoutListener viewTreeObserverListener;

    public KeyboardHelper(Activity activity) {
        activityRef = new WeakReference<>(activity);
        initialize();
    }

    public void setListener(OnKeyboardToggleListener onKeyboardToggleListener) {
        onKeyboardToggleListenerRef = new WeakReference<>(onKeyboardToggleListener);
    }

    public void destroy() {
        if (rootViewRef.get() != null)
            if (Build.VERSION.SDK_INT >= 16) {
                rootViewRef.get().getViewTreeObserver().removeOnGlobalLayoutListener(viewTreeObserverListener);
            } else {
                rootViewRef.get().getViewTreeObserver().removeGlobalOnLayoutListener(viewTreeObserverListener);
            }
    }

    private void initialize() {
        if (hasAdjustResizeInputMode()) {
            viewTreeObserverListener = new GlobalLayoutListener();
            rootViewRef = new WeakReference<>(activityRef.get().findViewById(Window.ID_ANDROID_CONTENT));
            rootViewRef.get().getViewTreeObserver().addOnGlobalLayoutListener(viewTreeObserverListener);
        } else {
            throw new IllegalArgumentException(String.format("Activity %s should have windowSoftInputMode=\"adjustResize\"" +
                    "to make KeyboardWatcher working. You can set it in AndroidManifest.xml", activityRef.get().getClass().getSimpleName()));
        }
    }

    private boolean hasAdjustResizeInputMode() {
        return (activityRef.get().getWindow().getAttributes().softInputMode & WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE) != 0;
    }

    private class GlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {
        int initialValue;
        boolean hasSentInitialAction;
        boolean isKeyboardShown;

        @Override
        public void onGlobalLayout() {
            if (initialValue == 0) {
                initialValue = rootViewRef.get().getHeight();
            } else {
                if (initialValue > rootViewRef.get().getHeight()) {
                    if (onKeyboardToggleListenerRef.get() != null) {
                        if (!hasSentInitialAction || !isKeyboardShown) {
                            isKeyboardShown = true;
                            onKeyboardToggleListenerRef.get().onKeyboardShown(initialValue - rootViewRef.get().getHeight());
                        }
                    }
                } else {
                    if (!hasSentInitialAction || isKeyboardShown) {
                        isKeyboardShown = false;
                        rootViewRef.get().post(new Runnable() {
                            @Override
                            public void run() {
                                if (onKeyboardToggleListenerRef.get() != null) {
                                    onKeyboardToggleListenerRef.get().onKeyboardClosed();
                                }
                            }
                        });
                    }
                }
                hasSentInitialAction = true;
            }
        }
    }

    public interface OnKeyboardToggleListener {
        void onKeyboardShown(int keyboardSize);

        void onKeyboardClosed();
    }

    public static void showKeyboard(Activity activity) {
        InputMethodManager inputManager = (InputMethodManager) activity.
                getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputManager != null) {
            inputManager.toggleSoftInput(0, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager inputManager = (InputMethodManager) activity.
                getSystemService(Context.INPUT_METHOD_SERVICE);
        if (activity.getCurrentFocus() != null) {
            inputManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static void hideKeyboard(View view) {
        InputMethodManager inputManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void hideKeyboardForcefully() {
        activityRef.get().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    public void showKeyboardForcefully() {
        activityRef.get().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
}
