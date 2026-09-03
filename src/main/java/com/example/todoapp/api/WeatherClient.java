package com.example.todoapp.api;
import java.time.*; import java.util.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.*;
@Component public class WeatherClient {
 private static final String F="https://api.open-meteo.com/v1/forecast", H="https://archive-api.open-meteo.com/v1/archive";
 private final RestClient client; private final ObjectMapper mapper=new ObjectMapper();
 public WeatherClient(){var f=new SimpleClientHttpRequestFactory();f.setConnectTimeout(Duration.ofSeconds(3));f.setReadTimeout(Duration.ofSeconds(5));client=RestClient.builder().requestFactory(f).build();}
 public WeatherResult getWeather(LocalDate from,LocalDate to){var out=new HashMap<String,String>();try{var now=LocalDate.now();if(from.isBefore(now))out.putAll(daily(H,from,to.isBefore(now)?to:now.minusDays(1),false));if(!to.isBefore(now)){var s=from.isAfter(now)?from:now;out.putAll(daily(F,s,to.isBefore(now.plusDays(15))?to:now.plusDays(15),true));}}catch(Exception e){System.err.println("Weather API request failed: "+e);return new WeatherResult(out,true);}return new WeatherResult(out,out.isEmpty());}
 private Map<String,String> daily(String base,LocalDate from,LocalDate to,boolean forecast)throws Exception{if(to.isBefore(from))return Map.of();String u=base+"?latitude=35.681236&longitude=139.767125&daily=weather_code,temperature_2m_mean&timezone=Asia/Tokyo&start_date="+from+"&end_date="+to;JsonNode d=mapper.readTree(client.get().uri(u).retrieve().body(String.class)).path("daily");var r=new HashMap<String,String>();for(int i=0;i<d.path("time").size();i++)r.put(d.path("time").get(i).asText(),(forecast?"\u4e88\u60f3\uff1a":"")+desc(d.path("weather_code").get(i).asInt())+" "+d.path("temperature_2m_mean").get(i).asText()+"\u2103");return r;}
 private String desc(int c){return switch(c){case 0->"\u5feb\u6674";case 1,2->"\u6674\u308c";case 3->"\u304f\u3082\u308a";case 45,48->"\u9727";case 51,53,55,56,57->"\u9727\u96e8";case 61,63,65,66,67->"\u96e8";case 71,73,75,77->"\u96ea";case 80,81,82->"\u306b\u308f\u304b\u96e8";case 85,86->"\u306b\u308f\u304b\u96ea";case 95,96,99->"\u96f7\u96e8";default->"\u5929\u6c17\u60c5\u5831";};}
 public record WeatherResult(Map<String,String> weather,boolean unavailable){}
}
