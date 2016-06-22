<?php
class DB_Connect {
    private $conn;
 
    // 데이터베이스 연결
    public function connect() {
        require_once 'Config.php';

        $this->conn = new mysqli(DB_HOST, DB_USER, DB_PASSWORD, DB_DATABASE);
        mysqli_query($mysqli,"set names utf8");  
        return $this->conn;
    }
}
 
?>

