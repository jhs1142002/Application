package com.example.kakao0;

import androidx.fragment.app.FragmentActivity;

import com.example.kakao0.widget.WaitingDialog;

public abstract class BaseActivity extends FragmentActivity {
    protected void showWaitingDialog() {
        WaitingDialog.showWaitingDialog(this);
    }

    protected void cancelWaitingDialog() {
        WaitingDialog.cancelWaitingDialog();
    }

}
