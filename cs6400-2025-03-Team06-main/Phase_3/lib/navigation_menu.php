<!--
  This is the new menu file for your project.
  It provides navigation to the dashboard and management pages.
  The 'active' class will highlight the current page in the nav bar.
-->
<div class="nav_bar">
    <ul>
        <li><a href="dashboard.php" <?php if($current_filename=='dashboard.php' || $current_filename=='index.php') echo "class='active'"; ?>>Dashboard</a></li>
        <li><a href="manage_holidays.php" <?php if($current_filename=='manage_holidays.php') echo "class='active'"; ?>>Manage Holidays</a></li>  
        <li><a href="update_population.php" <?php if($current_filename=='update_population.php') echo "class='active'"; ?>>Update Population</a></li>  
    </ul>
</div>
