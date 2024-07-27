import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class EliminarNoticia extends JFrame {
    private JTextField palabraClaveField;
    private JButton eliminarButton;
    private JTextArea statusArea;

    public EliminarNoticia() {
        setTitle("Eliminar Noticias");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1));

        palabraClaveField = new JTextField();
        eliminarButton = new JButton("Eliminar Noticia");
        statusArea = new JTextArea();
        statusArea.setEditable(false);

        panel.add(new JLabel("Palabra clave de la Noticia:"));
        panel.add(palabraClaveField);
        panel.add(eliminarButton);

        add(panel, BorderLayout.NORTH);
        add(new JScrollPane(statusArea), BorderLayout.CENTER);

        eliminarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String palabraClave = palabraClaveField.getText();
                if (palabraClave.isEmpty()) {
                    statusArea.setText("Por favor, ingresa una palabra clave.");
                } else {
                    try {
                        eliminarNoticia(palabraClave);
                        statusArea.setText("Noticia eliminada exitosamente.");
                    } catch (IOException ex) {
                        statusArea.setText("Error al eliminar la noticia: " + ex.getMessage());
                    }
                }
            }
        });
    }

    private void eliminarNoticia(String palabraClave) throws IOException {
        File inputFile = new File("pages\\noticias\\noticias.html");
        StringBuilder inputBuffer = new StringBuilder();

        BufferedReader reader = new BufferedReader(new FileReader(inputFile));

        String line;
        boolean dentroDelBloque = false;
        boolean eliminarBloque = false;
        StringBuilder bloqueActual = new StringBuilder();

        while ((line = reader.readLine()) != null) {
            if (line.contains("<div class=\"col s12 m6\">")) {
                dentroDelBloque = true;
                eliminarBloque = false;
                bloqueActual.setLength(0); // Clear the current block
            }

            if (dentroDelBloque) {
                bloqueActual.append(line).append("\n");
                if (line.contains(palabraClave)) {
                    eliminarBloque = true;
                }
                if (line.contains("<!-- FIN -->")) {
                    dentroDelBloque = false;
                    if (!eliminarBloque) {
                        inputBuffer.append(bloqueActual.toString());
                    }
                }
            } else {
                inputBuffer.append(line).append("\n");
            }
        }

        reader.close();

        FileWriter writer = new FileWriter(inputFile);
        writer.write(inputBuffer.toString());
        writer.close();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new EliminarNoticia().setVisible(true);
            }
        });
    }
}
