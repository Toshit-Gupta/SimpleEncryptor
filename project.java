import java.util.Scanner;       // Used to take input from the user via console
import java.util.List;          // Used to store lines read from the file as a list of strings
import java.io.FileWriter;      // Used to write (or overwrite/append) text data to a file
import java.io.File;            // Used to create a File object and access its path
import java.nio.file.Files;     // Used to read all lines from a file easily (modern I/O)
import java.io.IOException;     // Required to handle exceptions that may occur during file operations

public class project
{
    // File to store encrypted data
    private static final String DATA_FILE = "encrypted_data.txt";

    // XOR encryption/decryption (same method for both)
    private static String xorProcess(String input, String key) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char c = (char) (input.charAt(i) ^ key.charAt(i % key.length()));
            result.append(c);
        }
        return result.toString();
    }

    // Encrypt text and save to file
    public static void encrypt(String text, String key) throws IOException {
        String encrypted = xorProcess(text, key);
        FileWriter writer = new FileWriter(DATA_FILE, true); // true = append mode
        writer.write(key.hashCode() + ":" + encrypted + "\n");
        writer.close();
        System.out.println("Encrypted and saved!");
    }

    // Decrypt text from file - shows all messages with matching key
   public static void decrypt(String key) throws IOException {
    List<String> lines = Files.readAllLines(new File(DATA_FILE).toPath());
    String targetKey = String.valueOf(key.hashCode());
    boolean found = false;

    System.out.println("Decrypted messages with this key:");
    for (String line : lines) {
        if (line.startsWith(targetKey + ":")) {
            String encrypted = line.split(":")[1];
            System.out.println("- " + xorProcess(encrypted, key));
            found = true;
        }
    }
    
    if (!found) {
        System.out.println("No data found for this key!");
      }
  }

    // Clear all data (reset file)
    public static void clearData() throws IOException {
        new FileWriter(DATA_FILE, false).close(); // false = overwrite mode
        System.out.println("All data erased!");
    }

    // Main menu
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.println("=== Simple Encryptor ===");
        System.out.println("Data is saved in: " + DATA_FILE);

        while (true) {
            System.out.println("\n1. Encrypt\n2. Decrypt\n3. Clear All Data\n4. Exit");
            System.out.print("Choose an option (1-4): ");
            
            int choice = sc.nextInt();
            sc.nextLine(); // Clear the newline character

            switch (choice) {
                case 1:
                    System.out.print("Enter text to encrypt: ");
                    String text = sc.nextLine();
                    System.out.print("Enter encryption key: ");
                    encrypt(text, sc.nextLine());
                    break;
                case 2:
                    System.out.print("Enter decryption key: ");
                    decrypt(sc.nextLine());
                    break;
                case 3:
                    System.out.print("Are you sure? (y/n): ");
                    if (sc.nextLine().equalsIgnoreCase("y")) {
                        clearData();
                    } else {
                        System.out.println("Cancelled.");
                    }
                    break;
                case 4:
                    System.out.println("Goodbye!");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice! Please enter 1-4.");
            }
        }
    }
}
