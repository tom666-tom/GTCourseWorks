<?php

include('lib/common.php');

$sql = "
SELECT
    Manufacturer.manufacturer_id,
    Manufacturer.manufacturer_name,
    Manufacturer.max_discount,
    COUNT(Product.product_id) AS product_count,
    AVG(Product.retail_price) AS avg_retail_price,
    MIN(Product.retail_price) AS min_retail_price,
    MAX(Product.retail_price) AS max_retail_price,
    CASE
    WHEN COUNT(Product.product_id) > 0
    THEN (MAX(Product.retail_price) - MIN(Product.retail_price))
    ELSE NULL
    END AS price_range
FROM Manufacturer
LEFT JOIN Product ON Product.manufacturer_id = Manufacturer.manufacturer_id
GROUP BY Manufacturer.manufacturer_id,Manufacturer.manufacturer_name,Manufacturer.max_discount
ORDER BY avg_retail_price DESC
LIMIT 100";

$result = mysqli_query($db, $sql);

$manufacturers_data = array();
$main_result_data = array();
$manufacturer_ids = array();

if ($result) {
    while ($row = mysqli_fetch_assoc($result)) {
        $main_result_data[] = $row;
    }

    foreach ($main_result_data as $row) {
        $manufacturer_id = $row['manufacturer_id'];
        $manufacturer_ids[] = $row['manufacturer_id'];

        // Build manufacturer data structure
        $manufacturers_data[$manufacturer_id] = array(
            'manufacturer_info' => array(
                'manufacturer_id' => $row['manufacturer_id'],
                'manufacturer_name' => $row['manufacturer_name'],
                'max_discount' => $row['max_discount']
            ),
            'summary_info' => array(
                'product_count' => $row['product_count'],
                'avg_retail_price' => $row['avg_retail_price'],
                'min_retail_price' => $row['min_retail_price'],
                'max_retail_price' => $row['max_retail_price'],
                'price_range' => $row['price_range']
            ),
            'products' => array()
        );
    }
    mysqli_data_seek($result, 0);
}

$manufacturer_ids_string = implode("','", array_map('mysqli_real_escape_string', array_fill(0, count($manufacturer_ids), $db), $manufacturer_ids));
$products_sql = "SELECT
                    Product.product_id,
                    Product.product_name,
                    Product.retail_price,
                    Product.manufacturer_id,
                    GROUP_CONCAT(Belong.category_name ORDER BY Belong.category_name SEPARATOR '/') AS category_names
                FROM Product
                LEFT JOIN Belong ON Belong.product_id = Product.product_id
                WHERE Product.manufacturer_id IN ('$manufacturer_ids_string')
                GROUP BY Product.product_id, Product.product_name, Product.retail_price, Product.manufacturer_id
                ORDER BY Product.retail_price DESC, Product.product_id ASC";

$products_result = mysqli_query($db, $products_sql);
if ($products_result) {
    while ($product = mysqli_fetch_assoc($products_result)) {
        $manufacturer_id = $product['manufacturer_id'];
        if (isset($manufacturers_data[$manufacturer_id])) {
            $manufacturers_data[$manufacturer_id]['products'][] = $product;
        }
    }
}
?>

<!DOCTYPE html>
<html lang="en">
<head>
    <title>Manufacturer Report</title>
    <link rel="stylesheet" href="css/report.css">
