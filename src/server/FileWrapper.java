package server;

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

    private static Object readLock = new Object();
    private static Object writeLock = new Object();

    /**
     * reads a given file and returns an arraylist of strings, where one element corresponds to one line
     *
     * @param path - the filepath for the file to be read from
     *
     * @return contents - an ArrayList of Strings each containing a line from the read file
     */
    public static ArrayList<String> readFile(String path) {
        ArrayList<String> contents = new ArrayList<>();
        synchronized (readLock) {
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
        }
        return contents;
    }

    /**
     * writes to a given file the contents of a given arraylist of strings where one element gets written to a line
     *
     * @param path - the path for the file that the file that will be written to
     * @param contents - ArrayList of Strings to be written to the file
     *
     * @return success - whether the writing to a file succeeded
     */
    public static boolean writeFile(String path, ArrayList<String> contents) {
        synchronized (writeLock) {
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
        }
        return true;
    }

}
