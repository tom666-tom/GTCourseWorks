<?php
include('lib/common.php');

// Add holiday logic
if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $holiday_date = mysqli_real_escape_string($db, $_POST['holiday_date']);
    $holiday_name = mysqli_real_escape_string($db, $_POST['holiday_name']);

    if (empty($holiday_date)) {
        array_push($error_msg, "Please enter a date.");
    }
    if (empty($holiday_name)) {
        array_push($error_msg, "Please enter a holiday name.");
    }

    if (empty($error_msg)) {
        // Check if a holiday already exists on this date
        $holiday_check_query = "SELECT date FROM Holiday WHERE date = '$holiday_date'";
        $holiday_result = mysqli_query($db, $holiday_check_query);

        if ($holiday_result && mysqli_num_rows($holiday_result) > 0) {
            array_push($error_msg, "A holiday on '$holiday_date' already exists.");
        } else {
            $date_check_query = "SELECT date FROM Date WHERE date = '$holiday_date'";
            $date_result = mysqli_query($db, $date_check_query);

            if ($date_result && mysqli_num_rows($date_result) == 0) {
                // If date does not exist, insert it.
                $insert_date_query = "INSERT INTO Date (date) VALUES ('$holiday_date')";
                $insert_date_result = mysqli_query($db, $insert_date_query);
                if (!$insert_date_result) {
                    array_push($error_msg, "Failed to add new date to Date table: " . mysqli_error($db));
                }
            }

            if (empty($error_msg)) {
                $insert_holiday_query = "INSERT INTO Holiday (date, holiday_name) VALUES ('$holiday_date', '$holiday_name')";
                $insert_holiday_result = mysqli_query($db, $insert_holiday_query);

                if ($insert_holiday_result) {
                    array_push($query_msg, "Successfully added new holiday: $holiday_name on $holiday_date");
                } else {
                    array_push($error_msg, "Failed to add holiday: " . mysqli_error($db));
                }
            }
        }
    }
}

$query = "SELECT date, holiday_name FROM Holiday ORDER BY date ASC";
$result = mysqli_query($db, $query);

if (!$result) {
    array_push($error_msg, "Failed to retrieve holiday list: " . mysqli_error($db));
}
?>

<!DOCTYPE html>
<html lang="en">
<head>
    <title>Manage Holidays</title>
    <link rel="stylesheet" href="css/report.css">
</head>

<body>
<div class="container">
    <a href="dashboard.php" class="back-btn">← Back to Dashboard</a>
    <?php
    if (isset($error_msg) && !empty($error_msg)) {
        echo '<div class="error-container">';
        foreach ($error_msg as $error) {
            echo '<p>' . $error . '</p>';
        }
        echo '</div>';
    }
    if (isset($query_msg) && count($query_msg) > 1) {
        echo '<div class="success-container">';
        for ($i = 1; $i < count($query_msg); $i++) {
            echo '<p>' . $query_msg[$i] . '</p>';
        }
        echo '</div>';
    }
    ?>

    <h2 class="report-table-title">All Holidays</h2>
    <h2 class="report-table-subtitle">Add New Holiday</h2>

    <form name="add_holiday_form" action="manage_holidays.php" method="POST">
        <table class="report-table" style="width: 50%">
            <tr>
                <td style="width: 30%">Date</td>
                <td><input type="date" name="holiday_date" class="form-input" /></td>
            </tr>
            <tr>
                <td style="width: 30%">Holiday Name</td>
                <td><input type="text" name="holiday_name" class="form-input" /></td>
            </tr>
        </table>
        <button type="submit" class="back-btn" style="margin-top: 16px;">
            Add Holiday
        </button>
    </form>

    <div class="table-container">
        <h2 class="report-table-subtitle">All Holidays</h2>
        <table class="report-table">
            <thead>
            <tr>
                <th>Date</th>
                <th>Holiday Name</th>
            </tr>
            </thead>
            <tbody>
            <?php if (isset($result) && mysqli_num_rows($result) > 0): ?>
                <?php while ($row = mysqli_fetch_assoc($result)): ?>
                    <tr>
                        <td><?php echo $row['date']; ?></td>
                        <td><?php echo $row['holiday_name']; ?></td>
                    </tr>
                <?php endwhile; ?>
            <?php else: ?>
                <tr>
                    <td colspan="2" style="text-align: center; color: var(--muted-green);">No holidays found.</td>
                </tr>
            <?php endif; ?>
            </tbody>
        </table>
    </div>
</div>

</body>
</html>
