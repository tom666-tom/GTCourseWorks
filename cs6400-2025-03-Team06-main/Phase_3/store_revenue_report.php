<?php
include('lib/common.php');

$selected_state = '';
$report_data = [];

$sql = "SELECT DISTINCT state FROM Store ORDER BY state ASC";
$states_result = mysqli_query($db, $sql);

if (isset($_GET['state'])) {
    $selected_state = mysqli_real_escape_string($db, $_GET['state']);
    
    if (!empty($selected_state)) {
        $sql = "SELECT
                    Store.store_number,
                    Store.street_address,
                    Store.zipcode,
                    Store.city_name,
                    YEAR(Sold.date) AS year,
                    SUM(Sold.sold_price * Sold.quantity) AS total_revenue,
                    SUM(CASE WHEN Sold.sold_price = Product.retail_price
                        THEN Sold.sold_price * Sold.quantity ELSE 0 END) AS revenue_retail,
                    SUM(CASE WHEN Sold.sold_price < Product.retail_price
                        THEN Sold.sold_price * Sold.quantity ELSE 0 END) AS revenue_discounted
                FROM Store
                JOIN Sold ON Store.store_number = Sold.store_number
                JOIN Product ON Sold.product_id = Product.product_id
                WHERE Store.state = '$selected_state'
                GROUP BY Store.store_number, Store.street_address, Store.zipcode, Store.city_name, YEAR(Sold.date)
                ORDER BY year DESC, total_revenue DESC";
                
        $result = mysqli_query($db, $sql);

        if ($result) {
            while ($row = mysqli_fetch_assoc($result)) {
                $report_data[] = $row;
            }
        }
    }
}
?>

<!DOCTYPE html>
<html lang="en">
<head>
    <title>Store Revenue Report</title>
    <link rel="stylesheet" href="css/report.css">
</head>

<body>
    <div class="container">
        <a href="dashboard.php" class="back-btn">← Back to Reports Hub</a>

        <div class="table-container">
            <h2 class="report-table-title">Store Revenue by Year by State</h2>

            <form name="state_select_form" action="store_revenue_report.php" method="GET">
                <table style="margin-bottom: 24px;">
                    <tr>
                        <td style="font-size: 16px;font-weight: 600">Select a State</td>
                        <td style="width: 20px;"></td>
                        <td>
                            <select name="state" onchange="this.form.submit()" class="report-select">
                                <option value="">-- Select a State --</option>
                                <?php
                                if (isset($states_result)) {
                                    while ($row = mysqli_fetch_assoc($states_result)) {
                                        $state = $row['state'];
                                        $is_selected = ($state == $selected_state) ? 'selected' : '';
                                        echo "<option value='". htmlspecialchars($state) ."' $is_selected>". htmlspecialchars($state) ."</option>";
                                    }
                                }
                                ?>
                            </select>
                        </td>
                    </tr>
                </table>
            </form>
        </div>

        <?php if (!empty($selected_state) && !empty($report_data)) : ?>
            <div class="table-container">
                <h2 class="report-table-subtitle">Report for <?php echo $selected_state; ?></h2>
                <table class="report-table">
                    <thead>
                    <tr>
                        <th>Store Number</th>
                        <th>Street Address</th>
                        <th>Zip Code</th>
                        <th>City</th>
                        <th>Year</th>
                        <th style="text-align: center;">Total Revenue ⬇</th>
                        <th style="text-align: center;">Retail Revenue</th>
                        <th style="text-align: center;">Discounted Revenue</th>
                    </tr>
                    </thead>
                    <tbody>
                        <?php foreach ($report_data as $row): ?>
                            <tr>
                                <td><?php echo htmlspecialchars($row['store_number']); ?></td>
                                <td><?php echo htmlspecialchars($row['street_address']); ?></td>
                                <td><?php echo htmlspecialchars($row['zipcode']); ?></td>
                                <td><?php echo htmlspecialchars($row['city_name']); ?></td>
                                <td><?php echo htmlspecialchars($row['year']); ?></td>
                                <td style="text-align: center;color: var(--deep-green); font-weight: 600;">$<?php echo number_format($row['total_revenue'], 2); ?></td>
                                <td style="text-align: center;">$<?php echo number_format($row['revenue_retail'], 2); ?></td>
                                <td style="text-align: center;">$<?php echo number_format($row['revenue_discounted'], 2); ?></td>
                            </tr>
                        <?php endforeach; ?>
                    </tbody>
                </table>
            </div>

        <?php elseif (!empty($selected_state) && empty($report_data)) : ?>
            <div class="table-container">
                <h2 class="report-table-subtitle">No sales data found for <?php echo htmlspecialchars($selected_state); ?></h2>
                <p style="color: var(--muted-green); text-align: center; padding: 20px;">
                    No revenue records available for the selected state.
                </p>
            </div>
        <?php endif; ?>
    </div>
</body>
</html>