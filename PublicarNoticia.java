import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class PublicarNoticia {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Añadir Noticia");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(600, 500); // Ajustado el tamaño para acomodar dos áreas de texto
            frame.setLayout(new GridLayout(7, 2)); // Ajustado el layout a 7 filas

            JLabel titleLabel = new JLabel("Título:");
            JTextField titleField = new JTextField();

            JLabel briefDescriptionLabel = new JLabel("Descripción breve:");
            JTextArea briefDescriptionArea = new JTextArea(3, 20); // Área de texto para la descripción breve
            JScrollPane briefDescriptionScrollPane = new JScrollPane(briefDescriptionArea);

            JLabel modalDescriptionLabel = new JLabel("Descripción del modal:");
            JTextArea modalDescriptionArea = new JTextArea(5, 20); // Área de texto para la descripción del modal
            JScrollPane modalDescriptionScrollPane = new JScrollPane(modalDescriptionArea);

            JLabel imageLabel = new JLabel("Imagen:");
            JButton selectImageButton = new JButton("Seleccionar Imagen");
            JLabel imageFileName = new JLabel("Ninguna imagen seleccionada");

            JButton submitButton = new JButton("Añadir Noticia");
            JButton cancelButton = new JButton("Cancelar");

            frame.add(titleLabel);
            frame.add(titleField);
            frame.add(briefDescriptionLabel);
            frame.add(briefDescriptionScrollPane);
            frame.add(modalDescriptionLabel);
            frame.add(modalDescriptionScrollPane);
            frame.add(imageLabel);
            frame.add(selectImageButton);
            frame.add(new JLabel()); // Espacio en blanco
            frame.add(imageFileName);
            frame.add(submitButton);
            frame.add(cancelButton);

            selectImageButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JFileChooser fileChooser = new JFileChooser();
                    int returnValue = fileChooser.showOpenDialog(frame);
                    if (returnValue == JFileChooser.APPROVE_OPTION) {
                        File selectedFile = fileChooser.getSelectedFile();
                        imageFileName.setText(selectedFile.getAbsolutePath());
                    }
                }
            });

            submitButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String title = titleField.getText();
                    String briefDescription = briefDescriptionArea.getText();
                    String modalDescription = modalDescriptionArea.getText();
                    String imagePath = imageFileName.getText();

                    if (!title.isEmpty() && !briefDescription.isEmpty() && !modalDescription.isEmpty() && !imagePath.equals("Ninguna imagen seleccionada")) {
                        try {
                            // Copiar la imagen seleccionada al directorio de recursos
                            File sourceFile = new File(imagePath);
                            File destinationFile = new File("../drawable/images/" + sourceFile.getName());
                            try (InputStream in = new FileInputStream(sourceFile);
                                 OutputStream out = new FileOutputStream(destinationFile)) {
                                byte[] buffer = new byte[1024];
                                int bytesRead;
                                while ((bytesRead = in.read(buffer)) != -1) {
                                    out.write(buffer, 0, bytesRead);
                                }
                            }

                            // Insertar noticia en el archivo HTML
                            appendNewsToFile(title, briefDescription, modalDescription, destinationFile.getName());
                            JOptionPane.showMessageDialog(frame, "Noticia añadida exitosamente!");
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                            JOptionPane.showMessageDialog(frame, "Error al añadir noticia.");
                        }
                    } else {
                        JOptionPane.showMessageDialog(frame, "Todos los campos son obligatorios.");
                    }
                }
            });

            cancelButton.addActionListener(e -> frame.dispose());

            frame.setVisible(true);
        });
    }

    private static void appendNewsToFile(String title, String briefDescription, String modalDescription, String imageFileName) throws IOException {
        String filePath = "pages\\noticias.html";

        // Leer el archivo HTML y crear el nuevo contenido con la noticia
        StringBuilder newContent = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean inserted = false;

            while ((line = reader.readLine()) != null) {
                newContent.append(line).append(System.lineSeparator());

                // Buscar el marcador para insertar la noticia
                if (line.contains("<!-- INSERTAR AQUI -->")) {
                    newContent.append("<div class=\"col s12 m6\">\n");
                    newContent.append("    <div class=\"card horizontal\">\n");
                    newContent.append("        <div class=\"card-image\">\n");
                    newContent.append("            <img src=\"../drawable/images/").append(imageFileName).append("\" alt=\"").append(title).append("\">\n");
                    newContent.append("        </div>\n");
                    newContent.append("        <div class=\"card-stacked\">\n");
                    newContent.append("            <div class=\"card-content\">\n");
                    newContent.append("                <h5>").append(title).append("</h5>\n");
                    newContent.append("                <p>").append(briefDescription).append("</p>\n");
                    newContent.append("            </div>\n");
                    newContent.append("            <div class=\"card-action\">\n");
                    newContent.append("                <a class=\"modal-trigger\" href=\"#modal").append(title.hashCode()).append("\">Leer m&aacutes</a>\n");
                    newContent.append("            </div>\n");
                    newContent.append("        </div>\n");
                    newContent.append("    </div>\n");
                    newContent.append("</div>\n");
                    newContent.append("<div id=\"modal").append(title.hashCode()).append("\" class=\"modal\">\n");
                    newContent.append("    <div class=\"modal-content\">\n");
                    newContent.append("        <h4>").append(title).append("</h4>\n");
                    newContent.append("        <p>").append(modalDescription).append("</p>\n");
                    newContent.append("    </div>\n");
                    newContent.append("    <div class=\"modal-footer\">\n");
                    newContent.append("        <a href=\"#!\" class=\"modal-close waves-effect waves-green btn-flat\">Cerrar</a>\n");
                    newContent.append("    </div>\n");
                    newContent.append("</div>\n");
                    newContent.append("<!-- FIN -->\n");

                    inserted = true;
                }
            }

            // Si no se encontró el marcador, lo añadimos al final del archivo
            if (!inserted) {
                newContent.append("<!-- INSERTAR AQUI -->\n");
            }
        }

        // Reemplazar el archivo original con el nuevo contenido
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(newContent.toString());
        }
    }
}
