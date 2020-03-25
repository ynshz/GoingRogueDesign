package com.example.goingroguedesign.ui.projects.invoice;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.goingroguedesign.R;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;

public class InvoiceImageActivity extends AppCompatActivity {

    String url, name;
    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_image);
        ((AppCompatActivity) InvoiceImageActivity.this).getSupportActionBar().hide();

        TextView tvName = findViewById(R.id.tvName);
        ImageView ivBack = findViewById(R.id.ivBack);
        ImageView ivInvoice = findViewById(R.id.ivInvoice);

        if(getIntent().hasExtra("imageUrl") &&
                getIntent().hasExtra("name")) {
            url = getIntent().getStringExtra("imageUrl");
            name = getIntent().getStringExtra("name");
        }

        tvName.setText(name);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        new downloadImageTask(ivInvoice).execute(url);


    }

    private class downloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        @Override
        protected void onPreExecute() {

            loadingAnimation();
        }

        public downloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urlDisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urlDisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
            dialog.dismiss();
        }
    }

    public void loadingAnimation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(InvoiceImageActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.loading, null));
        builder.setCancelable(false);

        dialog = builder.create();
        dialog.show();
    }
}
