package GamePage;

import javafx.scene.Group;
import javafx.scene.shape.Line;

public class Lines {
    public static Group route(double[] coordinates,int width, int height) {
        Group lines = new Group();
        Line line1 = new Line(coordinates[1]*width, coordinates[2]*height, coordinates[3]*width, coordinates[4]*height),line2,line3;
        lines.getChildren().add(line1);
        if(coordinates[0] == 1){
            line2=new Line(coordinates[5]*width, coordinates[6]*height, coordinates[7]*width, coordinates[8]*height);
            lines.getChildren().add(line2);
        }
        if (coordinates[0] == 2){
            line3=new Line(coordinates[9]*width, coordinates[10]*height, coordinates[11]*width, coordinates[12]*height);
            lines.getChildren().add(line3);
        }
        return lines;
    }
}
