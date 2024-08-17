package di5.data.enums;

public enum Gender {
    MALE, FEMALE, OTHER;

    public int toInt() {
        switch (this) {
            case MALE:
                return 0;
            case FEMALE:
                return 1;
            case OTHER:
                return 2;
            default:
                throw new IllegalArgumentException("Unknown GENDER: " + this);
        }
    }

    public static Gender fromInt(int i) {
        switch (i) {
            case 0:
                return MALE;
            case 1:
                return FEMALE;
            case 2:
                return OTHER;
            default:
                throw new IllegalArgumentException("Unknown GENDER: " + i);
        }
    }
}
