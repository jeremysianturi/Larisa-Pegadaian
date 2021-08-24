package id.co.pegadaian.diarium.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.controller.home.main_menu.employee_corner.BookingcornerActivity;
import id.co.pegadaian.diarium.model.EmployeeCornerThemeModel;

public class EmployeeCornerThemeAdapter extends BaseAdapter {
    private Context mContext;
    private EmployeeCornerThemeModel model;
    private List<EmployeeCornerThemeModel> listModel;
    private TextView tvTitle, tvDate, tvDesc;
    private ImageView ivObat;
    private Button btnChoose;
    private LinearLayout pesan;

    public EmployeeCornerThemeAdapter(Context mContext, List<EmployeeCornerThemeModel> listModel) {
        this.mContext = mContext;
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

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        model = listModel.get(position);
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(R.layout.item_list_corner, null);
        }
        tvTitle = view.findViewById(R.id.tvThemeName);
        btnChoose = view.findViewById(R.id.btn_choose);
        tvTitle.setText(model.getTheme_name());
        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(mContext, BookingcornerActivity.class);
                a.putExtra("theme_id", listModel.get(position).getTheme_id());
                a.putExtra("theme_name", listModel.get(position).getTheme_name());
                a.putExtra("type", "new");
                a.putExtra("booking_code", "-");
                mContext.startActivity(a);
            }
        });
        return view;
    }
}
