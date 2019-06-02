package ru.smirnovv.iris.irisDetection.localization.hough;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.smirnovv.iris.irisDetection.localization.IrisBorders;
import ru.smirnovv.iris.irisDetection.localization.IrisBorders.Border;
import ru.smirnovv.iris.irisDetection.localization.Localization;

import static java.lang.Math.pow;
import static java.lang.Math.round;
import static java.lang.Math.sqrt;
import static java.lang.System.out;
import static org.opencv.imgproc.Imgproc.MORPH_RECT;
import static org.opencv.imgproc.Imgproc.Sobel;
import static org.opencv.imgproc.Imgproc.THRESH_BINARY;
import static org.opencv.imgproc.Imgproc.dilate;
import static org.opencv.imgproc.Imgproc.erode;
import static org.opencv.imgproc.Imgproc.getStructuringElement;
import static org.opencv.imgproc.Imgproc.threshold;

/**
 * A class that provides localization of iris.
 * Класс, обеспечивающий локализацию РОГ.
 */
@Component
public final class HoughLocalization extends Localization {
    /**
     * Parameters for the  Hough localization.
     * Параметры для локализации методом Хафа.
     */
    private final HoughParameters houghParameters;

    /**
     * Constructs an instance with injected dependencies.
     * Создает экземпляр с внедренными зависимостями.
     *
     * @param houghParameters parameters for the  Hough localization.
     *                        параметры для локализации методом Хафа.
     */
    public HoughLocalization(final HoughParameters houghParameters) {
        this.houghParameters = houghParameters;
    }

    /**
     * Constructs an instance.
     * Конструктор без параметров.
     */
    @Autowired
    public HoughLocalization() {
        this.houghParameters = new HoughParameters(true, 20, 60, 60, 20);
    }

    /**
     * Localizes iris.
     * Локализует РОГ.
     *
     * @param irisImage the iris image.
     *                  изображение РОГ.
     * @return the borders of iris.
     * границы РОГ.
     */
    public IrisBorders localize(final Mat irisImage) {
        if (houghParameters.isType()) {
            return searchIrisWithDifferentCentres(irisImage);
        } else {
            return searchIrisWithSameCentres(irisImage);
        }
    }

    /**
     * Localizes the iris when the centers do not match.
     * Локализует РОГ, когда центры не совпадают.
     *
     * @param irisImage the iris image.
     *                  изображение РОГ.
     * @return the borders of iris.
     * границы РОГ.
     */
    private IrisBorders searchIrisWithSameCentres(final Mat irisImage) {
        // Searches the inside border.
        // Поиск внутренней границы.
        Border inside = insideBorder(irisImage);

        // Creates parameters for the outside border.
        // Создание параметров для внешней границы.
        HoughParameters outsideParameters = houghParameters.outsideParameters(irisImage, inside);

        // Searches the outside border.
        // Поиск внешней границы.
        Border outside = outsideBorder(irisImage, outsideParameters, inside);

        return new IrisBorders(inside, outside);
    }

    /**
     * Localizes the iris when the centers match.
     * Локализует РОГ, когда центры совпадают.
     *
     * @param irisImage the iris image.
     *                  изображение РОГ.
     * @return the borders of iris.
     * границы РОГ.
     */
    private IrisBorders searchIrisWithDifferentCentres(final Mat irisImage) {
        // Searches the inside border.
        // Поиск внутренней границы.
        Border inside = insideBorder(irisImage);

        // Creates parameters for the outside border.
        // Создание параметров для внешней границы.
        HoughParameters outsideParameters = houghParameters.outsideParameters(irisImage, inside);

        // Searches the outside border.
        // Поиск внешней границы.
        Border outside = outsideBorder(irisImage, outsideParameters, inside, inside.getR() / 8);

        return new IrisBorders(inside, outside);
    }

