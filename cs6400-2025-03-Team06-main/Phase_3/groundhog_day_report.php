<?php
include('lib/common.php');

$category_name = 'air conditioning';  

$query = "SELECT
    YEAR(Sold.date) AS year,
    SUM(Sold.quantity) AS total_units_sold_year,
    ROUND(SUM(Sold.quantity) / 365, 2) AS avg_units_sold_per_day,
    SUM(
        CASE
            WHEN MONTH(Sold.date) = 2 AND DAY(Sold.date) = 2 THEN Sold.quantity
            ELSE 0
        END
    ) AS total_units_sold_groundhog_day
FROM Sold
JOIN Product ON Sold.product_id = Product.product_id
JOIN Belong ON Belong.product_id = Product.product_id
WHERE LOWER(Belong.category_name) = '$category_name'
GROUP BY YEAR(Sold.date)
ORDER BY year;";


$result = mysqli_query($db, $query);



?>

<!DOCTYPE html>
<html lang="en">
<head>
    <title>Air Conditioners on Groundhog Day</title>
    <link rel="stylesheet" href="css/report.css">
</head>
<body>
<div class="container">
    <a href="dashboard.php" class="back-btn">← Back to Reports Hub</a>

    <div class="table-container">
        <h2 class="report-table-title">Air Conditioners on Groundhog Day</h2>
        <h2 class="report-table-subtitle">Air Conditioner Sales Summary</h2>
        <table class="report-table">
            <thead>
                <tr>
                    <th>Year</th>
                    <th style="text-align: center;">Total Units Sold (Year)</th>
                    <th style="text-align: center;">Avg Units Sold Per Day</th>
                    <th style="text-align: center;">Units Sold on Groundhog Day</th>
                </tr>
            </thead>
            <tbody>
                <?php while ($row = mysqli_fetch_assoc($result)): ?>
                    <tr>
                        <td><?php echo htmlspecialchars($row['year']); ?></td>
                        <td style="text-align: center;"><?php echo htmlspecialchars($row['total_units_sold_year']); ?></td>
                        <td style="text-align: center;"><?php echo htmlspecialchars(number_format($row['avg_units_sold_per_day'],2)); ?>
                        <span style='padding-left: 10px;'>(0.27%)</span></td>
                        <td style="text-align: center;">
                            <?php
                            echo htmlspecialchars(number_format($row['total_units_sold_groundhog_day']));

                            // Calculate and display percentage
                            if ($row['total_units_sold_year'] > 0) {
                                $percentage = ($row['total_units_sold_groundhog_day'] / $row['total_units_sold_year']) * 100;
                                echo "<span style='padding-left: 10px;'>(" . number_format($percentage, 2) . "%)</span>";
                            }
                            ?>
                        </td>
                    </tr>
                <?php endwhile; ?>
            </tbody>
        </table>
    </div>
</div>
</body>
</html>