<?php
//게시판 글에 대한 정보를 DB에 저장한다.

class DB_Functions {
 
    private $conn;
 
    // constructor
    function __construct() {
        require_once 'DB_Connect.php';
        // DB연결
        $db = new Db_Connect();
        $this->conn = $db->connect();
    }
 
    // destructor
    function __destruct() {
         
    }
  //게시판 정보 저장
  public function storeBoard($category,$name,$stu_id,$title,$content,$see){

	$password = '0000';
        $stmt = $this->conn->prepare("INSERT INTO board(category,name,stu_id,title,content,see,password,created_at) VALUES(?,?,?,?,?,?,?,NOW())");
        $stmt->bind_param("sssssss", $category,$name,$stu_id,$title,$content,$see,$password);
        $result = $stmt->execute();
        $stmt->close();

        if($result){
        $stmt = $this->conn->prepare("SELECT * FROM board WHERE title = ?");
        $stmt->bind_param("s",$title);
        $stmt->execute();
        $stmt->store_result();
        $stmt->bind_result($v1,$v2,$v3,$v4,$v5,$v6,$v7,$v8,$v9,$v10);
        $board = array();
	

        while($stmt->fetch()){
        	$board[category] = $v2;
        	$board[name] = $v3;
        	$board[stu_id] = $v4;
        	$board[title] = $v5;
        	$board[content] = $v6;
        	$board[see] = $v7;
        	$board[password] = $v8;
        	$board[created_at] = $v9;
        	$board[updated_at] = $v10;
        }        
        $stmt->close();
	return $board;
      
	} else{
	return false;
	}

    }
}
?>


