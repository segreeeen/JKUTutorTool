package at.hsol.ttool;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

public class TutorTool {
    private final String SEVEN_ZIP_PATH = "F:\\7-Zip\\7z.exe";      //TODO change to your path or create chooser in gui
    private MatNrReader nameListReader;
    private AssignmentReader aReader;
    private String targetDir;

    public TutorTool(String studentList, String targetDir, String... dirs) {
        this.nameListReader = new MatNrReader(studentList);
        this.targetDir = targetDir;
        try {
            this.aReader = new AssignmentReader(dirs);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    //Console application (maybe not working as I changed some bits while creating the GUI)
    public static void main(String[] args) {
        TutorTool tt = new TutorTool(args[0], args[1], Arrays.copyOfRange(args, 2, args.length));
        tt.deleteUnneededDirs();
        tt.extractToTargetDir();
    }

    public void deleteUnneededDirs() {
        int filesKeptCount = 0;
        int filesRemovedCount = 0;
        List<LinkedList<File>> dirList = aReader.dirsToLists();
        List<String> matList = nameListReader.getNamesList();
        for (String s : matList) {
            System.out.println("\"" + s + "\"");
        }
        for (LinkedList<File> l : dirList) {
            for (File f : l) {
                boolean delete = false;
                for (String s : matList) {
                    if (!f.toString().contains(s)) {
                        delete = true;
                    } else {
                        System.out.println(f.toString() + " contains " + s + ". Keeping it!");
                        delete = false;
                        break;
                    }
                }
                if (delete) {
                    deleteDirectory(f);
                    filesRemovedCount++;
                } else {
                    filesKeptCount++;
                }
            }
        }
        System.out.println("Files Kept: " + filesKeptCount + ",\nFiles Deleted: " + filesRemovedCount);
    }

    public void extractToTargetDir() {
        List<LinkedList<File>> files = aReader.dirsToLists();
        List<String> students = nameListReader.getNamesList();

        int aCnt = 1;
        for (LinkedList<File> l : files) {
            Path p = Paths.get(targetDir, "Assignment" + aCnt);
            p.toFile().mkdirs();
            for (File f : l) {
                for (String s : students) {
                    if (f.toString().contains(s)) {
                        // File[] assignment = f.listFiles();   //Files are already the desired zip files
                        // if (assignment != null) {
                        //     for (File a : assignment) {
                        if (f.toString().endsWith(".zip")) {
                            Path dest = Paths.get(p.toString(), f.getName().split("_")[0]);
                            dest.toFile().mkdirs();

                            Path extractP = Paths.get(dest.toString() + File.separator + f.getName());
                            ProcessBuilder pb = new ProcessBuilder(SEVEN_ZIP_PATH,
                                    "x", f.toString(), "-o" + extractP, "-r");
                            pb.redirectError();
                            try {
                                Process proc = pb.start();
                                new Thread(new InputConsumer(proc.getInputStream())).start();
                                System.out.println("Exited with: " + proc.waitFor());
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }

                            // Files.copy(a.toPath(), Paths.get(dest.toString() + File.separator +
                            // a.getName()),
                            // StandardCopyOption.REPLACE_EXISTING);

                            File projectFile = findFile(".project", dest.toFile());
                            if (projectFile != null) {
                                String html = "";
                                List<String> lines;
                                try {
                                    lines = Files.readAllLines(projectFile.toPath());

                                    for (String fu : lines) {
                                        html += fu;
                                    }

                                    Document doc = Jsoup.parse(html, "fu", Parser.xmlParser());
                                    doc.getElementsByTag("name").first().append(s);
                                    PrintWriter writer = new PrintWriter(projectFile, "UTF-8");
                                    writer.write(doc.toString());
                                    System.out.println(doc.toString());
                                    writer.flush();
                                    writer.close();

                                } catch (IOException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }

                            }

                        }
                        //   }
                        //}
                    }
                }
            }
            aCnt++;
        }
    }

    public class InputConsumer implements Runnable {
        private InputStream is;

        public InputConsumer(InputStream is) {
            this.is = is;
        }

        @Override
        public void run() {
            try {
                int value = -1;
                while ((value = is.read()) != -1) {
                    System.out.print((char) value);
                }
            } catch (IOException exp) {
                exp.printStackTrace();
            }
            System.out.println("");
        }

    }

    public File findFile(String name, File file) {
        File[] list = file.listFiles();
        if (list != null) {
            for (File fil : list) {
                if (fil.isDirectory()) {
                    return findFile(name, fil);
                } else if (name.equalsIgnoreCase(fil.getName())) {
                    return fil;
                }
            }
        }
        return null;
    }

    /**
     * Force deletion of directory
     *
     * @param path
     * @return
     */
    static public boolean deleteDirectory(File path) {
        if (path.exists()) {
            if (path.isDirectory()) {
                File[] files = path.listFiles();
                for (int i = 0; i < files.length; i++) {
                    if (files[i].isDirectory()) {
                        deleteDirectory(files[i]);
                    } else {
                        files[i].delete();
                    }
                }
            } else {
                path.delete();
            }
        }
        return (path.delete());
    }
}
