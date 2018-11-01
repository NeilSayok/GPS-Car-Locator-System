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
    
    $inpEmailList = $_POST['emails'];
    //$inpEmailList = "priyom1499@gmail.com#!!#sdmsdm1998@gmail.com#!!#sayokdeymajumder1998@gmail.com#!!#sadsad";
    
    $emailarr = explode('#!!#',$inpEmailList);
    
    
    $query = "SELECT `log_stat` FROM car_location WHERE `email` = '".$inpEmailList."'";
    
    /*$sql =  mysqli_query($conn, $query);
    $row = mysqli_fetch_array($sql);
    echo $row['log_stat'];*/
    
    $arr = array();
    
    
    foreach($emailarr as $email){
        $query = "SELECT `log_stat` FROM car_location WHERE `email` = '".$email."'";
        $sql =  mysqli_query($conn, $query);
        $row = mysqli_fetch_array($sql);
        
        array_push($arr,$row['log_stat']);
        
    }
    
    echo json_encode($arr);
    
    
    
    
}
?>