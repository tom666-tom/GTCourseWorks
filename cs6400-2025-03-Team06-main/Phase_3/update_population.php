<?php
include('lib/common.php');

$selected_state = '';
$selected_city = '';
$current_population = null;

if (isset($_GET['msg']) && $_GET['msg'] === 'updated') {
    array_push($query_msg, "Successfully updated population.");
}

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $state = mysqli_real_escape_string($db, $_POST['state']);
    $city = mysqli_real_escape_string($db, $_POST['city']);
    $new_population = mysqli_real_escape_string($db, $_POST['new_population']);
    $new_population = str_replace(',', '', $new_population);

    if (!is_numeric($new_population)) {
        array_push($error_msg, "Population must be a non-negative integer.");
    } else if((int)$new_population > 2147483647) {
        array_push($error_msg, "Population value exceeds maximum allowed. Please enter a valid number.");
    } else {
        $sql = "UPDATE City 
                SET population = $new_population 
                WHERE state = '$state' AND city_name = '$city'";
                       
        $result = mysqli_query($db, $sql);
        
        if ($result) {
            header("Location: update_population.php?state=$state&city=$city&msg=updated");
            exit();
        } else {
            array_push($error_msg, "Failed to update population: " . mysqli_error($db));
        }
    }
}
else if (isset($_GET['state'])) {
    $selected_state = mysqli_real_escape_string($db, $_GET['state']);
    
    if (isset($_GET['city'])) {
        $selected_city = mysqli_real_escape_string($db, $_GET['city']);
        $sql = "SELECT population 
                FROM City 
                WHERE state = '$selected_state' AND city_name = '$selected_city'";
        
        $result = mysqli_query($db, $sql);
        
        if ($result && mysqli_num_rows($result) > 0) {
            $pop_row = mysqli_fetch_assoc($result);
            $current_population = $pop_row['population'];
        } else {
            array_push($error_msg, "Could not find population data for $selected_city, $selected_state.");
        }
    }
}

$sql = "SELECT DISTINCT state FROM City ORDER BY state ASC";
$states_result = mysqli_query($db, $sql);

if (!empty($selected_state)) {
    $sql = "SELECT city_name 
            FROM City 
            WHERE state = '$selected_state' 
            ORDER BY city_name ASC";
    $cities_result = mysqli_query($db, $sql);
    $result = $cities_result;
}
?>

<html lang="en">
<head>
    <title>Update Population</title>
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

    <div class="table-container">
        <h2 class="report-table-subtitle">Update City Population
            </br>Please select a State and City.
        </h2>
        <form method="POST" action="update_population.php">
        <input type="hidden" name="state" value="<?php echo $selected_state; ?>">
            <input type="hidden" name="city" value="<?php echo $selected_city; ?>">

            <table style="margin-bottom: 24px;">
                <tr>
                    <td style="font-size: 16px;font-weight: 600">Select a State</td>
                    <td style="width: 20px;"></td>
                    <td>
                        <select name="state" id="stateSelect" class="report-select">
                            <option value="">-- Select a State --</option>
                            <?php
                            if (isset($states_result)) {
                                while ($row = mysqli_fetch_assoc($states_result)) {
                                    $state = $row['state'];
                                    $is_selected = ($state == $selected_state) ? 'selected' : '';
                                    echo "<option value='". $state ."' $is_selected>". $state ."</option>";
                                }
                            }
                            ?>
                        </select>
                    </td>
                </tr>
            </table>

        <?php if (!empty($selected_state)) : ?>
            <table style="margin-bottom: 24px;">
                <tr>
                    <td style="font-size: 16px;font-weight: 600">Select a City</td>
                    <td style="width: 20px;"></td>
                    <td>
                        <select name="city" id="citySelect" class="report-select">
                            <option value="">-- Select a City --</option>
                            <?php
                            if (isset($cities_result)) {
                                while ($row = mysqli_fetch_assoc($cities_result)) {
                                    $city = $row['city_name'];
                                    $is_selected = ($city == $selected_city) ? 'selected' : '';
                                    echo "<option value='". $city ."' $is_selected>". $city ."</option>";
                                }
                            }
                            ?>
                        </select>
                    </td>
                </tr>
            </table>
        <?php endif; ?>

        <?php if (!empty($selected_city) && $current_population !== null) : ?>
            <table style="margin-top: 20px;">
                <tr>
                    <th style="text-align: left; padding: 15px 25px 15px 0px;;">Current Population</th>
                    <th><?php echo number_format($current_population); ?></th>
                </tr>
                <tr>
                    <th style="text-align: left; padding: 15px 25px 15px 0px;" >New Population</th>
                    <td>
                            <div class="input-group">
                                <input type="text"
                                       name="new_population"
                                       id="newPopulation"
                                       class="styled-input"
                                       placeholder="Enter new population"
                                       pattern="[0-9,]*" />
                                <button type="submit" class="submit-btn">
                                    Update Population
                                </button>
                            </div>
                    </td>
                </tr>
            </table>
        <?php endif; ?>
        </form>
    </div>

    <script>
        document.getElementById('stateSelect').addEventListener('change', function() {
            if (this.value) {
                window.location.href = 'update_population.php?state=' + encodeURIComponent(this.value);
            }
        });
        const citySelect = document.getElementById('citySelect');
        if (citySelect) {
            citySelect.addEventListener('change', function() {
                if (this.value) {
                    window.location.href = 'update_population.php?state=<?php echo urlencode($selected_state); ?>&city=' + encodeURIComponent(this.value);
                }
            });
        }
    </script>
</body>
</html>