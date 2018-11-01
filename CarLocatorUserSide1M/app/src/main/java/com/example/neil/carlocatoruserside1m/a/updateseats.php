<?php

session_start();
 $inpnumber = $_POST['number'];
 $servername = "localhost";
 $user = "id7017369_sayok";
 $password = "Mypassword1#";
 $database = "id7017369_iplticket";
 $conn = mysqli_connect($servername,$user,$password,$database);
 
 if (!$conn) {
    die("Connection failed: " . mysqli_connect_error());
}
 
 $_SESSION["email"];
 $_SESSION["numberoftickets"];
 $_SESSION["seats"]=$inpnumber;
 
include 'allocateseats.php';
if ($inpnumber == '1')
{
    
    $a = $row1["International(NW)"]- $_SESSION["numberoftickets"];
   $q = "UPDATE `Seats_availability` SET `International(NW)`= ".$a;
    mysqli_query($conn, $q);
    #echo "International(NW)";
    #echo arrray_to_string($s);
}
elseif ($inpnumber == '2')
{

    $a = $row["International(NE)"]- $_SESSION["numberoftickets"];
   $q = "UPDATE `Seats_availability` SET `International(NE)`= ".$a;
    mysqli_query($conn, $q);
    #echo "International(NE)";
    #echo arrray_to_string($s);
}
elseif ($inpnumber == '3')
{
    
    $a = $row["General(W)"]- $_SESSION["numberoftickets"];
    $q = "UPDATE `Seats_availability` SET `General(W)`= ".$a;
    mysqli_query($conn, $q);
    #echo "General(W)";
    #echo arrray_to_string($s);
}
elseif ($inpnumber == '4')
{
    
    $a = $row["General(E)"]- $_SESSION["numberoftickets"];
    $q = "UPDATE `Seats_availability` SET `General(E)`= ".$a;
    mysqli_query($conn, $q);
    #echo "General(E)";
    #echo arrray_to_string($s);
}
elseif ($inpnumber == '5')
{

    $a = $row["Premium(W)"]- $_SESSION["numberoftickets"];
    $q = "UPDATE `Seats_availability` SET `Premium(W)`= ".$a;
    mysqli_query($conn, $q);
#    echo "Premium(W)";
   # echo arrray_to_string($s);
}
elseif ($inpnumber == '6')
{
    $a = $row["Premium(E)"]- $_SESSION["numberoftickets"];
    $q = "UPDATE `Seats_availability` SET `Premium(E)`= ".$a;
    mysqli_query($conn, $q);
    #echo "Premium(E)";
    #echo arrray_to_string($s);
}
else
{
    $a = $row["Platinum"]- $_SESSION["numberoftickets"];
   $q = "UPDATE `Seats_availability` SET `Platinum`= ".$a;
    mysqli_query($conn, $q);
    #echo "Platinum";
    #echo arrray_to_string($s);
}
include send_ticket.php;
echo "mail sent";
mysqli_close($conn);
?>