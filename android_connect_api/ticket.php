<?php
//티켓 교환
 define('HOST','localhost');
 define('USER','wangki90');
 define('PASS','wangki1012!');
 define('DB','wangki90');

 $con = mysqli_connect(HOST,USER,PASS,DB) or die('Unable to Connect');


if (isset($_POST['helpy_stu_id']) && isset($_POST['helper_stu_id']) && isset($_POST['title'])) {

    // receiving the post params
    $helpy_stu_id = $_POST['helpy_stu_id'];
    $helper_stu_id = $_POST['helper_stu_id'];
    $title = $_POST['title'];
    
   
    $sql0 = "SELECT tickets FROM users WHERE stu_id = '$helpy_stu_id'";
    $result = mysqli_query($con, $sql0);
    $row = mysqli_fetch_row($result);
    $helpy_tickets['tickets'] = $row[0];	
	

    $sql = "UPDATE users SET tickets = tickets-1 WHERE stu_id = '$helpy_stu_id'";
    $sql2 = "UPDATE users SET tickets = tickets+1 WHERE stu_id = '$helper_stu_id'";
	if($helpy_tickets['tickets'] > 0){
	  mysqli_query($con, $sql);
 	  mysqli_query($con, $sql2);
	  $helpy_tickets['tickets'] = $helpy_tickets['tickets'] - 1;
	 
          $sql4 = "DELETE FROM connecter WHERE helpy_stu_id = '$helpy_stu_id' AND title = '$title'";
          $sql5 = "DELETE FROM board WHERE stu_id = '$helpy_stu_id' AND title = '$title'";
          mysqli_query($con,$sql4);
          mysqli_query($con,$sql5);

	  echo json_encode($helpy_tickets);
	}else{
	$response = array();
	$response["error"] = TRUE;
	$response["error_msg"] = "Required more tickets";
	echo json_encode($response);	
	}
    mysqli_close($con);

}
?>


