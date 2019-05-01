<?php

define('servername', 'localhost');
define('user', '<database user name>');
define('password', '<database password>');
define('database', '<database name>');

$conn = mysqli_connect(servername, user, password, database) or die("Connection failed: " . $conn->connect_error);
