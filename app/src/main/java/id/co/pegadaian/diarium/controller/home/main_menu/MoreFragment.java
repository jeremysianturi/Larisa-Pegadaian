package id.co.pegadaian.diarium.controller.home.main_menu;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.adapter.MenuDinamisAdapter;
import id.co.pegadaian.diarium.controller.home.main_menu.eleave.EleaveList;
import id.co.pegadaian.diarium.controller.home.main_menu.payslip.PayslipList;
import id.co.pegadaian.diarium.controller.home.main_menu.sppd_online.SppdOnline;
import id.co.pegadaian.diarium.controller.login.LoginActivity;
import id.co.pegadaian.diarium.controller.home.main_menu.myevent.MyEventActivity;
import id.co.pegadaian.diarium.controller.home.main_menu.myteam.MyTeamActivity;
import id.co.pegadaian.diarium.controller.home.main_menu.mytime.checkin.CheckinActivity;
import id.co.pegadaian.diarium.controller.home.main_menu.report.ReportActivity;
import id.co.pegadaian.diarium.controller.home.main_menu.survey.SurveyActivity;
import id.co.pegadaian.diarium.controller.home.main_menu.community.CommunityActivity;
import id.co.pegadaian.diarium.controller.home.main_menu.employee_care.EmployeeCareActivity;
import id.co.pegadaian.diarium.controller.home.main_menu.personal_data.PersonalDataActivity;
import id.co.pegadaian.diarium.controller.home.main_menu.employee_corner.EmployeeCornerActivity;
import id.co.pegadaian.diarium.controller.home.main_menu.today_activity.TodayActivity;
import id.co.pegadaian.diarium.model.MenuModel;
import id.co.pegadaian.diarium.util.UserSessionManager;
import id.co.pegadaian.diarium.util.element.ProgressDialogHelper;


/**
 * A simple {@link Fragment} subclass.
 */
public class MoreFragment extends Fragment {

    MenuModel menuModel;
    List<MenuModel> listMenuModel;
    MenuDinamisAdapter menuAdapter;

    FrameLayout fmMenuFrag;
    Toolbar mToolbar;
    GridView gridview;
//    String[] menu ={
//            "Check In",
//            "Today Activity",
//            "Report Activity",
//            "Personal Data",
//            "My Team",
////            "My Event",
////            "One Sheet",
////            "Employee Corner",
//////            "FAQ",
////            "Community",
////            "Employee Survey",
////            "Employee Care",
////            "HR Wiki"
//            // "Add Today Activity"
//    };


    //    int[] image={
//            R.drawable.ic_checkin,
//            R.drawable.ic_today_activity,
//            R.drawable.ic_report,
//            R.drawable.ic_personal,
//            R.drawable.myteam,
////            R.drawable.myevent,
////            R.drawable.cv,
////            R.drawable.employee_corner,
//////            R.drawable.ic_faq,
////            R.drawable.community,
////            R.drawable.employeesurvey,
////            R.drawable.horn,
////            R.drawable.horn
//            //  R.drawable.ic_today_activity
//    };
    UserSessionManager session;
//    private ProgressDialogHelper progressDialogHelper;
    SwipeRefreshLayout swipeRefreshLayout;


    public MoreFragment() {
        // Required empty public constructor
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_more, container, false);
//        final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), R.style.for_menu);
//        LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);
        gridview = (GridView) view.findViewById(R.id.gridview);
        mToolbar = view.findViewById(R.id.toolbar);
        fmMenuFrag = view.findViewById(R.id.fm_layout_menufrag);
        session = new UserSessionManager(getActivity());
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);

        AndroidNetworking.initialize(getContext());
