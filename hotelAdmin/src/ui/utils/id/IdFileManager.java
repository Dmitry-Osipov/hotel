package ui.utils.id;

import ui.utils.csv.FileAdditionResult;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public final class IdFileManager {
    private IdFileManager() {
    }

    public static int readMaxId(String fileName) {
        int defaultId = 1;
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line = reader.readLine();
            if (line != null && !line.isEmpty()) {
                return Integer.parseInt(line);
            }
        } catch (IOException e) {
            return defaultId;
        }

        return defaultId;
    }

    public static void writeMaxId(String fileName, int id) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(String.valueOf(id));
        }
    }
}
