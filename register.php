<?php
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Headers: Content-Type");
header("Content-Type: application/json");

include "db.php";

$data = json_decode(file_get_contents("php://input"), true);

$name = $data['name'];
$email = $data['email'];
$password = $data['password'];
$confirm_password = $data['confirmPassword'];

// Check if passwords match
if ($password !== $confirm_password) {
    echo json_encode([
        "success" => false,
        "message" => "Passwords do not match!"
    ]);
    exit;
}

// Check if email already exists
$checkEmail = $conn->query("SELECT * FROM users WHERE email='$email'");
if ($checkEmail->num_rows > 0) {
    echo json_encode([
        "success" => false,
        "message" => "Email already registered!"
    ]);
    exit;
}

// Insert into database
$sql = "INSERT INTO users (name, email, password) VALUES ('$name', '$email', '$password')";

if ($conn->query($sql)) {
    echo json_encode(["success" => true, "message" => "Registration successful"]);
} else {
    echo json_encode(["success" => false, "message" => $conn->error]);
}
?>
