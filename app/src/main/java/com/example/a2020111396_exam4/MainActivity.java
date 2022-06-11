package com.example.a2020111396_exam4;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    Button viewBtn;
    Button changeBtn;
    TextView textView;
    Button dateBtn;
    String city = "";
    String yearStr = "";
    String monthStr = "";
    String dayStr = "";
    String dateStr = "";
    static RequestQueue queue;
    String url = "http://sc1.swu.ac.kr/~kyung128/covid19app.jsp";
    String[] items = {"강원", "경기", "경남", "경북", "광주", "대구", "대전", "부산",
    "서울", "세종", "울산", "인천", "전남", "전북", "제주", "충남", "충북", "전체"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewBtn = findViewById(R.id.viewBtn);
        changeBtn = findViewById(R.id.changeBtn);
        textView = findViewById(R.id.textView);
        dateBtn = findViewById(R.id.dateBtn);

        // RequestQueue 객체 생성 (정보 받을 때 사용)
        if (queue == null) {
            queue = Volley.newRequestQueue(getApplicationContext());
        }

        Spinner selectSido = findViewById(R.id.selectSido);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectSido.setAdapter(adapter);
        selectSido.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                city = selectSido.getSelectedItem().toString(); // 선택된 도시 이름
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        changeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity2.class);
                startActivity(intent);
            }
        });

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // 시작 날짜 선택
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                yearStr = Integer.toString(year);
                if (dayOfMonth  < 10) {
                    dayStr = "0" + dayOfMonth;
                } else dayStr = Integer.toString(dayOfMonth);
                if ((month + 1) < 10) {
                    monthStr = "0" + (month+1);
                } else monthStr = Integer.toString((month+1));
                dateStr = yearStr + "년 " + monthStr + "월 " + dayStr + "일 00시";
                dateBtn.setText(dateStr);
            }
        }, year, month, day);
        dateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dateBtn.isClickable()) {
                    datePickerDialog.show();
                }
            }
        });

        viewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject covidObj = new JSONObject();
                try {
                    covidObj.put("city", city);
                    covidObj.put("stdDay", dateStr);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    makeRequest(covidObj);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // 서버로부터 데이터 받기
    public void makeRequest(JSONObject obj) throws UnsupportedEncodingException {
        String strUrl = url + "?searchparas=" + URLEncoder.encode(obj.toString(), "UTF-8");
        System.out.println(strUrl);
        StringRequest request = new StringRequest(Request.Method.GET, strUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObj = new JSONObject(response);
                    JSONArray jsonArr = jsonObj.getJSONArray("COVID19");
                    for (int i=0; i<jsonArr.length(); i++) {
                        JSONObject obj = jsonArr.getJSONObject(i);
                        println("도시명: " + obj.getString("city"));
                        println("기준 일시: " + obj.getString("stdDay"));
                        println("확진자 수: " + obj.getString("defCnt"));
                        println("사망자 수: " + obj.getString("deathCnt"));
                        println("해외유입: " + obj.getString("overflowCnt"));
                        println("국내 발생: " + obj.getString("localCnt"));
                        println("====================");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                println("에러-> " + error.getMessage());
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
    public void println(String data) {
        textView.append(data + "\n");
    }
}