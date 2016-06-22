package com.example.user.gachonhelper.app;

/**
 * Created by wangki on 2016-05-07.
 */
public class AppConfig {
    //서버 로그인
    public static String URL_LOGIN = "http://wangki90.cafe24.com/android_login_api/login.php";
    // 서버 회원가입
    public static String URL_REGISTER = "http://wangki90.cafe24.com/android_login_api/register.php";
    //서버 글쓰기
    public static String URL_BOARDWRITE = "http://wangki90.cafe24.com/android_board_api/write.php";
    //서버 글 목록 가져오기
    public static String URL_GETTEXT = "http://wangki90.cafe24.com/android_board_api/read.php";
    //조회수 올리기
    public static String URL_SEE = "http://wangki90.cafe24.com/android_board_api/see.php";
    //헬퍼,헬피 이어주기
    public static String URL_CONNECTER = "http://wangki90.cafe24.com/android_connect_api/connecter.php";
    //헬퍼 목록 가져오기
    public static String URL_GETHELPER = "http://wangki90.cafe24.com/android_connect_api/read_helper.php";
    //특정 헬퍼 불러오기
    public static String URL_HELPERS = "http://wangki90.cafe24.com/android_connect_api/helper_list.php";
    //헬퍼 티켓 수 가져오기
    public static String URL_TICKETS = "http://wangki90.cafe24.com/android_connect_api/ticket.php";
    //랭킹 정보 불러오기
    public static String URL_RANK = "http://wangki90.cafe24.com/android_login_api/rank.php";
    //푸쉬 메세지 보내기
    public static String URL_PUSH = "http://wangki90.cafe24.com/android_login_api/test.php";
    //정보 변경 요청
    public static String URL_ALTER = "http://wangki90.cafe24.com/android_login_api/alter_info.php";
    //비밀번호 변경 요청
    public static String URL_ALTER_PASS = "http://wangki90.cafe24.com/android_login_api/alter_pass.php";
}
