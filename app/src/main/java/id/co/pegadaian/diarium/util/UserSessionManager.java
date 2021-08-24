package id.co.pegadaian.diarium.util;

/**
 * Created by Gilang on 12-Apr-16.
 */

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class UserSessionManager {

    SharedPreferences pref;
    Editor editor;
    Context _context;

    int PRIVATE_MODE = 0;

    static final String SERVER_URL = "server_access";
//  static final String SERVER_DEFAULT_API_URL = "https://apidiarium.digitalevent.id/api/";
//  static final String SERVER_DEFAULT_API_URL = "https://apidiarium-pegadaian.digitalevent.id/api/";
    static final String SERVER_DEFAULT_API_URL = "https://newlarisa.pegadaian.co.id/api/";
    static final String SERVER_DEFAULT_API_URL_HCIS = "https://hcis.pegadaian.co.id/";
    static final String SERVER_DEFAULT_API_URL_HCIS_AUTH = "https://hcis-auth.pegadaian.co.id/";
    static final String SERVER_DEFAULT_API_SPPD = "https://hc-trip.pegadaian.co.id/api";

    static final String SERVER_DEFAULT_MYCAREER= "https://testapi.digitalevent.id/hcis/";
    static final String SERVER_DEFAULT_MARKETPLACE= "http://apimarketplace.digitalevent.id/";
    static final String SERVER_DEFAULT_LMSDEMO = "https://lmsdemo.digitalevent.id/";
    static final String USER_BUSINESS_CODE = "business_code";
    static final String USER_NIK = "nik";
    static final String USER_PASSWORD = "password";
    static final String USER_JWT_TOKEN = "token_jwt";
    static final String USER_FULL_NAME = "user_full_name";
    static final String USER_NICK_NAME = "user_nick_name";
    static final String USER_IS_LOGIN = "is_login";
    static final String USER_NOTIF = "notification";
    static final String USER_FACE_CODE = "user_face_code";
    static final String USER_ADDRESS = "user_address";
    public static  String ACTIVITY_KEY;


    private static final String PREFER_NAME = "diarium_telkomsigma";

    public UserSessionManager(Context context){
        this._context = context;

        pref = _context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);

        editor = pref.edit();
    }

//    public void setUserSession(int id, String name, String username, String pass){
//
//        editor.putBoolean(USER_IS_LOGIN, true);
//        editor.putBoolean("notifStatus", true);
//        editor.putInt("nik", id);
//        editor.putString(USE, username);
//        editor.putString("password", pass);
//        editor.commit();

