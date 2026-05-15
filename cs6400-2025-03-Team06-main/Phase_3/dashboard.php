<?php
include('lib/common.php');

$sql = "SELECT
            (SELECT COUNT(*) FROM Store) AS store_count,
            (SELECT COUNT(*) FROM Manufacturer) AS manufacturer_count,
            (SELECT COUNT(*) FROM Product) AS product_count,
            (SELECT COUNT(*) FROM Membership) AS membership_sold_count;";

$result = mysqli_query($db, $sql);

if ($result) {
    $stats = mysqli_fetch_assoc($result);
}
?>

<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Peachtree - Data Warehouse Reports (Themed)</title>
    <link rel="stylesheet" href="css/report.css">
    <style>
        .hero{
            display:grid; grid-template-columns: 1fr 360px; gap:28px; align-items:start; margin-bottom:8px;
        }

        .brand-row{display:flex; align-items:flex-start; gap:28px}

        .logo{
            height: 240px; width: auto; display:flex; align-items:center; justify-content:center;
        }
        .logo img{height: 100%; width: auto;display: block; object-fit: contain;}

        .hero-title{
            font-weight:800; font-size:36px; line-height:1; margin:8px 0 8px; color:var(--deep-green);
        }
        .hero-sub{color:var(--muted-green); font-size:18px;}

        /* Quick actions card */
        .quick{
            background:linear-gradient(180deg, rgba(20,73,47,0.02), rgba(20,73,47,0.01));
            border-radius:var(--radius); padding:14px; box-shadow:0 6px 18px var(--shadow);
            border:1px solid rgba(20,73,47,0.04);
        }
        .quick-title{font-size:20px; color:var(--deep-green);}
        .quick-action-row{display:flex;justify-content:space-between;align-items:center;
            padding: 10px 10px 10px 0px;
            border-radius:10px;border:1px dashed rgba(20,73,47,0.03)}
        .quick-sub{font-size:16px; color:var(--muted-green); }
        .quick p{margin:0 0 14px; color:var(--muted-green)}

        .btn{display:inline-block; padding:10px 16px; background:var(--peach); color:var(--accent-contrast); border-radius:999px; text-decoration:none; font-weight:600; box-shadow:0 4px 8px rgba(0,0,0,0.06)}


        /* Key signals */
        .signals{display:grid; grid-template-columns:repeat(4,1fr); gap:18px; margin:18px 0 26px}
        .signal{
            background:linear-gradient(180deg, rgba(20,73,47,0.03), rgba(20,73,47,0.01));
            border-radius:12px; padding:18px; border:1px solid rgba(20,73,47,0.04);
        }
        .signal .k{font-size:14px; letter-spacing:2px; color:var(--muted-green);}
        .signal .v{font-size:24px; font-weight:700; margin-top:8px}
        .signal p{margin:8px 0 0; color:var(--muted-green); font-size:14px}


        /* Report navigator grid */
        .navigator{display:grid; grid-template-columns:repeat(2,1fr); gap:18px}
        .card{
            background: linear-gradient(180deg, rgba(20,73,47,0.02), rgba(20,73,47,0.01));
            border-radius:14px; padding:18px; border:1px solid rgba(20,73,47,0.04); box-shadow:0 8px 30px rgba(20,73,47,0.03);
        }
        .card h3{margin:0 0 6px; font-size:18px}
        .card p{margin:0 0 12px; color:var(--muted-green)}

        .open-row{display:flex; justify-content:flex-start}
        .pill{background:transparent; border:2px solid var(--peach); color:var(--peach); padding:8px 14px; border-radius:999px; font-weight:700; text-decoration:none}

