package flc.data;

import flc.manager.BookingException;
import flc.manager.BookingManager;
import flc.manager.Timetable;
import flc.model.Booking;
import flc.model.Day;
import flc.model.ExerciseType;
import flc.model.Lesson;
import flc.model.Member;
import flc.model.TimeSlot;

import java.util.ArrayList;
import java.util.List;

public class DataSeeder {

    public static List<Member> createMembers() {
        List<Member> members = new ArrayList<>();
        members.add(new Member("M001", "Alice Johnson"));
        members.add(new Member("M002", "Bob Smith"));
        members.add(new Member("M003", "Carol White"));
        members.add(new Member("M004", "David Brown"));
        members.add(new Member("M005", "Emma Davis"));
        members.add(new Member("M006", "Frank Wilson"));
        members.add(new Member("M007", "Grace Taylor"));
        members.add(new Member("M008", "Henry Moore"));
        members.add(new Member("M009", "Isla Anderson"));
        members.add(new Member("M010", "Jack Thomas"));
        return members;
    }

    public static Timetable createTimetable() {
        Timetable timetable = new Timetable();

        // Rotation of exercise types per weekend slot.
        // Each row = [Morning, Afternoon, Evening] for that weekend.
        ExerciseType[][] satPattern = {
            {ExerciseType.YOGA,       ExerciseType.ZUMBA,      ExerciseType.BOX_FIT},    // W1
            {ExerciseType.ZUMBA,      ExerciseType.AQUACISE,   ExerciseType.BODY_BLITZ}, // W2
            {ExerciseType.AQUACISE,   ExerciseType.BODY_BLITZ, ExerciseType.YOGA},       // W3
            {ExerciseType.BODY_BLITZ, ExerciseType.BOX_FIT,    ExerciseType.ZUMBA},      // W4
            {ExerciseType.YOGA,       ExerciseType.ZUMBA,      ExerciseType.BOX_FIT},    // W5
            {ExerciseType.ZUMBA,      ExerciseType.AQUACISE,   ExerciseType.BODY_BLITZ}, // W6
            {ExerciseType.AQUACISE,   ExerciseType.BODY_BLITZ, ExerciseType.YOGA},       // W7
            {ExerciseType.BODY_BLITZ, ExerciseType.BOX_FIT,    ExerciseType.ZUMBA},      // W8
        };

        ExerciseType[][] sunPattern = {
            {ExerciseType.AQUACISE,   ExerciseType.BODY_BLITZ, ExerciseType.YOGA},       // W1
            {ExerciseType.BOX_FIT,    ExerciseType.YOGA,       ExerciseType.ZUMBA},      // W2
            {ExerciseType.ZUMBA,      ExerciseType.BOX_FIT,    ExerciseType.AQUACISE},   // W3
            {ExerciseType.YOGA,       ExerciseType.AQUACISE,   ExerciseType.BODY_BLITZ}, // W4
            {ExerciseType.AQUACISE,   ExerciseType.BODY_BLITZ, ExerciseType.YOGA},       // W5
            {ExerciseType.BOX_FIT,    ExerciseType.YOGA,       ExerciseType.ZUMBA},      // W6
            {ExerciseType.ZUMBA,      ExerciseType.BOX_FIT,    ExerciseType.AQUACISE},   // W7
            {ExerciseType.YOGA,       ExerciseType.AQUACISE,   ExerciseType.BODY_BLITZ}, // W8
        };

        TimeSlot[] slots = {TimeSlot.MORNING, TimeSlot.AFTERNOON, TimeSlot.EVENING};
        // Weekends 1-4 are May (month 5), weekends 5-8 are June (month 6)
        int[] months = {5, 5, 5, 5, 6, 6, 6, 6};

        int lessonNum = 1;
        for (int w = 0; w < 8; w++) {
            for (int s = 0; s < 3; s++) {
                timetable.addLesson(new Lesson(
                        String.format("L%03d", lessonNum++),
                        satPattern[w][s], Day.SATURDAY, slots[s], w + 1, months[w]));
            }
            for (int s = 0; s < 3; s++) {
                timetable.addLesson(new Lesson(
                        String.format("L%03d", lessonNum++),
                        sunPattern[w][s], Day.SUNDAY, slots[s], w + 1, months[w]));
            }
        }
        return timetable;
    }

    /**
     * Seeds 23 attended bookings with reviews so monthly reports have meaningful
     * data immediately on startup. All seeds are for May (month 5) lessons L001-L024.
     */
    public static void seedBookingsAndReviews(BookingManager manager) {
        // Each row: memberId, lessonId, reviewText, rating
        String[][] seeds = {
            {"M001", "L001", "Fantastic Yoga session, very calming.",          "5"},
            {"M002", "L001", "Good class, a little crowded though.",            "3"},
            {"M003", "L002", "Zumba was so much fun, loved every minute!",     "5"},
            {"M004", "L002", "Great energy from the instructor.",               "4"},
            {"M005", "L003", "Box Fit is intense but I feel amazing after.",   "4"},
            {"M006", "L003", "A bit too tough for my fitness level.",           "2"},
            {"M007", "L004", "Aquacise is so refreshing and low impact.",       "5"},
            {"M008", "L004", "Really enjoyed the water workout.",               "4"},
            {"M009", "L005", "Body Blitz left me feeling energised all day!",  "5"},
            {"M010", "L005", "Hard workout but definitely worth it.",           "4"},
            {"M001", "L006", "Evening Yoga is the perfect end to the weekend.", "5"},
            {"M002", "L007", "Zumba again and it keeps getting better!",       "5"},
            {"M003", "L008", "Aquacise in the afternoon was wonderful.",        "4"},
            {"M004", "L009", "Body Blitz challenged me in new ways.",           "3"},
            {"M005", "L010", "Box Fit on Sunday morning was a great start.",   "4"},
            {"M006", "L011", "Peaceful Yoga session in the afternoon.",        "5"},
            {"M007", "L012", "Evening Zumba is always a brilliant time.",       "4"},
            {"M001", "L013", "Another great Aquacise class, very relaxing.",   "4"},
            {"M002", "L014", "Body Blitz in the afternoon was really tough.",  "3"},
            {"M003", "L015", "Morning Yoga set me up perfectly for the day.",  "5"},
            {"M004", "L016", "Sunday morning Zumba was full of energy.",       "4"},
            {"M005", "L017", "Box Fit Sunday afternoon - excellent session.",  "5"},
            {"M006", "L018", "Evening Aquacise - best class I have attended!", "5"},
        };

        Timetable timetable = manager.getTimetable();
        for (String[] seed : seeds) {
            Member member = manager.findMemberById(seed[0]);
            Lesson lesson  = timetable.getLessonById(seed[1]);
            if (member == null || lesson == null) continue;
            try {
                Booking booking = manager.bookLesson(member, lesson);
                manager.attendLesson(booking.getBookingId(), seed[2], Integer.parseInt(seed[3]));
            } catch (BookingException ignored) {
                // skip any seed conflicts silently
            }
        }
    }
}
