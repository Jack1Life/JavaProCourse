public enum Currency {
    UAH,USD,EUR;

    public static String getStrValue(Currency cur) {
        switch (cur) {
            case UAH:
                return "UAH";
            case USD:
                return "USD";
            case EUR:
                return "EUR";
            default:
                return "Unknown";
        }
    }
}
