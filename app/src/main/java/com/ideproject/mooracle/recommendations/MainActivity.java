package com.ideproject.mooracle.recommendations;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    //field for views
    private RecyclerView recyclerView;
    private View progressBar;
    private TextView errorText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //prepares views by ids
        recyclerView = findViewById(R.id.recycler_view);
        progressBar = findViewById(R.id.progress_bar);
        errorText = findViewById(R.id.error_text);

        showLoading();
    }

    private void showLoading(){
        recyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        errorText.setVisibility(View.GONE);
    }

    private void showList(){
        recyclerView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        errorText.setVisibility(View.GONE);
    }

    private void showError(){
        recyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        errorText.setVisibility(View.VISIBLE);
    }
}
