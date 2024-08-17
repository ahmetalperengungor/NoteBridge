package di5.data.enums;

public enum PostType {
    Question, Discussion, Event, Sponsored;

    public int toInt() {
        switch (this) {
            case Question:
                return 0;
            case Discussion:
                return 1;
            case Event:
                return 2;
            case Sponsored:
                return 3;
            default:
                throw new IllegalArgumentException("Unknown PostType: " + this);
        }
    }

    public static PostType fromInt(int i) {
        switch (i) {
            case 0:
                return Question;
            case 1:
                return Discussion;
            case 2:
                return Event;
                case 3:
                    return Sponsored;
            default:
                throw new IllegalArgumentException("Unknown PostType: " + i);
        }
    }
}