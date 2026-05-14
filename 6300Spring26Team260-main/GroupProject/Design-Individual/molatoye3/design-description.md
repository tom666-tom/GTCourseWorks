**Assignment 5 – Design description**

# Requirements

1.  **When the app is started, the user is presented with the main menu, which allows the user to (1) enter or edit current job details, (2) enter job offers, (3) adjust the comparison settings, or (4) compare job offers (disabled if no job offers were entered yet.**

    To realize this requirement, I have Main class to orchestrate all the components of the app. It serves as an entry point of the app. Once the user enters the app, the main menu page with the 4 buttons will be shown: “current job”, “add job offer”, “settings” and “compare job offers”. The main method creates JobManager object instance with it’s attributes and a method. The JobManager constructor creates JobList, ComparisonSettings and JobScore objects instances.

    The JobManager class uses the attribute “jobList: ArrayList&lt;Job&gt;” of JobList class that contains all the job offers, including the current job (index = 0, null by default).

    To comply with the requirement “compare job offers (disabled if no job offers were entered yet)”, the button to compare offers will be enabled if (jobList.length > 2 || (jobList.length > 1 && jobList.get(0) != null)).

2.  **When choosing to _enter current job details,_ a user will:**
    -  **Be shown a user interface to enter (if it is the first time) or edit all the details of their current job, which consists of:**
        1.  **Title**
        2.  **Company**
        3.  **Location (entered as city and state)**
        4.  **Cost of living in the location (expressed as an** [**index**](https://www.expatistan.com/cost-of-living/index/north-america)**)**
        5.  **Yearly salary**
        6.  **Yearly bonus**
        7.  **Stock Option Shares (Whole number, assumes 3-year vesting period and $1 stock value)**
        8.  **Wellness Stipend ($0-$1200 Inclusive annually)**
        9.  **Life Insurance (Percentage of Yearly Salary as an integer: 0 – 10 inclusive)**
        10. **Personal Development Fund ($0 to $6000 inclusive annually)**

         To implement this functionality there is a class “Job” that has all the mentioned attributes to store their values. Current job is a variation of the job offer, because it has all the same attributes. The current job object is stored in jobList array in JobList at index 0. It is null by default when the app starts.

        When user clicks “current job” button, the app navigates to the page with input fields for jobList\[0\]. If the user never enters the current job details, the jobList\[0\] will be null, and the input placeholders will show empty fields. Otherwise, the input values will be mapped with the jobList\[0\] attributes.

    -  **Be able to either save the job details or cancel and exit without saving, returning in both cases to the main menu.**

        If user clicks “save” button it will call addJob method in the JobList class, which will add a Job instance to jobList at index 0. If clicks “cancel”, it will be handled by GUI and return to the main menu.

3.  **When choosing to _enter job offers,_ a user will:**
    1.  **Be shown a user interface to enter all the details of the offer, which are the same ones listed above for the current job.**
    2.  **Be able to either save the job offer details or cancel.**
    3.  **Be able to (1) enter another offer, (2) return to the main menu, or (3) compare the offer (if they saved it) with the current job details (if present).**

    When button “add job offer” is clicked, the app navigates to the page with input fields, which is handled by GUI. The input fields are the same as in the “current job” page, and are empty strings by default.

    If user clicks “save” button it will call addJob method in the JobList class, which will add a Job instance to the end of jobList. If clicks “cancel”, it will be handled by GUI and return to the main menu.

    To implement “compare with the current job” functionality, the JobManager fetches the attributes of the current job in JobList (Index 0) and show them below the input fields for comparison.

4.  **When _adjusting the comparison settings,_ the user can assign integer _weights_ to:**
    1.  **Yearly salary**
    2.  **Yearly bonus**
    3.  **Stock Option Shares**
    4.  **Wellness Stipend**
    5.  **Life Insurance**
    6.  **Personal Development Fund**

    **NOTE: These factors should be integer-based from 0 (no interest/don’t care) to 9 (highest interest). Default value for all weights: 1**

    - **If no weights are assigned, all factors are considered equal.**
    - **The user must be able to either save the comparison settings or cancel; both will return the user to the main menu.**

    When button “settings” is clicked, the app navigates to the page with input fields, which is handled by GUI. The input fields have the same names as the attribute names in ComparisonSettings class. By default all the input fields placeholders are 1.

    The constrain implementation for weights to be greater or equal to 0 and less than 10, will be done by conditional statement which will not allow to save the value to the corresponding settings attribute and throw an error, if it’s not complying with the constrain.

    If user clicks “save” button and the value constrain is not violated, it will update the settings variable (instance of ComparisonSettings class) and navigate to main menu.

    If clicks “cancel”, it will be handled by GUI and return to the main menu.

5.  **When choosing to _compare job offers,_ a user will:**
    -  **Be shown a list of job offers, displayed as Title and Company, ranked from best to worst (see below for details), and including the current job (if present), clearly indicated.**

        When button “compare job offers” is clicked in the main menu, the app navigates to the page with the sorted list of all the jobs in descending order starting from the one with the highest score.

        In order to do that, JobManager class runs the computeScore method from JobScore class providing jobs (JobList instance) and settings (ComparisonSettings instance) as attributes for the method. The method populates it’s scoreMap array attribute with float scores.

        After that the sortJobs method is run returning the HashMap with Job instances and their mapped scores from scoreMap array. The returned HashMap is used to list the jobs on the page. The Title, Company and Score attributes of the Job are shown as a card element. Every Job card has a checkbox.

        If a job in the list has isCurrent attribute equal to true, it changes the button background to orange and show “Current” text in the bottom left corner of the button.

    -  **Select two jobs to compare and trigger the comparison.**

        To do this two Job card checkboxes should be checked. When second checkbox is clicked, it navigates to the job comparison page.

    -  **Be shown a table comparing the two jobs, displaying, for each job:**
        1.  **Title**
        2.  **Company**
        3.  **Location**
        4.  **Yearly salary adjusted for cost of living**
        5.  **Yearly bonus adjusted for cost of living**
        6.  **Stock Option Shares (SOS)**
        7.  **Wellness Stipend (WS)**
        8.  **Life Insurance (LI)**
        9.  **Personal Development Fund (PDF)**
        10. **Job Score - (JS) Calculation shown in Requirement #6**

        The job comparison page shows the above attributes of the two jobs side by side.

    -  **Be offered to perform another comparison or go back to the main menu.**

        If “back” button is clicked in job comparison page, the app navigate to the page with the job cards listed in the descending order. The user can get back to the main menu by clicking “back” button in that page.

6.  **When ranking jobs, a job’s score is computed as the weighted average of:**

    **AYS + AYB + (SOS/3) + WS + (LI/100 \* YS) + PDF**

    **where:  
    AYS = Yearly Salary Adjusted for cost of living  
    AYB = Yearly Bonus Adjusted for cost of living  
    SOS = Stock Option Shares (Whole number, assumes 3-year vesting period and $1 stock value)**

    **WS = Wellness Stipend ($0-$1200 Inclusive annually)**

    **LI = Life Insurance (Percentage of Yearly Salary expressed as an integer: 0 – 10 inclusive)**

    **PDF = Personal Development Fund ($0 to $6000 inclusive annually)**

    **For example, if the weights are 2 for the adjusted yearly salary, 2 for the adjusted yearly bonus, 2 for SOS, and 1 for all other factors, the score would be computed as:**

    **JS = 2/9 \* AYS + 2/9 \* AYB + 2/9 \* (SOS/3) + 1/9 \* WS + 1/9 \* (LI/100 \* YS) + 1/9 \* PDF**

    **For example, if the weights are 3 for the adjusted yearly salary, 3 for the adjusted yearly bonus, 4 for SOS, and 1 for all other factors, the score would be computed as:**

    **JS = 3/13 \* AYS + 3/13 \* AYB + 4/13 \* (SOS/3) + 1/13 \* WS + 1/13 \* (LI/100 \* YS) + 1/13 \* PDF**

    The above algorithm is used in computeScore method in JobScore class to populate scoreMap array and update yearlySalaryAdjustedForCostOfLiving and yearlyBonusAdjustedForCostOfLiving in the Job instance.

7.  **The user interface must be intuitive and responsive.**

    **For simplicity, you may assume there is a _single system_ running the app (no communication or saving between devices is necessary).**

    This is handled within the GUI implementation.

    In order to make things simple, the Main class is the entry point of the app, which triggers main() method with the creating of the JobManager class instance with it’s attributes and methods.

