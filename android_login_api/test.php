<?php
 //토큰을 활용해서 해당 토큰을 가진 유저에게 푸쉬메세지 보내기
 define('HOST','localhost');
 define('USER','wangki90');
 define('PASS','wangki1012!');
 define('DB','wangki90');

 $con = mysqli_connect(HOST,USER,PASS,DB) or die('Unable to connect');
 mysqli_query($mysqli,"set names utf8");


if(isset ($_POST['helpy_stu_id'])){

 $helpy_stu_id = $_POST['helpy_stu_id'];
 $sql = "SELECT reg_id FROM users WHERE stu_id = '$helpy_stu_id'";
 $result = mysqli_query($con,$sql);
 $row = mysqli_fetch_row($result);
 $helpy_token['token'] = $row[0];

$url    = 'https://android.googleapis.com/gcm/send';
 
//API KEY값 입력
$apiKey = 'AIzaSyCFw7QoBQzklpfEe5rLT7eYACDrusLLU1Q';
 
//registration id 입력
$registrationIDs = array($row[0]);
 
//전송할 푸시 데이터 생성
$message = iconv("EUC-KR", "UTF-8", "헬퍼 등장..!!(두둥))");
$fields = array('registration_ids' => $registrationIDs,
                'data' => array("message" => $message),);

 
 
//http header
$headers = array('Authorization: key=' . $apiKey,
                 'Content-Type: application/json');
 
//curl connection
$ch = curl_init();
 
curl_setopt($ch, CURLOPT_URL, $url);
curl_setopt($ch, CURLOPT_POST, true);
curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
curl_setopt($ch, CURLOPT_RETURNTRANSFER, true );
curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($fields));
 
$result = curl_exec($ch);
 
curl_close($ch);
 
echo $result;
}
?>

