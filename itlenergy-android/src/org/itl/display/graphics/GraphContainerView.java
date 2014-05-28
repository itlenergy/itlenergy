package org.itl.display.graphics;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

public class GraphContainerView extends View {

    protected BivariateGraph[] graphs;

    public GraphContainerView(Context context) {
        super(context);

        this.initialise();
    }

    public GraphContainerView(Context context, AttributeSet set) {
        super(context, set);

        this.initialise();
    }

    protected void initialise() {
        this.graphs = new BivariateGraph[8];//need to think how these are laid out...

    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

    }

    protected void onDraw(Canvas canvas) {

    }
}
