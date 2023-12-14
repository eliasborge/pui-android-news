package com.example.android_news_pui;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Properties;

public class ArticleEdit extends AppCompatActivity {

    private EditText titleEditText, abstractEditText, bodyEditText;
    private Spinner categorySpinner;
    private String selectedCategory;
    private Button sendButton;
    private ImageView imageView;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Article article;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_edit);

        // Assuming Article object is passed via intent
        Article article = (Article) getIntent().getParcelableExtra("ARTICLE");

        if (article !=null){
            titleEditText = findViewById(R.id.TitleInput);
            abstractEditText = findViewById(R.id.AbstractInput);
            bodyEditText = findViewById(R.id.BodyInput);
            categorySpinner = findViewById(R.id.CategorySpinner);
            sendButton = findViewById(R.id.SendButton);
            populateSpinner();

            imageView = findViewById(R.id.ivImage);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { openImagePicker();}
                }
            });

            // Listener for spinner item selection
            categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    selectedCategory = parentView.getItemAtPosition(position).toString();
                }
                @Override
                public void onNothingSelected(AdapterView<?> parentView) {} // Do nothing
            });

            sendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {saveData();}
            });
        }else{ // article == null
            Toast.makeText(this, "Article data not available.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void populateSpinner() {
        // Define an array of categories
        String[] categories = {"Technology", "Sports", "Economy"};
        // Create an ArrayAdapter using the array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter); // Apply the adapter to the spinner
    }

    private void saveData() {
        article.setTitleText(titleEditText.getText().toString());
        article.setAbstractText(abstractEditText.getText().toString());
        article.setBodyText(bodyEditText.getText().toString());
        article.setCategory(selectedCategory);

        Toast.makeText(this, "Saving Data", Toast.LENGTH_LONG).show();

        //save the data to the API
        Properties prop = new Properties();
        prop.setProperty(ModelManager.ATTR_LOGIN_USER, "DEV_TEAM_03");
        prop.setProperty(ModelManager.ATTR_LOGIN_PASS, "123703@3");
        prop.setProperty(
                ModelManager.ATTR_SERVICE_URL,
                "https://sanger.dia.fi.upm.es/pui-rest-news/"
        );

        try {
            ModelManager modman = new ModelManager(prop);
            modman.save(article);
        } catch (es.upm.hcid.pui.assignment.exceptions.AuthenticationError e) {
            Toast.makeText(this, "Authentication Error.", Toast.LENGTH_SHORT).show();
            finish();
        } catch (es.upm.hcid.pui.assignment.exceptions.ServerCommunicationError e) {
            Toast.makeText(this, "Server Communication Error.", Toast.LENGTH_SHORT).show();
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
}