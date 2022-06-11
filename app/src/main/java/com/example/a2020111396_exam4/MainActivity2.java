package com.example.a2020111396_exam4;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.app.DatePickerDialog;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity2 extends AppCompatActivity {
    String url = "http://sc1.swu.ac.kr/~kyung128/covid19date.jsp";
    String yearStr;
    String monthStr;
    String dayStr;
    String startStr;
    String endStr;
    static RequestQueue queue;

    Button okBtn;
    Button start;
    Button end;
    Button backBtn;

    private ArrayList<Dictionary> myArrayList;
    private CustomAdapter myAdapter;
    private int count=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        RecyclerView myRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        myRecyclerView.setLayoutManager(mLinearLayoutManager);
        myArrayList = new ArrayList<>();
        myAdapter = new CustomAdapter(myArrayList);
        myRecyclerView.setAdapter(myAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(myRecyclerView.getContext(),
                mLinearLayoutManager.getOrientation());
        myRecyclerView.addItemDecoration(dividerItemDecoration);

        okBtn = findViewById(R.id.okBtn);
        start = findViewById(R.id.startDate);
        end = findViewById(R.id.endDate);
        backBtn = findViewById(R.id.backBtn);

        // RequestQueue 객체 생성 (정보 받을 때 사용)
        if (queue == null) {
            queue = Volley.newRequestQueue(getApplicationContext());
        }

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // 시작 날짜 선택
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                yearStr = Integer.toString(year);
                if (dayOfMonth < 10) {
                    dayStr = "0" + dayOfMonth;
                } else dayStr = Integer.toString(dayOfMonth);
                if (month+1 < 10) {
                    monthStr = "0" + (month+1);
                } else monthStr = Integer.toString((month+1));
                startStr = yearStr + "년 " + monthStr + "월 " + dayStr + "일 00시";
                start.setText("시작 날짜: " + startStr);
            }
        }, year, month, day);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (start.isClickable()) {
                    datePickerDialog.show();
                }
            }
        });

        // 종료 날짜 선택
        DatePickerDialog datePickerDialog2 = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                yearStr = Integer.toString(year);
                if (dayOfMonth < 10) {
                    dayStr = "0" + dayOfMonth;
                } else dayStr = Integer.toString(dayOfMonth);
                if (month+1 < 10) {
                    monthStr = "0" + (month+1);
                } else monthStr = Integer.toString((month+1));
                endStr = yearStr + "년 " + monthStr + "월 " + dayStr + "일 00시";
                end.setText("종료 날짜: " + endStr);
            }
        }, year, month, day);
        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (end.isClickable()) {
                    datePickerDialog2.show();
                }
            }
        });

        // 조회 버튼 클릭했을 시
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject obj = new JSONObject();
                try {
                    obj.put("startDay", startStr);
                    obj.put("endDay", endStr);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    makeRequest(obj);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });

        // 뒤로가기 버튼 클릭했을 시 (전 화면으로)
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }
    public void jsonParse(String response) {
        try {
            String city;
            String stdDay;
            String defCnt;
            String deathCnt;
            String localCnt;
            String overflowCnt;

            JSONObject jsonObj = new JSONObject(response);
            JSONArray jsonArr = jsonObj.getJSONArray("COVID19");
            for (int i=0; i<jsonArr.length(); i++) {
                JSONObject obj = jsonArr.getJSONObject(i);
                city = obj.getString("city");
                stdDay = obj.getString("stdDay");
                defCnt = obj.getString("defCnt");
                deathCnt = obj.getString("deathCnt");
                localCnt = obj.getString("localCnt");
                overflowCnt = obj.getString("overflowCnt");

                Dictionary data = new Dictionary(city, stdDay, defCnt, deathCnt, localCnt, overflowCnt);
                myArrayList.add(data);
                myAdapter.notifyDataSetChanged();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void makeRequest(JSONObject obj) throws UnsupportedEncodingException {
        String strUrl = url + "?searchparas=" + URLEncoder.encode(obj.toString(), "UTF-8");
        System.out.println(strUrl);
        StringRequest request = new StringRequest(Request.Method.GET, strUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                jsonParse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("에러-> " + error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }
        };
        request.setShouldCache(false);
        queue.add(request);
    }
}