package edu.gatech.seclass.jobcompare6300;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class CurrentJobActivity extends AppCompatActivity {

    private EditText currentJobTitle, currentJobCompany, currentJobLocation, currentJobCostOfLiving,
            currentJobYearlySalary, currentJobYearlyBonus, currentJobStockOptions,
            currentJobWellnessStipend, currentJobLifeInsurance, currentJobPersonalDevFund;
    private Button currentJobSave, currentJobCancel;
    private Job currentJob;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_job);

        // Initialize views
        initializeViews();

        JobManager jobManager = JobManager.getInstance(this);
        JobList jobList = jobManager.getJobList();
        currentJob = jobList.getCurrentJob();

        // Load existing data if available
        loadCurrentJobData();

        // Set click listeners
        currentJobSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInputs()) {
                    saveCurrentJob();

                    Intent intent = new Intent(CurrentJobActivity.this, MainActivity.class);
                    startActivity(intent);
                }

            }
        });

        currentJobCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CurrentJobActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initializeViews() {
        currentJobTitle = findViewById(R.id.currentJobTitle);
        currentJobCompany = findViewById(R.id.currentJobCompany);
        currentJobLocation = findViewById(R.id.currentJobLocation);
        currentJobCostOfLiving = findViewById(R.id.currentJobCostOfLiving);
        currentJobYearlySalary = findViewById(R.id.currentJobYearlySalary);
        currentJobYearlyBonus = findViewById(R.id.currentJobYearlyBonus);
        currentJobStockOptions = findViewById(R.id.currentJobStockOptions);
        currentJobWellnessStipend = findViewById(R.id.currentJobWellnessStipend);
        currentJobLifeInsurance = findViewById(R.id.currentJobLifeInsurance);
        currentJobPersonalDevFund = findViewById(R.id.currentJobPersonalDevFund);
        currentJobSave = findViewById(R.id.currentJobSave);
        currentJobCancel = findViewById(R.id.currentJobCancel);
    }

    private void loadCurrentJobData() {
        if (this.currentJob != null) {
            currentJobTitle.setText(currentJob.getTitle());
            currentJobCompany.setText(currentJob.getCompany());
            currentJobLocation.setText(currentJob.getLocation());
            if (currentJob.getCostOfLiving() > 0) {
                currentJobCostOfLiving.setText(String.valueOf(currentJob.getCostOfLiving()));
            }
            if (currentJob.getYearlySalary() > 0) {
                currentJobYearlySalary.setText(String.format("%.2f", currentJob.getYearlySalary()));
            }
            if (currentJob.getYearlyBonus() > 0) {
                currentJobYearlyBonus.setText(String.format("%.2f", currentJob.getYearlyBonus()));
            }
            if (currentJob.getStockOptionShares() > 0) {
                currentJobStockOptions.setText(String.valueOf(currentJob.getStockOptionShares()));
            }
            if (currentJob.getWellnessStipend() > 0) {
                currentJobWellnessStipend.setText(String.format("%.2f", currentJob.getWellnessStipend()));
            }
            if (currentJob.getLifeInsurance() > 0) {
                currentJobLifeInsurance.setText(String.valueOf(currentJob.getLifeInsurance()));
            }
            if (currentJob.getPersonalDevFund() > 0) {
                currentJobPersonalDevFund.setText(String.format("%.2f", currentJob.getPersonalDevFund()));
            }
        }
    }

    private boolean validateInputs() {
        // Check if all fields are filled
        if (currentJobTitle.getText().toString().trim().isEmpty() ||
                currentJobCompany.getText().toString().trim().isEmpty() ||
                currentJobLocation.getText().toString().trim().isEmpty() ||
                currentJobCostOfLiving.getText().toString().trim().isEmpty() ||
                currentJobYearlySalary.getText().toString().trim().isEmpty() ||
                currentJobYearlyBonus.getText().toString().trim().isEmpty() ||
                currentJobStockOptions.getText().toString().trim().isEmpty() ||
                currentJobWellnessStipend.getText().toString().trim().isEmpty() ||
                currentJobLifeInsurance.getText().toString().trim().isEmpty() ||
                currentJobPersonalDevFund.getText().toString().trim().isEmpty()) {

            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Validate numeric ranges
        try {
            float wellnessStipend = Float.parseFloat(currentJobWellnessStipend.getText().toString());
            int lifeInsurance = Integer.parseInt(currentJobLifeInsurance.getText().toString());
            float personalDevFund = Float.parseFloat(currentJobPersonalDevFund.getText().toString());

            if (wellnessStipend < 0 || wellnessStipend > 1200) {
                Toast.makeText(this, "Wellness Stipend must be between $0 and $1200", Toast.LENGTH_SHORT).show();
                return false;
            }

            if (lifeInsurance < 0 || lifeInsurance > 10) {
                Toast.makeText(this, "Life Insurance must be between 0% and 10%", Toast.LENGTH_SHORT).show();
                return false;
            }

            if (personalDevFund < 0 || personalDevFund > 6000) {
                Toast.makeText(this, "Personal Development Fund must be between $0 and $6000", Toast.LENGTH_SHORT).show();
                return false;
            }

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter valid numbers", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void saveCurrentJob() {

        //If inputs are validated
        if (validateInputs()) {
            if (this.currentJob != null) {
                currentJob.setTitle(currentJobTitle.getText().toString().trim());
                currentJob.setCompany(currentJobCompany.getText().toString().trim());
                currentJob.setLocation(currentJobLocation.getText().toString().trim());
                currentJob.setCostOfLiving(Integer.parseInt(currentJobCostOfLiving.getText().toString().trim()));
                currentJob.setYearlySalary(Float.parseFloat(currentJobYearlySalary.getText().toString().trim()));
                currentJob.setYearlyBonus(Float.parseFloat(currentJobYearlyBonus.getText().toString().trim()));
                currentJob.setStockOptionShares(Integer.parseInt(currentJobStockOptions.getText().toString().trim()));
                currentJob.setWellnessStipend(Float.parseFloat(currentJobWellnessStipend.getText().toString().trim()));
                currentJob.setLifeInsurance(Integer.parseInt(currentJobLifeInsurance.getText().toString().trim()));
                currentJob.setPersonalDevFund(Float.parseFloat(currentJobPersonalDevFund.getText().toString().trim()));

                JobDatabaseHelper dbHelper = new JobDatabaseHelper(this);
                dbHelper.updateCurrentJob(currentJob);

                Toast.makeText(this, "Current job saved successfully", Toast.LENGTH_SHORT).show();
            }
        }
    }
}