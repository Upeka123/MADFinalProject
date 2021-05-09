package com.example.expensetrackerforuniversitystudent.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

//import com.example.expensetrackerforuniversitystudent.activity.Main1Activity;

import com.example.expensetrackerforuniversitystudent.R;
import com.example.expensetrackerforuniversitystudent.adapter.CategoryAdapter;

import static com.example.expensetrackerforuniversitystudent.SplashActivity.catList;


public class CategoryActivity extends AppCompatActivity {
    GridView categorGridView;
    Toolbar toolbar;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("");




        categorGridView = findViewById(R.id.categoryview);
        CategoryAdapter adapter = new CategoryAdapter(catList);
        categorGridView.setAdapter(adapter);
        Log.d("listdata", "onCreate: ");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.share:
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, "Download The " + getString(R.string.app_name) + "* App : https://play.google.com/store/apps/details?id=" + getPackageName());
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                startActivity(Intent.createChooser(sharingIntent, "Share using"));

                break;

            case R.id.policy:
                startActivity(new Intent(CategoryActivity.this, Main1Activity.class));


                Toast.makeText(this, "Add Privacy Policy URL", Toast.LENGTH_SHORT).show();

                break;

            case R.id.rateus:
                try {
                    Uri marketUri = Uri.parse("market://details?id=" + getPackageName());
                    Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
                    startActivity(marketIntent);
                } catch (ActivityNotFoundException e) {
                    Uri marketUri = Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName());
                    Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
                    startActivity(marketIntent);
                }

                break;


        }

        return super.onOptionsItemSelected(item);
    }
}