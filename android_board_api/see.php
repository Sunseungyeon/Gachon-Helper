<?php
//글이 클릭될 경우, 조회수를 올려준다.
 define('HOST','localhost');
 define('USER','wangki90');
 define('PASS','wangki1012!');
 define('DB','wangki90');

 $con = mysqli_connect(HOST,USER,PASS,DB) or die('Unable to Connect');


if (isset($_POST['stu_id']) && isset($_POST['title'])) {

    // receiving the post params
    $stu_id = $_POST['stu_id'];
    $title = $_POST['title'];

    $sql = "UPDATE board SET see = see + 1 WHERE stu_id = '$stu_id' AND title = '$title'";
      mysqli_query($con, $sql);  
      mysqli_close($con);

}
?>


