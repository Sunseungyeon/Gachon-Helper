<?php
//글을 등록할 때 DB로 글에 대한 정보를 올려준다.
require_once 'DB_Functions.php';
$db = new DB_Functions();

$response = array("error" => FALSE);

if(isset($_POST['category']) && isset($_POST['name']) && isset ($_POST['stu_id']) 
		&& isset ($_POST['title']) && isset ($_POST['content']) && isset ($_POST['see'])){
	
	
        $category = $_POST['category'];
        $name = $_POST['name'];
        $stu_id = $_POST['stu_id'];
        $title = $_POST['title'];
        $content = $_POST['content'];
        $see = $_POST['see'];
      
	
        $board = $db->storeBoard($category,$name,$stu_id,$title,$content,$see);
        		
        if($board){
        		$response['error'] = FALSE;
        		$response["board"]["category"] = $board["category"];
        		$response["board"]["name"] = $board["name"];
        		$response["board"]["stu_id"] = $board["stu_id"];
        		$response["board"]["title"] = $board["title"];
        		$response["board"]["content"] = $board["content"];
        		$response["board"]["see"] = $board["see"];
			$resoibse["board"]["password"] = $board["password"];
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
