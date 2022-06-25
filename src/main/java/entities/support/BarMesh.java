package entities.support;

public class BarMesh {
    private String diameter;
    private String step;
    private String typeBar;
    private String length;
    private String outputFirst;
    private String outputSecond;
    private String extraStep;

    public BarMesh(String diameter, String step, String typeBar, String length, String outputFirst, String outputSecond, String extraStep) {
        this.diameter = diameter;
        this.step = step;
        this.typeBar = typeBar;
        this.length = length;
        this.outputFirst = outputFirst;
        this.outputSecond = outputSecond;
        this.extraStep = extraStep;
    }

    public BarMesh() {

    }

    public String getDiameter() {
        return diameter;
    }

    public void setDiameter(String diameter) {
        this.diameter = diameter;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
//        if (checkStepSignature(step)) {
//            // do something
//        } else {
//            this.step = step;
//        }

    }

    public String getTypeBar() {
        return typeBar;
    }

    public void setTypeBar(String typeBar) {
        this.typeBar = typeBar;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getOutputFirst() {
        return outputFirst;
    }

    public void setOutputFirst(String outputFirst) {
        this.outputFirst = outputFirst;
    }

    public String getOutputSecond() {
        return outputSecond;
    }

    public void setOutputSecond(String outputSecond) {
        this.outputSecond = outputSecond;
    }

    public String getExtraStep() {
        return extraStep;
    }

    public void setExtraStep(String extraStep) {
        this.extraStep = extraStep;
    }
}
