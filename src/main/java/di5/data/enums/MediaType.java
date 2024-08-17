package di5.data.enums;

public enum MediaType {
    POST, USER;

    public int toInt() {
        switch (this) {
            case POST:
                return 0;
            case USER:
                return 1;
            default:
                throw new IllegalArgumentException("Unknown PostType: " + this);
        }
    }

    public static MediaType fromInt(int i) {
        switch (i) {
            case 0:
                return POST;
            case 1:
                return USER;
            default:
                throw new IllegalArgumentException("Unknown PostType: " + i);
        }
    }
}
