package at.hsol.ttool;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

public class AssignmentReader {
    private String[] dirs;

    public AssignmentReader(String... dirs) throws FileNotFoundException {
        this.dirs = dirs;
        for (String d : this.dirs) {
            File f = Paths.get(d).toFile();
            System.out.println();
            if (!f.exists() || !f.isDirectory()) {
                throw new FileNotFoundException();
            }
        }
    }

    List<LinkedList<File>> dirsToLists() {
        List<LinkedList<File>> lists = new LinkedList<>();
        for (String d : this.dirs) {
            LinkedList<File> l = new LinkedList<>();
            File file = Paths.get(d).toFile();
            File[] files = file.listFiles();
            for (File f : files) {
            //    if (f.isDirectory()) {    //Deleted because the files in the directories are zip files
                    l.add(f);
                }
            //}
            if (l.size() > 0) {
                lists.add(l);
            }
        }
        return lists;
    }
}
