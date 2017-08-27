package armored.g12matrickapp.Utils;

/**
 * Created by Falcon on 6/13/2017 :: 04:18 inside G12TestPlace .
 * ALL RIGHTS RECEIVED!
 */

public class Constants {

    public static int ENGLISH = 1;
    public static int MATHS = 2;
    public static int SAT = 3;
    public static int PHYSICS = 4;
    public static int CHEMISTRY = 5;
    public static int BIOLOGY = 6;
    public static int CIVICS = 11;
    public static int HISTORY = 8;
    public static int ECONOMICS = 9;
    public static int GEOGRAPHY = 10;
    public static int MATHSSOCIAL = 7;

    public static int ENGLISHQ = 120;
    public static int MATHSQ = 65;
    public static int SATQ = 60;
    public static int PHYSICSQ= 50;
    public static int CHEMISTRYQ = 80;
    public static int BIOLOGYQ = 100;
    public static int CIVICSQ = 100;
    public static int HISTORYQ = 7; //TOBEFILLED
    public static int ECONOMICSQ = 8; //TOBEFILLED
    public static int GEOGRAPHYQ = 9; //TOBEFILLED
    public static int MATHSSOCIALQ = 11;

    public static int getCodeFromString(String subject){
        switch (subject){
            case "English":
                return 1;
            case "Mathematics":
                return 2;
            case "SAT":
                return 3;
            case "Physics":
                return 4;
            case "Chemistry":
                return 5;
            case "Biology":
                return 6;
            case "Civics":
                return 11;
            case "History":
                return 8;
            case "Economics":
                return 9;
            case "Geography":
                return 10;
            case "Maths-Social":
                return 7;
            default:
                return 0;
        }
    }

    public static int getHowMuchUnitsSubjectHasG11(int code){
        int units = 0;
        switch (code){
            case 1:
                units = -1;
                break;
            case 2:
                units = 6;
                break;
            case 3:
                units = -1;
                break;
            case 4:
                units = 8;
                break;
            case 5:
                units = 6;
                break;
            case 6:
                units = 5;
                break;
            case 7:
                units = 6;
                break;
            case 8:
                units = 4;
                break;
            case 9:
                units = 5;
                break;
            case 10:
                units = 4;
                break;
            case 11:
                units = 4;
                break;
            default:
                units = 0;
                break;
        }
        return units;
    }

    public static int getHowMuchUnitsSubjectHasG12(int code){
        int units = 0;
        switch (code){
            case 1:
                units = -1;
                break;
            case 2:
                units = 7;
                break;
            case 3:
                units = -1;
                break;
            case 4:
                units = 8;
                break;
            case 5:
                units = 6;
                break;
            case 6:
                units = 5;
                break;
            case 7:
                units = 4;
                break;
            case 8:
                units = 4;
                break;
            case 9:
                units = 5;
                break;
            case 10:
                units = 4;
                break;
            case 11:
                units = 7;
                break;
            default:
                units = 0;
                break;
        }
        return units;
    }

    public static int getTimerDataForSubject(int code){
        int times = 0;
        switch (code){
            case 1:
                times = 4;
                break;
            case 2:
                times = 6;
                break;
            case 3:
                times = 4;
                break;
            case 4:
                times = 4;
                break;
            case 5:
                times = 5;
                break;
            case 6:
                times = 4;
                break;
            case 7:
                times = 6;
                break;
            case 8:
                times = 4;
                break;
            case 9:
                times = 5;
                break;
            case 10:
                times = 4;
                break;
            case 11:
                times = 4;
                break;
            default:
                times = 0;
                break;
        }
        return times;
    }

    public static String getSubjectName(int code){
        switch (code){
            case 1:
                return "English";
            case 2:
                return "Mathematics";
            case 3:
                return "SAT";
            case 4:
                return "Physics";
            case 5:
                return "Chemistry";
            case 6:
                return "Biology";
            case 7:
                return "Maths=Social";
            case 8:
                return "History";
            case 9:
                return "Economics";
            case 10:
                return "Geography";
            case 11:
                return "Civics";
            default:
                return "Subject";
        }
    }

    public static String getSubjectNameForEvaluation(int code){
        switch (code){
            case 1:
                return "eng";
            case 2:
                return "mat";
            case 3:
                return "sat";
            case 4:
                return "phy";
            case 5:
                return "chem";
            case 6:
                return "bio";
            case 7:
                return "matsocial";
            case 8:
                return "hist";
            case 9:
                return "eco";
            case 10:
                return "geo";
            case 11:
                return "civ";
            default:
                return "";
        }
    }

    public static final int COLLAPSE_BOTTOM_MENU = 4415;
    public static final int EXPANDE_BOTTOM_MENU = 4416;
    public static final int HIDE_BOTTOM_MENU = 4417;




}
