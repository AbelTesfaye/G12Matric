package armored.g12matrickapp.Adapters;

/**
 * Created by Falcon on 7/29/2017 :: 06:43 inside G12MatrickApp .
 * ALL RIGHTS RECEIVED!
 */

public class ChapterListStructure {

    int capter_number;
    boolean capter_clicked;

    public ChapterListStructure(int capter_number, boolean capter_clicked) {
        this.capter_number = capter_number;
        this.capter_clicked = capter_clicked;
    }

    public int getCapter_number() {
        return capter_number;
    }

    public void setCapter_number(int capter_number) {
        this.capter_number = capter_number;
    }

    public boolean isCapter_clicked() {
        return capter_clicked;
    }

    public void setCapter_clicked(boolean capter_clicked) {
        this.capter_clicked = capter_clicked;
    }
}
