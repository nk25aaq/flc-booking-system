package flc.ui;

import flc.manager.BookingException;
import flc.manager.BookingManager;
import flc.model.Booking;
import flc.model.BookingStatus;
import flc.model.Day;
import flc.model.ExerciseType;
import flc.model.Lesson;
import flc.model.Member;
import flc.report.ReportGenerator;

import java.util.List;
import java.util.Scanner;

public class CLI {
    private final BookingManager manager;
    private final ReportGenerator reportGenerator;
    private final Scanner scanner;

    public CLI(BookingManager manager) {
        this.manager = manager;
        this.reportGenerator = new ReportGenerator();
        this.scanner = new Scanner(System.in);
    }

    public void run() {
        printBanner();
        boolean running = true;
        while (running) {
            printMainMenu();
            int choice = readInt("Enter choice: ");
            switch (choice) {
                case 1  -> bookLesson();
                case 2  -> changeBooking();
                case 3  -> cancelBooking();
                case 4  -> attendLesson();
                case 5  -> monthlyLessonReport();
                case 6  -> championReport();
                case 0  -> running = false;
                default -> System.out.println("Invalid option. Please choose 0-6.");
            }
        }
        System.out.println("\nGoodbye! Thank you for using the FLC Booking System.");
    }

    // -------------------------------------------------------------------------
    // Menu printing
    // -------------------------------------------------------------------------

    private void printBanner() {
        System.out.println("=================================================================");
        System.out.println("       Furzefield Leisure Centre  -  Booking System");
        System.out.println("=================================================================");
    }

    private void printMainMenu() {
        System.out.println("\n--- Main Menu ---");
        System.out.println("  1. Book a group exercise lesson");
        System.out.println("  2. Change a booking");
        System.out.println("  3. Cancel a booking");
        System.out.println("  4. Attend a lesson");
        System.out.println("  5. Monthly lesson report");
        System.out.println("  6. Monthly champion lesson type report");
        System.out.println("  0. Exit");
    }

    // -------------------------------------------------------------------------
    // Functionality 1 - Book a lesson
    // -------------------------------------------------------------------------

    private void bookLesson() {
        System.out.println("\n=== Book a Group Exercise Lesson ===");

        Member member = selectMember();
        if (member == null) return;

        Lesson lesson = selectLessonFromTimetable();
        if (lesson == null) return;

        try {
            Booking booking = manager.bookLesson(member, lesson);
            System.out.println("\nSUCCESS: Booking " + booking.getBookingId()
                    + " created for " + member.getName()
                    + " -> Lesson " + lesson.getLessonId()
                    + " (" + lesson.getExerciseType().getDisplayName() + ").");
        } catch (BookingException e) {
            System.out.println("\nERROR: " + e.getMessage());
        }
    }

    // -------------------------------------------------------------------------
    // Functionality 2 - Change a booking
    // -------------------------------------------------------------------------

    private void changeBooking() {
        System.out.println("\n=== Change a Booking ===");

        String bookingId = readString("Enter Booking ID to change: ").toUpperCase();
        Booking current = manager.findBookingById(bookingId);
        if (current == null) {
            System.out.println("ERROR: Booking " + bookingId + " not found.");
            return;
        }
        if (current.getStatus() == BookingStatus.CANCELLED
                || current.getStatus() == BookingStatus.ATTENDED) {
            System.out.println("ERROR: Booking " + bookingId + " has status "
                    + current.getStatus() + " and cannot be changed.");
            return;
        }
        System.out.println("Current booking: " + current);

        Lesson newLesson = selectLessonFromTimetable();
        if (newLesson == null) return;

        try {
            manager.changeBooking(bookingId, newLesson);
            System.out.println("\nSUCCESS: Booking " + bookingId
                    + " changed to lesson " + newLesson.getLessonId()
                    + " (" + newLesson.getExerciseType().getDisplayName() + ").");
        } catch (BookingException e) {
            System.out.println("\nERROR: " + e.getMessage());
        }
    }

    // -------------------------------------------------------------------------
    // Functionality 3 - Cancel a booking
    // -------------------------------------------------------------------------

    private void cancelBooking() {
        System.out.println("\n=== Cancel a Booking ===");

        String bookingId = readString("Enter Booking ID to cancel: ").toUpperCase();
        Booking booking = manager.findBookingById(bookingId);
        if (booking == null) {
            System.out.println("ERROR: Booking " + bookingId + " not found.");
            return;
        }
        System.out.println("Booking to cancel: " + booking);
        String confirm = readString("Are you sure you want to cancel? (yes/no): ");
        if (!confirm.equalsIgnoreCase("yes")) {
            System.out.println("Cancellation aborted.");
            return;
        }
        try {
            manager.cancelBooking(bookingId);
            System.out.println("\nSUCCESS: Booking " + bookingId + " has been cancelled.");
        } catch (BookingException e) {
            System.out.println("\nERROR: " + e.getMessage());
        }
    }

