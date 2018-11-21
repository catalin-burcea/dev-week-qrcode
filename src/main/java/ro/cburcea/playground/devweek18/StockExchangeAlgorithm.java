package ro.cburcea.playground.devweek18;

public class StockExchangeAlgorithm {

    public static TradePair stockExchange(double[] list) {
        if (list.length == 0) {
            return null;
        }

        TradePair tradePair = new TradePair(list[0], list[list.length - 1]);
        double maxDiff = -1;

        for (int i = list.length - 2; i >= 0; i--) {
            if (list[i] > tradePair.sellPoint) {
                tradePair.sellPoint = list[i];
            } else {
                double diff = tradePair.sellPoint - list[i];
                if (diff > maxDiff) {
                    maxDiff = diff;
                    tradePair.buyPoint = list[i];
                }
            }
        }
        return tradePair;
    }

    public static TradePair stockExchangeN2(double[] list) {
        if (list.length == 0) {
            return null;
        }

        TradePair tradePair = new TradePair(list[0], list[1]);
        double maxDiff = list[1] - list[0];
        for (int i = 0; i < list.length; i++) {
            for (int j = i + 2; j < list.length; j++) {
                if (list[j] - list[i] > maxDiff) {
                    tradePair.buyPoint = list[i];
                    tradePair.sellPoint = list[j];
                    maxDiff = list[j] - list[i];
                }
            }
        }
        return tradePair;
    }

    public static class TradePair {
        private double buyPoint;
        private double sellPoint;

        public TradePair() {
        }

        public TradePair(double buyPoint, double sellPoint) {
            this.buyPoint = buyPoint;
            this.sellPoint = sellPoint;
        }

        public double getBuyPoint() {
            return buyPoint;
        }

        public void setBuyPoint(double buyPoint) {
            this.buyPoint = buyPoint;
        }

        public double getSellPoint() {
            return sellPoint;
        }

        public void setSellPoint(double sellPoint) {
            this.sellPoint = sellPoint;
        }
    }


//    public static TradePair stockExchange(String[] list) {
//        if(list.length == 0) {
//            return null;
//        }
//
//        TradePair tradePair = new TradePair(list[0], list[list.length - 1]);
//
//        for(int i = 1, j = list.length - 2; i <= j; i++, j--) {
//            if(list[i].compareTo(tradePair.buyPoint) < 0) {
//                tradePair.buyPoint = list[i];
//            }
//            if(list[j].compareTo(tradePair.sellPoint) > 0) {
//                tradePair.sellPoint = list[j];
//            }
//        }
//        return tradePair;
//    }

//    public static TradePair stockExchange(double[] list) {
//        if(list.length == 0) {
//            return null;
//        }
//
//        TradePair tradePair = new TradePair(list[0], list[list.length - 1]);
//
//        for(int i = 1, j = list.length - 2; i <= j; i++, j--) {
//            if(list[i] < tradePair.buyPoint) {
//                tradePair.buyPoint = list[i];
//            }else
//            if(list[j] > tradePair.sellPoint) {
//                tradePair.sellPoint = list[j];
//            }
//        }
//        return tradePair;
//    }

}
