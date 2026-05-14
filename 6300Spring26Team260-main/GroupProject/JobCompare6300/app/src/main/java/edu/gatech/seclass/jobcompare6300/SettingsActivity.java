package edu.gatech.seclass.jobcompare6300;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.util.ArrayList;


public class SettingsActivity extends AppCompatActivity {
    private EditText weightSalary, weightBonus, weightStock,
            weightWellness, weightLifeInsurance, weightPdf;
    private Button btnSave, btnCancel, btnReset;

    private static final String PREFS_NAME = "JobCompareSettings";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Initialize views
        initializeViews();

        // Load Existing Settings
        loadSettings();

        // Set click listeners
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInputs()) {
                    saveSettings();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Return to main menu without saving
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetToDefault();
            }
        });
    }

    private void initializeViews() {
        weightSalary = findViewById(R.id.weightSalary);
        weightBonus = findViewById(R.id.weightBonus);
        weightStock = findViewById(R.id.weightStock);
        weightWellness = findViewById(R.id.weightWellness);
        weightLifeInsurance = findViewById(R.id.weightLifeInsurance);
        weightPdf = findViewById(R.id.weightPdf);

        btnSave = findViewById(R.id.btnSettingsSave);
        btnCancel = findViewById(R.id.btnSettingsCancel);
        btnReset = findViewById(R.id.btnSettingsReset);
    }

    private boolean validateInputs(){
        //chk if all fields are filled
        if (weightSalary.getText().toString().trim().isEmpty() ||
                weightBonus.getText().toString().trim().isEmpty() ||
                weightStock.getText().toString().trim().isEmpty() ||
                weightWellness.getText().toString().trim().isEmpty() ||
                weightLifeInsurance.getText().toString().trim().isEmpty() ||
                weightPdf.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Please fill all", Toast.LENGTH_SHORT).show();
            return false;

        }
        // Validate numeric ranges
        try {
            int salaryWeight = Integer.parseInt(weightSalary.getText().toString());
            int bonusWeight = Integer.parseInt(weightBonus.getText().toString());
            int stockWeight = Integer.parseInt(weightStock.getText().toString());
            int wellnessWeight = Integer.parseInt(weightWellness.getText().toString());
            int lifeWeight = Integer.parseInt(weightLifeInsurance.getText().toString());
            int pdfWeight = Integer.parseInt(weightPdf.getText().toString());

            if (salaryWeight < 0 || salaryWeight > 9) {
                Toast.makeText(this, "Salary weight must be between 0 and 9", Toast.LENGTH_SHORT).show();
                return false;
            }

            if (bonusWeight < 0 || bonusWeight > 9) {
                Toast.makeText(this, "Bonus weight must be between 0 and 9", Toast.LENGTH_SHORT).show();
                return false;
            }

            if (stockWeight < 0 || stockWeight > 9) {
                Toast.makeText(this, "Stock weight must be between 0 and 9", Toast.LENGTH_SHORT).show();
                return false;
            }

            if (wellnessWeight < 0 || wellnessWeight > 9) {
                Toast.makeText(this, "Wellness weight must be between 0 and 9", Toast.LENGTH_SHORT).show();
                return false;
            }

            if (lifeWeight < 0 || lifeWeight > 9) {
                Toast.makeText(this, "Life Insurance weight must be between 0 and 9", Toast.LENGTH_SHORT).show();
                return false;
            }

            if (pdfWeight < 0 || pdfWeight > 9) {
                Toast.makeText(this, "Personal Development Fund weight must be between 0 and 9", Toast.LENGTH_SHORT).show();
                return false;
            }

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter valid numbers", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;

        }


    private void saveSettings() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        int salaryWeight = Integer.parseInt(weightSalary.getText().toString().trim());
        int bonusWeight = Integer.parseInt(weightBonus.getText().toString().trim());
        int stockWeight = Integer.parseInt(weightStock.getText().toString().trim());
        int wellnessWeight = Integer.parseInt(weightWellness.getText().toString().trim());
        int lifeWeight = Integer.parseInt(weightLifeInsurance.getText().toString().trim());
        int pdfWeight = Integer.parseInt(weightPdf.getText().toString().trim());

        JobScoreSettings settings = new JobScoreSettings(salaryWeight, bonusWeight, stockWeight,
                wellnessWeight, lifeWeight, pdfWeight);

        Gson gson = new Gson();
        String updatedJsonString = gson.toJson(settings);
        editor.putString("jobSettings", updatedJsonString);

        editor.apply();

        Toast.makeText(this, "Your Settings saved successfully", Toast.LENGTH_SHORT).show();
        finish();
    }
    private void loadSettings() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        Gson gson = new Gson();
        String jsonSettings = prefs.getString("jobSettings", null);

        JobScoreSettings settings;
        if (jsonSettings == null) {
            settings = new JobScoreSettings(1,1,1,1,1,1);
        }
        else {
            settings = gson.fromJson(jsonSettings, JobScoreSettings.class);
        }

        weightSalary.setText(String.valueOf(settings.salaryWeight));
        weightBonus.setText(String.valueOf(settings.bonusWeight));
        weightStock.setText(String.valueOf(settings.stockWeight));
        weightWellness.setText(String.valueOf(settings.wellnessWeight));
        weightLifeInsurance.setText(String.valueOf(settings.lifeWeight));
        weightPdf.setText(String.valueOf(settings.pdfWeight));
    }

    private void resetToDefault() {
        weightSalary.setText("1");
        weightBonus.setText("1");
        weightStock.setText("1");
        weightWellness.setText("1");
        weightLifeInsurance.setText("1");
        weightPdf.setText("1");

        Toast.makeText(this, "Reset to default values (all weights = 1)", Toast.LENGTH_SHORT).show();
    }


}

