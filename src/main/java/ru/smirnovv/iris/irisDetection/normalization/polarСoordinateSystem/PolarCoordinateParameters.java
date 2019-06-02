package ru.smirnovv.iris.irisDetection.normalization.polarСoordinateSystem;

/**
 * Parameters for the normalization with polar coordinate.
 * Параметры для нормализации методом полярных координат.
 */
public class PolarCoordinateParameters {
    /**
     * The type of normalization(
     * true - the center mismatch drops).
     * Тип нормализации(
     * true - несоответствие центров опускается).
     */
    final private boolean type;

    /**
     * The number of columns of normalized image.
     * Количество столбцов нормализованного изображения.
     */
    final private int cols;

    /**
     * The number of rows of the normalized image.
     * Количество строк нормализованного изображения.
     */
    final private int rows;

    /**
     * Constructs an instance with injected dependencies.
     * Создает экземпляр с внедренными зависимостями.
     *
     * @param type The type of normalization.
     * @param cols the number of columns of normalized image.
     *             Количество столбцов нормализованного изображения.
     * @param rows the number of rows of the normalized image.
     *             Количество строк нормализованного изображения.
     */
    public PolarCoordinateParameters(final boolean type, final int cols, final int rows) {
        this.type = type;
        this.cols = cols;
        this.rows = rows;
    }

    /**
     * Returns the number of columns of normalized image.
     * Возвращает количество столбцов нормализованного изображения.
     *
     * @return the number of columns of normalized image.
     * количество столбцов нормализованного изображения.
     */
    public int getCols() {
        return cols;
    }

    /**
     * Returns the number of rows of normalized image.
     * Возвращает количество строк нормализованного изображения.
     *
     * @return the number of rows of normalized image.
     * количество строк нормализованного изображения.
     */
    public int getRows() {
        return rows;
    }

    /**
     * Returns the type of normalization.
     * Возвращает тип нормализации.
     *
     * @return the type of normalization.
     * тип нормализации.
     */
    public boolean isType() {
        return type;
    }
}
