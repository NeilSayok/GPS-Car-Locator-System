<?php
 $servername = "localhost";

 $user = "id7017369_sayok";

 $password = "Mypassword1#";

 $database = "id7017369_iplticket";

 $conn = mysqli_connect($servername,$user,$password,$database);



 if (!$conn) {

    die("Connection failed: " . mysqli_connect_error());

}
else{
      $sql= "UPDATE `Seats_availability` SET `International(NW)`=100,`International(NE)`=100,`General(W)`=500,`General(E)`=500,`Premium(W)`=150,`Premium(E)`=150,`Platinum`=50 WHERE 1";
      mysqli_query($conn, $sql);
}
?>
