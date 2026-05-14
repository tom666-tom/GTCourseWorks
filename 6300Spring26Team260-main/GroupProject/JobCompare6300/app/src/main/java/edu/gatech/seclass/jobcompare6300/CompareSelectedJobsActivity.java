package edu.gatech.seclass.jobcompare6300;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class CompareSelectedJobsActivity extends AppCompatActivity {

    private TextView jobTitle1, jobTitle2, jobCompany1, jobCompany2, jobLocation1, jobLocation2, yearlySalary1,
            yearlySalary2, yearlyBonus1, yearlyBonus2, stockOptions1, stockOptions2, wellnessStipend1, wellnessStipend2,
            lifeInsurance1, lifeInsurance2, personalDevFund1, personalDevFund2, jobScore1, jobScore2;
    private Button btnMainMenu, btnAnotherComparison;

    JobManager jobManager;
    ArrayList<Job> jobsToCompare;
    ArrayList<Float> jobsToCompareScores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare_selected_jobs);

        initializeViews();

        jobManager = JobManager.getInstance(this);
        jobsToCompare = jobManager.getJobsToCompare();
        jobsToCompareScores = jobManager.getJobsToCompareScores();

        Job job1 = jobsToCompare.get(0);
        float score1 = jobsToCompareScores.get(0);
        int score1Int = (int) score1; // convert score to int because decimal digits are not necessary

        Job job2 = jobsToCompare.get(1);
        float score2 = jobsToCompareScores.get(1);
        int score2Int = (int) score2; // convert score to int because decimal digits are not necessary

        // Yearly salary adjusted for cost of living
        double AYS1 = (job1.getYearlySalary() * 100.0) / job1.getCostOfLiving();
        AYS1 = Math.round(AYS1 * 100.0) / 100.0;
        double AYB1 = (job1.getYearlyBonus() * 100.0) / job1.getCostOfLiving();
        AYB1 = Math.round(AYB1 * 100.0) / 100.0;

        // Yearly bonus adjusted for cost of living
        double AYS2 = (job2.getYearlySalary() * 100.0) / job2.getCostOfLiving();
        AYS2 = Math.round(AYS2 * 100.0) / 100.0;
        double AYB2 = (job2.getYearlyBonus() * 100.0) / job2.getCostOfLiving();
        AYB2 = Math.round(AYB2 * 100.0) / 100.0;

        jobTitle1.setText(job1.getTitle());
        jobTitle2.setText(job2.getTitle());
        jobCompany1.setText(job1.getCompany());
        jobCompany2.setText(job2.getCompany());
        jobLocation1.setText(job1.getLocation());
        jobLocation2.setText(job2.getLocation());
        yearlySalary1.setText(String.format("%.2f", AYS1));
        yearlySalary2.setText(String.format("%.2f", AYS2));
        yearlyBonus1.setText(String.format("%.2f", AYB1));
        yearlyBonus2.setText(String.format("%.2f", AYB2));
        stockOptions1.setText(String.valueOf(job1.getStockOptionShares()));
        stockOptions2.setText(String.valueOf(job2.getStockOptionShares()));
        wellnessStipend1.setText(String.format("%.2f", job1.getWellnessStipend()));
        wellnessStipend2.setText(String.format("%.2f", job2.getWellnessStipend()));
        lifeInsurance1.setText(String.valueOf(job1.getLifeInsurance()));
        lifeInsurance2.setText(String.valueOf(job2.getLifeInsurance()));
        personalDevFund1.setText(String.format("%.2f", job1.getPersonalDevFund()));
        personalDevFund2.setText(String.format("%.2f", job2.getPersonalDevFund()));
        jobScore1.setText(String.valueOf(score1Int));
        jobScore2.setText(String.valueOf(score2Int));


        // Set click listeners
        btnMainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CompareSelectedJobsActivity.this, MainActivity.class);
                startActivity(intent);
                jobManager.clearJobsToCompareAndTheirScores();
            }
        });
        btnAnotherComparison.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CompareSelectedJobsActivity.this, CompareJobOffersActivity.class);
                startActivity(intent);
                jobManager.clearJobsToCompareAndTheirScores();
            }
        });

    }

    private void initializeViews() {
        jobTitle1 = findViewById(R.id.jobTitle1);
        jobTitle2 = findViewById(R.id.jobTitle2);
        jobCompany1 = findViewById(R.id.jobCompany1);
        jobCompany2 = findViewById(R.id.jobCompany2);
        jobLocation1 = findViewById(R.id.jobLocation1);
        jobLocation2 = findViewById(R.id.jobLocation2);
        yearlySalary1 = findViewById(R.id.yearlySalary1);
        yearlySalary2 = findViewById(R.id.yearlySalary2);
        yearlyBonus1 = findViewById(R.id.yearlyBonus1);
        yearlyBonus2 = findViewById(R.id.yearlyBonus2);
        stockOptions1 = findViewById(R.id.stockOptions1);
        stockOptions2 = findViewById(R.id.stockOptions2);
        wellnessStipend1 = findViewById(R.id.wellnessStipend1);
        wellnessStipend2 = findViewById(R.id.wellnessStipend2);
        lifeInsurance1 = findViewById(R.id.lifeInsurance1);
        lifeInsurance2 = findViewById(R.id.lifeInsurance2);
        personalDevFund1 = findViewById(R.id.personalDevFund1);
        personalDevFund2 = findViewById(R.id.personalDevFund2);
        jobScore1 = findViewById(R.id.jobScore1);
        jobScore2 = findViewById(R.id.jobScore2);
        btnMainMenu = findViewById(R.id.btnMainMenu);
        btnAnotherComparison = findViewById(R.id.btnAnotherComparison);
    }
}


