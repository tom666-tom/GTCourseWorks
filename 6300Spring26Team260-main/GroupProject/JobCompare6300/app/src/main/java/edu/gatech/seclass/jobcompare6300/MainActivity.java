package edu.gatech.seclass.jobcompare6300;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.Button;
import android.view.View;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private Button btnCurrentJob;
    private Button btnEnterOffers;
    private Button btnAdjustSettings;
    private Button btnCompareJobs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Initialize buttons
        btnCurrentJob = findViewById(R.id.btnCurrentJob);
        btnEnterOffers = findViewById(R.id.btnEnterOffers);
        btnAdjustSettings = findViewById(R.id.btnAdjustSettings);
        btnCompareJobs = findViewById(R.id.btnCompareJobs);

        // Get JobManager instance
        JobManager jobManager = JobManager.getInstance(getApplicationContext());
        JobList jobList = jobManager.getJobList();
        Job currentJob = jobList.getCurrentJob();

        // Disable "Compare Job Offers" button if one of the conditions are not met:  (1) at least two job offers,
        // in case there is no current job, or (2) at least one job offer, in case there is a current job.
        if (jobList.jobList.size() >= 3 || (jobList.jobList.size() >= 2 && !Objects.equals(currentJob.getTitle(), ""))) {
            btnCompareJobs.setEnabled(true);
        } else {
            btnCompareJobs.setEnabled(false);
        }

        // Set click listeners
        btnCurrentJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CurrentJobActivity.class);
                startActivity(intent);
            }
        });

        btnEnterOffers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, JobOfferActivity.class);
                startActivity(intent);
            }
        });

        btnAdjustSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        btnCompareJobs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CompareJobOffersActivity.class);
                startActivity(intent);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}