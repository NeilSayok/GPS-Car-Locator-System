<?php

$servername = "<hostname>";
$user = "<user name>";
$password = "<password>";
$database = "<database name>";
$conn = mysqli_connect($servername,$user,$password,$database);


if ($conn->connect_error)
{
    die("Connection failed: " . $conn->connect_error);
}else{
    
    $inpemail = $_POST['email'];
    
    $sql = "DELETE FROM `car_location` WHERE `car_location`.`email` = '".$inpemail."'";
    
    $result = mysqli_query($conn,$sql);
    
    $sql =  "DELETE FROM `OTP` WHERE `OTP`.`email` = '".$inpemail."'";
    
    $result = mysqli_query($conn,$sql);
}

    
    
    
?>
