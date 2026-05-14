**Design Discussion \- CS6300SpringTeam260**

**Design1 (molatoye3):**  
\!\[UMLDesign\_Max\](./images/design1.png)

Pros:   
1 **Encapsulated Data Management:** The design effectively utilizes a `JobList` wrapper alongside a `JobManager` class. By using `JobList` to handle the `ArrayList<Job>`, the design keeps list operations like `addJobs()` and `getCurrentJob()` clean and well-organized.

2 **Structured Scoring Logic:** The inclusion of a `JobScore` class with a `scoreMap: float[]` provides a clear and dedicated structure for storing and calculating job scores.

3 **Minimalist Entry Point:** The `Main Entry Point` is intentionally simple, keeping the initial logic minimal by delegating all complex operations to the `JobManager` class. 

Cons:   
HashMap should have a float data type as a key, not integer, since scores from the design are stored as floats (HashMap\<Float, Job\>).

**Design2 (lyao81):**   
\!\[UMLDesign\_Lynne\](./images/design2.png)

Pros:   
1 The design uses a `JobComparator` class to handle all ranking and scoring, which follows the Single Responsibility Principle 

2 Use the `ComparisonResult` class to encapsulate the result of comparing 2 jobs makes the design easier to maintain.

Cons:   
 **Overlap between `JobComparisonApp` and `JobManager`:** JobComparisonApp and JobManager are coordinate application logic and might cause confusion about where certain responsibilities belong. The boundary between the two could be clearer.

**Design 3(spandian7):**  
\!\[UMLDesign\_\](./images/design3.png)

Pros:   
1 **Intuitive UI Structure**: This design utilizes 3 page classes including CurrentJobPage, JobOffersPage, JobComparisonPage which map directly to the app’s screens. The navigation flow is easy to understand.   
2 **ComparisonSettings:** The design features a comprehensive ComparisonSettings: All six weight attributes are present along with save() and cancel() methods and addressing the request per assignment 05 directly. 

3 **Structured comparison result:** The `generateComparisonTable()` method provides a well-defined output format, ensuring a clear and structured view for comparing different jobs.

Cons:   
1 **Missing Instance Variable:** Lacks an isCurrent flag in the `Job` class, there is no mechanism to indicate the current job in the ranked list as required clearly. Missing Instance Variable under `Job`’s field. costOfLiving, yearlySalary, etc lack type declarations, and also contain typos in the design. 

**Design4(htao60):**   
\!\[UMLDesign\_\](./images/design4.png)

Pros:   
1 **Avoids Parallel Array Issues:** The `calculateScore(ComparisonSettings)` method is encapsulated within the `JobOffer` class. This approach avoids the common pitfalls and complexity.

2 **Streamlined Class Structure:** The design is clean and concise, consisting of only four classes. This simplicity makes the overall architecture straightforward and easy for developers to understand.

Cons:   
1 **Insufficient Attribute Detail:** The UML design lacks specificity. Using placeholders like "more weights" or "more job details" instead of explicit attribute names makes the design too vague for implementation.

2 **Missing Relationships and Multiplicity:** The diagram fails to show connections between classes. Without arrows or multiplicity (e.g., 1..\*), it is difficult for other engineers to determine how objects interact or how they are structured within the system.

**Team Design**   
\!\[UMLDesign\_\](./images/design4.png)

### **Commonalities with Individual Designs**

Lynne and Max’s individual designs share several core ideas that carry over into the team design. Utilize `JobManager` as a central coordinator. Both designs use a `JobManager` class to orchestrate data access and operations, which the team design retains as the primary controller.

 **`ComparisonSettings` class:** All designs separate the `ComparisonSettings` class and correctly identified the need for a standalone class to hold the six weight attributes with integer constraints (0–9), which the team design preserves.

**Job attributes:**  All designs include the required job attributes (title, company, location, cost of living, salary, bonus, stock options, wellness stipend, life insurance, personal development fund), though some (Design 4\) use placeholders like "more job detail" instead of listing them explicitly.

**Inheritance for `CurrentJob` and `JobOffer`**: All designs subclasses of `Job`, which the team design simplifies (see below).

**Key Design Decisions**   
The team decided to move forward with a design based on Max’s approach. Both Lynne’s and Max’s designs effectively utilize a `JobList` wrapper alongside a `JobManager` class. Specifically, this design incorporates a `JobScore` class with a `scoreMap: float[]` and uses a HashMap for the ranking return type. All required fields are now fully implemented as attributes.

We also corrected an error from the previous diagram: the `HashMap` now uses `Float` as the key type instead of `Integer`, as our scores are stored as floats (`HashMap<Float, Job>`).

Reasons: 

1**Clear separation of concerns**: Design 1 separates data storage (`JobList`), scoring logic (`JobScore`), and coordination (JobManager) into distinct classes, each with a single responsibility. This is cleaner than Design 3's page-based approach which mixes UI and logic, and Design 4's minimal structure which puts too many responsibilities on \`JobManager\`.

2\. **Dedicated scoring class**: The `JobScore` class isolates score computation, making it easier to test and modify.

4\. **Complete attribute listing:** Design 1 explicitly lists all job attributes with types unless using placeholders.

5\. **No UI elements in the design:** Design 1 keeps the design purely focused on domain logic, following the assignment guideline that the design "should not contain Android-specific elements."

**Summary** 

Discussion on Parallel Arrays and Data Structures:

Our team held an in-depth discussion regarding the use of parallel arrays and ultimately decided to move forward with a design based on Max’s approach.

We specifically compared the trade-offs between using a `List<Job>` versus a `HashMap`. Lynne raised the question of whether a `List<Job>` or a `LinkedHashMap` might be more appropriate. However, the team concluded that a `List<Job>` would be inefficient for potential future features, such as a "delete job" operation, due to the overhead of searching through a list.

Furthermore, Max pointed out that switching to a `List` would complicate our ability to track job scores. Currently, the unsorted `scoreMap` links to the `jobList` by index. If we were to sort the jobs to display them in descending order, this index-based connection would be broken, effectively defeating the purpose of the `scoreMap`. Consequently, the team reached a consensus to retain the `ScoreMap` and `HashMap` design.

