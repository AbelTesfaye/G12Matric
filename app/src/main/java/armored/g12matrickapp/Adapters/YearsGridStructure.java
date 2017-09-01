package armored.g12matrickapp.Adapters;

/**
 * Created by Falcon on 5/29/2010 :: 00:17 inside G12MatrickApp .
 * ALL RIGHTS RECEIVED!
 */

public class YearsGridStructure {

    String subject_name;
    double year_complition;

    public YearsGridStructure(String subject_name, double year_complition) {
        this.subject_name = subject_name;
        this.year_complition = year_complition;
    }

    public String getSubject_name() {
        return subject_name;
    }

    public void setSubject_name(String subject_name) {
        this.subject_name = subject_name;
    }

    public double getYear_complition() {
        return year_complition;
    }

    public void setYear_complition(double year_complition) {
        this.year_complition = year_complition;
    }
}
