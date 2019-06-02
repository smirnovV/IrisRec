package ru.smirnovv.index.irisRecognition;

import org.opencv.core.Mat;

/**
 * An abstract class that provides iris comparison.
 * Абстрактный класс, обеспечивающий сравнение РОГ.
 */
public abstract class IrisCompare {
    /**
     * Compares the iris.
     * Производит сравнение РОГ.
     *
     * @param irisImage    the image of the iris.
     *                     изображение РОГ.
     * @param irisTemplate the image of the iris.
     *                     изображение РОГ.
     * @return the percent of coincidence of the iris.
     *         процент совпадения РОГ.
     */
    public abstract double compare(final Mat irisImage, final Mat irisTemplate);

    /**
     * Compares the iris to authorize a person.
     * Производит сравнение РОГ для авторизации человека.
     *
     * @param irisImage    the image of the iris.
     *                     изображение РОГ.
     * @param irisTemplate the image of the iris.
     *                     изображение РОГ.
     * @return {@code true} if the iris coincided.
     *                      подтверждение совпадения РОГ.
     */
    public abstract boolean authorize(final Mat irisImage, final Mat irisTemplate);
}
