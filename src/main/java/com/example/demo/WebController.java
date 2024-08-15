package com.example.demo;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.tribuo.Example;
import org.tribuo.Feature;
import org.tribuo.MutableDataset;
import org.tribuo.clustering.ClusterID;
import org.tribuo.clustering.ClusteringFactory;
import org.tribuo.data.columnar.FieldProcessor;
import org.tribuo.data.columnar.RowProcessor;
import org.tribuo.data.columnar.processors.field.DoubleFieldProcessor;
import org.tribuo.data.columnar.processors.response.EmptyResponseProcessor;
import org.tribuo.data.csv.CSVDataSource;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.*;

@Controller
public class WebController {
    //http://api.weatherapi.com/v1/history.json?key=8592b3af0cfb4d70947161116240402&q=55.49401006728674,37.3000947378548&dt=2024-01-01
    //http://api.openweathermap.org/data/2.5/forecast?lat=44.34&lon=10.99&appid=3f7985299ec661ef2d1fd6d9a5942f73
    public static String APIshka = "http://api.weatherapi.com/v1/history.json?key=8592b3af0cfb4d70947161116240402&q=55.49401006728674,37.3000947378548&dt=2024-04-16";
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
    @GetMapping("/points")
    public String points(Model model) throws IOException {
        Map<String, FieldProcessor> regexMappingProcessors = new HashMap<>();
        regexMappingProcessors.put("Feature1", new DoubleFieldProcessor("Feature1"));
        regexMappingProcessors.put("Feature2", new DoubleFieldProcessor("Feature2"));
        RowProcessor<ClusterID> rowProcessor = new RowProcessor<>(new EmptyResponseProcessor<>(new ClusteringFactory()), regexMappingProcessors);
        var csvDataSource = new CSVDataSource<>(Paths.get("src/simple-2d-data-train.csv"), rowProcessor, false);
        var dataset = new MutableDataset<>(csvDataSource);
        List<Double> xList = new ArrayList<>();
        List<Double> yList = new ArrayList<>();
        for (Example<ClusterID> ex : dataset) {
            int i = 0;
            for (Feature f : ex) {
                if (i == 0)  xList.add(f.getValue());
                if (i == 1)  yList.add(f.getValue());
                i++;
            }
        }
        double[][] coordinates = convertListsTo2DArray(xList, yList);
        model.addAttribute("coordinates", coordinates);
        String[] colors = KMeans.work(coordinates[0], coordinates [1], 4);
        model.addAttribute("colors1", colors);
        model.addAttribute("colors2", HDBSCAN.work());
        model.addAttribute("colors3", Regression.find1());
        model.addAttribute("colors4", Regression.find2());
        System.out.println(Regression.find3(3.9, 3.0));
        System.out.println(Regression.find4(3.9, 3.0));
        return "points2";
    }
    public static double[][] convertListsTo2DArray(List<Double> xList, List<Double> yList) {
        int size = Math.min(xList.size(), yList.size());
        double[][] coordinates = new double[2][size];

        for (int i = 0; i < size; i++) {
            coordinates[0][i] = xList.get(i);
            coordinates[1][i] = yList.get(i);
        }

        return coordinates;
    }
}
