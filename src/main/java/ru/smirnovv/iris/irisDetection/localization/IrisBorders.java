package ru.smirnovv.iris.irisDetection.localization;

/**
 * The borders of iris.
 * Границы РОГ.
 */
public class IrisBorders {
    /**
     * The inside border of iris.
     * Внутренняя граница РОГ.
     */
    private final Border insideBorder;

    /**
     * The outside border of iris.
     * Внешняя граница РОГ.
     */
    private final Border outsideBorder;

    /**
     * Constructs an instance with injected dependencies.
     * Создает экземпляр с внедренными зависимостями.
     *
     * @param insideBorder  the inside border of iris.
     *                      внутренняя граница РОГ.
     * @param outsideBorder the outside border of iris.
     *                      внешняя граница РОГ.
     */
    public IrisBorders(final Border insideBorder, final Border outsideBorder) {
        this.insideBorder = insideBorder;
        this.outsideBorder = outsideBorder;
    }

    /**
     * Returns the inside border of iris.
     * Возвращает внутреннюю границу.
     *
     * @return the inside border of iris.
     *         внутренняя граница РОГ.
     */
    public Border getInsideBorder() {
        return insideBorder;
    }

    /**
     * Returns the outside border of iris.
     * Возвращает внешнюю границу.
     *
     * @return the outside border of iris.
     *         внешняя граница РОГ.
     */
    public Border getOutsideBorder() {
        return outsideBorder;
    }

    @Override
    public String toString() {
        return "(" + insideBorder +
                "); (" + outsideBorder + ")";
    }

    /**
     * The border.
     * Граница.
     */
    public static class Border {
        /**
         * The coordinates of the center (x, y).
         * Координаты центра (x, y).
         */
        private final int x;

        /**
         * The coordinates of the center (x, y).
         * Координаты центра (x, y).
         */
        private final int y;

        /**
         * The radius of the border.
         * Радиус границы.
         */
        private final int r;

        /**
         * Constructs an instance with injected dependencies.
         * Создает экземпляр с внедренными зависимостями.
         *
         * @param x the coordinates of the center (x, y).
         *          координаты центра (x, y).
         * @param y the coordinates of the center (x, y).
         *          координаты центра (x, y).
         * @param r the radius of the border.
         *          радиус границы.
         */
        public Border(final int x, final int y, final int r) {
            this.x = x;
            this.y = y;
            this.r = r;
        }

        /**
         * Returns x.
         * Возвращает x.
         *
         * @return x.
         */
        public int getX() {
            return x;
        }

        /**
         * Returns y.
         * Возвращает y.
         *
         * @return y.
         */
        public int getY() {
            return y;
        }

        /**
         * Returns r.
         * Возвращает r.
         *
         * @return r.
         */
        public int getR() {
            return r;
        }

        @Override
        public String toString() {
            return x + ";" + y + ";" + r;
        }
    }
}
