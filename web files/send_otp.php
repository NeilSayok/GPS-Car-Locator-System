<?php
$servername = "<hostname>";
$user = "<user name>";
$password = "<password>";
$database = "<database name>";
$conn = mysqli_connect($servername,$user,$password,$database);
 
 // Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}else{
     $variable = array();
    $variable['email'] = $_POST['email'];
    $variable['name'] = "";
    $variable['otp'] = "";
    $variable['reg_id'] = "";

    if (isEmailPresent($variable['email'])){
        $variable['name'] = getName($variable['email']);
        $variable['otp'] = getOTP($variable['email']);
        $variable['reg_id'] = getVehId($variable['email']);
    }
    else{
        $variable['name'] = getName($variable['email']);
        $variable['reg_id'] = getVehId($variable['email']);
        $variable['otp'] = mt_rand(100000,999999);
        while (isOTPPresent($variable['otp'])){
            $variable['otp'] = mt_rand(100000,999999);
        }
        
        $email = $variable['email'] ;
        $name =  $variable['name'];
        $otp = $variable['otp'];
        $vehid = $variable['reg_id'];

        $query =  "INSERT INTO OTP (email, name, otp, regid) VALUES ('$email' , '$name' , '$otp' , '$vehid')";
        $res = mysqli_query($conn,$query);
         
        
     
    }

    $subject = "User Verification";
    $from = "Someemail@someserver.domain";
    $headers = "MIME-Version: 1.0" . "\n";
    $headers .= "Content-type:text/html;charset=UTF-8" . "\n"; 
    $template = file_get_contents("email.html");
    
    $template = str_replace('##% name %##',$variable['name'],$template);
    $template = str_replace('##% otp %##',$variable['otp'],$template);
    $template = str_replace('##% email %##',$variable['email'],$template);
    $template = str_replace('##% regid %##',$variable['reg_id'],$template);
   
    
    if(mail($variable['email'],$subject,$template,$headers))
        echo "Mail_Sent"; 
    else
        echo "Mail_not_sent";
    
}
   





function getOTP($email){
    $query = "SELECT otp from OTP where email = '".$email."'";
    if ($sql = mysqli_query($GLOBALS['conn'],$query)){
        $row = mysqli_fetch_array($sql);
        
        return $row['otp'];
    }
    
    return "<br>";
}


function getName($email){
    $query = "SELECT name from car_location where email = '".$email."'";
    if ($sql = mysqli_query($GLOBALS['conn'],$query)){
        $row = mysqli_fetch_array($sql);
        
        return $row['name'];
    }
    
    return "";
}

function getVehId($email){
    $query = "SELECT reg_id from car_location where email = '".$email."'";
    if ($sql = mysqli_query($GLOBALS['conn'],$query)){
        $row = mysqli_fetch_array($sql);
        
        return $row['reg_id'];
    }
    
    return "";
}

function isEmailPresent($email){
    $query = "SELECT email FROM OTP where email = '".$email."'";
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

function isOTPPresent($otp){
    $query = "SELECT otp FROM OTP where otp = '".$otp."'";
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

function test($variable){
    echo "isEmailPresent()___________________________<br>";
    isEmailPresent($variable['email'] );
    echo "<br>isOTPPresent()_________________________<br>";
    isOTPPresent($variable['otp']);
    echo "<br>getOTP()_______________________________<br>";
    getOTP($variable['email'] );
    echo "<br>getName()______________________________<br>";
    getName($variable['email'] );
    echo "_______________________________________<br>";

}

mysqli_close($conn);
?>
