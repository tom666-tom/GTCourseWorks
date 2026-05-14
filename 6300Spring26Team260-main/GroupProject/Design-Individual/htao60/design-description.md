# Job Offer Comparison App - Design Description

## Overview
A simple, single-user console-based application that allows users to compare job offers with their current job based on weighted factors.

## Core Classes

### 1. JobManager (Controller Class)
Acts as the central coordination point for the application.

**Attributes:**
- `currentJob: JobOffer` - The user's current job (if entered)
- `jobOffers: List<JobOffer>` - Collection of all job offers entered
- `settings: ComparisonSettings` - Current comparison weight settings

**Methods:**
- `getCurrentJob(): JobOffer` - Returns the current job
- `setCurrentJob(job: JobOffer): void` - Sets or updates current job
- `addJob(offer: JobOffer): void` - Adds a new job offer
- `removeJob(index: int): void` - Removes a job offer
- `updateSettings(settings: ComparisonSettings): void` - Updates comparison weights

### 2. JobOffer (Model Class)
Represents a job position with all relevant details.

**Attributes:**
- `title: String` - Job title/position
- `company: String` - Company name
- `city: String` - Job location city
- `state: String` - Job location state
- `yearlySalary: double` - Annual base salary
- `yearlyBonus: double` - Annual bonus amount
- `personalDevFund: double` - Personal development fund amount
- `score: double` - Calculated score based on weights (for ranking)

**Methods:**
- Constructor with all parameters
- Getters and setters for all attributes
- `calculateScore(settings: ComparisonSettings): double` - Computes weighted score

### 3. ComparisonSettings (Model Class)
Manages the weight values used for job comparison calculations.

**Attributes:**
- `salaryWeight: int` - Weight for yearly salary (0-9, default: 1)
- `bonusWeight: int` - Weight for yearly bonus (0-9, default: 1)
- `devFundWeight: int` - Weight for personal development fund (0-9, default: 1)

**Methods:**
- Default constructor (sets all weights to 1)
- `setWeights(salary: int, bonus: int, fund: int): void` - Updates all weights
- Getters for each weight
- `validateWeights(): boolean` - Ensures weights are within 0-9 range
- `resetToDefault(): void` - Resets all weights to 1

### 4. RankedJob (View Helper Class)
Helper class for displaying jobs in ranked order.

**Attributes:**
- `job: JobOffer` - The job being displayed
- `rank: int` - Current rank position
- `score: double` - Calculated score
- `isCurrentJob: boolean` - Flag to identify current job

**Methods:**
- `display(): void` - Shows job information in ranked list format
- `getDisplayString(): String` - Returns formatted string for display

## Key Relationships

- **JobManager** contains **one** CurrentJob and **many** JobOffer objects
- **JobManager** has **one** ComparisonSettings object
- **RankedJob** is created on-demand by **JobManager** for display purposes

## Main Application Flow

1. **Startup** → Main Menu displayed
2. **User selects option:**
   - **Option 1:** Enter/Edit current job details
   - **Option 2:** Enter job offers
   - **Option 3:** Adjust comparison settings (weights)
   - **Option 4:** Compare job offers (disabled if no offers)
3. Each option leads to specific workflow then returns to Main Menu
4. Application continues until user exits