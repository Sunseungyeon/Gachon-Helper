<?php
class DB_Connect {
    private $conn;
 
    // DB연결
    public function connect() {
        require_once 'include/Config.php';
         
        // mysql 데이터 베이스 연결
        $this->conn = new mysqli(DB_HOST, DB_USER, DB_PASSWORD, DB_DATABASE);
        //데이터베이스 한글 설정
        mysqli_query($mysqli,"set names utf8");
        return $this->conn;
    }
}
 
?>

