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
    $inppsw = $_POST['psw'];

    //$inpemail = 'sdmsdm1998@gmail.com';
    //$inppsw = 'Mypassword1#';
    
    $query = "SELECT * FROM `car_location` WHERE `email` = '".$inpemail."' OR `reg_id` = '".$inpemail."'";
    
    
    if($result = mysqli_query($conn,$query)){
        $row = mysqli_fetch_array($result);
        
        if(count($row) > 0)
           if($row['password'] == $inppsw){
                $array = array('name' => $row['name'],'reg_id' => $row['reg_id'],'email' => $row['email'], 'verified' => $row['verified'],'status' => 'OK');
           }else{
             $array = array('status' => 'passMiss','email'=> $inpemail);
           }
        else{
                $array = array('status' => 'credMiss','email'=> $inpemail);
            } 
            
            echo json_encode($array);

    }

}
?>