package com.windbridget.w_alarmclock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import android.app.Activity;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class MainActivity extends Activity {
    TextView txtTime;
    EditText name;
    Button btnTime,btnAdd;
    //Khai báo Datasource lưu trữ danh sách công việc
    ArrayList<AlarmList>arrJob=new ArrayList<AlarmList>();
    //Khai báo ArrayAdapter cho ListView
    ArrayAdapter<AlarmList>adapter=null;
    ListView lvAlarm;
    Calendar cal;
    Date hour;
    Intent m_Intent;
    PendingIntent pending_intent;
    AlarmManager alarmManager;
    Toast toast;
    public Spinner spinner;
    public int wind_quote = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        getFormWidgets();
        getDefaultInfor();
        addEventFormWidgets();
    }
    /**
     * hàm dùng để load các control theo Id
     */
    public void getFormWidgets()
    {
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        txtTime=(TextView) findViewById(R.id.txttime);
        name =(EditText) findViewById(R.id.editname);
        btnTime=(Button) findViewById(R.id.btntime);
        btnAdd=(Button) findViewById(R.id.btnadd);
        lvAlarm=(ListView) findViewById(R.id.lvalarm);
        spinner = (Spinner) findViewById(R.id.wind_spinner);
        //Gán DataSource vào ArrayAdapterb
        adapter=new ArrayAdapter<AlarmList>
                (this,
                        android.R.layout.simple_list_item_1,
                        arrJob);
        //gán Adapter vào ListView
        lvAlarm.setAdapter(adapter);
        createSpinner();
    }
    /**
     * Hàm lấy các thông số mặc định khi lần đầu tiền chạy ứng dụng
     */
    public void getDefaultInfor()
    {
        //lấy ngày hiện tại của hệ thống
        cal=Calendar.getInstance();
        SimpleDateFormat dft=null;
        //Định dạng giờ phút am/pm
        dft=new SimpleDateFormat("HH:mm:ss",Locale.getDefault());
        String strTime=dft.format(cal.getTime());
        //đưa lên giao diện
//        txtTime.setText(strTime);
        //lấy giờ theo 24 để lập trình theo Tag
//        dft=new SimpleDateFormat("HH:mm",Locale.getDefault());
        txtTime.setTag(strTime);

        name.requestFocus();
        //gán cal.getTime() cho ngày hoàn thành và giờ hoàn thành
        hour =cal.getTime();
    }
    /**
     * Hàm gán các sự kiện cho các control
     */
    public void addEventFormWidgets()
    {
        btnTime.setOnClickListener(new MyButtonEvent());
        btnAdd.setOnClickListener(new MyButtonEvent());
        lvAlarm.setOnItemClickListener(new MyListViewEvent());
        lvAlarm.setOnItemLongClickListener(new MyListViewEvent());
    }
    /**
     * Class sự kiện của các Button
     * @author drthanh
     *
     */
    private class MyButtonEvent implements OnClickListener
    {
        @Override
        public void onClick(View v) {
            switch(v.getId())
            {
                case R.id.btntime:
                    showTimePickerDialog();
                    break;
                case R.id.btnadd:
                    processAddJob();
                    break;
            }
        }

    }
    /**
     * Class sự kiện của ListView
     * @author drthanh
     *
     */
    private class MyListViewEvent implements
            AdapterView.OnItemClickListener,
            OnItemLongClickListener {

        @Override
        public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                       final int arg2, long arg3) {
            //Xóa vị trí thứ arg2
            AlertDialog.Builder builder=new AlertDialog.Builder (MainActivity.this);
            builder.setTitle("Remove Alarm");
            builder.setMessage("Are you sure to remove this ?");
            builder.setIcon(android.R.drawable.ic_input_delete);
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    // TODO Auto-generated method stub
                    arg0.cancel();
                }
            });
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    arrJob.remove(arg2);
                    m_Intent = new Intent(MainActivity.this, AlarmReceiver.class);
                    m_Intent.putExtra("extra", "no");
                    m_Intent.putExtra("quote id", String.valueOf(wind_quote));
                    sendBroadcast(m_Intent);
                    alarmManager.cancel(pending_intent);
                    toast = Toast.makeText( MainActivity.this , "Alarm removed " , Toast.LENGTH_SHORT);
                    toast.show();
                    adapter.notifyDataSetChanged();
                }
            });
            builder.show();

            return false;
        }

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2,
                                long arg3) {
            //Hiển thị nội dung công việc tại vị trí thứ arg2
            AlertDialog.Builder builder=new AlertDialog.Builder (MainActivity.this);
            builder.setTitle("Cancel Alarm");
            builder.setMessage("Are you sure to cancel this ?");
            builder.setIcon(android.R.drawable.ic_input_delete);
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    // TODO Auto-generated method stub
                    arg0.cancel();
                }
            });
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stubarrJob.remove(arg2);
                    m_Intent = new Intent(MainActivity.this, AlarmReceiver.class);
                    m_Intent.putExtra("extra", "no");
                    m_Intent.putExtra("quote id", String.valueOf(wind_quote));
                    sendBroadcast(m_Intent);
                    alarmManager.cancel(pending_intent);
                    toast = Toast.makeText( MainActivity.this , "Alarm Canceled " , Toast.LENGTH_SHORT);
                    toast.show();
                }
            });
            builder.show();
        }

    }
    /**
     * Hàm hiển thị DatePicker dialog
     */
