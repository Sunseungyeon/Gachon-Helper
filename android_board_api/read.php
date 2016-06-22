<?php
//단과대학별 글목록을 불러온다.
 define('HOST','localhost');
 define('USER','wangki90');
 define('PASS','wangki1012!');
 define('DB','wangki90');
 
 $con = mysqli_connect(HOST,USER,PASS,DB) or die('Unable to Connect');
 
 
if (isset($_POST['category'])) {
 
    // receiving the post params
    $category = $_POST['category'];
    $sql = "SELECT * FROM board WHERE category = '".$category."' ORDER BY created_at DESC";

    $search = mysqli_query($con,$sql);

    $result = array();
	while($row = mysqli_fetch_array($search)){
	array_push($result,array(
	"category" => $row['category'],
	"name" => $row['name'],
	"stu_id" => $row['stu_id'],
	"title" => $row['title'],
	"content" => $row['content'],
	"see" => $row['see'],
	"password" => $row['password'],
	"created_at" => $row['created_at'],
	"updated_at" => $row['updated_at']
	)
	);
}
	echo json_encode(array("result"=>$result));
	mysqli_close($con);
	
              
} else {
  // required post params is missing
	$response = array();
    $response["error"] = TRUE;
    $response["error_msg"] = "Required parameters category is missing!";
    echo json_encode($response);
}
?>
 


