package com.p92.io;

import com.p92.bom.ExpectedOutputFormat;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;

/**
 * A helper class for reading in and writing out files using a GUI
 * @author Balu_ADMIN
 */
public class FileIO extends JPanel {
    /**
     * Input file extension
     */
    public static final String INPUT_EXTENSION = ".txt";
    /**
     * Separator character of new input lines.
     */
    public static final String LINE_SEPARATOR = System.getProperty("line.separator", "\n"); //LF is expected only
    /**
     * The '\' or '/' that separates directories in a file name for instance
     */
    public static final String FILEPATH_SEPARATOR = System.getProperty("file.separator");
    /**
     * Name of this currently running project
     */
    private static final String PROJECT_ROOT_NAME = "GoogleHashCode2019";
    /**
     * Folder name where we have the input data
     */
    private static final String RESOURCES_FOLDER = "resources"; //the current folder where the executable jar file is running from in NetBeans IDE and where the resources are located at
    /**
     * Folder name where we store our solution as an output data
     */
    private static final String OUTPUT_FOLDER = "resources"; //the current folder where the executable jar file is running from in NetBeans IDE and where the resources are located at
    /**
     * Prefix of the output file
     */
    private static final String OUTPUT_FILENAME = "";
    /**
     * Output file extension
     */
    private static final String OUTPUT_EXTENSION = ".out";
    
    private static final Logger LOGGER = Logger.getLogger(FileIO.class.getName());

    private String usedInputFileName; //we store the name of the read in input file in this
    
    /**
      * Helper method to read up an input file.
      * 
      * @param pathToFile
      * @return
    */
    public File getFile(String pathToFile) {
        File file = new File(pathToFile);
        if(!file.isFile()) {
            displayError(pathToFile + " is not a file.");
        }
        if(!file.getName().endsWith(INPUT_EXTENSION)) {
            displayError(pathToFile + " does not have the expected extension: " + INPUT_EXTENSION);
        }
        return file;
    }
    
    /**
      * Helper method to get back the string content of a file.
      * 
      * @param file
      * @return
    */
    public String getFileContent(File file) {
        if(file == null || !file.isFile() || !file.canRead()) {
            displayError("Cant read as file: " + file);
            System.exit(-1);
        }
        BufferedReader reader = null;
        StringBuilder sb = new StringBuilder();
        try {
            reader = new BufferedReader(new FileReader(file));
            String line = null;
            while((line = reader.readLine()) != null) {
                sb.append(line).append(LINE_SEPARATOR);
            }
        } 
        catch (FileNotFoundException e) {
            displayError("File not found: " + file);
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            System.exit(-1);
        }
        catch (IOException e) {
            displayError("IOException while reading file: " + file);
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            System.exit(-1);
        }
        finally {
            if(reader != null) {
                try {
                    reader.close();
                } 
                catch (IOException e) {
                    displayError("IOException while closing file: " + file);
                    LOGGER.log(Level.SEVERE, e.getMessage(), e);
                }
            }
        }
        return sb.toString();
    }
    
