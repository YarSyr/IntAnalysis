package com.example.demo;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;

@Controller
public class Contr {
    //ttp://api.weatherapi.com/v1/history.json?key=8592b3af0cfb4d70947161116240402&q=55.49401006728674,37.3000947378548&dt=2024-01-01
    //http://api.openweathermap.org/data/2.5/forecast?lat=44.34&lon=10.99&appid=3f7985299ec661ef2d1fd6d9a5942f73
    public static String APIshka = "http://api.weatherapi.com/v1/history.json?key=8592b3af0cfb4d70947161116240402&q=55.49401006728674,37.3000947378548&dt=2024-01-01";
    public static JSONObject getJson(URL url) throws IOException {
        String json = IOUtils.toString(url, Charset.forName("UTF-8"));
        return new JSONObject(json);
    }
    @GetMapping("/")
    public String greeting(Model model) throws IOException {
        URL adres = new URL(APIshka);
        String St = getJson(adres).optJSONObject("forecast").optJSONArray("forecastday").getJSONObject(0).getJSONObject("day").getBigDecimal("avgtemp_c").toString();



        model.addAttribute("name", St);
        System.out.println(St);
        return "test";
    }

}
