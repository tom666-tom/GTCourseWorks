package edu.gatech.seclass.jobcompare6300;

import java.util.ArrayList;

public class JobScore {

    private float[] scoreMap;

    public void computeScore(JobList jobList, JobScoreSettings settings) {

        ArrayList<Job> jobs = jobList.jobList;
        this.scoreMap = new float[jobs.size()];

        int i = 0; // Add counter to count index
        for (Job job : jobs) {
            float YS = job.getYearlySalary();
            float YB = job.getYearlyBonus();
            double AYS = (YS * 100.0) / job.getCostOfLiving();
            double AYB = (YB * 100.0) / job.getCostOfLiving();
            int SOS = job.getStockOptionShares();
            float WS = job.getWellnessStipend();
            int LI = job.getLifeInsurance();
            float PDF = job.getPersonalDevFund();

            int salaryWeight = 1;
            int bonusWeight = 1;
            int stockWeight = 1;
            int wellnessWeight = 1;
            int lifeWeight = 1;
            int pdfWeight = 1;

            if (settings != null) {
                salaryWeight = settings.salaryWeight;
                bonusWeight = settings.bonusWeight;
                stockWeight = settings.stockWeight;
                wellnessWeight = settings.wellnessWeight;
                lifeWeight = settings.lifeWeight;
                pdfWeight = settings.pdfWeight;
            }

            int divisor = salaryWeight + bonusWeight + stockWeight + wellnessWeight + lifeWeight + pdfWeight;

            // Use double data type for precision to store result
            double doubleJS = ((salaryWeight * AYS) + (bonusWeight * AYB) + ((stockWeight * SOS)/3.0) +
                    (wellnessWeight * WS) + (lifeWeight * (LI/100.0 * YS)) + (pdfWeight * PDF)) / divisor;

            // Round to two digits
//            double roundedDoubleJS = Math.round(doubleJS * 100.0) / 100.0;

            // Cast to float
//            float floatJS = (float) roundedDoubleJS;
            float floatJS = (float) doubleJS;

            // Add to scoreMap array and increment counter
            this.scoreMap[i++] = floatJS;
        }
    }

    public float[] getScoreMap() {
        return scoreMap;
    }
}
