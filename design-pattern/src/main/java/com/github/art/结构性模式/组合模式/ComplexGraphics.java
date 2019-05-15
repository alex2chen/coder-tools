package com.github.art.结构性模式.组合模式;

import java.util.ArrayList;
import java.util.List;

/**
 * ComplexGraphics
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2016/8/23
 */
public class ComplexGraphics extends AbstractGraphics {
    private List<AbstractGraphics> complexGraphicsList = null;

    public ComplexGraphics() {
        complexGraphicsList = new ArrayList<>();
    }

    @Override
    protected void add(AbstractGraphics image) {
        complexGraphicsList.add(image);
    }

    @Override
    protected void remove(AbstractGraphics image) {
        complexGraphicsList.add(image);
    }

    @Override
    protected void draw() {
        for (AbstractGraphics image : complexGraphicsList) {
            image.draw();
        }
    }
}
