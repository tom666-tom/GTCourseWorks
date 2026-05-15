<?php
include('lib/common.php');

// Determine which view to show based on GET parameters
$year = isset($_GET['year']) ? intval($_GET['year']) : null;
$city = isset($_GET['city']) ? mysqli_real_escape_string($db, $_GET['city']) : null;
$state = isset($_GET['state']) ? mysqli_real_escape_string($db, $_GET['state']) : null;

$query = null;
$result = null;

if ($year && $city && $state) {
    $query = "SELECT 
        s.store_number,
        s.street_address,
        s.zipcode,
        s.city_name,
        s.state,
        COUNT(m.signup_store) AS memberships_sold
    FROM Store s
    LEFT JOIN Membership m ON s.store_number = m.signup_store 
        AND YEAR(m.signup_date) = $year
    WHERE s.city_name = '$city' AND s.state = '$state'
    GROUP BY s.store_number, s.street_address, s.zipcode, s.city_name, s.state
    ORDER BY memberships_sold DESC, s.store_number ASC";
    
} elseif ($year) {
    $base_city_query = "
    WITH CityMembershipCounts AS (
        SELECT
            s.city_name,
            s.state,
            COUNT(*) AS memberships_sold
        FROM Membership AS m
        JOIN Store AS s ON m.signup_store = s.store_number
        WHERE YEAR(m.signup_date) = '$year'
        GROUP BY s.city_name, s.state
    ),
    CityStoreCounts AS (
        SELECT
            city_name,
            state,
            COUNT(*) AS store_count
        FROM Store
        GROUP BY city_name, state
    )
    SELECT
        cmc.city_name,
        cmc.state,
        cmc.memberships_sold,
        csc.store_count,
        CASE
            WHEN cmc.memberships_sold >= 250 THEN 'green'
            WHEN cmc.memberships_sold <= 30  THEN 'red'
            ELSE 'none'
        END AS highlight
    FROM CityMembershipCounts cmc
    JOIN CityStoreCounts csc ON cmc.city_name = csc.city_name AND cmc.state = csc.state
    ";
    
    // Top 25 cities
    $query_top = $base_city_query . " ORDER BY cmc.memberships_sold DESC, cmc.city_name ASC LIMIT 25";

    // Bottom 25 cities
    $query_bottom = $base_city_query . " ORDER BY cmc.memberships_sold ASC, cmc.city_name ASC LIMIT 25";
    
} else {
    // Total Membership Sold by Year
    $query = "SELECT 
        YEAR(m.signup_date) AS year,
        COUNT(*) AS total_memberships
    FROM Membership m
    GROUP BY YEAR(m.signup_date)
    ORDER BY year DESC";
}


// Execute queries
if ($query) {
    $result = mysqli_query($db, $query);
} elseif ($year) {
    $result_top = mysqli_query($db, $query_top);
    $result_bottom = mysqli_query($db, $query_bottom);
}
?>

<!DOCTYPE html>
<html lang="en">
<head>
    <title>Membership Trends</title>
    <link rel="stylesheet" href="css/report.css">
</head>

