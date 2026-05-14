package edu.gatech.seclass.jobcompare6300;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
// for SQlite
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

public class JobManager {

    private static JobManager instance;
    private JobList jobList;
    private JobScoreSettings jobSettings;
    private ComparisonSettings settings;
    private JobScore jobScore;
    private ArrayList<Job> jobsToCompare; // Store jobs selected for comparison.
    private ArrayList<Float> jobsToCompareScores; // Store scores of the selected jobs for comparison.

    // added SQLite JobDatabaseHelper
    private JobDatabaseHelper dbHelper;
    private static final String PREF_JOB_SETTINGS = "JobCompareSettings";
    private final SharedPreferences settingsPrefs;

    private JobManager(Context context) {

        Context appContext = context.getApplicationContext();
        settingsPrefs = appContext.getSharedPreferences(PREF_JOB_SETTINGS, Context.MODE_PRIVATE);

        this.dbHelper = new JobDatabaseHelper(context);
        this.jobList = new JobList();
        this.jobSettings = LoadSettings();
        this.settings = new ComparisonSettings();
        this.jobScore = new JobScore();
        this.jobsToCompare = new ArrayList<>();
        this.jobsToCompareScores = new ArrayList<>();

        // Upload jobs to jobList if there are any in DB.
        uploadJobsFromDB();
    }

    // Implement singleton design pattern used in lecture videos.-added getInstance to accept Context
    public static JobManager getInstance(Context context) {
        if (instance == null) {
            instance = new JobManager(context.getApplicationContext());
        }
        return instance;
    }
    public void addJob(Job job) {
        // save data to db
        dbHelper.addJob(job);
        // upload Jobs from DB to JobManager instance
        uploadJobsFromDB();
    }

    private void uploadJobsFromDB() {
        List<Job> jobsFromDB = dbHelper.getAllJobs();
        if (jobsFromDB != null) {
            this.jobList.jobList = new ArrayList<>(jobsFromDB);
        }
    }

    private JobScoreSettings LoadSettings(){
        Gson gson = new Gson();
        String jsonSettings = settingsPrefs.getString("jobSettings", null);
        if (jsonSettings == null) {
            return new JobScoreSettings();
        }
        return gson.fromJson(jsonSettings, JobScoreSettings.class);
    }

    public JobList getJobList() {
        return this.jobList;
    }

    public JobScore getJobScore() {
        return this.jobScore;
    }

    public ArrayList<Job> getJobsToCompare() {
        return this.jobsToCompare;
    }

    public ArrayList<Float> getJobsToCompareScores() {
        return this.jobsToCompareScores;
    }

    public void clearJobsToCompareAndTheirScores() {
        jobsToCompare.clear();
        jobsToCompareScores.clear();
    }

    public HashMap<Float, ArrayList<Job>> getMappedJobs() {

        // Create HashMap<Float, ArrayList<Job>>. Use ArrayList to be able to store Jobs with equal scores.
        HashMap<Float, ArrayList<Job>> mappedJobs = new HashMap<>();

        // Get the lattest settings and jobs data
        JobScoreSettings settings = LoadSettings();
        JobList jobs = getJobList();

        // Compute scores for jobs and get their scores array;
        jobScore.computeScore(jobs, settings);
        float[] scoreMap = jobScore.getScoreMap();

        int i = 0;
        // Add all Jobs to ArrayList inside the HashMap using scoreMap values as keys
        for (Job job : jobs.jobList) {

            float score = scoreMap[i++];

            if (!mappedJobs.containsKey(score)) {
                mappedJobs.put(score, new ArrayList<>());
            }
            mappedJobs.get(score).add(job);
        }
        // Sort scoreMap in ascending order
        Arrays.sort(scoreMap);

        return mappedJobs;
    };
}
