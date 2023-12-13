package com.example.android_news_pui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;

import es.upm.hcid.pui.assignment.exceptions.ServerCommunicationError;

public class ArticleDetails extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1; // Request code for picking an image
    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_details);

        imageView = findViewById(R.id.ivImage); // Make sure ivImage is the ID of your ImageView

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    openImagePicker();
                }
            }
        });

        // Assuming Article object is passed via intent
        Article article = (Article) getIntent().getParcelableExtra("ARTICLE");

        if(article != null){
            TextView tvTitle = findViewById(R.id.tvTitle);
            ImageView ivImage = findViewById(R.id.ivImage);
            TextView tvCategory = findViewById(R.id.tvCategory);
            TextView tvAbstract = findViewById(R.id.tvAbstract);
            TextView tvBody = findViewById(R.id.tvBody);

            tvTitle.setText(article.getTitleText());
            tvCategory.setText(article.getCategory());
            tvAbstract.setText(article.getAbstractText());
            tvBody.setText(article.getBodyText());

            // Decode and display the base64 encoded image
            byte[] imageBytes;
            try {
                imageBytes = Base64.decode(article.getImage().getImage(), Base64.DEFAULT);
            } catch (ServerCommunicationError e) {
                throw new RuntimeException(e);
            }
            Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            ivImage.setImageBitmap(decodedImage);
        }
        else{
            Toast.makeText(this, "Article data not available.", Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private void openImagePicker() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_MEDIA_IMAGES}, PICK_IMAGE_REQUEST);
        } else {
            // Permission has already been granted
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();

            // Use a content resolver to get the image
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to load the image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PICK_IMAGE_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        openImagePicker();
                    }
                } else {
                    // Permission denied
                    Toast.makeText(this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