    /**
      * Opens a dialog and returns the selected file's string content.
      * 
      * @return
    */
    public String loadUsingGUI() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.addChoosableFileFilter(new FileFilter() {
            @Override
            public String getDescription() {
                return "Accepting only files with extension: " + FileIO.INPUT_EXTENSION;
            }
            @Override
            public boolean accept(File f) {
                if(f.isDirectory()) {
                    return true;
                }
                if(f.canRead() && f.getName().endsWith(FileIO.INPUT_EXTENSION)) {
                    return true;
                }
                return false;
            }
        });
        fileChooser.setAcceptAllFileFilterUsed(false);
        try {
            URL inputDirectoryURL = FileIO.class.getResource("."); // <-- works only in IDE but FileIO.class.getResource(".") will be null if it is executed by a .jar file. This should be used for exact file reading within a jar: InputStream in = getClass().getResourceAsStream("/file.txt"); BufferedReader reader = new BufferedReader(new InputStreamReader(in));
//            displayInfo("Current directory where program is runnign from: " + inputDirectoryURL);
            if(inputDirectoryURL != null) {
                File inputDirectory = new File(inputDirectoryURL.toURI());
                String currentDirectoryName = inputDirectory.getName();
                int counter = 10; //we go up maximum of 10 directory parents to find the root project folder
                while(0 < counter && !PROJECT_ROOT_NAME.equals(currentDirectoryName)) {
                    if(inputDirectory.getParentFile() == null) {
                        counter = -1; //to indicate that we did not found the root folder. The last folder should be the root drive, e.g.: C:
                        break;
                    }
                    inputDirectory = inputDirectory.getParentFile();
                    currentDirectoryName = inputDirectory.getName();
                    counter--;
                }
                File resourcesDirectory = new File(inputDirectory.getAbsolutePath() + File.separator + RESOURCES_FOLDER);
    //            displayInfo("Resources directory: " + resourcesDirectory);
                if(resourcesDirectory.isDirectory()) {
                    fileChooser.setCurrentDirectory(resourcesDirectory);
                }
            }
        } 
        catch (URISyntaxException e) {
            displayError("URISyntaxException occured.");
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
        int returnVal = fileChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            if(selectedFile != null) {
                usedInputFileName = selectedFile.getName();
                int startOfExtension = usedInputFileName.lastIndexOf(".");
                usedInputFileName = usedInputFileName.substring(0, (startOfExtension == -1) ? usedInputFileName.length() : startOfExtension); //remove extension
            }
            return getFileContent(selectedFile);
        }
        else if(returnVal == JFileChooser.CANCEL_OPTION) {
            System.exit(0);
        }
        return null;
    }
    
    /**
      * Helper method to write out a string into the given file.
      * 
      * @param soultionFile
      * @param fileContent
    */
    public void writeSolution(File soultionFile, String fileContent) {
        if(soultionFile == null) {
            return;
        }
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(soultionFile));
            writer.write(fileContent);
            writer.flush();
        } 
        catch (IOException e) {
            displayError("IOException while writing file: " + soultionFile);
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            System.exit(-1);
        }
        finally {
            if(writer != null) {
                try {
                    writer.close();
                } 
                catch (IOException e) {
                    displayError("IOException while closing file: " + soultionFile);
                    LOGGER.log(Level.SEVERE, e.getMessage(), e);
                }
            }
        }
    }
    
    /**
      * Helper method to show a save dialog with a proposed directory and file name and execute saving the file.
      * 
      * @param result
      * @return
    */
    public File saveSolution(List<? extends ExpectedOutputFormat> result) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        try {
            URL inputDirectoryURL = FileIO.class.getResource(".");
//            displayInfo("Current directory where program is runnign from: " + inputDirectoryURL);
            if(inputDirectoryURL != null) {
                File inputDirectory = new File(inputDirectoryURL.toURI());
                String currentDirectoryName = inputDirectory.getName();
                int counter = 10; //we go up maximum of 10 directory parents to find the root project folder
                while(0 < counter && !PROJECT_ROOT_NAME.equals(currentDirectoryName)) {
                    if(inputDirectory.getParentFile() == null) {
                        counter = -1; //to indicate that we did not found the root folder. The last folder should be the root drive, e.g.: C:
                        break;
                    }
                    inputDirectory = inputDirectory.getParentFile();
                    currentDirectoryName = inputDirectory.getName();
                    counter--;
                }
                File outputDirectory = new File(inputDirectory.getAbsolutePath() + File.separator + OUTPUT_FOLDER);
    //            displayInfo("Output directory: " + outputDirectory);
                if(outputDirectory.isDirectory()) {
                        fileChooser.setCurrentDirectory(outputDirectory);
                }
            }
        } 
        catch(URISyntaxException e) {
            displayError("URISyntaxException occured.");
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm");
        String newOutPutFileName = 
                ((OUTPUT_FILENAME == null || OUTPUT_FILENAME.trim().equals("")) ? "" : (OUTPUT_FILENAME + "_")) + 
                ((usedInputFileName == null) ? "" : (usedInputFileName + "_")) + 
                dateFormat.format(new Date()) + 
                OUTPUT_EXTENSION
        ;
        fileChooser.setSelectedFile(new File(newOutPutFileName));
        int returnVal = fileChooser.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File solutionFile = fileChooser.getSelectedFile();
            StringBuilder sb = new StringBuilder();
            result.forEach((solution) -> {
                sb.append(solution.toExpectedOutputFormat()).append(FileIO.LINE_SEPARATOR);
            });
            writeSolution(solutionFile, sb.toString());
        }
        return null;
    }
    
    /**
    * Helper method to display the error in a dialog.
    * 
    * @param error
    */
    public void displayError(String error) {
        JOptionPane.showMessageDialog(this, error, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
    * Helper method to display the warning in a dialog.
    * 
    * @param warning
    */
    public void displayWarning(String warning) {
        JOptionPane.showMessageDialog(this, warning, "Warning", JOptionPane.WARNING_MESSAGE);
    }

    /**
    * Helper method to display the info in a dialog.
    * 
    * @param info
    */
    public void displayInfo(String info) {
        JOptionPane.showMessageDialog(this, info, "Info", JOptionPane.INFORMATION_MESSAGE);
    }
}