<body>
<div class="container">
    <a href="dashboard.php" class="back-btn">← Back to Reports Hub</a>
    <?php if ($year && $city && $state): ?>
        <a href="membership_trends_report.php?year=<?php echo $year; ?>" class="back-btn">← Back to Cities Trends</a>
    <?php endif; ?>
    <?php if ($year): ?>
        <a href="membership_trends_report.php" class="back-btn">← Back to Membership Trends</a>
    <?php endif; ?>

    <div class="table-container">
        <?php if ($year && $city && $state): ?>
            <h2 class="report-table-title">Store Details - <?php echo $city; ?>, <?php echo $state; ?> - <?php echo $year; ?></h2>
            <table class="report-table">
                <thead>
                    <tr>
                        <th>Store Number</th>
                        <th>Street Address</th>
                        <th>Zip Code</th>
                        <th>City</th>
                        <th>State</th>
                        <th style="text-align: center;">Total Memberships Sold ⬇</th>
                    </tr>
                </thead>
                <tbody>
                <?php while ($row = mysqli_fetch_assoc($result)): ?>
                    <tr>
                        <td><?php echo $row['store_number']; ?></td>
                        <td><?php echo $row['street_address']; ?></td>
                        <td><?php echo $row['zipcode']; ?></td>
                        <td><?php echo $city; ?></td>
                        <td><?php echo $state; ?></td>
                        <td style="text-align: center;color: var(--deep-green); font-weight: 600"><?php echo number_format($row['memberships_sold']); ?></td>
                    </tr>
                <?php endwhile; ?>
                </tbody>
            </table>
        <?php elseif ($year): ?>

            <h2 class="report-table-title">Memberships Sold data on <?php echo $year; ?></h2>
            <h2 class="report-table-subtitle">Top 25 Cities by Membership Sold on <?php echo $year; ?></h2>
            <table class="report-table">
                <thead>
                    <tr>
                        <th>City</th>
                        <th>State</th>
                        <th style="text-align: center;">Total Memberships Sold ⬇</th>
                    </tr>
                </thead>
                <tbody>
                <?php while ($row = mysqli_fetch_assoc($result_top)): ?>
                <?php
                    $count = intval($row['memberships_sold']);
                    $highlight = $row['highlight'];
                    $bg_color = '';
                    if ($highlight === 'green') {
                        $bg_color = 'background-color: #8fc9a9';
                    } elseif ($highlight === 'red') {
                        $bg_color = 'background-color: #FFA07A;';
                    }
                    $store_count = intval($row['store_count']);
                ?>
                    <tr style="<?php echo $bg_color; ?>">
                        <td><?php echo $row['city_name']; ?></td>
                        <td><?php echo $row['state']; ?></td>
                        <td class="name-cell">
                            <span style="color: var(--deep-green); font-weight: 600"><?php echo number_format($count); ?></span>
                            <?php if ($store_count > 1): ?>
                                <button type="button" class="detail-btn"
                                        onclick="window.location.href='membership_trends_report.php?' +
                                                'year=<?php echo $year; ?>' +
                                                '&city=<?php echo urlencode($row['city_name']); ?>' +
                                                '&state=<?php echo urlencode($row['state']); ?>'">
                                    View Store Data
                                </button>
                            <?php endif; ?>
                        </td>
                    </tr>
                    <?php endwhile; ?>
                </tbody>
            </table>
            <div style="height: 20px;"></div>
            <h2 class="report-table-subtitle">Bottom  25 Cities by Membership Sold on <?php echo $year; ?></h2>
            <table class="report-table">
                <thead>
                <tr>
                    <th>City</th>
                    <th>State</th>
                    <th style="text-align: center;">Total Memberships Sold ⬆</th>
                </tr>
                </thead>
                <tbody>
                <?php while ($row = mysqli_fetch_assoc($result_bottom)): ?>
                    <?php
                    $count = intval($row['memberships_sold']);
                    $highlight = $row['highlight'];
                    $bg_color = '';
                    if ($highlight === 'green') {
                        $bg_color = 'background-color: #8fc9a9';
                    } elseif ($highlight === 'red') {
                        $bg_color = 'background-color: #FFA07A;';
                    }
                    $store_count = intval($row['store_count']);
                    ?>
                    <tr style="<?php echo $bg_color; ?>">
                        <td><?php echo $row['city_name']; ?></td>
                        <td><?php echo $row['state']; ?></td>
                        <td class="name-cell">
                            <span style="color: var(--deep-green); font-weight: 600"><?php echo number_format($count); ?></span>
                            <?php if ($store_count > 1): ?>
                                <button type="button" class="detail-btn"
                                        onclick="window.location.href='membership_trends_report.php?' +
                                                'year=<?php echo $year; ?>' +
                                                '&city=<?php echo urlencode($row['city_name']); ?>' +
                                                '&state=<?php echo urlencode($row['state']); ?>'">
                                    View Store Data
                                </button>
                            <?php endif; ?>
                        </td>
                    </tr>
                <?php endwhile; ?>
                </tbody>
            </table>

        <?php else: ?>
            <h2 class="report-table-title">Total Memberships Sold by Year</h2>
            <table class="report-table">
                <thead>
                    <tr>
                        <th>Year</th>
                        <th style="text-align: center;">Total Memberships Sold</th>
                    </tr>
                </thead>
                <tbody>
                    <?php while ($row = mysqli_fetch_assoc($result)): ?>

                    <tr>
                        <td><?php echo $row['year']; ?></td>
                        <td class="name-cell">
                            <span><?php echo $row['total_memberships']; ?></span>
                            <button type="button" class="detail-btn"
                                    onclick="window.location.href='membership_trends_report.php?year=<?php echo $row['year']; ?>'">
                                View <?php echo $row['year']; ?> Data
                            </button>
                        </td>

                    <?php endwhile; ?>
                </tbody>
            </table>
        <?php endif; ?>
    </div>
</div>
</body>
</html>