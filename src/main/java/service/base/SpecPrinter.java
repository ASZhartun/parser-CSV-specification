package service.base;

import dao.ReaderCSV;
import entities.PositionBar;
import entities.RebarCage;
import entities.RebarMesh;
import entities.Structure;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

/**
 * �����, ������� ��������� csv ��������� ������������ ���������.
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
     * ��������� ��������� ������������ �� ������ ��������
     *
     * @param structures ������ ��������
     * @return ��������� ������������� �������
     */
    public String build(ArrayList<Structure> structures) {
        StringBuilder spec = new StringBuilder();
//        spec.append(makeSpecHeader());
        spec.append("���.;�����������;������������;���-��;����� ��.,��;����������;\n");
        for (Structure structure :
                structures) {
            spec.append(getBodyTableFrom(structure));
        }
        return spec.toString();
    }

    /**
     * ��������� ������, ������������� �� ����� ������� ���������, � ������ ��������������� ������.
     *
     * @param structure ������ ������� ���������
     * @return ������ (� ���. ������ �������) �� ���������
     */
    private String getBodyTableFrom(Structure structure) {
        final StringBuilder stringBuilder = new StringBuilder();
//        stringBuilder.append(createStructureTitle(structure)); done
        stringBuilder.append(" ; ;"+structure.getTitle() + "; ; ; ;\n");
        stringBuilder.append(createStructureRebarMeshesTablePart(structure)); //done
        stringBuilder.append(createStructureRebarCagesTablePart(structure));
        stringBuilder.append(createPositionBarsTablePart(structure)); //done
        stringBuilder.append(createConcreteTablePart(structure)); //done
        return stringBuilder.toString();
    }

    private String createStructureRebarCagesTablePart(Structure structure) {
        final StringBuilder lines = new StringBuilder();
        final ArrayList<RebarCage> rebarCages = structure.getRebarCages();
        for (RebarCage rebarCage :
                rebarCages) {
            lines.append(createRebarCageSignature(rebarCage));
        }
        return lines.toString();
    }

    private String createRebarCageSignature(RebarCage rebarCage) {
        final StringBuilder line = new StringBuilder(" ");
        line.append(" ;")
                .append(rebarCage.getDoc())
                .append(";")
                .append(rebarCage.getTitle())
                .append(";")
                .append(rebarCage.getQuantity())
                .append(";=")
                .append(String.format("%2f", rebarCage.getUnitWeight()))
                .append(" ;\n");
        return line.toString();
    }

    /**
     * ������� ��������� ����, ����������� ��� "���������". (� ����� ������ ����� ������)
     *
     * @param structure ������� ���������
     * @return ��������� ���� � ���� ������.
     */
    private String createConcreteTablePart(Structure structure) {
        final ArrayList<String> concreteDefinition = structure.getConcreteDefinition();
        final StringBuilder materials = new StringBuilder();
        materials.append(" ; ;���������; ; ; ;\n");
        concreteDefinition.forEach(materials::append);
        materials.append(" ; ; ; ; ; ;\n");
        return materials.toString();
    }

    /**
     * ������� �������� ������������� ������ �� ������� ���������.
     *
     * @param structure ������ ���������
     * @return ������-�������������
     */
    private String createConcreteContent(Structure structure) {
        final StringBuilder concreteContent = new StringBuilder();
        concreteContent.append(" ;")
                .append("��� EN 206-2016;")
                .append(structure.getConcreteDefinition())
                .append("; ; ;")
                .append(structure.getConcreteVolume())
                .append("�3;\n");


        return concreteContent.toString();
    }

    /**
     * ������� ��������� ����� "���������", � ������� ����������� �������� ���������� �������.
     *
     * @return ������ � ����������
     */
    private String createTitleMaterialsBlock() {
        final StringBuilder title = new StringBuilder();
        title.append(" ;")
                .append(" ;")
                .append("���������;")
                .append(" ;")
                .append(" ;\n");
        return title.toString();
    }

    /**
     * ������� ��������� ����, ����������� ��� "������". (� ����� ������ ���������� �������)
     *
     * @param structure ������� ���������
     * @return ��������� ���� � ���� ������.
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
     * ���������� ��������� ������ �� ���� ������� ���������� �������
     *
     * @param item ������ ���������� �������
     * @return ������-�������������
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
     * ������� ��������� ����� "������", � ������� ����������� �������� ���������� �������.
     *
     * @return ������ � ����������
     */
    private String createTitlePositionBarsBlock() {
        final StringBuilder title = new StringBuilder();
        title.append(" ;")
                .append(" ;")
                .append("������;")
                .append(" ;")
                .append(" ;\n");
        return title.toString();
    }

    /**
     * ������� ��������� ����, ����������� ��� "��������� �������". (� ����� ������ ���������� �����)
     *
     * @param structure ������� ���������
     * @return ��������� ���� � ���� ������.
     */
    private String createStructureRebarMeshesTablePart(Structure structure) {
        final StringBuilder part = new StringBuilder();
        part.append(" ; ;��������� �������; ; ; ;\n");
//        part.append(createTitleRebarBlock());
        part.append(createRebarMeshesLines(structure.getRebarMeshes()));
        return part.toString();
    }

    /**
     * ������� ��������� ������ ��� ���������� �����.
     *
     * @param rebarMeshes ������ ���� ����� ���������
     * @return ������ � ��������� ���������� �����.
     */
    private String createRebarMeshesLines(ArrayList<RebarMesh> rebarMeshes) {
        final StringBuilder lines = new StringBuilder();
        for (RebarMesh rebarMesh :
                rebarMeshes) {
            lines.append(createRebarMeshSignature(rebarMesh));
        }
//        lines.append('\n');
        return lines.toString();
    }

    /**
     * ������� ������ �� ������� �����
     *
     * @param rebarMesh ������ ���������� �����
     * @return ������
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
     * ������� ��������� ����� "��������� �������", � ������� ����������� �������� �����.
     *
     * @return ������ (�������)
     */
    private String createTitleRebarBlock() {
        String title = "";
        title += " ;";
        title += " ;";
        title += "��������� �������";
        title += " ;";
        title += " ;\n";
        return title;
    }

    private String createStructureTitle(Structure structure) {
        return ";" +
                ";" +
                structure.getTitle() +
                ";" + ";" +
                ";";
    }

    private Boolean checkRebarMeshesExisting(Structure structure) {
        return false;
    }

    private Boolean checkPositionBarsExisting(Structure structure) {
        return false;
    }
}
