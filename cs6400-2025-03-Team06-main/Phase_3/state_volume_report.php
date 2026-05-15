<?php
include('lib/common.php');

$current_year = date('Y');
$current_month = date('n');

$year = isset($_GET['year']) ? intval($_GET['year']) : $current_year;
$month = isset($_GET['month']) ? intval($_GET['month']) : $current_month;
$years_query = "SELECT DISTINCT YEAR(date) AS year FROM Sold ORDER BY year DESC";
$years_result = mysqli_query($db, $years_query);
$available_years = [];
if ($years_result) {
    while ($row = mysqli_fetch_assoc($years_result)) {
        $available_years[] = $row['year'];
    }
}

$available_months = [];
$months_result = null;
if ($year) {
    $months_query = "SELECT DISTINCT MONTH(date) AS month FROM Sold WHERE YEAR(date) = {$year} ORDER BY month ASC";
    $months_result = mysqli_query($db, $months_query);
    if ($months_result) {
        while ($row = mysqli_fetch_assoc($months_result)) {
            $available_months[] = $row['month'];
        }
    }
}


$query = "WITH Category_State_Unit AS (
    SELECT
        Belong.category_name,
        Store.state,
        SUM(Sold.quantity) AS total_sold
    FROM Sold
    JOIN Product ON Sold.product_id = Product.product_id
    JOIN Belong ON Belong.product_id = Product.product_id
    JOIN Store ON Sold.store_number = Store.store_number
    WHERE
        YEAR(Sold.date) = {$year}
        AND MONTH(Sold.date) = {$month}
    GROUP BY Belong.category_name, Store.state
),
Ranked AS (
    SELECT
        category_name,
        state,
        total_sold,
        RANK() OVER (PARTITION BY category_name ORDER BY total_sold DESC) AS rank_in_category
    FROM Category_State_Unit
)
SELECT
    category_name,
    state,
    total_sold
FROM Ranked
WHERE rank_in_category = 1
ORDER BY category_name, state;";

$result = mysqli_query($db, $query);



?>

<html lang="en">
<head>
    <title>State with Highest Volume</title>
    <link rel="stylesheet" href="css/report.css">
</head>

<body>
    <div class="container">
        <a href="dashboard.php" class="back-btn">← Back to Dashboard</a>

        <div class="table-container">
            <h2 class="report-table-title">State with Highest Volume for each Category</h2>
            <h2 class="report-table-subtitle">
                View state with the highest volume for each Category by Year and Month.
                </br>Please select a Year and Month to view the report.
            </h2>

            <form name="state_select_form" action="state_volume_report.php" method="GET">
                <table style="margin-bottom: 24px;">
                    <tr>
                        <td style="font-size: 16px;font-weight: 600;">Select a Year</td>
                        <td style="padding-left: 25px;">
                            <select name="year" class="report-select" onchange="window.location.href='state_volume_report.php?year=' + this.value">
                                <option value="">-- Select Year --</option>
                                <?php foreach ($available_years as $y): ?>
                                    <?php $selected = ($y == $year) ? 'selected' : ''; ?>
                                    <option value="<?php echo $y; ?>" <?php echo $selected; ?>><?php echo $y; ?></option>
                                <?php endforeach; ?>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td style="font-size: 16px;font-weight: 600;">Select a Month</td>
                        <td style="padding-left: 25px;">
                            <select name="month" class="report-select" onchange="this.form.submit()" <?php echo (empty($year)) ? 'disabled' : ''; ?>>
                                <option value="">-- Select Month --</option>
                                <?php foreach ($available_months as $m): ?>
                                    <?php
                                    $month_name = date("F", mktime(0, 0, 0, $m, 10));
                                    $selected = ($m == $month) ? 'selected' : '';
                                    ?>
                                    <option value="<?php echo $m; ?>" <?php echo $selected; ?>><?php echo $month_name; ?></option>
                                <?php endforeach; ?>
                            </select>
                        </td>
                    </tr>
                </table>
            </form>
        </div>

        <?php if (isset($result) && !empty($result)) : ?>
            <div class="table-container">
                <h2 class="report-table-subtitle">State with the highest volume for each Category on <?php echo $year . ' ' . date("F", mktime(0, 0, 0, $month, 10)); ?></h2>
                <table class="report-table">
                    <thead>
                    <tr>
                        <th>Category</th>
                        <th>State</th>
                        <th style="text-align: center;">Units Sold</th>
                    </tr>
                    </thead>
                    <tbody>
                    <?php foreach ($result as $row): ?>
                        <tr>
                            <td style="color: var(--deep-green); font-weight: 600;"><?php echo htmlspecialchars($row['category_name']); ?></td>
                            <td><?php echo htmlspecialchars($row['state']); ?></td>
                            <td style="text-align: center;"><?php echo htmlspecialchars(number_format($row['total_sold'])); ?></td>
                        </tr>
                    <?php endforeach; ?>
                    </tbody>
                </table>
            </div>

        <?php else : ?>
            <div class="table-container">
                <h2 class="report-table-subtitle">No data available for the selected year and month.</h2>
            </div>
        <?php endif; ?>
    </div>
</body>
</html>
