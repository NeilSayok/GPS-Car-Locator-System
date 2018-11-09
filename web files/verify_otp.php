<?php

$servername = "<hostname>";
$user = "<user name>";
$password = "<password>";
$database = "<database name>";
$conn = mysqli_connect($servername,$user,$password,$database);
     
     // Check connection
    if ($conn->connect_error) {
        die("Connection failed: " . $conn->connect_error);
    }
    $otp = $_POST['otp_conf'];
    $email = $_POST['email'];

    
    
    $query = "SELECT otp FROM `OTP` WHERE email = '".$email."'";
    
    if ($sql =  mysqli_query($conn, $query)){
         $row = mysqli_fetch_array($sql);
         //echo $row['otp']."<br>".$otp."<br>".$email;
         if ($row['otp'] == $otp){
             $query1 = "DELETE FROM `OTP` WHERE `OTP`.`email` = '".$email."' AND `OTP`.`otp` = ".$otp;
             $query2 = "UPDATE `car_location` SET `verified` = '1' WHERE `car_location`.`email` = '".$email."'";
             if (mysqli_query($conn, $query1) && mysqli_query($conn, $query2))
                echo "verified";
            else
                echo "not_verified";
         }else{
             echo "not_verified";
         }
    }else
        echo "server_error";
    

mysqli_close($conn);
?>
