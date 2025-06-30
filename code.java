import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

class Habit {
    private String name;
    private int streak;
    private String lastCompletedDate;

    public Habit(String name) {
        this.name = name;
        this.streak = 0;
        this.lastCompletedDate = "";
    }

    public String getName() { return name; }
    public int getStreak() { return streak; }
    public String getLastCompletedDate() { return lastCompletedDate; }

    public void completeHabit() {
        String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        if (!today.equals(lastCompletedDate)) {
            streak++;
            lastCompletedDate = today;
        }
    }
}

class HabitTracker {
    private ArrayList<Habit> habits;

    public HabitTracker() {
        habits = new ArrayList<>();
        loadHabits();
    }

    public void addHabit(String name) {
        habits.add(new Habit(name));
        saveHabits();
    }

    public void completeHabit(int index) {
        if (index >= 0 && index < habits.size()) {
            habits.get(index).completeHabit();
            saveHabits();
        }
    }

    public void displayHabits() {
        if (habits.isEmpty()) {
            System.out.println("No habits added yet.");
            return;
        }
        for (int i = 0; i < habits.size(); i++) {
            Habit h = habits.get(i);
            System.out.printf("%d. %s (Streak: %d days, Last: %s)\n",
                i + 1, h.getName(), h.getStreak(), 
                h.getLastCompletedDate().isEmpty() ? "Never" : h.getLastCompletedDate());
        }
    }

    private void saveHabits() {
        try (PrintWriter out = new PrintWriter("habits.txt")) {
            for (Habit h : habits) {
                out.println(h.getName() + "," + h.getStreak() + "," + h.getLastCompletedDate());
            }
        } catch (IOException e) {
            System.out.println("Error saving habits.");
        }
    }

    private void loadHabits() {
        File file = new File("habits.txt");
        if (!file.exists()) return;
        try (Scanner sc = new Scanner(file)) {
            while (sc.hasNextLine()) {
                String[] parts = sc.nextLine().split(",");
                Habit h = new Habit(parts[0]);
                h.completeHabit(); // Reset streak for existing habits
                for (int i = 1; i < Integer.parseInt(parts[1]); i++) {
                    h.completeHabit();
                }
                habits.add(h);
            }
        } catch (IOException e) {
            System.out.println("Error loading habits.");
        }
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        HabitTracker tracker = new HabitTracker();
        
        while (true) {
            System.out.println("\n=== Habit Tracker ===");
            System.out.println("1. Add Habit");
            System.out.println("2. Complete Habit");
            System.out.println("3. View Habits");
            System.out.println("4. Exit");
            System.out.print("Choose option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter habit name: ");
                    String name = scanner.nextLine();
                    tracker.addHabit(name);
                    break;
                case 2:
                    System.out.println("\nSelect habit to complete:");
                    tracker.displayHabits();
                    System.out.print("Enter habit number: ");
                    int habitNumber = scanner.nextInt() - 1;
                    tracker.completeHabit(habitNumber);
                    break;
                case 3:
                    tracker.displayHabits();
                    break;
                case 4:
                    scanner.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}
