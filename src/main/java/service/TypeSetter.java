package service;

import java.util.*;

public class TypeSetter {
    private HashMap<Integer, Double> S500 = new HashMap<>();
    private HashMap<Integer, Double> S240 = new HashMap<>();
    private int quantityS240;
    private int quantityS500;
    private int quantityCells;

    public TypeSetter(HashMap<Integer, Double> s500, HashMap<Integer, Double> s240) {
        S500 = s500;
        S240 = s240;
        quantityS240 = getQuantity(s240);
        quantityS500 = getQuantity(s500);
        quantityCells = 1 + quantityS240 + 1 + quantityS500 + 1 + 1;
    }

    public TypeSetter() {

    }





    private void calculateTotalFields(String[][] strings) {
        Double temp = 0D;
        for (int i = 0; i < quantityS240; i++) {
            String s = strings[6][1 + i];
            double v = Double.parseDouble(s);
            temp += v;
        }
        strings[6][1 + quantityS240] = temp.toString();

        temp = 0d;

        for (int i = 0; i < quantityS500; i++) {
            String s = strings[6][2 + i + quantityS240];
            double v = Double.parseDouble(s);
            temp += v;
        }
        strings[6][2 + quantityS240 + quantityS500] = temp.toString();

        double s240Total = Double.parseDouble(strings[6][1 + quantityS240]);
        double s500Total = Double.parseDouble(strings[6][2 + quantityS240 + quantityS500]);
        strings[6][3 + quantityS240 + quantityS500] = Double.toString(s240Total + s500Total);
    }

    private void fillWeights(String[][] strings) {
        int cap240 = S240.size();
        int cap500 = S500.size();
        Set<Map.Entry<Integer, Double>> entries240 = S240.entrySet();
        for (int i = 0; i < cap240; i++) {
            String tempBarType = strings[5][i + 1];
            strings[6][i + 1] = entries240
                    .stream()
                    .filter((item) -> item.getKey().toString().equals(tempBarType))
                    .findFirst()
                    .orElse(null)
                    .getValue()
                    .toString();
        }

        Set<Map.Entry<Integer, Double>> entries500 = S500.entrySet();
        for (int i = 0; i < cap240; i++) {
            String tempBarType = strings[5][i + 2 + cap500];
            strings[6][i + 2 + cap500] = entries500
                    .stream()
                    .filter((item) -> item.getKey().toString().equals(tempBarType))
                    .findFirst()
                    .orElse(null)
                    .getValue()
                    .toString();
        }
    }

    private void fillDiameters(String[][] strings) {
        final Set<Integer> integers = S240.keySet();
        final int cap = integers.size();
        final ArrayList<Integer> arrayList = new ArrayList<>(integers);
        arrayList.sort(Integer::compareTo);
        for (int i = 0; i < arrayList.size(); i++) {
            strings[5][i + 1] = arrayList.get(i).toString();
        }

        arrayList.clear();
        arrayList.addAll(S500.keySet());
        arrayList.sort(Integer::compareTo);
        for (int i = 0; i < quantityS500; i++) {
            strings[5][i + 2 + cap] = arrayList.get(i).toString();
        }
    }

    private int getQuantity(HashMap<Integer, Double> map) {
        return map.size();
    }

    public HashMap<Integer, Double> getS500() {
        return S500;
    }

    public void setS500(HashMap<Integer, Double> s500) {
        S500 = s500;
    }

    public HashMap<Integer, Double> getS240() {
        return S240;
    }

    public void setS240(HashMap<Integer, Double> s240) {
        S240 = s240;
    }
}
