<?php

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

//유저 정보 저장

 
 public function storeUser($name, $stu_id, $password, $category) {

	$tickets = "10";
	$uuid = uniqid('', true);
	$hash = $this->hashSSHA($password);
	$encrypted_password = $hash["encrypted"]; // 비밀번호 암호화
	$salt = $hash["salt"]; // salt값

	$stmt = $this->conn->prepare("INSERT INTO users(unique_id, name, stu_id,category,tickets, encrypted_password, salt, created_at) VALUES(?, ?, ?, ?, ?, ?, ?, NOW())");

	$stmt->bind_param("sssssss", $uuid, $name, $stu_id, $category, $tickets, $encrypted_password, $salt);

	$result = $stmt->execute();
	$stmt->close();

	// 저장됐는지 확인

	if ($result) {

	$stmt = $this->conn->prepare("SELECT * FROM users WHERE stu_id = ?");
	$stmt->bind_param("s", $stu_id);
	$stmt->execute();
	$stmt->store_result();
	$stmt->bind_result($v1,$v2,$v3,$v4,$v5,$v6,$v7,$v8,$v9,$v10);

	$user = array();

	while($stmt->fetch()){

	$user[unique_id] = $v2;
	$user[name] = $v3;
	$user[stu_id] = $v4;
	$user[category] = $v5;
	$user[tickets] = $v6;
	$user[encrypted_password] = $v7;
	$user[salt] = $v8;
	$user[created_at] = $v9;
	$user[updated_at] = $v10;

	}

	$stmt->close();

	return $user;

	} else {

	return false;
	}

}

/**

* ID와 비밀번호를 가지고 유저를 확인

*/

public function getUserByIdAndPassword($stu_id, $password) {

	$stmt = $this->conn->prepare("SELECT * FROM users WHERE stu_id = ? ");
	$stmt->bind_param("s", $stu_id);

	if ($stmt->execute()) {

	$stmt->store_result();
	$stmt->bind_result($v1,$v2,$v3,$v4,$v5,$v6,$v7,$v8,$v9,$v10,$v11);

	$user=array();

	while($stmt->fetch()){

	$user[unique_id] = $v2;
	$user[name] = $v3;
	$user[stu_id] = $v4;
	$user[category] = $v5;
	$user[tickets] = $v6;
	$user[encrypted_password] = $v7;
	$user[salt] = $v8;
	$user[created_at] = $v9;
	$user[updated_at] = $v10;
	$user[reg_id] = $v11;
	}

	$stmt->close();



	// 비밀번호가 맞는지 확인, salt 적용 후 암호화 된 비밀번호와 비교

	$salt = $user['salt'];
	$encrypted_password = $user['encrypted_password'];
	$hash = $this->checkhashSSHA($salt, $password);

	// 암호화된 비밀번호와 비교

	if ($encrypted_password == $hash) {

	// 맞을경우 유저 정보를 리턴

		return $user;		
	}
	}else {
		return NULL;
	}
    
}
 

 
    /**
     * 유저가 존재하는지 확인
     */
    public function isUserExisted($stu_id) {
        $stmt = $this->conn->prepare("SELECT stu_id from users WHERE stu_id = ?");
 
        $stmt->bind_param("s", $stu_id);
 
        $stmt->execute();
 
        $stmt->store_result();
 
        if ($stmt->num_rows > 0) {
            // 유저 존재 
            $stmt->close();
            return true;
        } else {
            // 유저 존재 X
            $stmt->close();
            return false;
        }
    }
 
    /**
     * 비밀번호 암호화
     * @param password
     * 리턴값 : salt, 암호화된 비밀번호
     */
    public function hashSSHA($password) {
 
        $salt = sha1(rand());
        $salt = substr($salt, 0, 10);
        $encrypted = base64_encode(sha1($password . $salt, true) . $salt);
        $hash = array("salt" => $salt, "encrypted" => $encrypted);
        return $hash;
    }
 
    /**
     * 비밀번호 해독
     * @param salt, password
     * returns hash값
     */
    public function checkhashSSHA($salt, $password) {
 
        $hash = base64_encode(sha1($password . $salt, true) . $salt);
 
        return $hash;
    }
 
}
 
?>

