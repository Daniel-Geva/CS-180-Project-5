package main;

import java.io.*;
import java.util.ArrayList;
/**
 * Class that handles all reading and writing to files directly
 * <p>
 * Contains only static methods for reading and writing.
 * <p>
 *
 *
 * @author Daniel Geva
 * @version 11/14/21
 */
public class FileWrapper {

    public static ArrayList<String> readFile(String path) {
        ArrayList<String> contents = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.isEmpty() && !line.isBlank()) {
                    contents.add(line);
                }
            }
        } catch (FileNotFoundException | NullPointerException e) {
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return contents;
    } //reads a given file and returns an arraylist of strings, where one element corresponds to one line

    public static boolean writeFile(String path, ArrayList<String> contents) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
            for (String line : contents) {
                bw.write(line);
                bw.newLine();
            }
        } catch (FileNotFoundException | NullPointerException e) {
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    } //writes to a given file the contents of a given arraylist of strings where one element gets written to a line

    public static ArrayList<String> readImportFile(String path) {
        ArrayList<String> contents = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            String readLine = "";
            while ((line = br.readLine()) != null) {
                if (line.startsWith("/") && !readLine.isBlank()) {
                    //if the line starts with a "/" (forward slash) and the buffer (readLine) is not empty,
                    // the buffer is added to the arraylist and reset to the line
                    contents.add(readLine);
                    readLine = line.substring(line.indexOf("/") + 1) + "\n";
                } else if (!line.startsWith("#")) {
                    //ignores any lines starting with "#" (pound), adds a read line to the buffer
                    if (line.startsWith("/")) {
                        readLine += line.substring(line.indexOf("/") + 1) + "\n";
                    } else {
                        readLine += line + "\n";
                    }
                }
            }
            if (!readLine.isBlank()) {
                contents.add(readLine);
            }
        } catch (FileNotFoundException | NullPointerException e) {
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return contents;
    } //A version of readFile that is specific for importing quizzes in a file

}
