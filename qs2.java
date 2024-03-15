import java.io.*;
import java.util.*;

class MarksManagementSystem {
    private static final String CSV_FILE = "student_marks.csv";
    private static final String[] HEADERS = {"Roll Number", "Name", "Subject 1", "Subject 2", "Subject 3"};

    // Method to load student marks from CSV
    private static List<List<String>> loadStudentMarks() {
        List<List<String>> studentMarks = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                List<String> row = Arrays.asList(line.split(","));
                studentMarks.add(row);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return studentMarks;
    }

    // Method to save student marks to CSV
    private static void saveStudentMarks(List<List<String>> studentMarks) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CSV_FILE))) {
            for (List<String> row : studentMarks) {
                bw.write(String.join(",", row));
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to update student marks by teacher
    public static void updateMarks(String rollNumber, int subjectIndex, String newMark) {
        List<List<String>> studentMarks = loadStudentMarks();
        for (List<String> row : studentMarks) {
            if (row.get(0).equals(rollNumber)) {
                row.set(subjectIndex, newMark);
                break;
            }
        }
        saveStudentMarks(studentMarks);
    }

    // Method to sort student marks by total marks
    public static void sortStudentMarks() {
        List<List<String>> studentMarks = loadStudentMarks();
        studentMarks.sort((a, b) -> {
            int totalA = Integer.parseInt(a.get(2)) + Integer.parseInt(a.get(3)) + Integer.parseInt(a.get(4));
            int totalB = Integer.parseInt(b.get(2)) + Integer.parseInt(b.get(3)) + Integer.parseInt(b.get(4));
            return Integer.compare(totalB, totalA);
        });
        saveStudentMarks(studentMarks);
    }

    public static void main(String[] args) {
        // Initialize CSV file if not exists
        File file = new File(CSV_FILE);
        if (!file.exists()) {
            try {
                FileWriter fw = new FileWriter(CSV_FILE);
                fw.write(String.join(",", HEADERS));
                fw.write("\n");
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        updateMarks("001", 2, "95"); // Teacher updating subject 1 marks for student with roll number 001
        sortStudentMarks(); // Sorting student marks by total marks
    }
}