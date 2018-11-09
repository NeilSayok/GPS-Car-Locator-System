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
    $inplogstat = $_POST['stat'];
    
    /*$inpemail = "sdmsdm1998@gmail.com";
    $inplogstat = '0';*/
    
    $sql = "UPDATE car_location SET `log_stat` = '".$inplogstat."' WHERE `email` = '".$inpemail."'";
    
    echo mysqli_query($conn,$sql); 
    
    

}
?>
