package com.example.demo;

import com.fasterxml.jackson.core.TreeNode;
import org.tribuo.*;
import org.tribuo.classification.Label;
import org.tribuo.classification.LabelFactory;
import org.tribuo.classification.dtree.CARTClassificationTrainer;
import org.tribuo.classification.sgd.LabelObjective;
import org.tribuo.classification.sgd.linear.LinearSGDTrainer;
import org.tribuo.classification.sgd.linear.LogisticRegressionTrainer;
import org.tribuo.classification.sgd.objectives.LogMulticlass;
import org.tribuo.data.csv.CSVLoader;
import org.tribuo.impl.ArrayExample;
import org.tribuo.math.optimisers.AdaGrad;
import org.tribuo.math.optimisers.GradientOptimiserOptions;
import org.tribuo.math.optimisers.SGD;
import org.tribuo.provenance.DataSourceProvenance;
import org.tribuo.provenance.SimpleDataSourceProvenance;
import org.tribuo.regression.RegressionFactory;
import org.tribuo.regression.Regressor;

import org.tribuo.regression.slm.LARSLassoTrainer;
import org.tribuo.regression.xgboost.XGBoostRegressionTrainer;

import java.io.IOException;
import java.nio.file.Paths;
import java.time.OffsetDateTime;


public class Regression {
    public static void main(String[] args) throws IOException {
        var cal = find1();
        var cal2 = find2();
        for (int v = 0; v< cal.length; v++){
            System.out.print(cal[v] + " ");
            System.out.println(cal2[v]);
        }
        System.out.print(find3(3,3));
    }
    public static String[] find1() throws IOException {
        var Headers = new String[]{"Координата 1", "Координата 2", "Район"};
        DataSource<Label> InData =
                new CSVLoader<>(new LabelFactory()).loadDataSource(Paths.get("src/simple-2d-data-train(1).csv"),
                        Headers[2],
                        Headers);
        var Data = new MutableDataset<>(InData);
        var linearTrainer2 = new LogisticRegressionTrainer();
        var linearTrainer = new LinearSGDTrainer(
                new LogMulticlass(),
                new AdaGrad(0.1),
                25,
                40000,
                Trainer.DEFAULT_SEED
        );

        Model<Label> Logistic = linearTrainer.train(Data);
        Prediction<Label> prediction = Logistic.predict(Data.getExample(5));
        String[] colors = new String[Data.size()];
        String[] clusterColors = {"Red", "Green", "Blue", "Cyan", "Orange", "Purple", "Pink", "Yellow", "Magenta", "Lime"};
        for (int i = 0; i< Data.size(); i++){
            System.out.print(i);System.out.println(")  ");
            System.out.println("    "+ Logistic.predict(Data.getExample(i)).getOutput());
            switch (Logistic.predict(Data.getExample(i)).getOutput().getLabel()){
                case "hill":
                    colors[i] = clusterColors[3];
                    break;
                case "center":
                    colors[i] = clusterColors[1];
                    break;
                case "periphery":
                    colors[i] = clusterColors[2];
                    break;
                default:
                    colors[i] = "Black";
                    break;
            }
        }
        System.out.println("Hello world!");
        return colors;
    }
    public static String[] find2() throws IOException {
        var Headers = new String[]{"Координата 1", "Координата 2", "Район"};
        DataSource<Label> InData =
                new CSVLoader<>(new LabelFactory()).loadDataSource(Paths.get("src/simple-2d-data-train(1).csv"),
                        Headers[2],
                        Headers);
        var Data = new MutableDataset<>(InData);
        var linearTrainer2 = new CARTClassificationTrainer();
        Model<Label> CART = linearTrainer2.train(Data);
        String[] colors = new String[Data.size()];
        String[] clusterColors = {"Red", "Green", "Blue", "Cyan", "Orange", "Purple", "Pink", "Yellow", "Magenta", "Lime"};
        for (int i = 0; i< Data.size(); i++){
            System.out.print(i);System.out.println(")  ");
            System.out.println("    "+ CART.predict(Data.getExample(i)).getOutput());
            switch (CART.predict(Data.getExample(i)).getOutput().getLabel()){
                case "hill":
                    colors[i] = clusterColors[3];
                    break;
                case "center":
                    colors[i] = clusterColors[1];
                    break;
                case "periphery":
                    colors[i] = clusterColors[2];
                    break;
                default:
                    colors[i] = "Black";
                    break;
            }
        }
        System.out.println("Hello world!");
        return colors;
        //CART.get
    }
    public static double find3(double x, double y) throws IOException {


        var regressionFactory = new RegressionFactory();
        var csvLoader = new CSVLoader<>(',',regressionFactory);
        var wineSource = csvLoader.loadDataSource(Paths.get("src/simple-2d-data-train(2).csv"),"price");
        Dataset<Regressor> trainData = new MutableDataset<>(wineSource);
        Regressor r = trainData.getExample(2+20).getOutput();
        var RegTrainer = new LARSLassoTrainer();
        DataSourceProvenance provenance = new SimpleDataSourceProvenance("TrainingData", OffsetDateTime.now(), regressionFactory);
        MutableDataset<Regressor> test = new MutableDataset<>(provenance, regressionFactory);
        test.add(new ArrayExample<>(new Regressor("dim1",1),new String[]{"x","y"},new double[]{x,y}));
        Model<Regressor> LASSO = RegTrainer.train(trainData);
        double res = LASSO.predict(test).get(0).getOutput().getValues()[0];
        System.out.println(LASSO.predict(test));
        return res;
    }
    public static double find4(double x, double y) throws IOException {
        var regressionFactory = new RegressionFactory();
        var csvLoader = new CSVLoader<>(',',regressionFactory);
        var wineSource = csvLoader.loadDataSource(Paths.get("src/simple-2d-data-train(2).csv"),"price");
        Dataset<Regressor> trainData = new MutableDataset<>(wineSource);
        Regressor r = trainData.getExample(2+20).getOutput();
        var RegTrainer = new XGBoostRegressionTrainer(6);
        DataSourceProvenance provenance = new SimpleDataSourceProvenance("TrainingData", OffsetDateTime.now(), regressionFactory);
        MutableDataset<Regressor> test = new MutableDataset<>(provenance, regressionFactory);
        test.add(new ArrayExample<>(new Regressor("dim1",1),new String[]{"x","y"},new double[]{x,y}));
        Model<Regressor> LASSO = RegTrainer.train(trainData);
        double res = LASSO.predict(test).get(0).getOutput().getValues()[0];
        System.out.println(LASSO.predict(test));
        return res;
    }
}