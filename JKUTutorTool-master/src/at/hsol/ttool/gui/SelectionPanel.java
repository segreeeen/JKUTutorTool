package at.hsol.ttool.gui;

import at.hsol.ttool.TutorTool;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Objects;

public class SelectionPanel extends JPanel {

    private String studentPath;
    private String targetPath;
    private ArrayList<String> sourceString = new ArrayList<>();

    private JTextField stPath;
    private JTextField tPath;
    private JTextField soPath;

    private boolean hasStudents = false;
    private boolean hasSource = false;
    private boolean hasTarget = false;

    private File sourceFolder;

    protected SelectionPanel() {
        JPanel selection = new JPanel(new FlowLayout());
        JPanel student = new JPanel(new GridLayout(2, 1));
        JPanel target = new JPanel(new GridLayout(2, 1));
        JPanel source = new JPanel(new GridLayout(2, 1));

        JButton selectStudentList = new JButton("Select Student File");
        JButton sourceFiles = new JButton("From");
        JButton selectTarget = new JButton("Extract To");

        stPath = new JTextField("StudentPath: ");
        stPath.setColumns(20);
        stPath.setEditable(false);
        soPath = new JTextField("SourcePath: ");
        soPath.setColumns(20);
        soPath.setEditable(false);
        tPath = new JTextField("Target: ");
        tPath.setColumns(20);
        tPath.setEditable(false);

        selectStudentList.addActionListener(new SelectionListener(SelectionMode.STUDENTS));
        sourceFiles.addActionListener(new SelectionListener(SelectionMode.SOURCE));
        selectTarget.addActionListener(new SelectionListener(SelectionMode.TARGET));

        student.add(selectStudentList);
        student.add(stPath);

        source.add(sourceFiles);
        source.add(soPath);

        target.add(selectTarget);
        target.add(tPath);

        selection.add(student);
        selection.add(source);
        selection.add(target);

        JButton run = new JButton("Run");
        run.addActionListener(e -> {
            if (hasStudents && hasSource && hasTarget) {
                String[] files = new String[sourceString.size()];
                TutorTool tool = new TutorTool(studentPath, targetPath, sourceString.toArray(files));
                tool.deleteUnneededDirs();
                tool.extractToTargetDir();
                sourceFolder.delete();
            } else {
                JOptionPane.showMessageDialog(this, "Missing Component(s): \n" + (hasStudents ? "" : "Students \n") +
                        (hasSource ? "" : "Source \n") + (hasTarget ? "" : "Target"));
            }
        });
        add(selection);
        add(run);
    }

    private class SelectionListener implements ActionListener {

        private SelectionMode mode;

        private SelectionListener(SelectionMode mode) {
            this.mode = mode;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser(System.getProperty("user.home") + "\\Desktop");
            int returnValue;
            switch (mode) {
                case STUDENTS:
                    fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                    returnValue = fileChooser.showOpenDialog(null);
                    if (returnValue == JFileChooser.APPROVE_OPTION) {
                        File opened = fileChooser.getSelectedFile();
                        studentPath = opened.getPath();
                        stPath.setText("StudentPath: " + opened.getName());
                        hasStudents = true;
                    }
                    break;
                case SOURCE:
                    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    fileChooser.setMultiSelectionEnabled(true);
                    returnValue = fileChooser.showOpenDialog(null);
                    if (returnValue == JFileChooser.APPROVE_OPTION) {
                        File opened = fileChooser.getSelectedFile();
                        if (opened.isDirectory()) {
                            sourceFolder = opened;
                            File[] fileList = opened.listFiles();
                            if (fileList != null) {
                                for (File f : fileList) {
                                    sourceString.add(f.getPath());
                                }
                            }
                        }
                        soPath.setText("SourcePath: " + opened.getName());
                        hasSource = true;
                    }
                    break;
                case TARGET:
                    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    returnValue = fileChooser.showOpenDialog(null);
                    if (returnValue == JFileChooser.APPROVE_OPTION) {
                        File opened = fileChooser.getSelectedFile();
                        targetPath = opened.getPath();
                        tPath.setText("Target: " + opened.getName());
                        hasTarget = true;
                    }
                    break;
                default:
                    System.out.println("Error: Not implemented");
            }
        }
    }

    private enum SelectionMode {
        STUDENTS, SOURCE, TARGET
    }
}
