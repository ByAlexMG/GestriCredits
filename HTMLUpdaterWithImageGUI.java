import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class HTMLUpdaterWithImageGUI {

    private String imagePath;

    public HTMLUpdaterWithImageGUI() {
        JFrame frame = new JFrame("Actualizador de HTML con Imagen");
        frame.setSize(600, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        // Campo para la ubicación
        JLabel locationLabel = new JLabel("Ubicación:");
        locationLabel.setBounds(10, 10, 80, 25);
        frame.add(locationLabel);

        JTextField locationField = new JTextField();
        locationField.setBounds(100, 10, 160, 25);
        frame.add(locationField);

        // Campo para el precio
        JLabel priceLabel = new JLabel("Precio:");
        priceLabel.setBounds(10, 40, 80, 25);
        frame.add(priceLabel);

        JTextField priceField = new JTextField();
        priceField.setBounds(100, 40, 160, 25);
        frame.add(priceField);

        // Campo para la descripción
        JLabel descriptionLabel = new JLabel("Descripción:");
        descriptionLabel.setBounds(10, 70, 80, 25);
        frame.add(descriptionLabel);

        JTextArea descriptionArea = new JTextArea();
        descriptionArea.setBounds(100, 70, 160, 75);
        frame.add(descriptionArea);

        // Campo para el título del card
        JLabel titleLabel = new JLabel("Título del Card:");
        titleLabel.setBounds(10, 150, 100, 25);
        frame.add(titleLabel);

        JTextField titleField = new JTextField();
        titleField.setBounds(120, 150, 160, 25);
        frame.add(titleField);

        // Campo para la imagen
        JLabel imageLabel = new JLabel("Imagen:");
        imageLabel.setBounds(10, 180, 80, 25);
        frame.add(imageLabel);

        JTextField imageField = new JTextField();
        imageField.setBounds(100, 180, 160, 25);
        frame.add(imageField);

        JButton browseButton = new JButton("Seleccionar Imagen");
        browseButton.setBounds(270, 180, 200, 25);
        frame.add(browseButton);

        // Botón de actualización
        JButton updateButton = new JButton("Actualizar");
        updateButton.setBounds(10, 220, 150, 25);
        frame.add(updateButton);

        // Acción del botón "Seleccionar Imagen"
        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(frame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    imagePath = selectedFile.getAbsolutePath();
                    imageField.setText(selectedFile.getName());
                }
            }
        });

        // Acción del botón "Actualizar"
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String location = locationField.getText();
                String price = priceField.getText();
                String description = descriptionArea.getText();
                String title = titleField.getText();

                if (imagePath != null && !imagePath.isEmpty()) {
                    String imageFileName = new File(imagePath).getName();
                    try {
                        copyImageToDirectory(imagePath, "../drawable/images/" + imageFileName);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                        JOptionPane.showMessageDialog(frame, "Error al guardar la imagen.");
                        return;
                    }
                    updateHTML(location, price, description, "../drawable/images/" + imageFileName, title);
                    JOptionPane.showMessageDialog(frame, "Archivo HTML actualizado.");
                } else {
                    JOptionPane.showMessageDialog(frame, "Selecciona una imagen primero.");
                }
            }
        });

        frame.setVisible(true);
    }

    private void copyImageToDirectory(String sourcePath, String destPath) throws IOException {
        File sourceFile = new File(sourcePath);
        File destFile = new File(destPath);

        // Obtener el directorio de destino
        File destDir = destFile.getParentFile();
        if (!destDir.exists()) {
            // Crear el directorio si no existe
            destDir.mkdirs();
        }

        // Copiar el archivo
        Files.copy(sourceFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    public void updateHTML(String location, String price, String description, String imagePath, String title) {
        String filePath = "pages\\venta.html"; // Reemplaza con la ruta real del archivo HTML
        StringBuilder htmlContent = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                htmlContent.append(line).append(System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        String newPropertyHTML =
            "<div class='col s12 m6 card-item' data-ubicacion='" + location + "' data-precio='" + price + "'>\n" +
            "    <div class='card' onclick='openModal(" + getNextId() + ")'>\n" +
            "        <div class='card-image'>\n" +
            "            <img src='" + imagePath + "' alt='Imagen de la Propiedad'>\n" +
            "            <span class='card-title' id=\"nombre\">" + title + "</span>\n" +
            "        </div>\n" +
            "        <div class='card-content'>\n" +
            "            <p>Ubicaci&oacuten: " + location + "</p>\n" +
            "            <p>Precio: " + price + "&euro;</p>\n" +
            "            <p>Descripci&oacuten: " + description + "</p>\n" +
            "        </div>\n" +
            "    </div>\n" +
            "</div>\n";

        int insertIndex = htmlContent.indexOf("<!-- INSERTAR AQUI -->");
        if (insertIndex == -1) {
            System.out.println("Marcador de posición no encontrado en el archivo HTML.");
            return;
        }

        htmlContent.insert(insertIndex, newPropertyHTML);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(htmlContent.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getNextId() {
        // Implementar lógica para obtener el siguiente ID de propiedad
        return 1;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HTMLUpdaterWithImageGUI());
    }
}
