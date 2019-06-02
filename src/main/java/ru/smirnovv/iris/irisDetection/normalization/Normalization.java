package ru.smirnovv.iris.irisDetection.normalization;

import org.opencv.core.Mat;
import ru.smirnovv.iris.irisDetection.localization.IrisBorders;

/**
 * An abstract class that provides normalization of iris.
 * Абстрактный класс, обеспечивающий нормализацию РОГ.
 */
public abstract class Normalization {
    /**
     * Normalizes iris.
     * Нормализует РОГ.
     *
     * @param irisImage the iris image.
     *                  изображение РОГ.
     * @param borders   the borders of iris.
     *                  границы РОГ.
     * @return the normalized iris image.
     * нормализованное изображение РОГ.
     */
    public abstract Mat normalize(final Mat irisImage, final IrisBorders borders);
}

