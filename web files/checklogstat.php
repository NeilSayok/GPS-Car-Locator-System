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
    
    $sql = "SELECT `log_stat` FROM car_location WHERE `email` = '".$inpemail."'";
    
    $result = mysqli_query($conn,$sql); 
    
    
    while($row=mysqli_fetch_array($result))
    {
        echo $row['log_stat'];
    }
    

}
?>