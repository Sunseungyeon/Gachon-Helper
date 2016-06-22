<?php
 //회원가입
require_once 'include/DB_Functions.php';
$db = new DB_Functions();
 
// json response array
$response = array("error" => FALSE);
 
if (isset($_POST['name']) && isset($_POST['stu_id']) && isset($_POST['password']) && isset ($_POST['category'])) {
 
    // 회원가입란의 정보를 받아온다
    $name = $_POST['name'];
    $stu_id = $_POST['stu_id'];
    $password = $_POST['password'];
    $category = $_POST['category'];
    // 같은 학번의 유저가 존재하는지 확인
    if ($db->isUserExisted($stu_id)) {
        // 같은 학번이 존재할경우
        $response["error"] = TRUE;
        $response["error_msg"] = "User already existed with " . $stu_id;
        echo json_encode($response);
    } else {
        //  존재하지 않을경우, 계정 생성
        $user = $db->storeUser($name, $stu_id, $password, $category);
        if ($user) {
            // 계정생성 성공할 경우
            $response["error"] = FALSE;
            $response["uid"] = $user["unique_id"];
            $response["user"]["name"] = $user["name"];
            $response["user"]["stu_id"] = $user["stu_id"];
	    $response["user"]["category"] = $user["category"];
	    $response["user"]["tickets"] = $user["tickets"];
            $response["user"]["created_at"] = $user["created_at"];
            $response["user"]["updated_at"] = $user["updated_at"];
            echo json_encode($response);
        } else {
            // 계정생성 실패
            $response["error"] = TRUE;
            $response["error_msg"] = "Unknown error occurred in registration!";
            echo json_encode($response);
        }
    }
} else {
    $response["error"] = TRUE;
    $response["error_msg"] = "Required parameters (name, student ID or password) is missing!";
    echo json_encode($response);
}
?>

