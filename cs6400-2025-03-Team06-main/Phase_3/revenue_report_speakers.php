<?php

//sql vraibles
include('lib/common.php');

$sql = "
WITH Revenue AS (
    SELECT
        Product.product_id,
        Product.product_name,
        Product.retail_price,
        SUM(Sold.quantity) AS total_sold,
        SUM(
            CASE 
                WHEN Sold.sold_price < Product.retail_price THEN Sold.quantity 
                ELSE 0 
            END
        ) AS sold_at_discount,
        SUM(
            CASE 
                WHEN Sold.sold_price = Product.retail_price THEN Sold.quantity 
                ELSE 0 
            END
        ) AS sold_at_retail_price,
        SUM(Sold.sold_price * Sold.quantity) AS actual_revenue,
        SUM(
            Product.retail_price *
            (CASE WHEN Sold.sold_price < Product.retail_price THEN 0.75 ELSE 1 END) *
            Sold.quantity
        ) AS predicted_revenue
    FROM Belong
    JOIN Product ON Product.product_id = Belong.product_id
    JOIN Sold ON Sold.product_id = Product.product_id
    WHERE Belong.category_name = 'Speaker'
    GROUP BY Product.product_id, Product.product_name, Product.retail_price
)
SELECT
    product_id,
    product_name,
    retail_price,
    Revenue.total_sold,
    Revenue.sold_at_discount,
    Revenue.sold_at_retail_price,
    Revenue.actual_revenue,
    Revenue.predicted_revenue,
    Revenue.actual_revenue - Revenue.predicted_revenue AS revenue_difference
FROM Revenue
WHERE ABS(Revenue.actual_revenue - Revenue.predicted_revenue) > 5000
ORDER BY (Revenue.actual_revenue - Revenue.predicted_revenue) DESC;";

$result = mysqli_query($db, $sql);


?>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Actual vs. Predicted Revenue (Speakers)</title>
    <link rel="stylesheet" href="css/report.css">
</head>
<body>

<div class="container">
    <a href="dashboard.php" class="back-btn">← Back to Reports Hub</a>
    <div class="table-container">
        <h2 class="report-table-title">Actual vs. Predicted Revenue (Speakers)</h2>
        <h2 class="report-table-subtitle">Compares actual revenue from discounted Speaker products against predicted revenue</h2>
        <table class="report-table">
            <thead>
            <tr>
                <th>Product ID</th>
                <th>Product Name</th>
                <th style="text-align: center;">Product's Retail Price</th>
                <th style="text-align: center;width: 98px;">Total Number of Units Sold</th>
                <th style="text-align: center;width: 127px;">Total Number of Units Sold at a Discount</th>
                <th style="text-align: center;width: 135px;">Total Number of Units Sold at Retail Price</th>
                <th style="text-align: center;">Actual Revenue</th>
                <th style="text-align: center;">Predicted Revenue</th>
                <th style="text-align: center;">Revenue Difference ⬇</th>
            </tr>
            </thead>
            <tbody>
            <?php while ($row = mysqli_fetch_assoc($result)): ?>
                <tr>
                    <td><?php echo htmlspecialchars($row['product_id']); ?></td>
                    <td><?php echo htmlspecialchars($row['product_name']); ?></td>
                    <td style="text-align: center;">$<?php echo htmlspecialchars($row['retail_price']); ?></td>
                    <td style="text-align: center;"><?php echo htmlspecialchars($row['total_sold']); ?></td>
                    <td style="text-align: center;"><?php echo htmlspecialchars($row['sold_at_discount']); ?></td>
                    <td style="text-align: center;"><?php echo htmlspecialchars($row['sold_at_retail_price']); ?></td>
                    <td style="text-align: center;">$<?php echo htmlspecialchars(number_format($row['actual_revenue'], 2)); ?></td>
                    <td style="text-align: center;">$<?php echo htmlspecialchars(number_format($row['predicted_revenue'], 2)); ?></td>
                    <td style="text-align: center;color: var(--deep-green); font-weight: 600;">$<?php echo htmlspecialchars(number_format($row['revenue_difference'], 2)); ?></td>
                </tr>
            <?php endwhile; ?>
            </tbody>
        </table>
    </div>
</div>
</body>
</html>