</head>
<body>
<div class="container">
    <a href="dashboard.php" class="back-btn">← Back to Reports Hub</a>

    <div id="detail-table"></div>

    <div class="table-container">
        <h2 class="report-table-title">Manufacturers Report</h2>
        <h2 class="report-table-subtitle">Top 100 Manufacturers by Average Retail Price</h2>
        <table class="report-table">
            <thead>
            <tr>
                <th>ID</th>
                <th>Manufacturer Name</th>
                <th style="text-align: center;">Product Count</th>
                <th style="text-align: center;">Avg Retail Price ⬇</th>
                <th style="text-align: center;">Min Retail Price</th>
                <th style="text-align: center;">Max Retail Price</th>
                <th style="text-align: center;">Price Range</th>
            </tr>
            </thead>
            <tbody>
                <?php while ($row = mysqli_fetch_assoc($result)): ?>
                <tr>
                    <td><?php echo htmlspecialchars($row['manufacturer_id']); ?></td>
                    <td  class="name-cell">
                        <span><?php echo htmlspecialchars($row['manufacturer_name']); ?></span>
                        <button type="button" class="detail-btn" onclick="toggleTable('<?php echo $row['manufacturer_id']; ?>')">
                            Detail
                        </button>
                    </td>
                    <td style="text-align: center;"><?php echo htmlspecialchars($row['product_count']); ?></td>
                    <td style="text-align: center; color: var(--deep-green); font-weight: 600;">
                        $<?php echo number_format($row['avg_retail_price'], 2); ?>
                    </td>
                    <td style="text-align: center;">$<?php echo number_format($row['min_retail_price'], 2); ?></td>
                    <td style="text-align: center;">$<?php echo number_format($row['max_retail_price'], 2); ?></td>
                    <td style="text-align: center;">$<?php echo number_format($row['price_range'], 2); ?></td>

                </tr>
                <?php endwhile; ?>
            </tbody>
        </table>
    </div>
</div>
</body>


<script>
    const manufacturersData = <?php echo json_encode($manufacturers_data); ?>;

    function toggleTable(manufacturer_id) {
        const data = manufacturersData[manufacturer_id];
        const container = document.getElementById('detail-table');

        let html = '<h2 class="report-table-subtitle">' + data.manufacturer_info.manufacturer_name + ' - Details</h2>';

        html += `
        <div class="table-container">
            <table class="report-table">
                <tr>
                    <td class="info-cell" style="width: 50%;">
                        <div class="info-box">
                            <h3>Manufacturer Information</h3>
                            <p><strong>Manufacturer ID:</strong> ${data.manufacturer_info.manufacturer_id}</p>
                            <p><strong>Manufacturer Name:</strong> ${data.manufacturer_info.manufacturer_name}</p>
                            <p><strong>Maximum Discount:</strong> ${parseFloat(100*data.manufacturer_info.max_discount).toFixed(2)}%</p>
                        </div>
                    </td>
                    <td class="info-cell">
                        <div class="info-box">
                            <h3>Product Summary Information</h3>
                            <p><strong>Product Count:</strong> ${data.summary_info.product_count}</p>
                            <p><strong>Average Retail Price:</strong> $${parseFloat(data.summary_info.avg_retail_price).toFixed(2)}</p>
                            <p><strong>Minimum Retail Price:</strong> $${parseFloat(data.summary_info.min_retail_price).toFixed(2)}</p>
                            <p><strong>Maximum Retail Price:</strong> $${parseFloat(data.summary_info.max_retail_price).toFixed(2)}</p>
                            <p><strong>Price Range:</strong> $${parseFloat(data.summary_info.price_range).toFixed(2)}</p>
                        </div>
                    </td>
                </tr>
            </table>
        </div>`;

        html += '<h2 class="report-table-subtitle">' + data.manufacturer_info.manufacturer_name + ' - Products Sorted by Price</h2>'
        html += `
    <div class="table-container">
        <table class="report-table">
            <thead>
                <tr>
                    <th>Product ID</th>
                    <th>Product Name</th>
                    <th>Category</th>
                    <th style="text-align: center;">Price⬇</th>
                </tr>
            </thead>
            <tbody>`;

        data.products.forEach(product => {
            html += `
            <tr>
                <td>${product.product_id}</td>
                <td>${product.product_name}</td>
                <td>${product.category_names}</td>
                <td style="color: var(--deep-green); font-weight: 600;text-align: center;">$${parseFloat(product.retail_price).toFixed(2)}</td>
            </tr>`;
        });
        html += `
                </tbody>
            </table>
        </div>`;

        container.innerHTML = html;
        window.scrollTo({
            top: 0,
            behavior: 'smooth'
        });
    }
</script>

</html>