//    }

    public void setTrue(int id){
        editor.putInt("true", id);
        editor.commit();
    }

    public int getTrue() {
        return pref.getInt("true", 0);
    }

    public void setSiteId(String id){
        editor.putString("siteId", id);
        editor.commit();
    }

    public String getSiteId() {
        return pref.getString("siteId", "");
    }

    public void setTempPers(String id){
        editor.putString("pers", id);
        editor.commit();
    }

    public String getTempPers() {
        return pref.getString("pers", "");
    }

    public void setBpjs(String id){
        editor.putString("bpjs", id);
        editor.commit();
    }

    public String getBpjs() {
        return pref.getString("bpjs", "Update your data");
    }

    public void setNpwp(String id){
        editor.putString("npwp", id);
        editor.commit();
    }

    public String getNpwp() {
        return pref.getString("npwp", "Update your data");
    }
    public void setRek(String id){
        editor.putString("rek", id);
        editor.commit();
    }

    public String getRek() {
        return pref.getString("rek", "Update your data");
    }
    public void setKtp(String id){
        editor.putString("ktp", id);
        editor.commit();
    }

    public String getKtp() {
        return pref.getString("ktp", "Update your data");
    }

    public void setactivity_nameLMS(String id){
        editor.putString("activity_name", id);
        editor.commit();
    }

    public String getactivity_nameLMS(){
        return pref.getString("activity_name", "-");
    }

    public void setsession_nameLMS(String id){
        editor.putString("session_name", id);
        editor.commit();
    }

    public String getsession_nameLMS(){
        return pref.getString("session_name", "-");
    }

    public void setbegin_date_activityLMS(String id){
        editor.putString("begin_date_activity", id);
        editor.commit();
    }

    public String getbegin_date_activityLMS(){
        return pref.getString("begin_date_activity", "-");
    }

    public void setend_date_activityLMS(String id){
        editor.putString("end_date_activity", id);
        editor.commit();
    }

    public String getend_date_activityLMS(){
        return pref.getString("end_date_activity", "-");
    }


    public void setcompany_nameLMS(String id){
        editor.putString("company_name_lms", id);
        editor.commit();
    }

    public String getcompany_nameLMS(){
        return pref.getString("company_name_lms", "-");
    }

    public void setBatchLMS(String id){
        editor.putString("batch_lms", id);
        editor.commit();
    }

    public String getBatchLMS(){
        return pref.getString("batch_lms", "-");
    }

    public void setBatchNameLMS(String id){
        editor.putString("batch_name_lms", id);
        editor.commit();
    }

    public String getBatchNameLMS(){
        return pref.getString("batch_name_lms", "-");
    }

    public void setEventIdLMS(String id){
        editor.putString("event_id_lms", id);
        editor.commit();
    }

    public String getEventIdLMS(){
        return pref.getString("event_id_lms", "-");
    }

    public void setEventNameLMS(String id){
        editor.putString("event_name_lms", id);
        editor.commit();
    }

    public String getEventNameLMS(){
        return pref.getString("event_name_lms", "-");
    }

    public void setBeginDateLMS(String id){
        editor.putString("begin_date_lms", id);
        editor.commit();
    }

    public String getBeginDateLMS(){
        return pref.getString("begin_date_lms", "-");
    }

    public void setEndDateLMS(String id){
        editor.putString("end_date_lms", id);
        editor.commit();
    }

    public String getEndDateLMS(){
        return pref.getString("end_date_lms", "-");
    }

    public void setevent_curr_statLMS(String id){
        editor.putString("event_curr_stat_lms", id);
        editor.commit();
    }

    public String getevent_curr_statLMS(){
        return pref.getString("event_curr_stat_lms", "-");
    }

    public void setevnt_curr_statidLMS(String id){
        editor.putString("evnt_curr_statid_lms", id);
        editor.commit();
    }

    public String getevnt_curr_statidLMS(){
        return pref.getString("evnt_curr_statid_lms", "-");
    }

    public void setevent_statusLMS(String id){
        editor.putString("event_status_lms", id);
        editor.commit();
    }

    public String getevent_statusLMS(){
        return pref.getString("event_status_lms", "-");
    }

    public void setevent_stat_idLMS(String id){
        editor.putString("event_stat_id_lms", id);
        editor.commit();
    }

    public String getevent_stat_idLMS(){
        return pref.getString("event_stat_id", "-");
    }

    public void setlocation_idLMS(String id){
        editor.putString("location_id_lms", id);
        editor.commit();
    }

    public String getlocation_idLMS(){
        return pref.getString("location_id_lms", "-");
    }

    public void setlocationLMS(String id){
        editor.putString("location_lms", id);
        editor.commit();
    }

    public String getlocationLMS(){
        return pref.getString("location_lms", "-");
    }

    public void setcur_idLMS(String id){
        editor.putString("cur_id_lms", id);
        editor.commit();
    }

    public String getcur_idLMS(){
        return pref.getString("cur_id_lms", "-");
    }

    public void setcurriculumLMS(String id){
        editor.putString("curriculum_lms", id);
        editor.commit();
    }

    public String getcurriculumLMS(){
        return pref.getString("curriculum_lms", "-");
    }

    public void setevent_typeLMS(String id){
        editor.putString("event_type_lms", id);
        editor.commit();
    }

    public String getevent_typeLMS(){
        return pref.getString("event_type_lms", "-");
    }

    public void setparticipant_idLMS(String id){
        editor.putString("participant_id_lms", id);
        editor.commit();
    }

    public String getparticipant_idLMS(){
        return pref.getString("participant_id_lms", "-");
    }

    public void setpartcipant_nameLMS(String id){
        editor.putString("partcipant_name_lms", id);
        editor.commit();
    }

    public String getpartcipant_nameLMS(){
        return pref.getString("partcipant_name_lms", "-");
    }

    public void setparti_nicknmLMS(String id){
        editor.putString("parti_nicknm_lms", id);
        editor.commit();
    }

    public String getparti_nicknmLMS(){
        return pref.getString("parti_nicknm_lms", "-");
    }

    public void setCountTest(int id){
        editor.putInt("CountTest", id);
        editor.commit();
    }

    public int getCountTest(){
        return pref.getInt("CountTest", 0);
    }

    public void setCountLMS(int id){
        editor.putInt("count_question_lms", id);
        editor.commit();
    }

    public int getCountLMS(){
        return pref.getInt("count_question_lms", 0);
    }

    public void setCountQuestion(int id){
        editor.putInt("count_question", id);
        editor.commit();
    }

    public int getCountQuestion(){
        return pref.getInt("count_question", 0);
    }

    public void setThemeId(String id){
        editor.putString("themeid", id);
        editor.commit();
    }

    public String getThemeId(){
        return pref.getString("themeid", "");
    }

    public void setEmpId(String id){
        editor.putString("empid", id);
        editor.commit();
    }

    public String getEmpId(){
        return pref.getString("empid", "");
    }

    public void setGeneratedContentId(String id){
        editor.putString("contentId", id);
        editor.commit();
    }

    public String getGeneratedContentId(){
        return pref.getString("contentId", "");
    }

    public void setGeneratedCommentId(String id){
        editor.putString("commentId", id);
        editor.commit();
    }

    public String getGeneratedCommentId(){
        return pref.getString("commentId", "");
    }

    public void setGeneratedPostId(String id){
        editor.putString("postId", id);
        editor.commit();
    }

    public String getGeneratedPostId(){
        return pref.getString("postId", "");
    }

    public void setGenerateActivityId(String id){
        editor.putString("activityId", id);
        editor.commit();
    }

    public String getGenerateActivityId(){
        return pref.getString("activityId", "");
    }

    public void setCountType(String stat){
        editor.putString("countType", stat);
        editor.commit();
    }

    public String getCountType(){
        return pref.getString("countType", "");
    }

    public void setCurrentDate(String emot){
        editor.putString("currentDate", emot);
        editor.commit();
    }


    public String getCurrentDate(){
        return pref.getString("currentDate", "");
    }

    public void setData(String key, String value) {
        editor.putString(ACTIVITY_KEY + key, value);
        editor.commit();
    }

    public String getData(String key) {
        return pref.getString(ACTIVITY_KEY + key, "");
    }

    public void setJob(String job){
        editor.putString("job", job);
        editor.commit();
    }

    public String getJob(){
        return pref.getString("job", "");
    }

    public void setPersonalNumberLoginTeam(String name){
        editor.putString("yglogin", name);
        editor.commit();
    }

    public String getPersonalNumberLoginTeam(){
        return pref.getString("yglogin", "");
    }

    public void setStatus(String name){
        editor.putString("statusnya", name);
        editor.commit();
    }

    public String getStatus(){
        return pref.getString("statusnya", "");
    }

    public void setTodayActivityName(String name){
        editor.putString("todayActivityName", name);
        editor.commit();
    }

    public String getTodayActivityName(){
        return pref.getString("todayActivityName", "");
    }

    public void setTodayActivityPersonalNumber(String name){
        editor.putString("ActivityPersonalNumbe", name);
        editor.commit();
    }

    public String getTodayActivityPersonalNumber(){
        return pref.getString("ActivityPersonalNumbe", "");
    }

    public void setTodayActivity(String name){
        editor.putString("todayActivity", name);
        editor.commit();
    }

    public String getTodayActivity(){
        return pref.getString("todayActivity", "");
    }

    public void setName(String name){
        editor.putString("name", name);
        editor.commit();
    }

    public String getName(){
        return pref.getString("name", "");
    }

    public void setFormatFrom(String stat){
        editor.putString("formatfrom", stat);
        editor.commit();
    }

    public String getFormatFrom(){
        return pref.getString("formatfrom", "");
    }

    public void setFormatUntil(String stat){
        editor.putString("formatuntil", stat);
        editor.commit();
    }

    public String getFormatUntil(){
        return pref.getString("formatuntil", "");
    }

    public void setFrom(String stat){
        editor.putString("from", stat);
        editor.commit();
    }

    public String getFrom(){
        return pref.getString("from", "");
    }

    public void setUntil(String stat){
        editor.putString("until", stat);
        editor.commit();
    }

    public String getUntil(){
        return pref.getString("until", "CO");
    }

    public void setType(String stat){
        editor.putString("type", stat);
        editor.commit();
    }

    public String getType(){
        return pref.getString("type", "CO");
    }

    public void setGalleryId(String emot){
        editor.putString("gallry_id", emot);
        editor.commit();
    }


    public String getGalleryId(){
        return pref.getString("gallery_id", "");
    }

    public void setEventId(String emot){
        editor.putString("event_id", emot);
        editor.commit();
    }


    public String getEventId(){
        return pref.getString("event_id", "");
    }

    public void setEmot(String emot){
        editor.putString("emot", emot);
        editor.commit();
    }


    public String getEmot(){
        return pref.getString("emot", "");
    }

    public void setLatLon(String stat){
        editor.putString("latlon", stat);
        editor.commit();
    }

    public String getLatLon(){
        return pref.getString("latlon", "0");
    }

    public void setJawaban(String stat){
        editor.putString("jawaban", stat);
        editor.commit();
    }

    public String getJawaban(){
        return pref.getString("jawaban", "-");
    }

    public void setStat(String stat){
        editor.putString("stat", stat);
        editor.commit();
    }

    public String getStat(){
        return pref.getString("stat", "CO");
    }

    public void setToken(String token){
        editor.putString(USER_JWT_TOKEN, token);
        editor.commit();
    }

    public String  getToken(){
        return pref.getString(USER_JWT_TOKEN, "-");
    }

    public void setUserBusinessCode(String buscd){
        editor.putString(USER_BUSINESS_CODE, buscd);
        editor.commit();
    }

    public String  getUserBusinessCode(){
        return pref.getString(USER_BUSINESS_CODE, "-");
    }



    public void setUserNik(String id){
        editor.putString(USER_NIK, id);
        editor.commit();
    }

    public String  getUserNIK(){
        return pref.getString(USER_NIK, "-");
    }

    public void setUserFullName(String name){
        editor.putString(USER_FULL_NAME, name);
        editor.commit();
    }

    public String getUserFullName(){
        return pref.getString(USER_FULL_NAME, "-");
    }


    public void setBornDate(String name){
        editor.putString("borndate", name);
        editor.commit();
    }

    public void setStatusClickBornDate(String click){
        editor.putString("borndatestatusclick", click);
        editor.commit();
    }

    public  String getStatusClickBornDate() {
        return pref.getString("borndatestatusclick", "0");
    }

    public  String getBornDate() {
        return pref.getString("borndate", "-");
    }


    public void setIdentifier(String name){
        editor.putString("ide", name);
        editor.commit();
    }

    public  String getIdentifier() {
        return pref.getString("ide", "-");
    }

    public void setRoleLMS(String name){
        editor.putString("role_lms", name);
        editor.commit();
    }

    public  String getRoleLMS() {
        return pref.getString("role_lms", "-");
    }


    public void setTokenLdap(String name){
        editor.putString("token_ldap", name);
        editor.commit();
    }

    public  String getTokenLdap() {
        return pref.getString("token_ldap", "-");
    }
    public void setTokenMarket(String name){
        editor.putString("token_market", name);
        editor.commit();
    }

    public  String getTokenMarket() {
        return pref.getString("token_market", "-");
    }

    public void setTokenMyCareer(String token_mycareer){
        editor.putString("token_mycareer", token_mycareer);
        editor.commit();
    }

    public  String getTokenMyCareer() {
        return pref.getString("token_mycareer", "-");
    }


    public void setAvatar(String name){
        editor.putString("avatar", name);
        editor.commit();
    }

    public  String getAvatar() {
        return pref.getString("avatar", "-");
    }


    public void setUserNickName(String name){
        editor.putString(USER_NICK_NAME, name);
        editor.commit();
    }

    public  String getUserNickName() {
        return pref.getString(USER_NICK_NAME, "-");
    }

    public void setLoginState(boolean state){
        editor.putBoolean(USER_IS_LOGIN, state);
        editor.commit();
    }

    public boolean isLogin(){
        return pref.getBoolean(USER_IS_LOGIN, false);
    }

    public void setUserFaceCode(String id){
        editor.putString(USER_FACE_CODE, id);
        editor.commit();
    }

    public String  getUserFaceCode(){
        return pref.getString(USER_FACE_CODE, "");
    }

    public void setFMResponse(String res){

        editor.putString("fm_response", res);
        editor.commit();
    }

    public String  getFMResponse(){
        return pref.getString("fm_response", "-");
    }

    public void setFMMessage(String res){

        editor.putString("fm_message", res);
        editor.commit();
    }

    public String  getFMMessage(){
        return pref.getString("fm_message", "-");
    }

    public void setFMResult(int res){

        editor.putInt("fm_result", res);
        editor.commit();
    }

    public int  getFMResult(){
        return pref.getInt("fm_result", Activity.RESULT_CANCELED);
    }

    public void setServerURL(String url){

        editor.putString(SERVER_URL, url);
        editor.commit();
    }

    public String getServerURL(){
        return pref.getString(SERVER_URL,SERVER_DEFAULT_API_URL);
    }

    public String getServerURLHCIS(){
        return pref.getString(SERVER_URL,SERVER_DEFAULT_API_URL_HCIS);
    }

    public String getServerURLHCISAUTH(){
        return pref.getString(SERVER_URL,SERVER_DEFAULT_API_URL_HCIS_AUTH);
    }

    public String getServerURLSPPD(){
        return pref.getString(SERVER_URL,SERVER_DEFAULT_API_SPPD);
    }

    public void setServerMyCareer(String url){

        editor.putString(SERVER_URL, url);
        editor.commit();
    }

    public String getServerMyCareer(){
        return pref.getString(SERVER_URL,SERVER_DEFAULT_MYCAREER);
    }

    public void setServerMarketPlacer(String url){

        editor.putString(SERVER_URL, url);
        editor.commit();
    }

    public String getServerMarketPlacer(){
        return pref.getString(SERVER_URL,SERVER_DEFAULT_MARKETPLACE);
    }
    public void setServerLMSDEMO(String url){

        editor.putString(SERVER_URL, url);
        editor.commit();
    }

    public String getServerLMSDEMO(){
        return pref.getString(SERVER_URL,SERVER_DEFAULT_LMSDEMO);
    }

    public void setCheckedServer(boolean status){

        editor.putBoolean("checked_server", status);
        editor.commit();
    }

    public void setVersiServer (String version){
        editor.putString("version", version);
        editor.commit();
    }

    public String getVersiServer(){
        return pref.getString("version", "");
    }

    public boolean getNotificationStatus(){
        return pref.getBoolean(USER_NOTIF,false);
    }


    public void logoutUser(){
        editor.clear();
        editor.commit();

    }

    public void setLoginUsername(String usrnm){
        editor.putString("usrnm", usrnm);
        editor.commit();
    }

    public String getLoginUsername(){
        return pref.getString("usrnm", null);
    }
    public void setLoginPassword(String pswrd){
        editor.putString("pswrd", pswrd);
        editor.commit();
    }

    public String getLoginPassword(){
        return pref.getString("pswrd", null);
    }

}
