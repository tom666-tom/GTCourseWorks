<?php

include('lib/common.php');

$sql = "
WITH Category_Product AS (
    SELECT
        Category.category_name,
        Product.product_id,
        Product.manufacturer_id,
        Product.retail_price
    FROM Category
    LEFT JOIN Belong ON Belong.category_name = Category.category_name
    LEFT JOIN Product ON Product.product_id = Belong.product_id
),
Revenue AS (
    SELECT
        Category_Product.category_name,
        SUM(Sold.sold_price * Sold.quantity) AS total_revenue
    FROM Category_Product
    LEFT JOIN Sold ON Sold.product_id = Category_Product.product_id
    GROUP BY Category_Product.category_name
)
SELECT
    Category_Product.category_name,
    COUNT(DISTINCT Category_Product.product_id) AS product_count,
    COUNT(DISTINCT Category_Product.manufacturer_id) AS manufacturer_count,
    AVG(Category_Product.retail_price) AS avg_retail_price,
    COALESCE(Revenue.total_revenue, 0) AS total_revenue
FROM Category_Product
LEFT JOIN Revenue ON Revenue.category_name = Category_Product.category_name
GROUP BY Category_Product.category_name, Revenue.total_revenue
ORDER BY Category_Product.category_name ASC;";

$result = mysqli_query($db, $sql);


?>

<!DOCTYPE html>
<html lang="en">
<head>
    <title>Category Report</title>
    <link rel="stylesheet" href="css/report.css">
</head>
<body>
<div class="container">
    <a href="dashboard.php" class="back-btn">← Back to Reports Hub</a>

    <div class="table-container">
        <h2 class="report-table-title">Category Report</h2>
        <h2 class="report-table-subtitle">All Categories Information</h2>
        <table class="report-table">
            <thead>
                <tr>
                    <th>Category Name</th>
                    <th style="text-align: center;">Product Count</th>
                    <th style="text-align: center;">Manufacturer Count</th>
                    <th style="text-align: center;">Average Retail Price</th>
                    <th style="text-align: center;">Total Revenue</th>
                </tr>
            </thead>
            <tbody>
                <?php while ($row = mysqli_fetch_assoc($result)): ?>
                    <tr>
                        <td style="color: var(--deep-green); font-weight: 600;"><?php echo $row['category_name']; ?></td>
                        <td style="text-align: center;"><?php echo $row['product_count']; ?></td>
                        <td style="text-align: center;"><?php echo $row['manufacturer_count']; ?></td>
                        <td style="text-align: center;">
                            <?php
                            echo isset($row['avg_retail_price']) && $row['avg_retail_price'] !== null
                                    ? htmlspecialchars(number_format($row['avg_retail_price'], 2))
                                    : 'N/A';
                            ?>
                        </td>
                        <td style="text-align: center;">$<?php echo number_format($row['total_revenue'], 2); ?></td>
                    </tr>
                <?php endwhile; ?>
            </tbody>
        </table>
    </div>
</div>
</body>
</html>