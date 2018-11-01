<?php
session_start();
 $inpfname = $_POST['fname'];
 $inplname = $_POST['lname'];
 $inpnot = $_POST['number'];
 $inpemail = $_POST['email'];
 $inpmob = $_POST['mob'];
 $inpaddress = $_POST['address'];
 
 $servername = "localhost";
 $user = "id7017369_sayok";
 $password = "Mypassword1#";
 $database = "id7017369_iplticket";
 $conn = mysqli_connect($servername,$user,$password,$database);
 
 /*echo"$inpfname";
 echo"$inplname";
 echo"$inpnot";
 echo"$inpemail";
 echo"$inpmob";
 echo"$inpaddress";*/
 
 // Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
} 
else
{
  $_SESSION["email"] =$inpemail ;
  $_SESSION["numberoftickets"] =$inpnot ;
      $sql = "INSERT INTO Booking (firstname, lastname,number_of_tickets,email, mobile, address)
   VALUES ('$inpfname' , '$inplname' ,' $inpnot', '$inpemail' , '$inpmob','$inpaddress')";
   
if (mysqli_query($conn, $sql))
{
    header('Location: https://iplbookticket.000webhostapp.com/seats.php');  
}
else{
    echo"not done";
}
}

mysqli_close($conn);
?>  