    // -------------------------------------------------------------------------
    // Functionality 4 - Attend a lesson
    // -------------------------------------------------------------------------

    private void attendLesson() {
        System.out.println("\n=== Attend a Lesson ===");

        String bookingId = readString("Enter Booking ID to attend: ").toUpperCase();
        Booking booking = manager.findBookingById(bookingId);
        if (booking == null) {
            System.out.println("ERROR: Booking " + bookingId + " not found.");
            return;
        }
        if (booking.getStatus() == BookingStatus.CANCELLED
                || booking.getStatus() == BookingStatus.ATTENDED) {
            System.out.println("ERROR: Booking " + bookingId + " has status "
                    + booking.getStatus() + " and cannot be attended.");
            return;
        }
        System.out.println("Attending: " + booking);
        System.out.println("\nPlease leave a review for the lesson.");

        String reviewText = "";
        while (reviewText.isEmpty()) {
            System.out.print("Review (a few words or sentences): ");
            reviewText = scanner.nextLine().trim();
            if (reviewText.isEmpty()) System.out.println("Review cannot be empty.");
        }

        int rating = 0;
        while (rating < 1 || rating > 5) {
            System.out.println("Rating scale: 1=Very Dissatisfied  2=Dissatisfied  3=Ok  4=Satisfied  5=Very Satisfied");
            rating = readInt("Enter rating (1-5): ");
            if (rating < 1 || rating > 5) System.out.println("Please enter a number between 1 and 5.");
        }

        try {
            manager.attendLesson(bookingId, reviewText, rating);
            System.out.println("\nSUCCESS: Attendance recorded. Thank you for your review!");
        } catch (BookingException e) {
            System.out.println("\nERROR: " + e.getMessage());
        }
    }

    // -------------------------------------------------------------------------
    // Functionality 5 - Monthly lesson report
    // -------------------------------------------------------------------------

    private void monthlyLessonReport() {
        System.out.println("\n=== Monthly Lesson Report ===");
        int month = readInt("Enter month number (5=May, 6=June): ");
        System.out.println();
        System.out.println(reportGenerator.monthlyLessonReport(month, manager));
    }

    // -------------------------------------------------------------------------
    // Functionality 6 - Monthly champion report
    // -------------------------------------------------------------------------

    private void championReport() {
        System.out.println("\n=== Monthly Champion Exercise Type Report ===");
        int month = readInt("Enter month number (5=May, 6=June): ");
        System.out.println();
        System.out.println(reportGenerator.championExerciseReport(month, manager));
    }

    // -------------------------------------------------------------------------
    // Shared helpers
    // -------------------------------------------------------------------------

    private Member selectMember() {
        System.out.println("\n-- Registered Members --");
        for (Member m : manager.getMembers()) {
            System.out.println("  " + m);
        }
        String id = readString("Enter Member ID: ").toUpperCase();
        Member member = manager.findMemberById(id);
        if (member == null) {
            System.out.println("ERROR: Member " + id + " not found.");
        }
        return member;
    }

    private Lesson selectLessonFromTimetable() {
        System.out.println("\n-- View Timetable --");
        System.out.println("  1. By day (Saturday or Sunday)");
        System.out.println("  2. By exercise type");
        int choice = readInt("Select view: ");

        List<Lesson> lessons;
        if (choice == 1) {
            System.out.println("  1. Saturday");
            System.out.println("  2. Sunday");
            int dayChoice = readInt("Select day: ");
            if (dayChoice != 1 && dayChoice != 2) {
                System.out.println("Invalid day selection.");
                return null;
            }
            Day day = dayChoice == 1 ? Day.SATURDAY : Day.SUNDAY;
            lessons = manager.getTimetable().getLessonsByDay(day);
            System.out.println("\nLessons on " + day.getDisplayName() + ":");
        } else if (choice == 2) {
            ExerciseType[] types = ExerciseType.values();
            System.out.println("Exercise types:");
            for (int i = 0; i < types.length; i++) {
                System.out.printf("  %d. %s%n", i + 1, types[i].getDisplayName());
            }
            int typeChoice = readInt("Select exercise type: ");
            if (typeChoice < 1 || typeChoice > types.length) {
                System.out.println("Invalid exercise type selection.");
                return null;
            }
            ExerciseType type = types[typeChoice - 1];
            lessons = manager.getTimetable().getLessonsByExerciseType(type);
            System.out.println("\nLessons for " + type.getDisplayName() + ":");
        } else {
            System.out.println("Invalid timetable view selection.");
            return null;
        }

        System.out.println(manager.getTimetable().formatLessonList(lessons));

        String lessonId = readString("\nEnter Lesson ID (or 0 to go back): ").toUpperCase();
        if (lessonId.equals("0")) return null;

        Lesson lesson = manager.getTimetable().getLessonById(lessonId);
        if (lesson == null) {
            System.out.println("ERROR: Lesson " + lessonId + " not found.");
        }
        return lesson;
    }

    private String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
}
