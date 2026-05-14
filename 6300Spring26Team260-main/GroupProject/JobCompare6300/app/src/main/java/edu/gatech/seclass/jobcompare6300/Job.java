package edu.gatech.seclass.jobcompare6300;

public class Job {
    private int id;
    private String title;
    private String company;
    private String location;
    private int costOfLiving;
    private float yearlySalary;
    private float yearlyBonus;
    private float yearlySalaryAdjustedForCOL;
    private float yearlyBonusAdjustedForCOL;
    private int stockOptionShares;
    private float wellnessStipend;
    private int lifeInsurance;
    private float personalDevFund;
    private boolean isCurrentJob;

    // Default constructor
    public Job() {
    }

    // Constructor with all fields
    public Job(String title, String company, String location, int costOfLiving,
               float yearlySalary, float yearlyBonus, int stockOptionShares,
               float wellnessStipend, int lifeInsurance, float personalDevFund) {
        this.title = title;
        this.company = company;
        this.location = location;
        this.costOfLiving = costOfLiving;
        this.yearlySalary = yearlySalary;
        this.yearlyBonus = yearlyBonus;
        this.stockOptionShares = stockOptionShares;
        this.wellnessStipend = wellnessStipend;
        this.lifeInsurance = lifeInsurance;
        this.personalDevFund = personalDevFund;
        this.isCurrentJob = false;
    }

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getCostOfLiving() {
        return costOfLiving;
    }

    public void setCostOfLiving(int costOfLiving) {
        this.costOfLiving = costOfLiving;
    }

    public float getYearlySalary() {
        return yearlySalary;
    }

    public void setYearlySalary(float yearlySalary) {
        this.yearlySalary = yearlySalary;
    }

    public float getYearlyBonus() {
        return yearlyBonus;
    }

    public void setYearlyBonus(float yearlyBonus) {
        this.yearlyBonus = yearlyBonus;
    }

    public int getStockOptionShares() {
        return stockOptionShares;
    }

    public void setStockOptionShares(int stockOptionShares) {
        this.stockOptionShares = stockOptionShares;
    }

    public float getWellnessStipend() {
        return wellnessStipend;
    }

    public void setWellnessStipend(float wellnessStipend) {
        this.wellnessStipend = wellnessStipend;
    }

    public int getLifeInsurance() {
        return lifeInsurance;
    }

    public void setLifeInsurance(int lifeInsurance) {
        this.lifeInsurance = lifeInsurance;
    }

    public float getPersonalDevFund() {
        return personalDevFund;
    }

    public void setPersonalDevFund(float personalDevFund) {
        this.personalDevFund = personalDevFund;
    }

    public boolean isCurrentJob() {
        return isCurrentJob;
    }

    public void setCurrentJob(boolean currentJob) {
        isCurrentJob = currentJob;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}