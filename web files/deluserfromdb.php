<?php

$servername = "localhost";
$user = "id7082910_admin";
$password = "Mypassword1#";
$database = "id7082910_carlocator";
$conn = mysqli_connect($servername,$user,$password,$database);


if ($conn->connect_error)
{
    die("Connection failed: " . $conn->connect_error);
}else{
    
    $inpemail = $_POST['email'];
    
    //$inpemail = "sdmsdm1998@gmail.com";
    
    
    $sql = "DELETE FROM `car_location` WHERE `car_location`.`email` = '".$inpemail."'";
    
    $result = mysqli_query($conn,$sql);
    
    $sql =  "DELETE FROM `OTP` WHERE `OTP`.`email` = '".$inpemail."'";
    
    $result = mysqli_query($conn,$sql);
}

    
    
    
?>