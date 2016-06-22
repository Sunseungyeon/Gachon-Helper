<?php
//데이터베이스에 GCM 사용을 위한 토큰을 유저정보에 업데이트 시켜준다.
 define('HOST','localhost');
 define('USER','wangki90');
 define('PASS','wangki1012!');
 define('DB','wangki90');

 $con = mysqli_connect(HOST,USER,PASS,DB) or die('Unable to Connect');


if (isset($_POST['stu_id']) && isset($_POST['reg_id'])){
	
	$stu_id = $_POST['stu_id'];
	$reg_id = $_POST['reg_id'];

	$sql = "UPDATE users SET reg_id = '$reg_id' WHERE stu_id = '$stu_id'";
	mysqli_query($con,$sql);

  }else{
	$response = array();
	$response["error"] = TRUE;
	$response["error_msg"] = "GCM ERROR";
	echo json_encode($response);
}
	mysqli_close($con);
?>
