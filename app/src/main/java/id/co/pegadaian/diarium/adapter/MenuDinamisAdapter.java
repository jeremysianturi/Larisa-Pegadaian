package id.co.pegadaian.diarium.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.squareup.picasso.Picasso;

import java.util.List;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.controller.home.main_menu.mytime.checkin.CheckinActivity;
import id.co.pegadaian.diarium.model.MenuModel;
import id.co.pegadaian.diarium.util.UserSessionManager;

public class MenuDinamisAdapter extends BaseAdapter {
    Context context;
    private MenuModel model;
    private List<MenuModel> listModel;
    UserSessionManager session;
    TextView menuTitle;
    ImageView mImageView;
    LayoutInflater layoutInflater;

    public MenuDinamisAdapter(Context context, List<MenuModel> listModel) {
        this.context = context;
        this.listModel = listModel;
    }

    @Override
    public int getCount() {
        return listModel.size();
    }

    @Override
    public Object getItem(int position) {
        return listModel.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        session = new UserSessionManager(context.getApplicationContext());

        model = listModel.get(position);
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_gridmenu, null);
        }

        mImageView = (ImageView) convertView.findViewById(R.id.imView);
        menuTitle = (TextView) convertView.findViewById(R.id.txtJudul);

            menuTitle.setText(listModel.get(position).getMenuName());
            System.out.println("test menu title: " + model.getMenuName() + "\n" + "status: " + session.getStat());

            // checkin or checkout gridmenu
            if (session.getStat().equals("CO") &&  model.getMenuName().equals("Check In")) {
                menuTitle.setText("Check In");
            } else if (session.getStat().equals("CI") && model.getMenuName().equals("Check In")){
                menuTitle.setText("Check Out");
            }

            try {
                Picasso.get().load(listModel.get(position).getMenuIcon()).error(R.drawable.profile).into(mImageView);
            } catch (Exception e){
                Picasso.get().load(R.drawable.profile).into(mImageView);
            }

//        LinearLayout menu = (LinearLayout) convertView.findViewById(R.id.item);
//        menu.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                Intent hariian = new Intent(context , CheckinActivity.class);
//                context.startActivity(hariian);
//            }
//        });

        return convertView;
    }
}
