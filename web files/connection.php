<?php

define('servername', 'localhost');
define('user', 'id8959544_project_travel_admin');
define('password', 'Mypassword1#');
define('database', 'id8959544_project_travel');

$conn = mysqli_connect(servername, user, password, database) or die("Connection failed: " . $conn->connect_error);
