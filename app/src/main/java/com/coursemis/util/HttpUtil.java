package com.coursemis.util;

public class HttpUtil {
	//public static String server = "http://172.22.82.34:8080/KaoQing";
	//public static String server = "http://172.22.116.204:8080/CourseMis";
	//public static String server = "http://139.199.74.120:8080/CourseMis2";
	//普通android模拟器访问本地服务器
	//public static String server = "http://10.0.2.2:8080/CourseMis2";
	//genymotion android模拟器访问本地服务器
	//public static String server = "http://10.0.3.2:8080/CourseMis2";
	//本机开发局域网访问
	//夜神测试
//	public static String server = "http://39.108.112.190:8080/CourseMis";
	//本地服务器真机调试
	public static String server = "http://192.168.137.45:8080/CourseMis";
	//云服务器
	//public static String server = "http://39.108.112.190:8080/CourseMis";
	//
	public static String server_evaluate_suggest=server+"/evaluate_suggest.action";
	public static String server_login = server + "/loginCheck.action";
	public static String server_course_teacher = server + "/course_teacher.action";
	public static String server_course_add = server + "/course_add.action";
	public static String server_course_edit = server + "/course_edit.action";
	public static String server_pwd_change = server + "/pwd_change.action";
	public static String server_course_info = server + "/course_info.action";
	public static String server_course_del = server + "/course_del.action";
	public static String server_student_del = server + "/student_del.action";
	
	//public static String server_telNum_get = server + "/telNum_get.action";
	
	public static String server_note_get = server + "/note_get.action";
	
	public static String server_note_add = server + "/note_add.action";
	public static String server_student_course = server + "/student_course.action";
	public static String server_student_del_all = server + "/student_del_all.action";
	public static String server_get_student = server + "/student_get_all.action";
	public static String server_add_student = server + "/add_student.action";
	public static String server_course_student = server + "/course_student.action";
	public static String server_evaluate = server + "/evaluate.action";
	public static String server_evaluate_get = server + "/evaluate_get.action";
	
	public static String server_evaluate_zhu_get = server + "/evaluate_zhu_get.action";
	public static String server_evaluate_Sms_get = server + "/evaluate_sms_get.action";
	

	public static String server_teacher_course = server + "/teacher_course.action";
	public static String server_teacher_course_week = server + "/teacher_course_week.action";
	public static String server_teacher_course_time = server + "/teacher_course_time.action";
	public static String server_teacher_course_studentNames = server + "/teacher_course_studentNames.action";
	
	public static String server_teacher_course_randomAsk = server + "/teacher_course_randomAsk.action";
	public static String server_teacher_course_randomAsk_submit = server + "/teacher_course_randomAsk_submit.action";

	//public static String server_userinfo = server + "/servlet/LoginServlet";///
	
	public static String server_teacher_courseStudentCount = server + "/teacher_courseStudentCount.action";
	public static String server_teacher_SignIn = server + "/teacher_SignIn.action";
	public static String server_teacher_StudentCourse = server + "/teacher_StudentCourse.action";
	public static String server_student_StudentCourse = server + "/student_StudentCourse.action";
	public static String server_student_SignIn = server + "/student_SignIn.action";
	public static String server_student_SignInComfirm = server + "/student_SignInComfirm.action";
	public static String server_teacher_homework_select = server + "/teacher_homeworkSelect.action";
	public static String server_teacher_check_homework = server + "/teacher_checkhomework.action";
	public static String server_teacher_comment_homework = server + "/teacher_commenthomework.action";
	public static String server_student_StudentCourseCheckhomework = server + "/student_StudentCourseCheckhomework.action";
	public static String server_teacher_tupLoadHomework = server + "/teacher_tupLoadHomework.action";
	public static String server_student_supLoadHomework = server + "/student_supLoadHomework.action";
	public static String server_student_StudentClassCourseCheckhomework = server + "/student_StudentClassCourseCheckhomework.action";
	public static String server_student_StudentClassHMPath = server + "/student_StudentClassHMPath.action";
	public static String server_student_StudentClassCourseCheckWhichhomework = server + "/student_StudentClassCourseCheckWhichhomework.action";
	public static String server_ShareMediaData = server + "/ShareMediaData.action";
	public static String server_getMedia = server + "/getMedia.action";


}
