package flc.model;

public enum ExerciseType {
    YOGA("Yoga", 15.00),
    ZUMBA("Zumba", 12.00),
    AQUACISE("Aquacise", 10.00),
    BOX_FIT("Box Fit", 18.00),
    BODY_BLITZ("Body Blitz", 14.00);

    private final String displayName;
    private final double price;

    ExerciseType(String displayName, double price) {
        this.displayName = displayName;
        this.price = price;
    }

    public String getDisplayName() {
        return displayName;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return displayName + " (£" + String.format("%.2f", price) + ")";
    }
}
