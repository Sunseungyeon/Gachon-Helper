<?php

require_once 'DB_Functions.php';
$db = new DB_Functions();

$response = array("error" => FALSE);

if(isset($_POST['helpy_stu_id']) && isset($_POST['helper_name']) && isset ($_POST['helper_stu_id']) 
		&& isset ($_POST['helper_category']) && isset ($_POST['helper_tickets']) && isset ($_POST['helper_phone']) && isset ($_POST['title'])){
	
	
        $helpy_stu_id = $_POST['helpy_stu_id'];
        $helper_name = $_POST['helper_name'];
        $helper_stu_id = $_POST['helper_stu_id'];
        $helper_category = $_POST['helper_category'];
        $helper_tickets = $_POST['helper_tickets'];
        $helper_phone = $_POST['helper_phone'];
        $title = $_POST['title'];

	
        $connecter = $db->storeConnecter($helpy_stu_id,$helper_name,$helper_stu_id,$helper_category,$helper_tickets,$helper_phone,$title);
        		
        if($connecter){
        		$response['error'] = FALSE;
        		$response["connecter"]["helpy_stu_id"] = $connecter["helpy_stu_id"];
        		$response["connecter"]["helper_name"] = $connecter["helper_name"];
        		$response["connecter"]["helper_stu_id"] = $connecter["helper_stu_id"];
        		$response["connecter"]["helper_category"] = $connecter["helper_category"];
        		$response["connecter"]["helper_tickets"] = $connecter["helper_tickets"];
        		$response["connecter"]["helper_phone"] = $connecter["helper_phone"];
        		$response["connecter"]["title"] = $connecter["title"];
			echo json_encode($response);
        		
        		}else{
        				$response["error"] = TRUE;
        				$response["error_msg"] = "Unknown error occurred in registration!";
        				echo json_encode($response);
        		}
       }else{
        				$response["error"] = TRUE;
        				$response["error_msg"] = "Required parameters (-----------)s missing!";
        				echo json_encode($response);
        }
?>
        				
        		
        				
        		
