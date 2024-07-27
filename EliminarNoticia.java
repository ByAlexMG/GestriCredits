import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class EliminarNoticia extends JFrame {
    private JTextField tituloField;
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

        tituloField = new JTextField();
        eliminarButton = new JButton("Eliminar Noticia");
        statusArea = new JTextArea();
        statusArea.setEditable(false);

        panel.add(new JLabel("Palabra clave de la Noticia:"));
        panel.add(tituloField);
        panel.add(eliminarButton);

        add(panel, BorderLayout.NORTH);
        add(new JScrollPane(statusArea), BorderLayout.CENTER);

        eliminarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String titulo = tituloField.getText();
                if (titulo.isEmpty()) {
                    statusArea.setText("Por favor, ingresa una palabra clave.");
                } else {
                    try {
                        eliminarNoticia(titulo);
                        statusArea.setText("Noticia eliminada exitosamente.");
                    } catch (IOException ex) {
                        statusArea.setText("Error al eliminar la noticia: " + ex.getMessage());
                    }
                }
            }
        });
    }

    private void eliminarNoticia(String palabraClave) throws IOException {
        File inputFile = new File("pages\\noticias.html");
        StringBuilder inputBuffer = new StringBuilder();

        BufferedReader reader = new BufferedReader(new FileReader(inputFile));

        String line;
        boolean skip = false;

        while ((line = reader.readLine()) != null) {
            if (line.contains("<!-- INSERTAR AQUI -->")) {
                StringBuilder block = new StringBuilder();
                block.append(line).append("\n");
                while ((line = reader.readLine()) != null && !line.contains("<!-- FIN -->")) {
                    block.append(line).append("\n");
                }
                block.append(line).append("\n");
                if (block.toString().contains(palabraClave)) {
                    skip = true;
                } else {
                    inputBuffer.append(block.toString());
                }
            } else if (!skip) {
                inputBuffer.append(line).append("\n");
            } else if (line.contains("<!-- FIN -->")) {
                skip = false;
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
