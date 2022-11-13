<%@ page language="java" contentType="application/json; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="org.json.simple.JSONObject"%>
<%@ page import="org.json.simple.JSONArray"%>
<%@ page import="org.json.simple.parser.JSONParser"%>
<%@ page import="org.json.simple.parser.ParseException"%>
<%@ page import="java.net.URLEncoder"%>
<%@ page import="java.net.URLDecoder"%>
<%@ page import="java.sql.*"%>
<%@ page import="java.util.*" %>
<%@ page import="java.io.PrintWriter"%>
<%
    request.setCharacterEncoding("UTF-8");
    String sSearchParas = URLDecoder.decode(request.getParameter("searchparas"),"UTF-8"); 
    
    String city = "";
    String stdDay = "";
    
    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    
    try {
        JSONParser jsonParser = new JSONParser();
        //JSON데이터를 넣어 JSON Object 로 만들어 준다.
        JSONObject jsonObject = (JSONObject) jsonParser.parse(sSearchParas);
        // 도시 이름 추출하기
        city = jsonObject.get("city").toString();
        stdDay = jsonObject.get("stdDay").toString();
        
      //mariaDB 준비
        Class.forName("org.mariadb.jdbc.Driver");
        System.out.println("mariadb 사용 가능");
                
        // mariaDB 연결
        conn = DriverManager.getConnection("jdbc:mariadb://sc1.swu.ac.kr:13306/kyung128_ts", "kyung128", "kyung1284");
        System.out.println(conn + " <-- conn");
        
        String sql = "";
        
        if (city.equals("전체")) {
        	// 도시 이름으로 행 가져오기
            sql = "SELECT * FROM kyung128_ts.covidTbl WHERE stdDay=? order by stdDay desc, city asc";

            stmt = conn.prepareStatement(sql);
            stmt.setString(1, stdDay);

            rs = stmt.executeQuery(); // 쿼리 실행
            
            JSONObject covid = new JSONObject(); 
            JSONArray covidArr = new JSONArray(); 
            
            while (rs.next()) {
                JSONObject cityInfo = new JSONObject();
                cityInfo.put("city", rs.getString("city"));
                cityInfo.put("deathCnt", rs.getString("deathCnt"));
                cityInfo.put("defCnt", rs.getString("defCnt"));
                cityInfo.put("localCnt", rs.getString("localCnt"));
                cityInfo.put("overflowCnt", rs.getString("overflowCnt"));
                cityInfo.put("stdDay", rs.getString("stdDay"));
                
                if (cityInfo != null) covidArr.add(cityInfo);
            }
            
            covid.put("COVID19", covidArr);
            out.print(covid.toString());
            // DB 종료
            rs.close();
            stmt.close();
            conn.close();
        	
        } else {
        	// 도시 이름으로 행 가져오기
            sql = "SELECT * FROM kyung128_ts.covidTbl WHERE city=? AND stdDay=? order by stdDay desc, city asc";

            stmt = conn.prepareStatement(sql);
            stmt.setString(1, city);
            stmt.setString(2, stdDay);

            rs = stmt.executeQuery(); // 쿼리 실행
            
            JSONObject covid = new JSONObject(); 
            JSONArray covidArr = new JSONArray(); 
            
            while (rs.next()) {
                JSONObject cityInfo = new JSONObject();
                cityInfo.put("city", rs.getString("city"));
                cityInfo.put("deathCnt", rs.getString("deathCnt"));
                cityInfo.put("defCnt", rs.getString("defCnt"));
                cityInfo.put("localCnt", rs.getString("localCnt"));
                cityInfo.put("overflowCnt", rs.getString("overflowCnt"));
                cityInfo.put("stdDay", rs.getString("stdDay"));
                
                if (cityInfo != null) covidArr.add(cityInfo);
            }
            
            covid.put("COVID19", covidArr);
            out.print(covid.toString());
            // DB 종료
            rs.close();
            stmt.close();
            conn.close();
        }
              
    // TODO: Send JSONObject 
    } catch (ParseException e) {
    // TODO Auto-generated catch block
        e.printStackTrace();
    } finally {
    }
%>