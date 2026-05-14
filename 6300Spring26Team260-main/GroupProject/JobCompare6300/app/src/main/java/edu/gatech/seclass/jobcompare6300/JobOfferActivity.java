package edu.gatech.seclass.jobcompare6300;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class JobOfferActivity extends AppCompatActivity {

    private EditText jobTitle, jobCompany, jobLocation, costOfLiving, yearlySalary, yearlyBonus,
            stockOptions, wellnessStipend, lifeInsurance, personalDevFund;
    private Button btnSave, btnCancel, btnAddAnother, btnCompareWithCurrent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job);

        // Initialize views
        initializeViews();

        // Gray out button if there is no current job offer
        ArrayList<Job> jobList = JobManager.getInstance(this).getJobList().jobList;
        boolean currentJobExists = JobManager.getInstance(this).getJobList().getCurrentJob() != null;


        if (!currentJobExists || (currentJobExists && jobList.size() == 1)) {
            btnCompareWithCurrent.setEnabled(false);
        }

        // Set click listeners
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInputs()) {
                    saveJob();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(JobOfferActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        btnAddAnother.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearFields();
            }
        });

        btnCompareWithCurrent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                JobManager.getInstance(JobOfferActivity.this).getMappedJobs();
                float[] scores = JobManager.getInstance(JobOfferActivity.this).getJobScore().getScoreMap();


                ArrayList<Job> jobList = JobManager.getInstance(JobOfferActivity.this).getJobList().jobList;
                Job newOffer = jobList.get(jobList.size() - 1);

                JobManager jobManager = JobManager.getInstance(JobOfferActivity.this);
                Job currentJob = jobManager.getJobList().getCurrentJob();

                // clear existing jobs to compare
                jobManager.getJobsToCompare().clear();
                jobManager.getJobsToCompareScores().clear();

                // Add the two specific jobs
                jobManager.getJobsToCompare().add(currentJob);
                jobManager.getJobsToCompare().add(newOffer);

                // grab scores from scores list and add to jobsToCompare
                jobManager.getJobsToCompareScores().add(scores[jobManager.getJobList().getCurrentJobIndex()]);
                jobManager.getJobsToCompareScores().add(scores[scores.length - 1]);

                // go to compare selected jobs page
                Intent intent = new Intent(JobOfferActivity.this, CompareSelectedJobsActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void initializeViews() {
        jobTitle = findViewById(R.id.jobTitle);
        jobCompany = findViewById(R.id.jobCompany);
        jobLocation = findViewById(R.id.jobLocation);
        costOfLiving = findViewById(R.id.costOfLiving);
        yearlySalary = findViewById(R.id.yearlySalary);
        yearlyBonus = findViewById(R.id.yearlyBonus);
        stockOptions = findViewById(R.id.stockOptions);
        wellnessStipend = findViewById(R.id.wellnessStipend);
        lifeInsurance = findViewById(R.id.lifeInsurance);
        personalDevFund = findViewById(R.id.personalDevFund);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);
        btnAddAnother = findViewById(R.id.btnAddAnother);
        btnCompareWithCurrent = findViewById(R.id.btnCompareWithCurrent);
    }

    private void clearFields() {
        jobTitle.setText("");
        jobCompany.setText("");
        jobLocation.setText("");
        costOfLiving.setText("");
        yearlySalary.setText("");
        yearlyBonus.setText("");
        stockOptions.setText("");
        wellnessStipend.setText("");
        lifeInsurance.setText("");
        personalDevFund.setText("");
        jobTitle.requestFocus();
    }

    private boolean validateInputs() {
        // Check if all fields are filled
        if (jobTitle.getText().toString().trim().isEmpty() ||
                jobCompany.getText().toString().trim().isEmpty() ||
                jobLocation.getText().toString().trim().isEmpty() ||
                costOfLiving.getText().toString().trim().isEmpty() ||
                yearlySalary.getText().toString().trim().isEmpty() ||
                yearlyBonus.getText().toString().trim().isEmpty() ||
                stockOptions.getText().toString().trim().isEmpty() ||
                wellnessStipend.getText().toString().trim().isEmpty() ||
                lifeInsurance.getText().toString().trim().isEmpty() ||
                personalDevFund.getText().toString().trim().isEmpty()) {

            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Validate numeric ranges
        try {
            float wellnessStipendValue = Float.parseFloat(wellnessStipend.getText().toString());
            int lifeInsuranceValue = Integer.parseInt(lifeInsurance.getText().toString());
            float personalDevFundValue = Float.parseFloat(personalDevFund.getText().toString());

            if (wellnessStipendValue < 0 || wellnessStipendValue > 1200) {
                Toast.makeText(this, "Wellness Stipend must be between $0 and $1200", Toast.LENGTH_SHORT).show();
                return false;
            }

            if (lifeInsuranceValue < 0 || lifeInsuranceValue > 10) {
                Toast.makeText(this, "Life Insurance must be between 0% and 10%", Toast.LENGTH_SHORT).show();
                return false;
            }

            if (personalDevFundValue < 0 || personalDevFundValue > 6000) {
                Toast.makeText(this, "Personal Development Fund must be between $0 and $6000", Toast.LENGTH_SHORT).show();
                return false;
            }

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter valid numbers", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void saveJob() {
        // Create Job object
        Job jobOffer = new Job(
                jobTitle.getText().toString().trim(),
                jobCompany.getText().toString().trim(),
                jobLocation.getText().toString().trim(),
                Integer.parseInt(costOfLiving.getText().toString()),
                Float.parseFloat(yearlySalary.getText().toString()),
                Float.parseFloat(yearlyBonus.getText().toString()),
                Integer.parseInt(stockOptions.getText().toString()),
                Float.parseFloat(wellnessStipend.getText().toString()),
                Integer.parseInt(lifeInsurance.getText().toString()),
                Float.parseFloat(personalDevFund.getText().toString())
        );

        JobManager jobManager = JobManager.getInstance(this);
        jobManager.addJob(jobOffer);

        Toast.makeText(this, "Job offer saved successfully", Toast.LENGTH_SHORT).show();
    }
}
