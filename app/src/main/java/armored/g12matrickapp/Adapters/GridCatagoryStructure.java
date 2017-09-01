package armored.g12matrickapp.Adapters;

/**
 * Created by Falcon on 7/18/2017 :: 14:57 inside G12MatrickApp .
 * ALL RIGHTS RECEIVED!
 */

public class GridCatagoryStructure {

    private String subjectName;
    private int resourceAddress;
    private int colorBg;

    public GridCatagoryStructure(String subjectName, int resourceAddress , int colorBg) {
        this.subjectName = subjectName;
        this.resourceAddress = resourceAddress;
        this.colorBg = colorBg;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public int getResourceAddress() {
        return resourceAddress;
    }

    public void setResourceAddress(int resourceAddress) {
        this.resourceAddress = resourceAddress;
    }

    public int getColorBg() {
        return colorBg;
    }

    public void setColorBg(int colorBg) {
        this.colorBg = colorBg;
    }
}
