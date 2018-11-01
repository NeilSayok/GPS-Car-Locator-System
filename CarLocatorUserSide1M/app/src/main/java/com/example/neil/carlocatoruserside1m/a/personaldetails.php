<!DOCTYPE html>
<html>
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
	border: 2px solid black;
    border-radius: 15px;
    background-color: #f2f2f2;
    padding: 20px;
}
h3{
text-align:center;
}
.right{
float:right;
}
.left{
float:left;
}
</style>
<body>

<h3>Enter your personal information here</h3>

<div class="content">
  <form action="personal_details.php" method="post">
    <label><b>First Name :</b></label>
      <input type="text" placeholder="Enter First Name" name="fname" required>
      
      <label><b>Last Name :</b></label>
      <input type="text" placeholder="Enter Last Name" name="lname" required><br><br>
        
        <label><b>Number of tickets :</b></label>
		<select name ="number">
			<option value="0">0</option>
			<option value="1">1</option>
			<option value="2">3</option>
			<option value="4">4</option>
			<option value="5">5</option>
			<option value="6">6</option>
			<option value="7">7</option>
			<option value="8">8</option>
			<option value="9">9</option>
			<option value="10">10</option>
		</select>
        <br>
        <?php
             $servername = "localhost";
             $user = "id7017369_sayok";
             $password = "Mypassword1#";
             $database = "id7017369_iplticket";
             $conn = mysqli_connect($servername,$user,$password,$database);
            
            $sql= "SELECT * FROM Booking";
            $row = mysqli_query($conn, $sql)->fetch_assoc();
            
            ?>
        <label><b>Teams :</b></label>
		<select name ="number">
			<option value="<?php echo $row["id"];?>"><?php echo ?> </option>
		</select>
        <br>
        
      <label><b>Email :</b></label>
      <input type="text" placeholder="Enter Email" name="email" required>
      
      <label><b>Mobile Number :</b></label>
      <input type="text" placeholder="Enter your  mobile number" name="mob" required>
     <br><br>
      <label><b>Address :</b></label>
      <input type="text" placeholder="Enter Address" name="address" required>
      
        <button type="submit" class="signupbtn" name="submit">ENTER</button> 
  </form>
</div>

</body>
</html>
