import java.util.*;
import java.io.*;

// Represents a Task
class Task {
    private String id;
    private String description;
    private boolean completed;

    public Task(String id, String description) {
        this.id = id;
        this.description = description;
        this.completed = false;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void markAsCompleted() {
        this.completed = true;
    }

    @Override
    public String toString() {
        return "[" + (completed ? "X" : " ") + "] " + description;
    }
}

// Represents the Task Management System
class TaskManagementSystem {
    private List<Task> tasks;
    private String taskHistoryFile = "task_history.txt";

    public TaskManagementSystem() {
        this.tasks = new ArrayList<>();
    }

    public void addTask(Task task) {
        tasks.add(task);
        logTaskAction("Task added: " + task.getDescription());
    }

    public void editTask(String taskId, String newDescription) {
        for (Task task : tasks) {
            if (task.getId().equals(taskId)) {
                task.setDescription(newDescription);
                logTaskAction("Task edited (ID: " + taskId + "): " + newDescription);
                return;
            }
        }
        System.out.println("Task not found.");
    }

    public void completeTask(String taskId) {
        for (Task task : tasks) {
            if (task.getId().equals(taskId)) {
                task.markAsCompleted();
                logTaskAction("Task completed (ID: " + taskId + ")");
                return;
            }
        }
        System.out.println("Task not found.");
    }

    public void displayTasks() {
        System.out.println("Tasks:");
        for (Task task : tasks) {
            System.out.println(task);
        }
    }

    private void logTaskAction(String action) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(taskHistoryFile, true))) {
            String timestamp = new Date().toString();
            writer.println("Timestamp: " + timestamp);
            writer.println("Action: " + action);
            writer.println();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class TaskManagementCLI {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        TaskManagementSystem taskManagementSystem = new TaskManagementSystem();

        while (true) {
            System.out.println("\nTask Management CLI Tool:");
            System.out.println("1. Add Task");
            System.out.println("2. Edit Task");
            System.out.println("3. Complete Task");
            System.out.println("4. Display Tasks");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter task description: ");
                    String description = scanner.nextLine();
                    Task taskToAdd = new Task(UUID.randomUUID().toString(), description);
                    taskManagementSystem.addTask(taskToAdd);
                    break;
                case 2:
                    System.out.print("Enter task ID to edit: ");
                    String taskIdToEdit = scanner.nextLine();
                    System.out.print("Enter new task description: ");
                    String newDescription = scanner.nextLine();
                    taskManagementSystem.editTask(taskIdToEdit, newDescription);
                    break;
                case 3:
                    System.out.print("Enter task ID to mark as completed: ");
                    String taskIdToComplete = scanner.nextLine();
                    taskManagementSystem.completeTask(taskIdToComplete);
                    break;
                case 4:
                    taskManagementSystem.displayTasks();
                    break;
                case 5:
                    System.out.println("Exiting Task Management CLI Tool...");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}

