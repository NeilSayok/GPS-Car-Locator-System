<?php
session_start();
$servername = "localhost";
 $user = "id7017369_priyom";
 $password = "Mypassword1#";
 $database = "id7017369_university";
 $conn = mysqli_connect($servername,$user,$password,$database);
 $_SESSION["email"];
 // Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
} 
    $variable = array();
    $variable['number_of_tickets'] ="";
    $variable['name'] = "";
    $variable['venue'] = "";
	$variable['team']= "";
	$variable['ticket_allocation']= "";
	$sql = "SELECT * FROM `Booking` WHERE email = '".$_SESSION["email"]."'";
		$result = mysqli_query($conn, $sql);


if (mysqli_num_rows($result) > 0) {
    // output data of each row
    while($row = mysqli_fetch_assoc($result)) 
	{
        if($row["email"] == $_SESSION["email"])
        {
            $variable['name']= $row['firstname']. " " .$row['lastname'];
			$variable['number_of_tickets']=$row['number_of_tickets'];
			$variable['venue']=$row['venue'];
			$variable['team']=$row['team'];
			$variable['ticket_allocation']=$row['ticket_allocation'];
			$variable['date']=$row['date'];
		}
	}
}

		/*	$variable['name']= "Priyom Saha";
			$variable['password']="abcd";
			$variable['mobile']="9804465884";
			$variable['email']="priyom1499@gmail.com";*/
			
	$subject = "Password";
    $from = "priyom1499@gmail.com";
    $headers = "MIME-Version: 1.0" . "\n";
    $headers = "Content-type:text/html;charset=UTF-8" . "\n"; 
    $template = file_get_contents("mail.html");
    
	$template = str_replace('##% name %##',$variable['name'],$template);
    $template = str_replace('##% team %##',$variable['team'],$template);
    $template = str_replace('##% venue %##',$variable['venue'],$template);
    $template = str_replace('##% date %##',$variable['date'],$template);
    $template = str_replace('##% seats %##',$variable['ticket_allocation'],$template);
    $template = str_replace('##% time %##',$variable['time'],$template);
    $template = str_replace('##% number_of_tickets %##',$variable['number_of_tickets'],$template);
	
   mail($_SESSION["email"],$subject,$template,$headers);
        //return();


mysqli_close($conn);
?>