<?php
 $servername = "localhost";
 $user = "id7017369_sayok";
 $password = "Mypassword1#";
 $database = "id7017369_iplticket";
 $conn1 = mysqli_connect($servername,$user,$password,$database);
 
 if (!$conn1) {
    die("Connection failed: " . mysqli_connect_error());
}
 
 $_SESSION["email"];
 $_SESSION["numberoftickets"];
  $_SESSION["seats"];
 
 
$sql= "SELECT * FROM Seats_availability";
$row1 = mysqli_query($conn1, $sql)->fetch_assoc();

$sql= "SELECT * FROM Booking WHERE email = '".$_SESSION["email"]."'";
$row2 = mysqli_query($conn1, $sql)->fetch_assoc();
 //echo $row2["firstname"];
if ($_SESSION["seats"] == '1')
{
     $x = 1; 
    $s = array();
    $used = 101 - (int)$row1["International(NW)"];
    while($x <= $_SESSION["numberoftickets"]) {
        array_push($s,$used);
        $used=$used + 1;
        $x++; 
} 
//echo arrray_to_string($s);
}
elseif ( $_SESSION["seats"] == '2')
{
    $x = 1; 
    $s = array();
    $used = 101 - (int)$row1["International(NE)"];
    while($x <= $_SESSION["numberoftickets"]) {
        
        //$s= "$used" .",";
        array_push($s,$used);
        $used=$used + 1;
        $x++;
    }   
}
elseif ($_SESSION["seats"] == '3')
{
    $x = 1; 
    $s = array();
    $used = 101 - (int)$row1["General(W)"];
    while($x <= $_SESSION["numberoftickets"]) {
        
        //$s= "$used" .",";
        array_push($s,$used);
        $used=$used + 1;
        $x++;
    }   
}
elseif ($_SESSION["seats"] == '4')
{
    $x = 1; 
    $s = array();
    $used = 101 - (int)$row1["General(E)"];
    while($x <= $_SESSION["numberoftickets"]) {
        
        //$s= "$used" .",";
        array_push($s,$used);
        $used=$used + 1;
        $x++;
    }   
}
elseif ($_SESSION["seats"] == '5')
{
    
    $x = 1; 
    $s = array();
    $used = 101 - (int)$row1["General(E)"];
    while($x <= $_SESSION["numberoftickets"]) {
        
        //$s= "$used" .",";
        array_push($s,$used);
        $used=$used + 1;
        $x++;
    }   
}
elseif ($_SESSION["seats"] == '6')
{
    $a = $row["Premium(E)"]- $_SESSION["numberoftickets"];
    $q = "UPDATE `Seats_availability` SET `Premium(E)`= ".$a;
    mysqli_query($conn, $q);
}
else
{
    $a = $row["Platinum"]- $_SESSION["numberoftickets"];
   $q = "UPDATE `Seats_availability` SET `Platinum`= ".$a;
    mysqli_query($conn, $q);
}
function arrray_to_string($arr)
{
    $str = "";
    foreach ($arr as $a)
    {
        $str = $str." ".$a;
    }
   return $str;
}
mysqli_close($conn1);
?>