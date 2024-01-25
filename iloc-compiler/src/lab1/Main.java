package src.lab1;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.HashMap;
import java.io.File;

public class Main {

    // Flags to determine the type of operation to perform
    private static boolean hasError = false;
    
    public static void main(String[] args) throws IOException {
        HashMap<String, String> opts = new HashMap<>();

        try {
            for (int i = 0; i < args.length; i++) {
                if (args[i].startsWith("-")) {
                    opts.put(args[i], args[i+1]);
                    i++; // Skip next arg
                }
            }

            if (isNumeric(args[0])) {
                int k = Integer.parseInt(args[0]);
                if (k > 64 || k < 3) {
                    System.err.println("ERROR: k must be an integer between 3 and 64.");
                } else {
                    //allocate(args[1], k);
                }
            } else {
                if (opts.containsKey("-x")) {
                    rename(opts.get("-x"));
                } else if (opts.containsKey("-h")) {
                    printHelp();
                    System.exit(0);
                } else {
                    printHelp();
                    System.exit(1);
                }
            }
        } catch (Exception e) {
            printHelp();
            System.exit(1);
        }
    }

    /**
    private static void allocate(String filename, int k) {
        try {
            // Read file content
            byte[] bytes = Files.readAllBytes(Paths.get(filename));
            String file = new String(bytes, Charset.defaultCharset());
            // Run scanner
            Scanner scanner = new Scanner(file);
            List<Token> tokens = scanner.addTokens();
            // Run parser
            Parser parser = new Parser(tokens);
            List<Operations> ops = parser.parse();
            // Run allocator
            if (parser.hasError()) {
                System.out.println("Parse Unsuccessful");
            } else {
                    Renamer renamer = new Renamer(parser);
                    renamer.renameSR2LiveRange();
                    AllocatorWithSpill allocator = new AllocatorWithSpill(renamer, k);
                    allocator.allocateWithSpill();
                    allocator.printAllocatedBlock();
            }

        } catch (IOException e) {
            // Use the report function to display the error
            reportError(-1, "File Read Error", "Error reading file: " + filename);
            System.exit(1);
        }
    }
    */


   /** Renamer. */
    private static void rename(String filename) throws IOException {

        try {
            // Read file content
            byte[] bytes = Files.readAllBytes(Paths.get(filename));
            String file = new String(bytes, Charset.defaultCharset());
            // Run scanner
            Scanner scanner = new Scanner(file);
            List<Token> tokens = scanner.addTokens();
            // Run parser
            Parser parser = new Parser(tokens);
            List<Operations> ops = parser.parse();
            // Run renamer
            if (parser.hasError()) {
                reportError(-1, "File Parse Error", "Error parsing file: " + filename);
            } else {
                    Renamer renamer = new Renamer(parser);
                    //renamer.renameSR2LiveRange();
                    //renamer.printRenamedBlock();
            }

        } catch (IOException e) {
            // Use the report function to display the error
            reportError(-1, "File Read Error", "Error reading file: " + filename);
            System.exit(1);
        }
        
    }


    /**
     * Prints the help message which provides information about the
     * available command-line options and their functionality.
     */
    private static void printHelp() {
        System.out.println("COMP 412 Lab 2: Local Register Allocation");
        System.out.println("Command Syntax:");
        System.out.println("      412alloc k filename [-h] [-x]");
        System.out.println("\nRequired arguments:");
        System.out.println("       k        specifies the number of register available");
        System.out.println("       filename the pathname (absolute or relative) to the input file");
        System.out.println("\nOptional flags:");
        System.out.println("       -h        prints this message");
        System.out.println("       -x        performs register renaming");
    }


    private static boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    /**
     * Calls reportError to report an error message at the specified line number.
     * 
     * @param line The line number where the error occurred.
     * @param msg  The error message to be displayed.
     */
    static void error(int line, String msg) {
        reportError(line, "", msg);
    }

    /**
     * Reports an error message in context to a specific token.
     * 
     * @param token The token where the error occurred.
     * @param msg   The error message to be displayed.
     */
    static void error(Token token, String msg) {
        if (token.category == Category.EOF) {
            reportError(token.line, " at end", msg);
        } else {
            reportError(token.line, " at '" + token.lexeme + "'", msg);
        }
    }

    /**
     * Formats and prints the error message with the line number and context.
     * 
     * @param line         The line number where the error occurred.
     * @param errorContext The context of the error.
     * @param msg          The error message to be displayed.
     */
    private static void reportError(int line, String errorContext, String msg) {
        // Split the message by newline to handle multi-line error messages
        String[] msgLines = msg.split("\n");

        // Print the first line with "ERROR", line number, and colon
        System.err.println("ERROR " + line + ": " + msgLines[0] + " " + errorContext);

        // Print any additional lines without the prefix
        for (int i = 1; i < msgLines.length; i++) {
            System.err.println(msgLines[i]);
        }

        hasError = true;
    }
}