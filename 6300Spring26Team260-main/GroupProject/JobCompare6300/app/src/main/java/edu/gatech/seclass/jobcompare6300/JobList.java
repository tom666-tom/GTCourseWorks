package edu.gatech.seclass.jobcompare6300;

import java.util.ArrayList;

public class JobList {

    public ArrayList<Job> jobList;

    public JobList() {
        this.jobList = new ArrayList<>();
    }


    public Job getCurrentJob(){
        for (int i = 0; i < jobList.size(); i++) {
            if (jobList.get(i).isCurrentJob()) {
                return jobList.get(i);
            }
        }
        return null;
    }

    public int getCurrentJobIndex() {
        for (int i = 0; i < jobList.size(); i++) {
            if (jobList.get(i).isCurrentJob()) {
                return i;
            }
        }
        return -1;
    }

    public void addJob(Job job){
        jobList.add(job);
    }
}
