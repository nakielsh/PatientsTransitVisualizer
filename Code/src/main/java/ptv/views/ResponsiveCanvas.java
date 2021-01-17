package ptv.views;

import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Affine;

public class ResponsiveCanvas extends Canvas {

    View view;

    public ResponsiveCanvas() {
        widthProperty().addListener(evt -> redraw());
        heightProperty().addListener(evt -> redraw());
    }

    public void setView(View view) {
        this.view = view;
    }

    public void redraw() {
        double width = getWidth();
        double height = getHeight();
        if (view != null && this.view.getIsLoadedMap()) {
            this.view.countAffine();
            //this.view.countTransformPoint();
            Affine affine = new Affine();
            //affine.appendTranslation(this.view.getP0().getX(), this.view.getP0().getY());
            affine.appendScale(this.view.getScaleAffine(), this.view.getScaleAffine());
            this.view.setAffine(affine);
            view.paintObjectsOnMap();
        } else {
            GraphicsContext gc = getGraphicsContext2D();
            gc.clearRect(0, 0, width, height);

            gc.setStroke(Color.RED);
            gc.setTextAlign(TextAlignment.CENTER);
            gc.setTextBaseline(VPos.CENTER);
            gc.fillText(
                    "Load File to see the map",
                    Math.round(getWidth() / 2),
                    Math.round(getHeight() / 2)
            );
        }

    }

    @Override
    public double minHeight(double width) {
        return 64;
    }

    @Override
    public double maxHeight(double width) {
        return 6000;
    }

    @Override
    public double prefHeight(double width) {
        return minHeight(width);
    }

    @Override
    public double minWidth(double height) {
        return 0;
    }

    @Override
    public double maxWidth(double height) {
        return 10000;
    }

    @Override
    public boolean isResizable() {
        return true;
    }

    @Override
    public void resize(double width, double height) {
        super.setWidth(width);
        super.setHeight(height);
        redraw();
    }


}
