package com.example.android_news_pui;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class ArticleCreate extends AppCompatActivity {

    private EditText titleEditText, abstractEditText, bodyEditText;
    private Spinner categorySpinner;
    private String selectedCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_create);

        titleEditText = findViewById(R.id.TitleInput);
        abstractEditText = findViewById(R.id.AbstractInput);
        bodyEditText = findViewById(R.id.BodyInput);
        categorySpinner = findViewById(R.id.CategorySpinner);

        populateSpinner();

        // Set a listener for spinner item selection
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedCategory = parentView.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing
            }
        });
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

    // Method to save the inputed data (you can call this method when a button is clicked, for example)
    private void saveData() {
        String title = titleEditText.getText().toString();
        String abstractText = abstractEditText.getText().toString();
        String body = bodyEditText.getText().toString();

        // Now you can use 'title', 'abstractText', 'body', and 'selectedCategory' as needed
        // For example, you can pass them to another activity or perform any other actions.

        // Display a Toast message (you can replace this with your actual logic)
        Toast.makeText(this, "Data Saved:\nTitle: " + title + "\nAbstract: " + abstractText +
                "\nBody: " + body + "\nCategory: " + selectedCategory, Toast.LENGTH_LONG).show();
    }
}