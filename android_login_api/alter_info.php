<?php
//유저 단과대학 변경
 define('HOST','localhost');
 define('USER','wangki90');
 define('PASS','wangki1012!');
 define('DB','wangki90');

 $con = mysqli_connect(HOST,USER,PASS,DB) or die('Unable to Connect');
 
	 if(isset($_POST['stu_id']) && isset($_POST['category'])){
		
		 $stu_id = $_POST['stu_id'];
	 	 $category = $_POST['category'];
	
		 $sql = "UPDATE users SET category = '$category' WHERE stu_id = '$stu_id'";
	 	 mysqli_query($con,$sql);

	}else{
		 $response = array();
		 $response["error"] = TRUE;
		 $response["error_msg"] = "정보 변경을 할 수 없습니다.";
		 echo json_encode($response);	
	
	}
	mysqli_close($con);
?>	
