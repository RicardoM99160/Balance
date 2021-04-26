package com.domos.balance;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Window;

public class CustomYesNoDialog extends Dialog  {
    public CustomYesNoDialog(Activity a) {
        super(a);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }


}
