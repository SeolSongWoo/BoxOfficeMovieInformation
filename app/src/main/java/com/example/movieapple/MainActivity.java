package com.example.movieapple;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private MovieClient movieClient;
    private MovieInterface movieInterface;
    private String API_KEY = "4815cf3f21d985c2705081471bde4c16";

    Button DateSet,BTput;
    TextView DateView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("날짜별 박스오피스 TOP10");
        recyclerView = findViewById(R.id.recyclerView);
        DateSet = (Button)findViewById(R.id.DateSet);
        DateView = (TextView)findViewById(R.id.DateView);
        BTput = (Button)findViewById(R.id.BTput);


        //어제 날짜 셋팅 (앱 첫시작 셋팅)
        Date date=new Date();
        date.setTime(date.getTime()-(1000*60*60*24));
        SimpleDateFormat sdf= new SimpleDateFormat("yyyyMMdd");
        final String[] dateStr = {sdf.format(date)};
        StringBuffer sb = new StringBuffer();
        sb.append(dateStr[0]);
        sb.insert(4,"년");
        sb.insert(7,"월");
        sb.insert(10,"일");
        DateView.setText(sb);

        //API 인터페이스 셋팅 및 API 함수들 호출
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        movieClient = MovieClient.getInstance();
        movieInterface = MovieClient.getMovieInterface();

        //데이트피커 날짜설정 및 TextView에 데이트피커에 설정한 날짜 넣기 및 API함수에 들어갈 DateStr(해당 날짜 기준 영화정보)를 설정
        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                DateView.setText(year+"년"+(month+1)+"월"+dayOfMonth+"일");
                dateStr[0] = IntToChar(year,month,dayOfMonth);
        }
        }, mYear, mMonth, mDay);

        //데이트피커 SHOW
        DateSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });

        //API함수들을 이용해 영화 정보 출력
        BTput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                movieInterface.getBoxOffice(API_KEY, dateStr[0]).enqueue(new Callback<Result>() {
                    @Override
                    public void onResponse(Call<Result> call, Response<Result> response) {
                        Result result = response.body();
                        BoxOfficeResult boxOfficeResult = result.getBoxOfficeResult();
                        mAdapter = new MovieAdapter(boxOfficeResult.getDailyBoxOfficeList());

                        recyclerView.setAdapter(mAdapter);
                    }

                    @Override
                    public void onFailure(Call<Result> call, Throwable t) {
                    }
                });
            }
        });

    }


    //숫자 -> YYYYMMDD 형식으로 변환. 2020년2월2일의 경우 202022로 출력되어 따로 유틸 함수 추가
    public static String IntToChar(int Year,int Month, int Day) {
        String StrMonth,StrDay,StrYear;
        StrYear = Integer.toString(Year);
        if(Month < 10) {
            if(Day < 10) {
                StrMonth = "0"+Integer.toString(Month+1);
                StrDay = "0"+Integer.toString(Day);
            }
            else {
                StrMonth = "0"+Integer.toString(Month+1);
                StrDay = Integer.toString(Day);
            }
        }
        else if(Day < 10) {
            StrDay = "0"+Integer.toString(Day);
            StrMonth = Integer.toString(Month+1);
        }
        else {
            StrMonth = Integer.toString(Month+1);
            StrDay = Integer.toString(Day);
        }
        return StrYear+StrMonth+StrDay;
    }

}