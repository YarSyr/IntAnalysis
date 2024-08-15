package com.example.demo;

import org.tribuo.*;
import org.tribuo.clustering.*;
import org.tribuo.clustering.hdbscan.*;
import org.tribuo.data.columnar.*;
import org.tribuo.data.columnar.processors.field.DoubleFieldProcessor;
import org.tribuo.data.columnar.processors.response.EmptyResponseProcessor;
import org.tribuo.data.csv.CSVDataSource;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;


public class HDBSCAN {
    public static String[] work() {
        Map<String, FieldProcessor> regexMappingProcessors = new HashMap<>();
        regexMappingProcessors.put("Feature1", new DoubleFieldProcessor("Feature1"));
        regexMappingProcessors.put("Feature2", new DoubleFieldProcessor("Feature2"));
        RowProcessor<ClusterID> rowProcessor = new RowProcessor<>(new EmptyResponseProcessor<>(new ClusteringFactory()), regexMappingProcessors);
        var csvDataSource = new CSVDataSource<>(Paths.get("src/simple-2d-data-train.csv"), rowProcessor, false);
        var dataset = new MutableDataset<>(csvDataSource);


        var trainer = new HdbscanTrainer(5);
        var model = trainer.train(dataset);

        var clusterLabels = model.getClusterLabels();
        System.out.println(clusterLabels);
        String[] colors = new String[dataset.size()];
        String[] clusterColors = {"Red", "Green", "Blue", "Cyan", "Orange", "Purple", "Pink", "Yellow", "Magenta", "Lime"};
        int i = 0;
        for (Integer label : clusterLabels) {
            switch (label){
                case 5:
                    colors[i] = clusterColors[1];
                    break;
                case 4:
                    colors[i] = clusterColors[2];
                    break;
                case 3:
                    colors[i] = clusterColors[3];
                    break;
                default:
                    colors[i] = "Black";
                    break;
            }
            //System.out.println(colors[i]);
            i++;
        }
        return colors;
    }
}
