package ru.smirnovv.iris.irisDetection.localization;

import org.opencv.core.Mat;

/**
 * An abstract class that provides localization of iris.
 * Абстрактный класс, обеспечивающий локализацию РОГ.
 */
public abstract class Localization {
    /**
     * Localizes iris.
     * Локализует РОГ.
     * @param irisImage the iris image.
     *                  изображение РОГ.
     * @return the borders of iris.
     *         границы РОГ.
     */
    public abstract IrisBorders localize(final Mat irisImage);
}
