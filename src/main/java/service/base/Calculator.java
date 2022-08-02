package service.base;

import entities.PositionBar;
import entities.RebarCage;
import entities.RebarMesh;
import entities.Structure;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.stream.Stream;

public class Calculator {

    /**
     * »спользуетс€ дл€ урезани€ double значений до двух знаков после зап€той
     */
    private static final DecimalFormat decimalFormat = new DecimalFormat("0.00");
    /**
     * —писок структур, передаваемых дл€ подсчета масс вход€щих в них позиций. » формирование на их базе ведомости и
     * спецификации.
     */
    private ArrayList<Structure> structures = new ArrayList<>();
    /**
     * ƒополнительый модуль дл€ просчета структур. »спользуетс€ дл€ формировани€ структур в просчитанные структуры,
     * которые будут использованы при создании ведомости элементов.
     */
    private CalculatedStructureCreator calculatedStructureCreator;

    public Calculator() {

    }

    /**
     * ќсновной движок ѕодсчета.
     */
    public void calculate(ArrayList<Structure> structures) {
        this.structures = structures;
        completeStructures(); //done
        calculatedStructureCreator.build(this.structures);
    }


    /**
     * ѕроходит по всем структурам, обсчитыва€ массу дл€ каждой позиции структуры.
     */
    private void completeStructures() {
        for (Structure structure :
                structures) {
            completeRebarMeshesWeights(structure.getRebarMeshes());
            completePositionWeights(structure.getPositions());
            completeRebarCagesWeights(structure.getRebarCages());
        }
    }

    /**
     * ќбрезает массу каркаса до 2х знаков после зап€той.
     *
     * @param rebarCages арматурный каркас
     */
    private void completeRebarCagesWeights(ArrayList<RebarCage> rebarCages) {
        final ArrayList<RebarCage> customRebarCages = new ArrayList<>();
        rebarCages.forEach(item -> {
            item.getBars().forEach(positionBar -> {
                try {
                    positionBar.setWeight(Double.parseDouble(decimalFormat.format(positionBar.getWeight()).replaceAll(",", ".")));
                } catch (NumberFormatException e) {
                    System.out.println("ѕроеб с конвертацией после усечение массы до двух знаков, жмых");
                }
            });
            item.refresh();

            rebarCages.forEach(rebarCage -> {
                rebarCage.getBars().forEach((positionBar -> {
                    positionBar.setQuantity(positionBar.getQuantity() * rebarCage.getQuantity());
                }));
            });
        });
    }

    /**
     * ќбходит все арматурные сетки структуры, обсчитыва€ массу позиций сетки.
     *
     * @param rebarMeshes список арматурных сеток структуры дл€ обсчета
     */
    private void completeRebarMeshesWeights(ArrayList<RebarMesh> rebarMeshes) {
        for (RebarMesh rebarMesh :
                rebarMeshes) {
            calculateWeight(rebarMesh.getBase());
//            rebarMesh.getBase().setQuantity(rebarMesh.getBase().getQuantity() * rebarMesh.getQuantity());
            calculateWeight(rebarMesh.getCross());
//            rebarMesh.getCross().setQuantity(rebarMesh.getCross().getQuantity() * rebarMesh.getQuantity());
        }
    }

    /**
     * ќбходит все позиции структуры, обсчитыва€ массу дл€ текущей позиции.
     *
     * @param positions список позиции дл€ обсчета.
     */
    private void completePositionWeights(ArrayList<PositionBar> positions) {
        for (PositionBar position :
                positions) {
            calculateWeight(position);
        }

    }

    /**
     * ¬ычисление массы данной позиции.
     *
     * @param position данна€ позици€ (стержень арматуры).
     */
    private void calculateWeight(PositionBar position) {
        double weight = position.getLength() * getMeterWeight(position.getDiameter()) / 1000;
        try {
            position.setWeight(Double.parseDouble(decimalFormat.format(weight).replaceAll(",", ".")));
        } catch (NumberFormatException e) {
            System.out.println("ѕроеб с конвертацией после усечение массы до двух знаков, жмых");
        }
    }

    /**
     * ¬озвращает массу метра погонного. —равнивает данный диаметр с диаметрами из множества. ѕри совпадении
     * возвращает массу метра погонного, указанного во множестве согласно диаметру.
     *
     * @param diameter данный диаметр
     * @return double масса метра погонного
     */
    private double getMeterWeight(int diameter) {
        final RebarWeightGOST rebarWeightGOST = Stream.of(RebarWeightGOST.values()).filter((item) ->
                item.getDiameter() == diameter
        ).findFirst().orElse(RebarWeightGOST.D0);
        return rebarWeightGOST.getMeterWeight();
    }

    public ArrayList<Structure> getStructures() {
        return structures;
    }

    public void setStructures(ArrayList<Structure> structures) {
        this.structures = structures;
    }

    public CalculatedStructureCreator getCalculatedStructureCreator() {
        return calculatedStructureCreator;
    }

    @Autowired
    public void setCalculatedStructureCreator(CalculatedStructureCreator calculatedStructureCreator) {
        this.calculatedStructureCreator = calculatedStructureCreator;
    }

    private enum RebarWeightGOST {
        D0(0, 0.0),
        D4(4, 0.099),
        D5(5, 0.154),
        D6(6, 0.222),
        D8(8, 0.395),
        D10(10, 0.617),
        D12(12, 0.888),
        D14(14, 1.208),
        D16(16, 1.578),
        D20(20, 2.466),
        D25(25, 3.853),
        D32(32, 6.313);

        private int diameter;
        private double meterWeight;

        RebarWeightGOST(int diameter, double meterWeight) {
            this.diameter = diameter;
            this.meterWeight = meterWeight;
        }

        public int getDiameter() {
            return diameter;
        }

        public void setDiameter(int diameter) {
            this.diameter = diameter;
        }

        public double getMeterWeight() {
            return meterWeight;
        }

        public void setMeterWeight(double meterWeight) {
            this.meterWeight = meterWeight;
        }
    }
}
