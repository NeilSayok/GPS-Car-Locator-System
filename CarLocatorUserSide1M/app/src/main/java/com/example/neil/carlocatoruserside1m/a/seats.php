<?php
session_start();
?>
<!DOCTYPE html>
<html>
<head>
  <link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
</head>
<style>
input[type=text], select {
    width: 100%;
    padding: 12px 20px;
    margin: 8px 0;
    display: inline-block;
    border: 1px solid #ccc;
    border-bottom-left-radius: 25px;
	border-bottom-right-radius: 25px;
	border-top-left-radius: 25px;
	border-top-right-radius: 25px;
    box-sizing: border-box;
}

button[type=submit] {
    width: 100%;
    background-color: #4CAF50;
    color: white;
    padding: 14px 20px;
    margin: 8px 0;
    border: none;
    border-radius: 25px;
    cursor: pointer;
}

button[type=submit]:hover {
    background-color: #45a049;
}

.content {
	 margin:10%;
	 margin-top:50px;
   margin-bottom:5px;
	border: 2px solid black;
    border-radius: 15px;
    background-color: #f2f2f2;
    padding: 20px;
}
h3{
text-align:center;
}
h4
{
    margin-left:30%;
}
}
</style>
<body>

<h3>Enter Ticket information here</h3>
<?php
 $servername = "localhost";
 $user = "id7017369_sayok";
 $password = "Mypassword1#";
 $database = "id7017369_iplticket";
 $conn = mysqli_connect($servername,$user,$password,$database);

$sql= "SELECT * FROM Seats_availability";
$row = mysqli_query($conn, $sql)->fetch_assoc();

?>
<div class="content">
  <form action="updateseats.php" method="post">
        <label><b>Number of tickets :</b></label>
		<select name ="number">
			<option value="1">INTERNATIONAL SEAT (NORTH WEST)&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		    	<?php
                    echo $row["International(NW)"];
                ?>
			</option>
			<option value="2">INTERNATIONAL SEAT (NORTH EAST)&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<?php
                    echo $row["International(NE)"];
            ?>
			</option>
			<option value="3">GENERAL ADMISSION SEATINGS (WEST)&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<?php
                    echo $row["General(W)"];

            ?>
			</option>
			<option value="4">GENERAL ADMISSION SEATINGS (EAST)&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<?php
                    echo $row["General(E)"];
            ?>
			</option>
			<option value="5">PREMIUM SEATINGS (WEST)&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<?php
                    echo $row["Premium(W)"];
            ?>
			</option>
			<option value="6">PREMIUM SEATINGS (EAST)&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<?php
                    echo $row["Premium(E)"];
            ?>
			</option>
			<option value="7">PLATINUM SEATINGS&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<?php
                    echo $row["Platinum"];
            ?>
			</option>
		</select>
        <br>
        <button type="submit" class="signupbtn" name="submit">ENTER</button>
  </form>
</div>
    <h4>PLATINUM SEATINGS                    <progress value=<?php echo $row["Platinum"]; ?> max="50"></progress></h4><br>
    <h4>PREMIUM SEATINGS (WEST)              <progress value="<?php echo $row["Premium(W)"];?>" max="150"></progress></h4><br>
    <h4>PREMIUM SEATINGS (EAST)              <progress value="<?php echo $row["Premium(E)"]; ?>" max="150"></progress></h4><br>
    <h4>INTERNATIONAL SEAT (NORTH WEST)      <progress value="<?php echo $row["International(NW)"];?>" max="100"></progress></h4><br>
    <h4>INTERNATIONAL SEAT (NORTH EAST)      <progress value="<?php echo $row["International(NE)"];?>" max="100"></progress></h4><br>
    <h4>GENERAL ADMISSION SEATINGS (WEST)    <progress value="<?php echo $row["General(W)"]; ?>" max="500"></progress></h4><br>
    <h4>GENERAL ADMISSION SEATINGS (EAST)    <progress value="<?php echo $row["General(E)"]; ?>" max="500"></progress></h4><br>
</body>
</html>
