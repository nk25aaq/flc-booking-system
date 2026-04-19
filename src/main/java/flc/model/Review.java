package flc.model;

public class Review {
    private static final String[] RATING_LABELS = {
        "", "Very Dissatisfied", "Dissatisfied", "Ok", "Satisfied", "Very Satisfied"
    };

    private final String reviewId;
    private final String text;
    private final int rating;

    public Review(String reviewId, String text, int rating) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5, got: " + rating);
        }
        this.reviewId = reviewId;
        this.text = text;
        this.rating = rating;
    }

    public String getReviewId() {
        return reviewId;
    }

    public String getText() {
        return text;
    }

    public int getRating() {
        return rating;
    }

    public String getRatingLabel() {
        return RATING_LABELS[rating];
    }

    @Override
    public String toString() {
        return String.format("[%s] Rating: %d/5 (%s) - \"%s\"",
                reviewId, rating, getRatingLabel(), text);
    }
}
