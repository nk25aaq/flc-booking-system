package flc.model;

public class Booking {
    private final String bookingId;
    private final Member member;
    private Lesson lesson;
    private BookingStatus status;
    private Review review;

    public Booking(String bookingId, Member member, Lesson lesson) {
        this.bookingId = bookingId;
        this.member = member;
        this.lesson = lesson;
        this.status = BookingStatus.BOOKED;
    }

    // Setters used exclusively by BookingManager to perform state transitions.
    // Keeping them public for simplicity; in a production system access would be restricted.
    public void setStatus(BookingStatus status) { this.status = status; }
    public void setLesson(Lesson lesson)         { this.lesson = lesson; }
    public void setReview(Review review)         { this.review = review; }

    public String getBookingId()    { return bookingId; }
    public Member getMember()       { return member; }
    public Lesson getLesson()       { return lesson; }
    public BookingStatus getStatus(){ return status; }
    public Review getReview()       { return review; }
    public boolean hasReview()      { return review != null; }

    @Override
    public String toString() {
        String reviewPart = hasReview()
                ? " | Review: " + review.getRating() + "/5 (" + review.getRatingLabel() + ")"
                : " | No review";
        return String.format("[%s] %s | Lesson: %s (%s) | Status: %s%s",
                bookingId, member.getName(),
                lesson.getLessonId(), lesson.getExerciseType().getDisplayName(),
                status, reviewPart);
    }
}
