<?php
include('lib/common.php');

$query = "WITH City_Revenue AS (
    SELECT
        YEAR(Sold.date) AS year,
        City.city_name,
        City.state,
        City.population,
        SUM(Sold.sold_price * Sold.quantity) AS revenue
    FROM Sold
    JOIN Store ON Store.store_number = Sold.store_number
    JOIN City ON City.city_name = Store.city_name AND City.state = Store.state
    GROUP BY
        YEAR(Sold.date),
        City.city_name,
        City.state
)
SELECT
    year,
    AVG(CASE WHEN population < 3700000 THEN revenue END) AS avg_revenue_small,
    AVG(CASE WHEN population >= 3700000 AND population < 6700000 THEN revenue END) AS avg_revenue_medium,
    AVG(CASE WHEN population >= 6700000 AND population < 9000000 THEN revenue END) AS avg_revenue_large,
    AVG(CASE WHEN population >= 9000000 THEN revenue END) AS avg_revenue_extra_large
FROM City_Revenue
GROUP BY year
ORDER BY year;";

$result = mysqli_query($db, $query);


?>

<!DOCTYPE html>
<html lang="en">
<head>
    <title>Revenue by Population</title>
    <link rel="stylesheet" href="css/report.css">
</head>
<body>
<div class="container">
    <a href="dashboard.php" class="back-btn">← Back to Reports Hub</a>

    <div class="table-container">
        <h2 class="report-table-title">Revenue by Population</h2>
        <h2 class="report-table-subtitle">Average Revenue by City Population Category</h2>
        <table class="report-table">
            <thead>
                <tr>
                    <th>Year⬆</th>
                    <th style="text-align: center;">Small Cities</br>Average Revenue</th>
                    <th style="text-align: center;">Medium Cities</br>Average Revenue</th>
                    <th style="text-align: center;">Large Cities</br>Average Revenue</th>
                    <th style="text-align: center;">Extra Large Cities</br>Average Revenue</th>
                </tr>
            </thead>
            <tbody>
            <?php while ($row = mysqli_fetch_assoc($result)): ?>
                <tr>
                    <td style="color: var(--deep-green); font-weight: 600;"><?php echo $row['year']; ?></td>
                    <td style="text-align: center;"><?php echo number_format($row['avg_revenue_small'], 2); ?></td>
                    <td style="text-align: center;"><?php echo number_format($row['avg_revenue_medium'], 2); ?></td>
                    <td style="text-align: center;"><?php echo number_format($row['avg_revenue_large'], 2); ?></td>
                    <td style="text-align: center;"><?php echo number_format($row['avg_revenue_extra_large'], 2); ?></td>
                </tr>
            <?php endwhile; ?>
            </tbody>
        </table>
    </div>
</div>
</body>
</html>
