package websocket.Misc;

public class ColConvertor {

    public ColConvertor() {}

    public Integer convertor(String column) {
        switch (column) {
            case "A","a":
                return 1;
            case "B","b":
                return 2;
            case "C", "c":
                return 3;
            case "D", "d":
                return 4;
            case "E", "e":
                return 5;
            case "F","f":
                return 6;
            case "G","g":
                return 7;
            case "H","h":
                return 8;
            default:
                throw new IllegalArgumentException("Invalid column: " + column);
        }
    }

}