</style>
</head>
<body>
<div class="container">
    <header class="hero">
        <div>
            <div class="brand-row">
                <div class="logo"><img src="img/Logo.png"></div>
                <div>
                    <h1 class="hero-title">Peachtree Saving Club
                        <br/>Reports Hub</h1>
                    </br>
                    <p class="hero-sub">
                        Navigate every executive-ready insight in one view.
                        </br>Tap a report to dive into detailed dashboards.
                        </br></br>Status: Demo data loaded.
                    </p>
                </div>
            </div>
        </div>

        <aside class="quick">
            <h1 class="quick-title">Quick Actions</h1>
            <p>Simple tools for maintaining data warehouse.</p>
            <div style="display:flex;flex-direction:column;gap:10px">
                <div class="quick-action-row">
                    <div class="quick-sub">Update Holiday</div>
                    <a class="btn" href="manage_holidays.php">Update</a>
                </div>
                <div class="quick-action-row">
                    <div class="quick-sub">Update Population</div>
                    <a class="btn" href="update_population.php">Update</a>
                </div>
            </div>
        </aside>
    </header>
    <section>
        <h4 style="letter-spacing:3px;color:var(--muted-green);margin-bottom:12px">KEY SIGNALS</h4>
        <div class="signals">
            <div class="signal">
                <div class="k">STORES</div>
                <div class="v"><?php echo number_format($stats['store_count']); ?></div>
                <p>Across the Country</p>
            </div>
            <div class="signal">
                <div class="k">MANUFACTURERS</div>
                <div class="v"><?php echo number_format($stats['manufacturer_count']); ?></div>
                <p>Key partners feeding the warehouse</p>
            </div>
            <div class="signal">
                <div class="k">PRODUCTS</div>
                <div class="v"><?php echo number_format($stats['product_count']); ?></div>
                <p>Active SKUs with complete traceability</p>
            </div>
            <div class="signal">
                <div class="k">MEMBERSHIPS SOLD</div>
                <div class="v"><?php echo number_format($stats['membership_sold_count']); ?></div>
                <p>Lifetime members with current status</p>
            </div>
        </div>
    </section>
    <div style="height: 5px;"></div>
    <section>
        <h4 style="letter-spacing:3px;color:var(--muted-green);margin:20px 0 12px">REPORT NAVIGATOR</h4>
        <div class="navigator">
            <div class="card">
                <h3>Manufacturer Report</h3>
                <p>Manufacturer and their product portfolios.</p>
                <div class="open-row"><a href="manufacturer_report.php" class="pill">Open →</a></div>
            </div>
            <div class="card">
                <h3>Category Report</h3>
                <p>Detailed insights into product categories.</p>
                <div class="open-row"><a href="category_report.php" class="pill">Open →</a></div>
            </div>
            <div class="card">
                <h3>Actual versus Predicted Revenue for Speaker units</h3>
                <p>Compare real revenue performance against forecasts.</p>
                <div class="open-row"><a href="revenue_report_speakers.php" class="pill">Open →</a></div>
            </div>
            <div class="card">
                <h3>Store Revenue by Year by State</h3>
                <p>Analyze revenue trends across states and years.</p>
                <div class="open-row"><a href="store_revenue_report.php" class="pill">Open →</a></div>
            </div>
            <div class="card">
                <h3>Air Conditioners on Groundhog Day</h3>
                <p>Analyze air conditioner sales on Groundhog Day.</p>
                <div class="open-row"><a href="groundhog_day_report.php" class="pill">Open →</a></div>
            </div>
            <div class="card">
                <h3>State with Highest Volume by Category</h3>
                <p>Identify top-performing states for each product category.</p>
                <div class="open-row"><a href="state_volume_report.php" class="pill">Open →</a></div>
            </div>
            <div class="card">
                <h3>Revenue by Population</h3>
                <p>Compare revenue performance against population metrics.</p>
                <div class="open-row"><a href="population_revenue_report.php" class="pill">Open →</a></div>
            </div>
            <div class="card">
                <h3>Membership Trends</h3>
                <p>Track membership growth and engagement metrics over time.</p>
                <div class="open-row"><a href="membership_trends_report.php" class="pill">Open →</a></div>
            </div>
        </div>
    </section>

</div>
</body>
</html>
