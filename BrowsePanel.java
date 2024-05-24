import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class BrowsePanel extends JPanel{
    private Browse browse;
    private JTextArea textArea;

    public BrowsePanel(Browse browse){
        this.browse = browse;
        setLayout(new BorderLayout());

        textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);

        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> refreshTextArea());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(refreshButton);

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        refreshTextArea();
    }

    private void refreshTextArea() {
        ArrayList<Issue> issues = browse.browseAll();
        StringBuilder stringBuilder = new StringBuilder();

        for(Issue issue : issues){
            stringBuilder.append("Title: ").append(issue.getTitle()).append("\n");
            stringBuilder.append("Status: ").append(issue.getStatus()).append("\n");
            stringBuilder.append("Priority: ").append(issue.getPriority()).append("\n");
            stringBuilder.append("Date: ").append(issue.getDate()).append("\n");
            stringBuilder.append("Reporter: ").append(issue.getReporter()).append("\n");
            stringBuilder.append("Assignee: ").append(issue.getAssignee()).append("\n");
            stringBuilder.append("Fixer: ").append(issue.getFixer()).append("\n");
        }

        textArea.setText(stringBuilder.toString());
    }
}