    /**
     * Localizes inside border.
     * Локализует внутреннюю границу.
     *
     * @param irisImage the iris image.
     *                  изображение РОГ.
     * @return the inside border.
     * внешняя граница РОГ.
     */
    private Border insideBorder(final Mat irisImage) {
        Mat temp = irisImage.clone();

        // The binarization.
        // Бинаризация.
        threshold(temp, temp, houghParameters.getThreshold(), 255, THRESH_BINARY);

        // The morphological operations.
        // Морфологические операции.
        erode(temp, temp, getStructuringElement(MORPH_RECT, new Size(7, 7)));
        dilate(temp, temp, getStructuringElement(MORPH_RECT, new Size(7, 7)));

        // Sobel.
        // Оператор Собеля.
        Sobel(temp, temp, 0, 1, 1);


        // Search border.
        // Поиск границы.
        double[][][] acc = new double[temp.rows()][temp.cols()][houghParameters.getMaxR()];
        double max = 0;
        int resX = 0;
        int resY = 0;
        int resR = 0;


        // Перебор возможных точек границы.
        for (int x = houghParameters.getDistance(); x < temp.rows() - houghParameters.getDistance(); ++x) {
            for (int y = houghParameters.getDistance(); y < temp.cols() - houghParameters.getDistance(); ++y) {
                try {
                    // Если точка не черная, то находим все возможные центры границы.
                    if (temp.get(x, y)[0] > 0) {
                        int left, right;

                        // Ограничиваем поиск центра РОГ (по горизонтали).
                        if (houghParameters.getMaxR() < x && x < temp.rows() - houghParameters.getMaxR()) {
                            left = x - houghParameters.getMaxR();
                            right = x + houghParameters.getMaxR();
                        } else if (x < houghParameters.getMaxR()) {
                            left = 0;
                            right = x + houghParameters.getMaxR();
                        } else {
                            left = x - houghParameters.getMaxR();
                            right = temp.rows();
                        }

                        for (; left < right; ++left) {
                            int down, up;

                            // Ограничиваем поиск центра РОГ (по  вертикали).
                            if (houghParameters.getMaxR() < y && y < temp.cols() - houghParameters.getMaxR()) {
                                down = y - houghParameters.getMaxR();
                                up = y + houghParameters.getMaxR();
                            } else if (y < houghParameters.getMaxR()) {
                                down = 0;
                                up = y + houghParameters.getMaxR();
                            } else {
                                down = y - houghParameters.getMaxR();
                                up = temp.cols();
                            }

                            for (; down < up; ++down) {
                                // Находим возможный центр РОГ и увеличиваем аккумулятор.
                                double r = sqrt(pow(x - left, 2) + pow(y - down, 2));
                                if (houghParameters.getMinR() < round(r) && round(r) < houghParameters.getMaxR()) {
                                    acc[left][down][(int) round(r)] = acc[left][down][(int) round(r)] + 1d / round(r);
                                    if (acc[left][down][(int) round(r)] > max) {
                                        resR = (int) round(r);
                                        resX = left;
                                        resY = down;
                                        max = (int) acc[left][down][(int) round(r)];
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    out.println(e.getMessage());
                }
            }
        }

        return new Border(resX, resY, resR);
    }

    /**
     * Localizes outside border when the centers match.
     * Локализует внешнюю границу, когда центры совпадают.
     *
     * @param irisImage  the iris image.
     *                   изображение РОГ.
     * @param parameters localizations parameters.
     *                   параметры локализации
     * @param inside     inside border.
     *                   внутренняя граница.
     * @return the outside border.
     * внешняя граница РОГ.
     */
    private Border outsideBorder(final Mat irisImage,
                                 final HoughParameters parameters,
                                 final Border inside) {
        Mat temp = irisImage.clone();

        // The binarization.
        // Бинаризация.
        threshold(temp, temp, parameters.getThreshold(), 255, THRESH_BINARY);

        // The morphological operations.
        // Морфологические операции.
        erode(temp, temp, getStructuringElement(MORPH_RECT, new Size(5, 5)));
        dilate(temp, temp, getStructuringElement(MORPH_RECT, new Size(5, 5)));

        // Sobel.
        // Оператор Собеля.
        Sobel(temp, temp, 0, 1, 1);

        // Search border.
        // Поиск границы.
        double[] acc = new double[parameters.getMaxR()];
        double max = 0;
        int resR = 0;

        // Перебор возможных точек границы.
        for (int x = inside.getX() - parameters.getMaxR(); x < inside.getX() + parameters.getMaxR(); ++x) {
            for (int y = inside.getY() - parameters.getMaxR(); y < inside.getY() + parameters.getMaxR(); ++y) {
                try {
                    // Если точка не черная, то находим радиус.
                    if (0 < temp.get(x, y)[0]) {
                        // Увеличиваем аккумулятор.
                        double r = sqrt(pow(x - inside.getX(), 2) + pow(y - inside.getY(), 2));
                        if (parameters.getMinR() < round(r) && round(r) < parameters.getMaxR()) {
                            acc[(int) round(r)] = acc[(int) round(r)] + 1d / round(r);

                            if (acc[(int) round(r)] > max) {
                                resR = (int) round(r);
                                max = (int) acc[(int) round(r)];
                            }
                        }
                    }
                } catch (Exception e) {
                    out.println(e.getMessage());
                }
            }
        }
        return new Border(inside.getX(), inside.getY(), resR);
    }

    /**
     * Localizes outside border when the centers do not match.
     * Локализует внешнюю границу, когда центры не совпадают.
     *
     * @param irisImage  the iris image.
     *                   изображение РОГ.
     * @param parameters localizations parameters.
     *                   параметры локализации.
     * @param inside     inside border.
     *                   внутренняя граница.
     * @param d          the inside border neighborhood.
     *                   окрестность внутренней границы.
     * @return the outside border.
     * внешняя граница РОГ.
     */
    private Border outsideBorder(final Mat irisImage,
                                 final HoughParameters parameters,
                                 final Border inside, final int d) {
        Mat temp = irisImage.clone();


        // The binarization.
        // Бинаризация.
        threshold(temp, temp, parameters.getThreshold(), 255, THRESH_BINARY);

        // The morphological operations.
        // Морфологические операции.
        dilate(temp, temp, getStructuringElement(MORPH_RECT, new Size(5, 5)));
        erode(temp, temp, getStructuringElement(MORPH_RECT, new Size(5, 5)));

        // Sobel.
        // Оператор Собеля.
        Sobel(temp, temp, 0, 1, 1);

        // Search border.
        // Поиск границы.
        double[][][] acc = new double[temp.rows()][temp.cols()][parameters.getMaxR()];
        double max = 0;
        int resX = 0, resY = 0, resR = 0;
        int left, right;

        // Перебор возможных точек границы в возможной области внутренней границы.
        if (0 < inside.getX() - parameters.getMaxR() - d && inside.getX() + parameters.getMaxR() + d < temp.rows()) {
            left = inside.getX() - parameters.getMaxR() - d;
            right = inside.getX() + parameters.getMaxR() + d;
        } else if (inside.getX() - parameters.getMaxR() - d < 0) {
            left = 0;
            right = inside.getX() + parameters.getMaxR() + d;
        } else {
            left = inside.getX() - parameters.getMaxR() - d;
            right = temp.rows();
        }

        for (; left < right; ++left) {
            int down, up;

            if (0 < inside.getY() - parameters.getMaxR() - d && inside.getY() + parameters.getMaxR() + d < temp.cols()) {
                down = inside.getY() - parameters.getMaxR() - d;
                up = inside.getY() + parameters.getMaxR() + d;
            } else if (inside.getY() - parameters.getMaxR() - d < 0) {
                down = 0;
                up = inside.getY() + parameters.getMaxR() + d;
            } else {
                down = inside.getY() - parameters.getMaxR() - d;
                up = temp.cols();
            }


            for (; down < up; ++down) {
                try {
                    // Если точка не черная, то находим все возможные центры границы.
                    if (temp.get(left, down)[0] > 0) {
                        int left1, right1;

                        // Ограничиваем поиск центра РОГ (по горизонтали).
                        if (0 < inside.getX() - d && inside.getX() + d < temp.rows()) {
                            left1 = inside.getX() - d;
                            right1 = inside.getX() + d;
                        } else if (inside.getX() - d < 0) {
                            left1 = 0;
                            right1 = inside.getX() + d;
                        } else {
                            left1 = inside.getX() - d;
                            right1 = temp.rows();
                        }

                        for (; left1 < right1; ++left1) {
                            int down1, up1;

                            // Ограничиваем поиск центра РОГ (по  вертикали).
                            if (0 < inside.getY() - d && inside.getY() + d < temp.cols()) {
                                down1 = inside.getY() - d;
                                up1 = inside.getY() + d;
                            } else if (inside.getY() - d < 0) {
                                down1 = 0;
                                up1 = inside.getY() + d;
                            } else {
                                down1 = inside.getY() - d;
                                up1 = temp.cols();
                            }

                            for (; down1 < up1; ++down1) {
                                double r = sqrt(pow(down1 - down, 2) + pow(left1 - left, 2));

                                // Находим возможный центр РОГ и увеличиваем аккумулятор.
                                if (parameters.getMinR() < round(r) && round(r) < parameters.getMaxR()) {
                                    acc[left1][up1][(int) round(r)] = acc[left1][up1][(int) round(r)] + 1d / round(r);
                                    if (acc[left1][up1][(int) round(r)] > max) {
                                        resR = (int) round(r);
                                        resX = left1;
                                        resY = up1;
                                        max = (int) acc[left1][up1][(int) round(r)];
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    out.println(e.getMessage());
                }
            }
        }

        return new Border(resX, resY, resR);
    }


}