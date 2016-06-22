<?php
//헬퍼에 대한 정보를 불러온다
 define('HOST','localhost');
 define('USER','wangki90');
 define('PASS','wangki1012!');
 define('DB','wangki90');

 $con = mysqli_connect(HOST,USER,PASS,DB) or die('Unable to Connect');


if (isset($_POST['helpy_stu_id'])) {

    // receiving the post params
    $helpy_stu_id = $_POST['helpy_stu_id'];
    $sql = "SELECT helpy_stu_id, helper_name, helper_stu_id, helper_category, helper_tickets, helper_phone, title, created_at  FROM connecter WHERE helpy_stu_id = '".$helpy_stu_id."' group by title";

    $search = mysqli_query($con,$sql);

    $result = array();
        while($row = mysqli_fetch_array($search)){
        array_push($result,array(
        "helpy_stu_id" => $row['helpy_stu_id'],
        "helper_name" => $row['helper_name'],
        "helper_stu_id" => $row['helper_stu_id'],
        "helper_category" => $row['helper_category'],
        "helper_tickets" => $row['helper_tickets'],
        "helper_phone" => $row['helper_phone'],
        "title" => $row['title'],
        "created_at" => $row['created_at']
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

