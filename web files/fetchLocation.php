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
    
    $inpEmail = $_POST['email'];
    $inpPass = $_POST['password'];
    
    if (isEmailPresent($inpEmail)){
        if (doesPasswordMatch($inpEmail,$inpPass)){
            $query = "SELECT `latitude`,`longitude`,`time` FROM car_location WHERE `email` = '".$inpEmail."'";
            $sqli =  mysqli_query($conn, $query);
            $row = mysqli_fetch_array($sqli);
            
            echo json_encode(array('lat' => $row['latitude'], 'longi' => $row['longitude'], 'time' => $row['time']));
            
        }
        else{
            echo json_encode(array('lat' => "passMiss"));
        }
        
    }else{
        echo json_encode(array('lat' => "credMiss"));
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

function doesPasswordMatch($email,$pass){
    $query = "SELECT password FROM car_location where email = '".$email."'";
    if ($sql =  mysqli_query($GLOBALS['conn'], $query))
    {
        $row = mysqli_fetch_array($sql);
        
        if ($row['password'] == $pass){
            return true;
        }else{
            return false;
        }
    }
    
    return false;
    
}

?>
