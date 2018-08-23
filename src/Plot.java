
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;


public class Plot {


    public static void main(String[] args) {

        String input = args[0];
        String filename = args[1];
        int firstNode = Integer.parseInt(args[2]);

        if (input.equals("q1")){
            writeOutput(filename, firstNode);
        }
        else if (input.equals("q2")){
            visualize(filename, firstNode);
        }else{
            plot(filename, firstNode);
        }

    }

    public static void writeOutput(String filename, int firstNode){

        PushProtocol v = new PushProtocol();
        v.initialize(filename, firstNode, 0, false);
        System.exit(0);
    }


    public static void visualize(String filename, int firstNode){

        PushProtocol v = new PushProtocol();
        v.initialize(filename, firstNode, 0, true);

    }

    public static void plot(String filename, int firstNode){

        long[] plotTable = new long[20];

        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries series = new XYSeries("P vs. Time");

        for (int i = 0; i < 20; i++) {
            PushProtocol v = new PushProtocol();
            int p = i * 5;
            long time = v.initialize(filename, firstNode, p, false);

            plotTable[i] = time;
            System.out.println(time + " " + p);
        }

        for (int j = 0; j < plotTable.length; j++){
            series.add(j*5, plotTable[j]);
        }

        dataset.addSeries(series);

        JFreeChart chart = ChartFactory.createScatterPlot(
                "P vs. Time Scatter Plot", // chart title
                "X", // x axis label
                "Y", // y axis label
                dataset, // data  ***-----PROBLEM------***
                PlotOrientation.VERTICAL,
                true, // include legend
                true, // tooltips
                false // urls
        );

        ChartFrame frame = new ChartFrame("First", chart);
        frame.pack();
        frame.setVisible(true);

    }


}