//    public void showDatePickerDialog()
//    {
//        OnDateSetListener callback=new OnDateSetListener() {
//            public void onDateSet(DatePicker view, int year,
//                                  int monthOfYear,
//                                  int dayOfMonth) {
//                //Mỗi lần thay đổi ngày tháng năm thì cập nhật lại TextView Date
//                txtDate.setText(
//                        (dayOfMonth) +"/"+(monthOfYear+1)+"/"+year);
//                //Lưu vết lại biến ngày hoàn thành
//                cal.set(year, monthOfYear, dayOfMonth);
//                dateFinish=cal.getTime();
//            }
//        };
//        //các lệnh dưới này xử lý ngày giờ trong DatePickerDialog
//        //sẽ giống với trên TextView khi mở nó lên
//        String s=txtDate.getText()+"";
//        String strArrtmp[]=s.split("/");
//        int ngay=Integer.parseInt(strArrtmp[0]);
//        int thang=Integer.parseInt(strArrtmp[1])-1;
//        int nam=Integer.parseInt(strArrtmp[2]);
//        DatePickerDialog pic=new DatePickerDialog(
//                MainActivity.this,
//                callback, nam, thang, ngay);
//        pic.setName("Chọn ngày hoàn thành");
//        pic.show();
//    }
    /**
     * Hàm hiển thị TimePickerDialog
     */
    public void showTimePickerDialog()
    {
        OnTimeSetListener callback=new OnTimeSetListener() {
            public void onTimeSet(TimePicker view,
                                  int hourOfDay, int minute) {
                cal=Calendar.getInstance();
                //Xử lý lưu giờ và AM,PM
                int second = cal.get(Calendar.SECOND);
                String s=hourOfDay +":"+minute + ":" + second;

                String hour_string = String.valueOf(hourOfDay);

                if(hourOfDay < 10){
                    hour_string = "0" + String.valueOf(hourOfDay);
                }
                String minute_string = String.valueOf(minute);
                if(minute < 10){
                    minute_string = "0" + String.valueOf(minute);
                }
                txtTime.setText
                        (hour_string +":"+minute_string +":" + second);
                //lưu giờ thực vào tag
                txtTime.setTag(s);
                //lưu vết lại giờ vào hour
                cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
                cal.set(Calendar.MINUTE, minute);

                hour =cal.getTime();


            }
        };
        //các lệnh dưới này xử lý ngày giờ trong TimePickerDialog
        //sẽ giống với trên TextView khi mở nó lên
        String s=txtTime.getTag()+"";
        String strArr[]=s.split(":");
        int hour=Integer.parseInt(strArr[0]);
        int minute=Integer.parseInt(strArr[1]);
        TimePickerDialog time=new TimePickerDialog(
                MainActivity.this,
                callback, hour, minute, true);
        time.setTitle("Setup your alarm");
        time.show();
    }
    /**
     * Hàm xử lý đưa công việc vào ListView khi nhấn nút Thêm Công việc
     */
    public void processAddJob()
    {
        cal=Calendar.getInstance();
        m_Intent = new Intent(MainActivity.this, AlarmReceiver.class);
        String title= name.getText()+"";
        AlarmList job=new AlarmList(title ,hour);
        arrJob.add(job);
        adapter.notifyDataSetChanged();
        //sau khi cập nhật thì reset dữ liệu và cho focus tới editCV
        name.setText("");
        name.requestFocus();
        m_Intent.putExtra("extra","yes");
        m_Intent.putExtra("quote id", String.valueOf(wind_quote));
        pending_intent = PendingIntent.getBroadcast(MainActivity.this, 0, m_Intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pending_intent);

    }

    public void createSpinner(){
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.wind_sounds, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        // Spinner click listener
        spinner.setOnItemSelectedListener(new MySpinnerEvent());
    }

    private class MySpinnerEvent implements AdapterView.OnItemSelectedListener{

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            wind_quote = (int) i;
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }
}
