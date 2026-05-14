package edu.gatech.seclass.jobcompare6300;

public class JobScoreSettings {
    public int salaryWeight;
    public int bonusWeight;
    public int stockWeight;
    public int wellnessWeight;
    public int lifeWeight;
    public int pdfWeight;

    public JobScoreSettings(){
        this.salaryWeight = 1;
        this.bonusWeight = 1;
        this.stockWeight = 1;
        this.wellnessWeight = 1;
        this.lifeWeight = 1;
        this.pdfWeight = 1;
    }

    public JobScoreSettings(int salaryWeight, int bonusWeight, int stockWeight,
                               int wellnessWeight, int lifeWeight, int pdfWeight) {
        this.salaryWeight = salaryWeight;
        this.bonusWeight = bonusWeight;
        this.stockWeight = stockWeight;
        this.wellnessWeight = wellnessWeight;
        this.lifeWeight = lifeWeight;
        this.pdfWeight = pdfWeight;
    }
}
