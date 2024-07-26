import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.*;
import java.util.regex.*;

public class EliminarVentas {
    private JFrame frame;
    private JTextField titleField;
    private JButton removeButton;

    public EliminarVentas() {
        frame = new JFrame("Remove Property");
        frame.setSize(400, 150);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        JLabel titleLabel = new JLabel("Enter Property Title:");
        titleLabel.setBounds(20, 20, 150, 25);
        frame.add(titleLabel);

        titleField = new JTextField();
        titleField.setBounds(180, 20, 180, 25);
        frame.add(titleField);

        removeButton = new JButton("Remove Property");
        removeButton.setBounds(120, 60, 150, 25);
        frame.add(removeButton);

        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeProperty();
            }
        });

        frame.setVisible(true);
    }

    private void removeProperty() {
        String titleToRemove = titleField.getText().trim();
        if (titleToRemove.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please enter a property title.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Path path = Paths.get("pages\\venta.html"); // Replace with the path to your HTML file
            String content = new String(Files.readAllBytes(path));

            // Updated regular expression
            String regex = "<div\\s+class=['\"]col\\s+s12\\s+m6\\s+card-item['\"][^>]*>\\s*" +
                           "<div\\s+class=['\"]card['\"][^>]*>\\s*" +
                           "<div\\s+class=['\"]card-image['\"][^>]*>\\s*" +
                           "<img[^>]*>\\s*" +
                           "<span\\s+class=['\"]card-title['\"][^>]*>\\Q" + titleToRemove + "\\E</span>\\s*" +
                           ".*?\\s*" +
                           "</div>\\s*" +
                           "<div\\s+class=['\"]card-content['\"][^>]*>\\s*.*?</div>\\s*" +
                           "</div>\\s*" +
                           "</div>";
            Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
            Matcher matcher = pattern.matcher(content);

            // Print the content and regex for debugging
            System.out.println("Content Before:\n" + content);
            System.out.println("Regex Used: " + regex);

            // Check if the title was found and remove the matching block
            if (matcher.find()) {
                content = matcher.replaceAll("");
                Files.write(path, content.getBytes());
                JOptionPane.showMessageDialog(frame, "Property removed successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(frame, "Property not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            
            titleField.setText("");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(frame, "Error while processing the file.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(EliminarVentas::new);
    }
}
