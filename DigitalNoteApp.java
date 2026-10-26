import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

class Note implements Serializable {
    private String title;
    private String content;

    public Note(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return title;
    }
}

public class DigitalNoteApp extends JFrame {

    private DefaultListModel<Note> model = new DefaultListModel<>();
    private JList<Note> noteList = new JList<>(model);
    private static final String FILE_NAME = "notes.dat";

    public DigitalNoteApp() {
        setTitle("Digital Note Organizer");
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        loadNotes();

        // Scroll pane
        add(new JScrollPane(noteList), BorderLayout.CENTER);

        // Buttons
        JPanel panel = new JPanel();

        JButton addBtn = new JButton("Add");
        JButton editBtn = new JButton("Edit");
        JButton deleteBtn = new JButton("Delete");
        JButton saveBtn = new JButton("Save");

        panel.add(addBtn);
        panel.add(editBtn);
        panel.add(deleteBtn);
        panel.add(saveBtn);

        add(panel, BorderLayout.SOUTH);

        // Button Actions
        addBtn.addActionListener(e -> addNote());
        editBtn.addActionListener(e -> editNote());
        deleteBtn.addActionListener(e -> deleteNote());
        saveBtn.addActionListener(e -> saveNotes());
    }

    private void addNote() {
        String title = JOptionPane.showInputDialog(this, "Enter Title:");
        if (title == null || title.isEmpty()) return;

        String content = JOptionPane.showInputDialog(this, "Enter Content:");
        if (content == null || content.isEmpty()) return;

        model.addElement(new Note(title, content));
    }

    private void editNote() {
        int index = noteList.getSelectedIndex();
        if (index == -1) {
            JOptionPane.showMessageDialog(this, "Select a note first!");
            return;
        }

        Note note = model.get(index);

        String newTitle = JOptionPane.showInputDialog(this, "Edit Title:", note.getTitle());
        String newContent = JOptionPane.showInputDialog(this, "Edit Content:", note.getContent());

        if (newTitle != null && newContent != null) {
            note.setTitle(newTitle);
            note.setContent(newContent);
            noteList.repaint();
        }
    }

    private void deleteNote() {
        int index = noteList.getSelectedIndex();
        if (index != -1) {
            model.remove(index);
        } else {
            JOptionPane.showMessageDialog(this, "Select a note first!");
        }
    }

    private void saveNotes() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            ArrayList<Note> list = new ArrayList<>();
            for (int i = 0; i < model.size(); i++) {
                list.add(model.get(i));
            }
            out.writeObject(list);
            JOptionPane.showMessageDialog(this, "Notes saved!");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving notes!");
        }
    }

    private void loadNotes() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            ArrayList<Note> list = (ArrayList<Note>) in.readObject();
            for (Note n : list) {
                model.addElement(n);
            }
        } catch (Exception e) {
            // First run - ignore
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DigitalNoteApp().setVisible(true));
    }
}