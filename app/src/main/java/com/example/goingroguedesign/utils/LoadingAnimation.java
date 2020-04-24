package com.example.goingroguedesign.utils;

import android.content.Context;
import android.view.LayoutInflater;

import androidx.appcompat.app.AlertDialog;

import com.example.goingroguedesign.R;

public class LoadingAnimation {
    private Context context;
    AlertDialog dialog;

    public LoadingAnimation(Context myContext) {
        context = myContext;
    }

    public void openLoadingAnimation() {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        builder.setView(inflater.inflate(R.layout.loading, null));
        builder.setCancelable(false);

        dialog = builder.create();
        dialog.show();
    }

    public void closeLoadingAnimation() {
        dialog.dismiss();
    }
}
