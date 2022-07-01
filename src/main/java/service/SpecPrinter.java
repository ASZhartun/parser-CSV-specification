package service;

import dao.ReaderCSV.ReaderCSV;
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
        return readerCSV.getContent().get(0) +
                readerCSV.getContent().get(1);
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
        spec.append(makeSpecHeader());
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
        stringBuilder.append(createStructureTitle(structure));
        stringBuilder.append(createStructureRebarMeshesTablePart(structure));
        stringBuilder.append(createPositionBarsTablePart(structure));
        stringBuilder.append(createConcreteTablePart(structure));
        return stringBuilder.toString();
    }

    /**
     * ������� ��������� ����, ����������� ��� "���������". (� ����� ������ ����� ������)
     * @param structure ������� ���������
     * @return ��������� ���� � ���� ������.
     */
    private String createConcreteTablePart(Structure structure) {
        return null;
    }

    /**
     * ������� ��������� ����, ����������� ��� "������". (� ����� ������ ���������� �������)
     * @param structure ������� ���������
     * @return ��������� ���� � ���� ������.
     */
    private String createPositionBarsTablePart(Structure structure) {
        return null;
    }

    /**
     * ������� ��������� ����, ����������� ��� "��������� �������". (� ����� ������ ���������� �����)
     *
     * @param structure ������� ���������
     * @return ��������� ���� � ���� ������.
     */
    private String createStructureRebarMeshesTablePart(Structure structure) {
        final StringBuilder part = new StringBuilder();
        part.append(createTitleRebarBlock());
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
                .append(" ;")
                .append(rebarMesh.getStartNaming())
                .append(" ;")
                .append(rebarMesh.getQuantity())
                .append(" ;")
                .append(rebarMesh.getCross().getWeight() + rebarMesh.getBase().getWeight())
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
                ";" +
                ";\n";
    }

    private Boolean checkRebarMeshesExisting(Structure structure) {
        return false;
    }

    private Boolean checkPositionBarsExisting(Structure structure) {
        return false;
    }
}