//        progressDialogHelper = new ProgressDialogHelper();

        // swipe refresh layout
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_morefrag);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getGridMenu();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        getGridMenu();
        return view;
    }

    private void getGridMenu(){

        SimpleDateFormat tgl = new SimpleDateFormat("yyyy-MM-dd");
//        String tRes = tgl.format(new Date());
        String tRes = tgl.format(new Date());

        System.out.println("cek tanggal get menu:" + tRes);
//        progressDialogHelper.showProgressDialog(getActivity(), "Getting data...");
        String urlGridMenu = session.getServerURLHCISAUTH()+"api/role_menus?begda_le="+tRes+"&endda_ge="+tRes+"&page=1&per_page=15&application_id=6&role_code=EMPLOYEE";
        System.out.println("get grid menu url: "+ urlGridMenu);

        AndroidNetworking.get(urlGridMenu)
                .addHeaders("Accept","application/json")
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization","Bearer "+session.getToken())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("response get grid menu : " + response + " SAMPE SINI YA");
                        listMenuModel = new ArrayList<MenuModel>();

                        try {
                            if(response.getInt("status")==200){

                                JSONArray jsonArrayMenu = response.getJSONArray("data");
                                System.out.println("panjang menu: " + jsonArrayMenu.length());

                                for (int a=0;a<jsonArrayMenu.length();a++) {
                                    JSONObject objMenu = jsonArrayMenu.getJSONObject(a).getJSONObject("menu");
                                    String name = objMenu.getString("menu_name");
//                                    JSONObject objname = objcommunication.getJSONObject("communication_name");
                                    String code = objMenu.getString("menu_code");
                                    String icon = objMenu.getString("menu_icon");

                                    menuModel = new MenuModel(code,name,icon);
                                    listMenuModel.add(menuModel);

                                    menuAdapter = new MenuDinamisAdapter(getActivity(), listMenuModel);
                                    gridview.setAdapter(menuAdapter);

                                    System.out.println("name first" + a + name);

                                    //        // hardcode menu SPPD Online
                                    //        String nameHardcode = "SPPD Online";
                                    //        String codeHardcode = "SPPDONLINE";
                                    //        menuModel = new MenuModel(codeHardcode,nameHardcode,"");
                                    //        listMenuModel.add(menuModel);
                                    //
                                    //        menuAdapter = new MenuDinamisAdapter(getActivity(),listMenuModel);
                                    //        gridview.setAdapter(menuAdapter);
                                    //        // hardcode menu SPPD Online
                                }

                                System.out.println("MODEL PERSONAL:" + "\n" + listMenuModel);
//                                progressDialogHelper.dismissProgressDialog(getActivity());

                            }else{

                                if (response.getInt("status") == 401){
                                    popUpLogin();
                                } else {
                                    Toast.makeText(getContext(),response.getString("message"),Toast.LENGTH_LONG).show();
                                }
//                                progressDialogHelper.dismissProgressDialog(getActivity());
                                System.out.println("error get menu di else: " + response.getString("message"));
                            }
                        }catch (Exception e){
//                            progressDialogHelper.dismissProgressDialog(getActivity());
                            System.out.println(" Error get menu dicatch: "+e.getMessage());
                        }

//                        MenuAdapter gridAdapter = new MenuAdapter(getActivity(),image,menu);
//                        gridview.setAdapter(gridAdapter);

                        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener()
                        {
                            @Override
                            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                                    long arg3) {
                                System.out.println("tes onclick api grid menu");
                                String clickedText = listMenuModel.get(position).getMenuCode();
                                System.out.println("clicked grid menu: " + clickedText);
//                Toast.makeText(getActivity(), "posisi"+clickedText, Toast.LENGTH_SHORT).show();
                                switch (clickedText) {
                                    case "CHKIN":

//                                        if (session.getStat()=="CO"){
//
//                                        }
                                        Intent a = new Intent(getActivity(), CheckinActivity.class);
                                        startActivity(a);
                                        break;
                                    case "TDACT":
                                        Intent b = new Intent(getActivity(), TodayActivity.class);
                                        b.putExtra("personal_number", session.getUserNIK());
                                        b.putExtra("name", session.getUserFullName());
                                        b.putExtra("status", "none");
                                        b.putExtra("position", session.getJob());
                                        b.putExtra("avatar", session.getAvatar());
                                        startActivity(b);
                                        break;
                                    case "RPACT":
                                        Intent c = new Intent(getActivity(), ReportActivity.class);
                                        startActivity(c);
                                        break;
                                    case "PRSNL":
                                        Intent intent = new Intent(getActivity(), PersonalDataActivity.class);
//                                        Intent intent = new Intent(getActivity(), SppdOnline.class);   // numpang sebentar
                                        startActivity(intent);
                                        break;
                                    case "MYTEM":
//                        Toast.makeText(getActivity(), "Coming soon!", Toast.LENGTH_SHORT).show();
                                        Intent d = new Intent(getActivity(), MyTeamActivity.class);
//                                        Intent d = new Intent(getActivity(), EleaveList.class);       // numpang sebentar
                                        startActivity(d);
                                        break;
                                    case "MYEVN":
//                                        Toast.makeText(getActivity(), "Coming soon!", Toast.LENGTH_SHORT).show();
                                        Intent e = new Intent(getActivity(), MyEventActivity.class);
                                        startActivity(e);
                                        break;
                                    case "One Sheet":
                                        Toast.makeText(getActivity(), "This menu not available", Toast.LENGTH_SHORT).show();
//
//                        Intent f = new Intent(getActivity(), OneSheetActivity.class);
//                        startActivity(f);
                                        break;
                                    case "Employee Corner":
//                        Toast.makeText(getActivity(), "This menu not available", Toast.LENGTH_SHORT).show();
                                        Intent g = new Intent(getActivity(), EmployeeCornerActivity.class);
                                        startActivity(g);
                                        break;
                                    case "FAQ":
                                        Toast.makeText(getActivity(), "not available", Toast.LENGTH_SHORT).show();
//                        Intent h = new Intent(getActivity(), QiscusChatActivity.class);
//                        System.out.println(session.getUserNIK()+"@diarium.co.id"+ "NIKNYA");
//                        h.putExtra("name","Dexy-Kiwari");
//                        h.putExtra("email",session.getUserNIK()+"@diarium.co.id");
//                        startActivity(h);
                                        break;
                                    case "Community":
//                        Toast.makeText(getActivity(), "This menu not available", Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(getActivity(), CommunityActivity.class);
                                        startActivity(i);
                                        break;
                                    case "Employee Survey":
//                        Toast.makeText(getActivity(), "This menu not available", Toast.LENGTH_SHORT).show();
                                        Intent j = new Intent(getActivity(), SurveyActivity.class);
                                        startActivity(j);
                                        break;
                                    case "Employee Care":
//                        Toast.makeText(getActivity(), "This menu not available", Toast.LENGTH_SHORT).show();
                                        Intent k = new Intent(getActivity(), EmployeeCareActivity.class);
                                        startActivity(k);
                                        break;
                                    case "HR Wiki":
                                        Toast.makeText(getActivity(), "This menu not available", Toast.LENGTH_SHORT).show();
//                        Intent l = new Intent(getActivity(), HRWikiActivity.class);
//                        startActivity(l);
                                        break;
                                    case "ESPPD":
                                        Intent l = new Intent(getActivity(), SppdOnline.class);
                                        startActivity(l);
                                        break;
                                    case "ELEAV" :
                                        Intent m = new Intent(getActivity(), EleaveList.class);
                                        startActivity(m);
                                        break;
                                    case "PAYSL" :
                                        Intent n = new Intent(getActivity(), PayslipList.class);
                                        startActivity(n);
                                        break;
                                    default :
                                        System.out.println("Invalid grade");
                                }
                            }
                        });

                    }
                    @Override
                    public void onError(ANError error) {
//                        progressDialogHelper.dismissProgressDialog(getActivity());
                        System.out.println( "Error get menu di onError: " + error.getErrorBody()+ "    " + error.getMessage());
                    }
                });
    }

    private void popUpLogin() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.layout_session_end);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(lp);
        dialog.setTitle("Input Code Here");
        Button btnYes = (Button) dialog.findViewById(R.id.btnYes);
        dialog.show();
        dialog.setCancelable(false);
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.setLoginState(false);
                Intent i = new Intent(getActivity(), LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
//        getGridMenu();
//                ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        System.out.println("INIKERUNGAK");
        inflater.inflate(R.menu.menu_activity, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.change:
//                AlertDialog.Builder builderChange = new AlertDialog.Builder(getContext());
//
//                builderChange.setTitle("Confirm");
//                builderChange.setMessage("Are you sure to change password ?");
//
//                builderChange.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//
//                    public void onClick(DialogInterface dialog, int which) {

                Intent intent = new Intent(getContext(),ChangePassword.class);
                startActivity(intent);
//                    }
//                });
//                builderChange.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//                AlertDialog alertChange = builderChange.create();
//                alertChange.show();
                return true;

            case R.id.logout:
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                builder.setTitle("Confirm");
                builder.setMessage("Are you sure to logout ?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog
                        session.setLoginState(false);
//                        session.logoutUser();
                        logout();

                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logout(){
//        progressDialogHelper.showProgressDialog(getActivity(),"Load Data...");
        AndroidNetworking.get(session.getServerURLHCISAUTH()+"api/auth/logout")
                .addHeaders("Accept","application/json")
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization","Bearer "+session.getToken())
                //.addJSONObjectBody(body)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("Response getCommunication: " + response);
                        // do anything with response
                        try {
                            if (response.getInt("status")==200){
                                Toast.makeText(getContext(),"Logout Success!", Toast.LENGTH_LONG);
//                                Snackbar.make(fmMenuFrag,"Logout Success!", Snackbar.LENGTH_LONG).show();
                                System.out.println("Print response success logout: " + response.get("message"));

                                Intent i = new Intent(getContext(), LoginActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                            }
                            else {
                                Toast.makeText(getContext(),"Logout Failed!", Toast.LENGTH_LONG);
//                                Snackbar.make(fmMenuFrag,"Logout Success!", Snackbar.LENGTH_LONG).show();
                                System.out.println("Print response failed logout: " + response.get("message"));

                                Intent i = new Intent(getContext(), LoginActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                            }
//                            progressDialogHelper.dismissProgressDialog(getActivity());

                        } catch (Exception e){
                            e.printStackTrace();
//                            progressDialogHelper.dismissProgressDialog(getActivity());
                            System.out.println("Print response error logout: " + e.getMessage());

                            Intent i = new Intent(getContext(), LoginActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);

                        }

                    }
                    @Override
                    public void onError(ANError error) {
//                        progressDialogHelper.dismissProgressDialog(getActivity());
                        System.out.println("get my post error"+ error.getErrorBody());
                        System.out.println("get my post error"+ error);
                        System.out.println("get my post error"+ error.getResponse());

                        Intent i = new Intent(getContext(), LoginActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
//        getGridMenu();
    }
}
