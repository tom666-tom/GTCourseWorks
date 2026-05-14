package edu.gatech.seclass.jobcompare6300;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CompareJobOffersActivity extends AppCompatActivity {

    JobManager jobManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare_job_offers);

        jobManager = JobManager.getInstance(this);

        Button btnMainMenu = findViewById(R.id.btnMainMenu);
        btnMainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CompareJobOffersActivity.this, MainActivity.class);
                startActivity(intent);
                jobManager.clearJobsToCompareAndTheirScores();
            }
        });

        listJobs();
    }

    private void listJobs() {
        LinearLayout jobCardContainer = findViewById(R.id.jobCardContainer);

        JobDatabaseHelper dbHelper = new JobDatabaseHelper(this);
        List<Job> jobsFromDB = dbHelper.getAllJobs();

        if (jobsFromDB == null || jobsFromDB.isEmpty()) {
            Toast.makeText(this, "No jobs to compare", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        jobManager.getJobList().jobList = new ArrayList<>(jobsFromDB);

        HashMap<Float, ArrayList<Job>> mappedJobs = jobManager.getMappedJobs();
        ArrayList<Job> jobsToCompare = jobManager.getJobsToCompare();
        ArrayList<Float> jobsToCompareScores = jobManager.getJobsToCompareScores();

        float[] scoreMap = jobManager.getJobScore().getScoreMap();

        LayoutInflater inflater = LayoutInflater.from(this);

        for (int i=(scoreMap.length-1); i>=0; i--) {
            float score = scoreMap[i];
            ArrayList<Job> jobs = mappedJobs.get(score);

            if (jobs == null) {
                continue;
            }

            for (Job job : jobs) {
                if (job.isCurrentJob() && job.getTitle().isEmpty()) continue; // skip if the current job has not been added
                View jobCard = inflater.inflate(R.layout.job_card, jobCardContainer, false);

                TextView jobTitle = jobCard.findViewById(R.id.jobTitle);
                jobTitle.setText(job.getTitle());

                TextView jobCompany = jobCard.findViewById(R.id.jobCompany);
                jobCompany.setText(job.getCompany());

                TextView jobScore = jobCard.findViewById(R.id.jobScore);
                int scoreTextInt = (int) score;
                String scoreText = Integer.toString(scoreTextInt);
                jobScore.setText(scoreText);

                TextView currentJob = jobCard.findViewById(R.id.currentJob);
                if (job.isCurrentJob()) {
                    currentJob.setVisibility(View.VISIBLE);
                } else {
                    currentJob.setVisibility(View.GONE);
                }

                CheckBox jobCheckbox = jobCard.findViewById(R.id.jobCardCheckbox);
                jobCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (isChecked) {
                        jobsToCompare.add(job);
                        jobsToCompareScores.add(score);
                    } else {
                        jobsToCompare.remove(job);
                        jobsToCompareScores.remove(score);
                    }

                    if (jobsToCompare.size() == 2) {
                        Intent intent = new Intent(this, CompareSelectedJobsActivity.class);
                        startActivity(intent);
                    }
                });

                jobCardContainer.addView(jobCard);
            }
        }
    }
}