<?php
require_once 'include/DB_Functions.php';
$db = new DB_Functions();
 
// json response array
$response = array("error" => FALSE);

if (isset($_POST['stu_id']) && isset($_POST['password']) && isset($_POST['token'])) {
 
    // 학번, 비밀번호, 토큰(GCM) 값을 받음
    $stu_id = $_POST['stu_id'];
    $password = $_POST['password'];
    $reg_id = $_POST['token'];
    
    
    // 유저 정보를 활용해 유저가 존재하는지 확인
    $user = $db->getUserByIdAndPassword($stu_id, $password);
 
    if ($user) {
        // 존재할 경우
        $response["error"] = FALSE;
        $response["uid"] = $user["unique_id"];
        $response["user"]["name"] = $user["name"];
        $response["user"]["stu_id"] = $user["stu_id"];
	$response["user"]["category"] = $user["category"];
	$response["user"]["tickets"] = $user["tickets"];
        $response["user"]["created_at"] = $user["created_at"];
        $response["user"]["updated_at"] = $user["updated_at"];	

 	define('HOST','localhost');
 	define('USER','wangki90');
 	define('PASS','wangki1012!');
 	define('DB','wangki90');

 	$con = mysqli_connect(HOST,USER,PASS,DB) or die('Unable to Connect');
	$sql = "UPDATE users SET reg_id = '$reg_id' WHERE stu_id = '$stu_id'";
	mysqli_query($con,$sql);
	mysqli_close($con);
	
        echo json_encode($response);
    } else {
        // 존재하지 않을경우
        $response["error"] = TRUE;
        $response["error_msg"] = "Login credentials are wrong. Please try again!";
        echo json_encode($response);
    }
} else {
    // 서버로 변수가 전달 안됐을 경우
    $response["error"] = TRUE;
    $response["error_msg"] = "Required parameters student ID or password is missing!";
    echo json_encode($response);
}
?>

