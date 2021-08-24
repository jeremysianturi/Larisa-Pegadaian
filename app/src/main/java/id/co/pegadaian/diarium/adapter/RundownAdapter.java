package id.co.pegadaian.diarium.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.model.RundownModel;
import id.co.pegadaian.diarium.util.DataSession;
import id.co.pegadaian.diarium.util.LocalData;
import id.co.pegadaian.diarium.util.reminder.AlarmReceiver;
import id.co.pegadaian.diarium.util.reminder.NotificationScheduler;

public class RundownAdapter extends BaseAdapter {

    public static final int DAILY_REMINDER_REQUEST_CODE = 100;
    public static final String TAG = "NotificationScheduler";
    private Context mContext;
    private RundownModel model;
    private List<RundownModel> listModel;
    private TextView txt_expo, txt_cate;
    private Typeface font, fontbold;
    private DataSession dataSess;
    private String idAgenda;
    private String idEvent;
    private LocalData localData;
    private int hour;

    public RundownAdapter(Context mContext, List<RundownModel> listModel, DataSession dataSess, String idEvent, String idAgenda) {
        this.mContext = mContext;
        this.listModel = listModel;
        this.dataSess = dataSess;
        this.idEvent = idEvent;
        this.idAgenda = idAgenda;

        fontbold = Typeface.createFromAsset(mContext.getAssets(), "fonts/AvenirLTStd-Medium.otf");
        font = Typeface.createFromAsset(mContext.getAssets(), "fonts/AvenirLTStd-Book.otf");
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


    public View getView(final int position, View view, ViewGroup parent) {
        model = listModel.get(position);
        final ViewHolder holder;

        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(R.layout.item_list_rundown, null);
            holder = new ViewHolder();
            holder.txtTitle_tiga = view.findViewById(R.id.txt_jam);
            holder.txtTitle_empat = view.findViewById(R.id.txt_acara);
            holder.lay_reminder = view.findViewById(R.id.lay_reminder);
            holder.imageView = view.findViewById(R.id.iv_reminder);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        String startTime = model.getRundown_begin();
        SimpleDateFormat formatAwal = new SimpleDateFormat("HH:mm:ss");
        try {
            Date date = formatAwal.parse(startTime);
            SimpleDateFormat sdf = new SimpleDateFormat("HH");
            String jam = sdf.format(date);
            hour = Integer.parseInt(jam);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.txtTitle_tiga.setText(model.getRundown_begin() + " - " + model.getRundown_end());
        holder.txtTitle_tiga.setTypeface(fontbold);
        holder.txtTitle_empat.setText(model.getName());
        holder.txtTitle_empat.setTypeface(fontbold);

        String lonceng = dataSess.getData("lonceng" + (position) + idEvent + idAgenda);
        if (lonceng.equals("")) {
            holder.imageView.setImageResource(R.drawable.notification);
            dataSess.setData("lonceng" + (position) + idEvent + idAgenda, "off");

            holder.lay_reminder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(v.getRootView().getContext())
                            .setMessage("Are you sure you want to turn on reminder?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    localData = new LocalData(mContext);
                                    Toast.makeText(mContext, "Alarm has been set, see you soon!", Toast.LENGTH_SHORT).show();
                                    holder.imageView.setImageResource(R.drawable.icon_bell_on);
                                    holder.imageView.setTag(position);
                                    dataSess.setData("lonceng" + (position) + idEvent + idAgenda, "on");
                                    localData.setReminderStatus(true);
                                    localData.set_hour(hour);
                                    localData.set_min(0);
                                    NotificationScheduler.setReminder(mContext, AlarmReceiver.class, localData.get_hour(), localData.get_min());
                                }
                            })
                            .setNegativeButton("No", null)
                            .show();
                }
            });
        } else {
            if (lonceng.equals("off")) {
                holder.imageView.setImageResource(R.drawable.notification);

                holder.lay_reminder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(v.getRootView().getContext())
                                .setMessage("Are you sure you want to turn on reminder?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        localData = new LocalData(mContext);
                                        Toast.makeText(mContext, "Alarm has been set, see you soon!", Toast.LENGTH_SHORT).show();
                                        holder.imageView.setImageResource(R.drawable.icon_bell_on);
                                        holder.imageView.setTag(position);
                                        dataSess.setData("lonceng" + (position) + idEvent + idAgenda, "on");
                                        localData.setReminderStatus(true);
                                        localData.set_hour(hour);
                                        localData.set_min(0);
                                        NotificationScheduler.setReminder(mContext, AlarmReceiver.class, localData.get_hour(), localData.get_min());
                                        System.out.println("jam " + localData.get_hour());
                                    }
                                })
                                .setNegativeButton("No", null)
                                .show();
                    }
                });
            } else {
                holder.imageView.setImageResource(R.drawable.icon_bell_on);
                holder.lay_reminder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(v.getRootView().getContext())
                                .setMessage("Are you sure you want to turn off reminder?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        holder.imageView.setImageResource(R.drawable.notification);
                                        holder.imageView.setTag(position);
                                        dataSess.setData("lonceng" + (position) + idEvent + idAgenda, "off");
                                        NotificationScheduler.cancelReminder(mContext, AlarmReceiver.class);
                                    }
                                })
                                .setNegativeButton("No", null)
                                .show();
                    }
                });
            }
        }

        return view;
    }

    class ViewHolder {
        TextView txtTitle_tiga;
        TextView txtTitle_empat;
        LinearLayout lay_reminder;
        ImageView imageView;
    }
}