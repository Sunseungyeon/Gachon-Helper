<?php
 //DB로부터 랭킹 정보를 받아온다
 define('HOST','localhost');
 define('USER','wangki90');
 define('PASS','wangki1012!');
 define('DB','wangki90');

 $con = mysqli_connect(HOST,USER,PASS,DB) or die('Unable to Connect');

    

        $sql = "SELECT name,stu_id,tickets FROM users ORDER BY ABS(tickets) DESC limit 3";
        $search = mysqli_query($con, $sql);
	
	$result = array();

	while($row = mysqli_fetch_array($search)){
		array_push($result,array(
		"name" => $row['name'],
		"stu_id" => $row['stu_id'],
		"tickets" => $row['tickets']
	)
	);
	}
	echo json_encode(array("result"=>$result));
	    
    mysqli_close($con);

?>

