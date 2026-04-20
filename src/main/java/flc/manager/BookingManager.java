package flc.manager;

import flc.model.Booking;
import flc.model.BookingStatus;
import flc.model.Lesson;
import flc.model.Member;
import flc.model.Review;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BookingManager {
    private final List<Member> members;
    private final Timetable timetable;
    private final List<Booking> bookings;
    private int bookingCounter = 1;
    private int reviewCounter = 1;

    public BookingManager(List<Member> members, Timetable timetable) {
        this.members = new ArrayList<>(members);
        this.timetable = timetable;
        this.bookings = new ArrayList<>();
    }

    private String nextBookingId() {
        return String.format("B%03d", bookingCounter++);
    }

    private String nextReviewId() {
        return String.format("R%03d", reviewCounter++);
    }

    public Booking bookLesson(Member member, Lesson lesson) {
        if (!lesson.hasSpace()) {
            throw new BookingException("Lesson " + lesson.getLessonId() + " is full (capacity 4).");
        }
        for (Booking b : bookings) {
            if (b.getMember().equals(member)
                    && b.getLesson().equals(lesson)
                    && b.getStatus() != BookingStatus.CANCELLED) {
                throw new BookingException("You already have an active booking for lesson " + lesson.getLessonId() + ".");
            }
        }
        for (Booking b : bookings) {
            if (b.getMember().equals(member)
                    && b.getStatus() != BookingStatus.CANCELLED
                    && b.getLesson().getWeekendNumber() == lesson.getWeekendNumber()
                    && b.getLesson().getDay() == lesson.getDay()
                    && b.getLesson().getTimeSlot() == lesson.getTimeSlot()) {
                throw new BookingException("Time conflict: you already have booking "
                        + b.getBookingId() + " in the same slot (Weekend "
                        + lesson.getWeekendNumber() + ", " + lesson.getDay()
                        + ", " + lesson.getTimeSlot() + ").");
            }
        }
        lesson.addMember(member);
        Booking booking = new Booking(nextBookingId(), member, lesson);
        bookings.add(booking);
        return booking;
    }

    public void changeBooking(String bookingId, Lesson newLesson) {
        Booking booking = findBookingById(bookingId);
        if (booking == null) {
            throw new BookingException("Booking " + bookingId + " not found.");
        }
        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new BookingException("Cannot change a cancelled booking.");
        }
        if (booking.getStatus() == BookingStatus.ATTENDED) {
            throw new BookingException("Cannot change an already attended booking.");
        }
        if (booking.getLesson().equals(newLesson)) {
            throw new BookingException("New lesson is the same as the current lesson.");
        }
        if (!newLesson.hasSpace()) {
            throw new BookingException("Lesson " + newLesson.getLessonId() + " is full (capacity 4).");
        }
        Member member = booking.getMember();
        for (Booking b : bookings) {
            if (b.equals(booking)) continue;
            if (b.getMember().equals(member)
                    && b.getLesson().equals(newLesson)
                    && b.getStatus() != BookingStatus.CANCELLED) {
                throw new BookingException("You already have an active booking for lesson " + newLesson.getLessonId() + ".");
            }
        }
        for (Booking b : bookings) {
            if (b.equals(booking)) continue;
            if (b.getMember().equals(member)
                    && b.getStatus() != BookingStatus.CANCELLED
                    && b.getLesson().getWeekendNumber() == newLesson.getWeekendNumber()
                    && b.getLesson().getDay() == newLesson.getDay()
                    && b.getLesson().getTimeSlot() == newLesson.getTimeSlot()) {
                throw new BookingException("Time conflict: you already have booking "
                        + b.getBookingId() + " in the same slot.");
            }
        }
        booking.getLesson().removeMember(member);
        newLesson.addMember(member);
        booking.setLesson(newLesson);
        booking.setStatus(BookingStatus.CHANGED);
    }

    public void cancelBooking(String bookingId) {
        Booking booking = findBookingById(bookingId);
        if (booking == null) {
            throw new BookingException("Booking " + bookingId + " not found.");
        }
        if (booking.getStatus() == BookingStatus.ATTENDED) {
            throw new BookingException("Cannot cancel an already attended booking.");
        }
        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new BookingException("Booking " + bookingId + " is already cancelled.");
        }
        booking.getLesson().removeMember(booking.getMember());
        booking.setStatus(BookingStatus.CANCELLED);
    }

    public void attendLesson(String bookingId, String reviewText, int rating) {
        Booking booking = findBookingById(bookingId);
        if (booking == null) {
            throw new BookingException("Booking " + bookingId + " not found.");
        }
        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new BookingException("Cannot attend a cancelled booking.");
        }
        if (booking.getStatus() == BookingStatus.ATTENDED) {
            throw new BookingException("Booking " + bookingId + " has already been attended.");
        }
        if (rating < 1 || rating > 5) {
            throw new BookingException("Rating must be between 1 and 5.");
        }
        Review review = new Review(nextReviewId(), reviewText, rating);
        booking.setReview(review);
        booking.setStatus(BookingStatus.ATTENDED);
    }

    public Booking findBookingById(String bookingId) {
        return bookings.stream()
                .filter(b -> b.getBookingId().equalsIgnoreCase(bookingId))
                .findFirst()
                .orElse(null);
    }

    public Member findMemberById(String memberId) {
        return members.stream()
                .filter(m -> m.getMemberId().equalsIgnoreCase(memberId))
                .findFirst()
                .orElse(null);
    }

    public List<Booking> getBookingsForMember(Member member) {
        return bookings.stream()
                .filter(b -> b.getMember().equals(member)
                        && b.getStatus() != BookingStatus.CANCELLED)
                .collect(Collectors.toList());
    }

    public List<Booking> getAllBookings() {
        return new ArrayList<>(bookings);
    }

    public List<Member> getMembers() {
        return new ArrayList<>(members);
    }

    public Timetable getTimetable() {
        return timetable;
    }
}
