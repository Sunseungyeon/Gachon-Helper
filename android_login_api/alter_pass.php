<?php
//유저 비밀번호 변경
require_once 'include/DB_Functions.php';
$db = new DB_Functions();

// json response array
$response = array("error" => FALSE);
//학번, 비밀번호, 변경 할 비밀번호 받기
if (isset($_POST['stu_id']) && isset($_POST['password']) && isset($_POST['c_password'])) {

    // 각 값들 저장
    $stu_id = $_POST['stu_id'];
    $password = $_POST['password'];
    $c_password = $_POST['c_password'];

    // 학번과 비밀번호가 맞는지 확인
    $user = $db->getUserByIdAndPassword($stu_id, $password);

    if ($user) {
        // 맞을 경우, 비밀번호 교체
        define('HOST','localhost');
        define('USER','wangki90');
        define('PASS','wangki1012!');
        define('DB','wangki90');


	$con = mysqli_connect(HOST,USER,PASS,DB) or die('Unable to Connect');

        //salt값을 받아서 db에 저장된 encrypt_password를 업데이트 시켜준다
        $sql1 = "SELECT salt FROM users WHERE stu_id = '$stu_id'";
        $result = mysqli_query($con,$sql1);
        $row = mysqli_fetch_row($result);
        $salt['salt'] = $row[0];
	
	$encrypted_password = $db->checkhashSSHA($row[0], $c_password);

        $sql = "UPDATE users SET encrypted_password = '$encrypted_password'  WHERE stu_id = '$stu_id'";
        mysqli_query($con,$sql);
        mysqli_close($con);

	$response["salt"] = $row[0];
	$response["encrypted_password"] = $encrypted_password;
        echo json_encode($response);
    } else {
        // 유저 정보 불일치
        $response["error"] = TRUE;
        $response["error_msg"] = "회원정보가 일치하지 않습니다.";
        echo json_encode($response);
    }
}

?>
