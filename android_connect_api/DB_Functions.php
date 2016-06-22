<?php
//헬퍼와 헬피의 연결정보를 담고있는 데이터를 DB에 저장
class DB_Functions {

    private $conn;

    // constructor
   
   
    function __construct() {
        require_once 'DB_Connect.php';
        // connecting to database
        $db = new Db_Connect();
        $this->conn = $db->connect();
    }

    // destructor
    function __destruct() {

    }

  public function storeConnecter($helpy_stu_id,$helper_name,$helper_stu_id,$helper_category,$helper_tickets,$helper_phone,$title){

        $stmt = $this->conn->prepare("INSERT INTO connecter(helpy_stu_id,helper_name,helper_stu_id,helper_category,helper_tickets,helper_phone,title,created_at) VALUES(?,?,?,?,?,?,?,NOW())");
        $stmt->bind_param("sssssss", $helpy_stu_id,$helper_name,$helper_stu_id,$helper_category,$helper_tickets,$helper_phone,$title);
        $result = $stmt->execute();
        $stmt->close();

        if($result){
        $stmt = $this->conn->prepare("SELECT * FROM connecter WHERE helpy_stu_id = ?");
        $stmt->bind_param("s",$helpy_stu_id);
        $stmt->execute();
        $stmt->store_result();
        $stmt->bind_result($v1,$v2,$v3,$v4,$v5,$v6,$v7,$v8,$v9);
        $connecter = array();


        while($stmt->fetch()){
                $connecter[helpy_stu_id] = $v2;
                $connecter[helper_name] = $v3;
                $connecter[helper_stu_id] = $v4;
                $connecter[helper_category] = $v5;
                $connecter[helper_tickets] = $v6;
                $connecter[helper_phone] = $v7;
                $connecter[title] = $v8;
                $connecter[created_at] = $v9;
        }
        $stmt->close();
        return $connecter;

        } else{
        return false;
        }

    }
}
?>

