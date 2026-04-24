package flc;

import flc.model.Review;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the Review constructor's rating validation.
 */
class ReviewTest {

    @Test
    void constructor_validRating_createsReviewSuccessfully() {
        Review review = new Review("R001", "Enjoyed the session.", 4);
        assertEquals(4, review.getRating());
        assertEquals("Satisfied", review.getRatingLabel());
        assertEquals("Enjoyed the session.", review.getText());
    }

    @Test
    void constructor_ratingBelowOne_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Review("R002", "Some text", 0),
                "Rating of 0 must throw IllegalArgumentException");
    }

    @Test
    void constructor_ratingAboveFive_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Review("R003", "Some text", 6),
                "Rating of 6 must throw IllegalArgumentException");
    }
}
