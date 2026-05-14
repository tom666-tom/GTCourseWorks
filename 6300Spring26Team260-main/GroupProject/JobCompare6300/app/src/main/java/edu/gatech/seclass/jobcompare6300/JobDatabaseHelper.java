package edu.gatech.seclass.jobcompare6300;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class JobDatabaseHelper extends SQLiteOpenHelper {

    // Database Version & Name
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "JobCompare.db";

    // Table
    private static final String TABLE_JOBS = "jobs";

    // Column
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_COMPANY = "company";
    private static final String KEY_LOCATION = "location";
    private static final String KEY_COST_OF_LIVING = "cost_of_living";
    private static final String KEY_YEARLY_SALARY = "yearly_salary";
    private static final String KEY_YEARLY_BONUS = "yearly_bonus";
    private static final String KEY_STOCK_OPTION_SHARES = "stock_option_shares";
    private static final String KEY_WELLNESS_STIPEND = "wellness_stipend";
    private static final String KEY_LIFE_INSURANCE = "life_insurance";
    private static final String KEY_PERSONAL_DEV_FUND = "personal_dev_fund";
    private static final String KEY_IS_CURRENT_JOB = "is_current_job";

    public JobDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_JOBS_TABLE = "CREATE TABLE " + TABLE_JOBS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_TITLE + " TEXT,"
                + KEY_COMPANY + " TEXT,"
                + KEY_LOCATION + " TEXT,"
                + KEY_COST_OF_LIVING + " INTEGER,"
                + KEY_YEARLY_SALARY + " REAL,"
                + KEY_YEARLY_BONUS + " REAL,"
                + KEY_STOCK_OPTION_SHARES + " INTEGER,"
                + KEY_WELLNESS_STIPEND + " REAL,"
                + KEY_LIFE_INSURANCE + " INTEGER,"
                + KEY_PERSONAL_DEV_FUND + " REAL,"
                + KEY_IS_CURRENT_JOB + " INTEGER" + ")";
        db.execSQL(CREATE_JOBS_TABLE);

        //Insert the first entry to be current job with blank fields.
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, "");
        values.put(KEY_COMPANY, "");
        values.put(KEY_LOCATION, "");
        values.put(KEY_COST_OF_LIVING, 0);
        values.put(KEY_YEARLY_SALARY, 0.0);
        values.put(KEY_YEARLY_BONUS, 0.0);
        values.put(KEY_STOCK_OPTION_SHARES, 0);
        values.put(KEY_WELLNESS_STIPEND, 0.0);
        values.put(KEY_LIFE_INSURANCE, 0);
        values.put(KEY_PERSONAL_DEV_FUND, 0.0);
        // SQLite does not support boolean, store as 0 or 1
        values.put(KEY_IS_CURRENT_JOB, 1);

        db.insert(TABLE_JOBS, null, values);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_JOBS);
        // Create tables again
        onCreate(db);
    }

    // Add a new job
    public void addJob(Job job) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, job.getTitle());
        values.put(KEY_COMPANY, job.getCompany());
        values.put(KEY_LOCATION, job.getLocation());
        values.put(KEY_COST_OF_LIVING, (int) job.getCostOfLiving());
        values.put(KEY_YEARLY_SALARY, job.getYearlySalary());
        values.put(KEY_YEARLY_BONUS, job.getYearlyBonus());
        values.put(KEY_STOCK_OPTION_SHARES, job.getStockOptionShares());
        values.put(KEY_WELLNESS_STIPEND, job.getWellnessStipend());
        values.put(KEY_LIFE_INSURANCE, job.getLifeInsurance());
        values.put(KEY_PERSONAL_DEV_FUND, job.getPersonalDevFund());
        // SQLite does not support boolean, store as 0 or 1
        values.put(KEY_IS_CURRENT_JOB, job.isCurrentJob() ? 1 : 0);

        db.insert(TABLE_JOBS, null, values);
        db.close();
    }

    // Update current job
    public void updateCurrentJob(Job job) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, job.getTitle());
        values.put(KEY_COMPANY, job.getCompany());
        values.put(KEY_LOCATION, job.getLocation());
        values.put(KEY_COST_OF_LIVING, (int) job.getCostOfLiving());
        values.put(KEY_YEARLY_SALARY, job.getYearlySalary());
        values.put(KEY_YEARLY_BONUS, job.getYearlyBonus());
        values.put(KEY_STOCK_OPTION_SHARES, job.getStockOptionShares());
        values.put(KEY_WELLNESS_STIPEND, job.getWellnessStipend());
        values.put(KEY_LIFE_INSURANCE, job.getLifeInsurance());
        values.put(KEY_PERSONAL_DEV_FUND, job.getPersonalDevFund());
        values.put(KEY_IS_CURRENT_JOB, job.isCurrentJob() ? 1 : 0);

        db.update(TABLE_JOBS, values, KEY_ID + " =?", new String[]{"1"});
        db.close();
    }

    // Get all jobs
    public List<Job> getAllJobs() {
        List<Job> jobList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_JOBS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Job job = new Job();
                job.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(KEY_TITLE)));
                job.setCompany(cursor.getString(cursor.getColumnIndexOrThrow(KEY_COMPANY)));
                job.setLocation(cursor.getString(cursor.getColumnIndexOrThrow(KEY_LOCATION)));
                job.setCostOfLiving(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_COST_OF_LIVING)));
                job.setYearlySalary(cursor.getFloat(cursor.getColumnIndexOrThrow(KEY_YEARLY_SALARY)));
                job.setYearlyBonus(cursor.getFloat(cursor.getColumnIndexOrThrow(KEY_YEARLY_BONUS)));
                job.setStockOptionShares(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_STOCK_OPTION_SHARES)));
                job.setWellnessStipend(cursor.getFloat(cursor.getColumnIndexOrThrow(KEY_WELLNESS_STIPEND)));
                job.setLifeInsurance(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_LIFE_INSURANCE)));
                job.setPersonalDevFund(cursor.getFloat(cursor.getColumnIndexOrThrow(KEY_PERSONAL_DEV_FUND)));
                job.setCurrentJob(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_IS_CURRENT_JOB)) == 1);

                jobList.add(job);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return jobList;
    }
}
