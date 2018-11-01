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
    $inplat = $_POST['lat'];
    $inplong = $_POST['longi'];
    $inptime = $_POST['time'];
    
    
    /*$inpemail = "sayokdeymajumder1998@gmail.com";
    $inplat = "qqqqqq";
    $inplong = "wwwwww";
    $inptime = "ttttt";*/
    
    if (!isEmailPresent($inpemail)){
        echo "Error";
    }else{
        $sql = "UPDATE car_location SET `latitude` = '".$inplat."', `longitude`= '".$inplong."', `time`= '".$inptime."' WHERE `email` = '".$inpemail."'";
        
        echo mysqli_query($conn,$sql);
    }
    
}

function isEmailPresent($email){
    $query = "SELECT email FROM car_location where email = '".$email."'";
    if ($sql =  mysqli_query($GLOBALS['conn'], $query))
    {
        if(mysqli_num_rows($sql) >= 1){
            
            return true;
        }else{
           
            return false;
        }
    }
   
    return false;
}
?>