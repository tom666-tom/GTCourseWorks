[design-description.md](http://design-description.md)  
Lynne 

**Overview** 

This document describes the design of a Job Comparison App, including multiple weighted factors. The design uses a UML diagram to represent the system’s structure and classes. 

---

**BreakDown** 

The design consists of eight main classes:

**1 JobComparisonApp** \- Main entry point and coordinator

**2 JobManager** \- Manages all job-related data (current job and job offers)

**3 /Job** \- Abstract base class for all jobs

Extends job **CurrentJob** \- Represents the user's current job 

Extends job **Inherit from Job JobOffer** \- Represents a job offer

**6 ComparisonSettings** \- Stores comparison weight preferences

**7 JobComparator** \- Handles job ranking and comparison logic

**8 ComparisonResult** \- Encapsulates comparison results

---


**Requirement (1): Enter or Edit Current Job Details**

**How it is realized**

When the user chooses to enter or edit current job details, the system uses the following design elements:

1 JobComparisonApp.enterCurrentJob(): Displays the UI and coordinates the workflow

2 JobComparisonApp.editCurrentJob(): Handles editing of existing current job

3 CurrentJob class: Inherits from Job and stores all job details

4 JobManager.setCurrentJob(): Stores the current job 

5 JobManager.getCurrentJob(): Retrieves the current job for editing

Job attributes are inherited from the Job base class.

i. Title   
ii. Company  
iii. Location (entered as city and state)  
iv. Cost of living in the location (expressed as an [index](https://www.expatistan.com/cost-of-living/index/north-america))  
v. Yearly salary  
vi. Yearly bonus  
vii. Stock Option Shares (Whole number, assumes 3-year vesting period and $1 stock value)  
viii. Wellness Stipend ($0-$1200 Inclusive annually)  
ix. Life Insurance (Percentage of Yearly Salary as an integer: 0 – 10 inclusive)  
x. Personal Development Fund ($0 to $6000 inclusive annually)

***Methods used***

Job.set() methods: Set individual attributes with validation

Job.validate(): Ensures all data meets constraints

Job.save(): Persists the job data

JobManager.setCurrentJob(): Updates the current job in the system

***Relationship***

JobComparisonApp has a composition relationship with JobManager (multiplicity 1), and JobManager has an aggregation relationship with CurrentJob ( 0..1), since the user may not have a current job initially.

***Save() and Cancel() behavior***

The save() method in Job persists data, while cancel simply discards the temporary Job object and returns to the main menu and is handled by JobComparisonApp navigation logic.

**Requirement (2): Enter Job Offers**

**How it is realize**

The job offer entry workflow is similar to current job entry but uses different classes:

1 JobComparisonApp.enterJobOffer():  Coordinates the job offer entry process

2 JobOffer class:  Inherits from Job, stores offer details

3 JobManager.addJobOffer(): Adds the offer to the system's list

4 JobManager.getJobOffers(): Retrieves all job offers

*After saving an offer, the user can:*

1 Enter another offer : Calls enterJobOffer() again

2 Return to main menu: Navigation handled by displayMainMenu()

3 Compare with current job: Calls compareJobOffers() which uses JobComparator

*Key design decision: Both CurrentJob and JobOffer inherit from the /Job class \<abstract\> to*

Avoid code duplication (all attributes and most methods are identical)

Enable polymorphism (they can be treated uniformly in lists and comparisons)

Maintain type safety (distinguish current job from offers at the type level)

The only difference between CurrentJob and JobOffer is the return value of the isCurrent() method. CurrentJob.isCurrent() returns true and JobOffer.isCurrent() returns false

***Relationship***

JobManager has an aggregation relationship with JobOffer (multiplicity 0..\*), allowing for zero or more job offers.

### **Requirement (3): Adjust Comparison Settings**

**How it is realized**

The comparison settings functionality is implemented through below:

**1** ComparisonSettings class:  Stores all weight values

2 JobComparisonApp.adjustComparisonSettings(): Displays settings UI and handles user interaction

3 JobComparisonApp.settings : The app maintains a single ComparisonSettings instance

**Attributes:**

**a. Yearly salary**                           salaryWeight: int \= 1  
**b. Yearly bonus**                           bonusWeight: int \= 1  
**c. Stock Option Shares**              stockWeight: int \= 1  
**d. Wellness Stipend**                wellnessWeight: int \= 1  
**e. Life Insurance**                          lifeInsWeight: int \= 1  
**f. Personal Development Fund**  devFundWeight: int \= 1

Requirement: All weights default to 1, representing equal importance.

***Methods used*** 

**1 setWeight(factor, value)** \- Sets a specific factor's weight (validates 0-9 range)

**2 getWeight(factor)**:  Retrieves a specific weight value

**3 getTotalWeight()**: Returns the sum of all weights (used in score calculation)

**4 save()**: Confirms and saves the settings

**5 cancel()**: Discards changes and reverts to previous settings

**6 isDefault()**: Returns true if all weights are still 1

***Validation***

The setWeight() method validates that values are integers between 0 and 9 (inclusive).

***Relationship:***

JobComparisonApp has a composition relationship with ComparisonSettings (multiplicity 1), meaning the settings are an integral part of the application and exist for its lifetime.

### **Requirement (4): Compare Job Offers**

**How it is realized:**

The comparison feature is the most complex workflow, involving multiple classes:

**Main workflow (coordinated by JobComparisonApp.compareJobOffers()):**

***1 Retrieve all jobs:***

* Calls JobManager.getAllJobs() to get a combined list of current job and all offers

***2 Rank jobs:***

* Calls JobComparator.rankJobs(jobs, settings)  
  * JobComparator calculates scores for each job using the calculateScore() method  
  * Returns a sorted list from best to worst

***3 Display ranked list:***

* Shows jobs as "Title and Company" (using Job.getTitle() and Job.getCompany())  
  * Current job is marked (identified by Job.isCurrent() returning true)  
  * This display is handled by the GUI layer

***4 User selects two jobs:***

* User interaction handled by GUI

***5 Generate detailed comparison:***

* Calls JobComparator.compareJobs(job1, job2, settings)  
  * Returns a ComparisonResult object

***6 Display comparison table:***

* ComparisonResult.getFormattedComparison() provides formatted data  
  * Shows for each job:  
    * Title (Job.getTitle())  
    * Company (Job.getCompany())  
    * Location (Job.getLocation() \- returns "city, state")  
    * Adjusted yearly salary (Job.getAdjustedYearlySalary())  
    * Adjusted yearly bonus (Job.getAdjustedYearlyBonus())  
    * Stock Option Shares (Job.getStockOptionShares())  
    * Wellness Stipend (Job.getWellnessStipend())  
    * Life Insurance (Job.getLifeInsurance())  
    * Personal Development Fund (Job.getPersonalDevFund())  
    * Job Score (ComparisonResult.getJob1Score() / getJob2Score())

**Classes involved:**

1 JobManager: Provides getAllJobs() to combine current job and offers

2 JobComparator: Performs ranking and comparison calculations

3 ComparisonResult: Encapsulates comparison data for display

4 Job: Provides all necessary getters and calculation methods

**Design decision:** 

Separating JobComparator from JobComparisonApp follows the Single Responsibility Principle:

* JobComparisonApp handles user interaction and workflow  
* JobComparator handles comparison algorithms and calculations  
* This separation makes the code more testable and maintainable

### **Requirements 5 and 6: Job Ranking and Score Calculation**

**How it is realized:**

The job scoring algorithm is implemented in JobComparator.calculateScore():

**Formula from the Assignment 05:**

score \= (w1 × AYS \+ w2 × AYB \+ w3 × (SOS/3) \+ w4 × WS \+ w5 × (LI/100 × YS) \+ w6 × PDF) / totalWeight

* AYS \= Adjusted Yearly Salary  
* AYB \= Adjusted Yearly Bonus  
* SOS \= Stock Option Shares  
* WS \= Wellness Stipend  
* LI \= Life Insurance (percentage)  
* YS \= Yearly Salary (used for LI calculation)  
* PDF \= Personal Development Fund  
* w1-w6 \= weights from ComparisonSettings  
* totalWeight \= sum of all weights

**Key calculation methods:**

1. **Job.getAdjustedYearlySalary():**

  adjustedSalary \= (yearlySalary / costOfLivingIndex) × 100

This normalizes salary based on cost of living. For example:

* $100,000 salary in a city with index 120 → $83,333 adjusted  
* $80,000 salary in a city with index 80 → $100,000 adjusted  
    
2. **Job.getAdjustedYearlyBonus():**

  adjustedBonus \= (yearlyBonus / costOfLivingIndex) × 100

Same adjustment logic as salary.

3. **JobComparator.calculateScore(job, settings):**  
   * Retrieves adjusted values from the Job  
   * Gets weight values from ComparisonSettings  
   * Applies the weighted average formula  
   * Returns the final score as a double  
4. **JobComparator.rankJobs(jobs, settings):**  
   * Calculates score for each job  
   * Sorts jobs by score in descending order  
   * Returns the sorted list

**Example calculation:**

Given:

* Adjusted salary: $100,000  
* Adjusted bonus: $20,000  
* Stock options: 3,000 shares  
* Wellness stipend: $500  
* Life insurance: 5% (of $100,000 salary \= $5,000)  
* Dev fund: $3,000  
* Weights: 2, 2, 2, 1, 1, 1 (total \= 9\)

Score \= (2×100000 \+ 2×20000 \+ 2×(3000/3) \+ 1×500 \+ 1×5000 \+ 1×3000) / 9 \= (200000 \+ 40000 \+ 2000 \+ 500 \+ 5000 \+ 3000\) / 9 \= 250500 / 9 \= 27,833.33

**Design rationale:** The calculation logic is in JobComparator rather than Job because:

1 Job class represents data, not business logic

2 Comparison logic depends on settings, which Job shouldn't know about

3 Easier to modify or extend comparison algorithms

### **Requirement 7: Intuitive and Responsive UI**

**How it is realized:**

This requirement does not directly affect the class design. The user interface is implemented separately in the GUI layer, which interacts with the design classes through their public methods.

**Additional Design Decisions** 

To realize requirements, I decided to create a separate JobManager class to manage all job related data.  

**Why**

The system follows the Single Responsibility Principle by delegating all data operations to JobManager, while JobComparisonApp focuses on user interaction and coordination. This separation of concerns ensures that the core logic is isolated, meaning updates to job management won't break the UI or workflow. This modular approach allows JobManager to be tested in isolation and provides a flexible foundation for adding future functionality, such as data exports or analytics.

Reference: 

1 UML Class Diagram Tutorial [https://www.visual-paradigm.com/guide/uml-unified-modeling-language/uml-class-diagram-tutorial/](https://www.visual-paradigm.com/guide/uml-unified-modeling-language/uml-class-diagram-tutorial/)

2 Android Development 

https://www.youtube.com/watch?v=fis26HvvDII

