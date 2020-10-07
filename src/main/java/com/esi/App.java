package com.esi;

import com.esi.Entities.Message;
import com.esi.Entities.MessageType;
import com.esi.Threads.Start;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.util.*;

import static java.lang.Thread.sleep;

/**
 * JavaFX App
 */
public class App extends Application {


    private static AnchorPane graph_container;
    public static Map<Integer, ArrayList<Double>> correspondace = new HashMap<Integer, ArrayList<Double>>();

    private class CustomListCell extends ListCell<Message> {
        private HBox content;

        Circle circle = new Circle(15);

        Color[] colors = new Color [] {Color.GREEN, Color.YELLOW, Color.VIOLET};

        public Text type;
        public Text timestamp;
        public Text addresses;


        public CustomListCell() throws IOException {
            super();
            type = new Text();
            timestamp = new Text();
            addresses = new Text();

            Parent component = loadFXML("MessageComponent");

            VBox vBox = new VBox(type, timestamp,addresses);

            content = new HBox(circle, vBox, new Circle(2), new Circle(2));
            content.setSpacing(10);
        }


        @Override
        protected void updateItem(Message item, boolean empty) {
            super.updateItem(item, empty);
            if (item != null && !empty) { // <== test for null item and empty parameter
                circle.setFill(this.colors[item.type.ordinal()]);
                type.setText("Type : " + item.type.toString());
                timestamp.setText( "estampille : " + ((Integer) (item.timestamp)).toString());
                addresses.setText(" de : " + ((Integer) (item.sender_index)).toString()
                                + " à "+
                                ((Integer) (item.receiver_index)).toString()

                );

                setGraphic(content);
            } else {
                setGraphic(null);
            }
        }
    }

    private static Scene scene;

    public static Canvas graph;
    public static ListView listView;

    public ListView getListView() {
        return listView;
    }

    private void add_Nodes(GraphicsContext gc) {


        int regions =(int) Math.ceil(Math.sqrt(Start.num_threads));
        for (int i = 0; i <Start.num_threads -1 ; i++) {

            ArrayList<Double> coordinates = new ArrayList<>();
            double x = Math.min(Math.max(Math.random() * graph.getWidth() / regions,40 - Math.random() * 10) + ((int) i / regions) * graph.getWidth() / regions, graph.getWidth()  - 40 + Math.random() * 10);
            System.out.println(x);
            double y = Math.min(Math.max(Math.random() * graph.getHeight(),40 - Math.random() * 10), graph.getHeight() - 40 + Math.random() * 10 );
            System.out.println(y);
            coordinates.add(x);
            coordinates.add(y);



            correspondace.put(i,coordinates);


            gc.setStroke(Color.FORESTGREEN.brighter());
            gc.setLineWidth(5);
            gc.strokeOval(x, y, 40, 40);

            gc.fillText(
                    "Node " + i,
                    x ,
                    y
            );
            //gc.setFill(Color.FORESTGREEN);
            //gc.fillOval(130, 30, 80, 80);
        }

    }
    @Override
    public void start(Stage stage) throws IOException {

        stage.setResizable(false);
        scene = new Scene(loadFXML("primary"));

        System.out.println();
        try{

            App.listView = (ListView) scene.lookup("#list_view");


            App.listView.setCellFactory(new Callback<ListView<Message>, ListCell<Message>>() {
                @Override
                public ListCell<Message> call(ListView<Message> listView) {
                    try {
                        return new CustomListCell();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            });

            App.graph_container = (AnchorPane) scene.lookup("#graph_container");

            graph = (Canvas) scene.lookup("#canvas");


            GraphicsContext gc = graph.getGraphicsContext2D();

            add_Nodes(gc);










        }
        catch (Exception e){
            e.printStackTrace();
        }




        stage.setScene(scene);
        stage.show();
        Start.main(new String[1]);

    }

    public static void drawPoint(GraphicsContext gc,ArrayList<Double> point,Color color,int i){
        Double x = point.get(0);
        Double y = point.get(1);
        gc.setStroke(color);
        gc.setLineWidth(5);
        gc.strokeOval(x, y, 40, 40);
        gc.fillText(
                "Node " + i,
                x ,
                y
        );
    }

    public static void drawLine(GraphicsContext gc,ArrayList<Double> pointA,ArrayList<Double> pointB,Color color,String message){
        Double xA = pointA.get(0);
        Double yA = pointA.get(1);
        Double xB = pointB.get(0);
        Double yB = pointB.get(1);
        gc.setStroke(color);
        gc.setLineWidth(5);
        gc.strokeLine(xA, yA, xB, yB);
        gc.fillText(message,(xA+xB)/2,(yA+yB)/2);
    }
    public static <K, V> K getKey(Map<K, V> map, V value) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (value.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }
    public static void add_Message(Message s) throws InterruptedException {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                GraphicsContext gc = graph.getGraphicsContext2D();
                gc.clearRect(0, 0, graph.getWidth(), graph.getHeight());
                for (ArrayList<Double> point: correspondace.values()){



                    drawPoint(gc,point,Color.GREEN.brighter(),getKey(correspondace,point));
                }

                ArrayList<Double> r1 = correspondace.get(s.sender_index);
                ArrayList<Double> r2 = correspondace.get(s.receiver_index);

                drawPoint(gc,r1,Color.YELLOW.brighter(),getKey(correspondace,r1));
                drawPoint(gc,r2,Color.RED.brighter(),getKey(correspondace,r2));
                Color[] colors = new Color [] {Color.GREEN, Color.YELLOW, Color.VIOLET};
                drawLine(gc,r1,r2,colors[s.type.ordinal()],s.type.toString());




                /*

        ObservableList<ICell> cells = App.graph.getModel().getAllCells();
        ICell sender = cells.get(s.sender_index);
        ICell receiver = cells.get(s.sender_index);
        final Edge edgeAB = new Edge(sender, receiver);
        final Model model = App.graph.getModel();
        App.graph.beginUpdate();
        edgeAB.textProperty().set("de " + s.sender_index + "  à " + s.receiver_index);
        model.addEdge(edgeAB);
        App.graph.endUpdate();
                try {
                    sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

        App.graph.getModel().getAllEdges().remove(edgeAB);
        App.graph.endUpdate();
                // Update UI here.

 */
        App.listView.getItems().add(s);
        }

        });




    }
    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}