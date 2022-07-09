package service;

import dao.ReaderCSV.ReaderCSV;
import entities.PositionBar;
import entities.RebarMesh;
import entities.Structure;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

/**
 * Класс, который формирует csv финальную спецификацию элементов.
 */
public class SpecPrinter {

    private ReaderCSV readerCSV;

    public SpecPrinter() {
    }

    private String makeSpecHeader() {
        return readerCSV.getHeadings();
    }

    public ReaderCSV getReaderCSV() {
        return readerCSV;
    }

    @Autowired
    public void setReaderCSV(ReaderCSV readerCSV) {
        this.readerCSV = readerCSV;
    }

    /**
     * Формирует финальную спецификацию из списка структур
     *
     * @param structures список структур
     * @return строковое представление таблицы
     */
    public String build(ArrayList<Structure> structures) {
        StringBuilder spec = new StringBuilder();
        spec.append(makeSpecHeader());
        for (Structure structure :
                structures) {
            spec.append(getBodyTableFrom(structure));
        }
        return spec.toString();
    }

    /**
     * Склеивает строки, формирующиеся из полей объекта структуры, в единую таблицеподобную строку.
     *
     * @param structure объект текущей структуры
     * @return строка (в физ. смысле таблица) из структуры
     */
    private String getBodyTableFrom(Structure structure) {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(createStructureTitle(structure)); //done
        stringBuilder.append(createStructureRebarMeshesTablePart(structure)); //done
        stringBuilder.append(createPositionBarsTablePart(structure)); //done
        stringBuilder.append(createConcreteTablePart(structure)); //done
        return stringBuilder.toString();
    }

    /**
     * Создает табличный блок, описывающий все "Материалы". (В нашем случае марка бетона)
     *
     * @param structure текущая структура
     * @return табличный блок в виде строки.
     */
    private String createConcreteTablePart(Structure structure) {
        final StringBuilder materials = new StringBuilder();
        materials.append(createTitleMaterialsBlock());
        materials.append(createConcreteContent(structure));
        return materials.toString();
    }

    /**
     * Создает строчное представление бетона из объекта структуры.
     *
     * @param structure объект структуры
     * @return строка-представление
     */
    private String createConcreteContent(Structure structure) {
        final StringBuilder concreteContent = new StringBuilder();
        concreteContent.append(" ;")
                .append("СТБ EN 206-2016;")
                .append(structure.getConcreteDefinition())
                .append("; ; ;")
                .append(structure.getConcreteVolume())
                .append("м3;\n");


        return concreteContent.toString();
    }

    /**
     * Создает заголовок блока "Материалы", в которой размещается описание стержневых позиций.
     *
     * @return строка с заголовком
     */
    private String createTitleMaterialsBlock() {
        final StringBuilder title = new StringBuilder();
        title.append(" ;")
                .append(" ;")
                .append("Материалы;")
                .append(" ;")
                .append(" ;\n");
        return title.toString();
    }

    /**
     * Создает табличный блок, описывающий все "Детали". (В нашем случае арматурные стержни)
     *
     * @param structure текущая структура
     * @return табличный блок в виде строки.
     */
    private String createPositionBarsTablePart(Structure structure) {
        final ArrayList<PositionBar> positions = structure.getPositions();
        final StringBuilder positionBarBlock = new StringBuilder();
        positionBarBlock.append(createTitlePositionBarsBlock());
        for (PositionBar item :
                positions) {
            positionBarBlock.append(createPositionBarLine(item));
        }
        return positionBarBlock.toString();
    }

    /**
     * Возвращает табличную строку на базе объекта стержневой позиции
     *
     * @param item объект стержневой позиции
     * @return строка-представление
     */
    private String createPositionBarLine(PositionBar item) {
        final StringBuilder line = new StringBuilder();
        line.append(" ;")
                .append(" ;")
                .append(item.toString())
                .append(";")
                .append(item.getQuantity())
                .append(";=")
                .append(String.format("%2f", item.getWeight()))
                .append(";")
                .append(" ;\n");
        return line.toString();
    }

    /**
     * Создает заголовок блока "Детали", в которой размещается описание стержневых позиций.
     *
     * @return строка с заголовком
     */
    private String createTitlePositionBarsBlock() {
        final StringBuilder title = new StringBuilder();
        title.append(" ;")
                .append(" ;")
                .append("Детали;")
                .append(" ;")
                .append(" ;\n");
        return title.toString();
    }

    /**
     * Создает табличный блок, описывающий все "Сборочные единицы". (В нашем случае арматурные сетки)
     *
     * @param structure текущая структура
     * @return табличный блок в виде строки.
     */
    private String createStructureRebarMeshesTablePart(Structure structure) {
        final StringBuilder part = new StringBuilder();
        part.append(createTitleRebarBlock());
        part.append(createRebarMeshesLines(structure.getRebarMeshes()));
        return part.toString();
    }

    /**
     * Создает табличные строки для арматурных сеток.
     *
     * @param rebarMeshes список всех сеток структуры
     * @return строки с описанием арматурных сеток.
     */
    private String createRebarMeshesLines(ArrayList<RebarMesh> rebarMeshes) {
        final StringBuilder lines = new StringBuilder();
        for (RebarMesh rebarMesh :
                rebarMeshes) {
            lines.append(createRebarMeshSignature(rebarMesh));
        }
        return lines.toString();
    }

    /**
     * Создает строку из объекта сетки
     *
     * @param rebarMesh объект арматурной сетки
     * @return строка
     */
    private String createRebarMeshSignature(RebarMesh rebarMesh) {
        final StringBuilder line = new StringBuilder(" ");
        line.append(" ;")
                .append(rebarMesh.getDoc())
                .append(";")
                .append(rebarMesh.getStartNaming())
                .append(";")
                .append(rebarMesh.getQuantity())
                .append(";=")
                .append(String.format("%2f", rebarMesh.getCross().getWeight() + rebarMesh.getBase().getWeight()))
                .append(" ;\n");
        return line.toString();
    }

    /**
     * Создает заголовок блока "Сборочные единицы", в которой размещается описание сеток.
     *
     * @return строка (таблицы)
     */
    private String createTitleRebarBlock() {
        String title = "";
        title += " ;";
        title += " ;";
        title += "Сборочные единицы";
        title += " ;";
        title += " ;\n";
        return title;
    }

    private String createStructureTitle(Structure structure) {
        return ";" +
                ";" +
                structure.getTitle() +
                ";" + ";" +
                ";\n";
    }

    private Boolean checkRebarMeshesExisting(Structure structure) {
        return false;
    }

    private Boolean checkPositionBarsExisting(Structure structure) {
        return false;
    }
}
