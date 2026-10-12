<?php
$host = "127.0.0.1";
$port = "3307"; 
$user = "root";
$pass = "";
$db   = "homecare";

$conn = new mysqli($host, $user, $pass, $db, $port);

if ($conn->connect_error) {
    die(json_encode(["error" => "DB Connection Failed: " . $conn->connect_error]));
}
?>