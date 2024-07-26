import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class HTMLUpdaterWithImageGUI {

    private String imagePath;
    private int nextId = 1; // Para manejar el ID de la propiedad

    public HTMLUpdaterWithImageGUI() {
        loadNextId(); // Cargar el ID al iniciar

        JFrame frame = new JFrame("Actualizador de HTML con Imagen");
        frame.setSize(600, 700);
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

        // Campos para el modal
        JLabel modalTitleLabel = new JLabel("Título del Modal:");
        modalTitleLabel.setBounds(10, 210, 100, 25);
        frame.add(modalTitleLabel);

        JTextField modalTitleField = new JTextField();
        modalTitleField.setBounds(120, 210, 160, 25);
        frame.add(modalTitleField);

        JLabel modalContentLabel = new JLabel("Contenido del Modal:");
        modalContentLabel.setBounds(10, 240, 140, 25);
        frame.add(modalContentLabel);

        JTextArea modalContentArea = new JTextArea();
        modalContentArea.setBounds(160, 240, 300, 100);
        frame.add(modalContentArea);

        // Botón de actualización
        JButton updateButton = new JButton("Actualizar");
        updateButton.setBounds(10, 350, 150, 25);
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
                String modalTitle = modalTitleField.getText();
                String modalContent = modalContentArea.getText();

                if (imagePath != null && !imagePath.isEmpty()) {
                    String imageFileName = new File(imagePath).getName();
                    try {
                        copyImageToDirectory(imagePath, "../drawable/images/" + imageFileName);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                        JOptionPane.showMessageDialog(frame, "Error al guardar la imagen.");
                        return;
                    }
                    updateHTML(location, price, description, "../drawable/images/" + imageFileName, title, modalTitle, modalContent);
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

    public void updateHTML(String location, String price, String description, String imagePath, String title, String modalTitle, String modalContent) {
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

        // HTML para la nueva propiedad
        String newPropertyHTML =
            "<div class='col s12 m6 card-item' data-ubicacion='" + location + "' data-precio='" + price + "'>\n" +
            "    <div class='card' onclick='openModal(" + nextId + ")'>\n" +
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

        // HTML para el nuevo modal
        String newModalHTML =
            "<div id='modal" + nextId + "' class='modal'>\n" +
            "    <div class='modal-content'>\n" +
            "        <h4>" + modalTitle + "</h4>\n" +
            "        <p>" + modalContent + "</p>\n" +
            "    </div>\n" +
            "    <div class='modal-footer'>\n" +
            "        <a href='#!' class='modal-close waves-effect waves-green btn-flat'>Cerrar</a>\n" +
            "    </div>\n" +
            "</div>\n";

        int insertIndex = htmlContent.indexOf("<!-- INSERTAR AQUI -->");
        if (insertIndex == -1) {
            System.out.println("Marcador de posición no encontrado en el archivo HTML.");
            return;
        }

        htmlContent.insert(insertIndex, newPropertyHTML);

        int modalInsertIndex = htmlContent.indexOf("<!-- INSERTAR MODALES AQUI -->");
        if (modalInsertIndex == -1) {
            System.out.println("Marcador de posición para modales no encontrado en el archivo HTML.");
            return;
        }

        htmlContent.insert(modalInsertIndex, newModalHTML);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(htmlContent.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        nextId++; // Incrementa el ID después de la actualización
        saveNextId(); // Guarda el nuevo valor de nextId
    }

    private void loadNextId() {
        File file = new File("nextId.txt");
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                nextId = Integer.parseInt(reader.readLine().trim());
            } catch (IOException | NumberFormatException e) {
                e.printStackTrace();
                nextId = 1; // Valor por defecto si ocurre un error
            }
        } else {
            nextId = 1; // Valor por defecto si el archivo no existe
        }
    }

    private void saveNextId() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("nextId.txt"))) {
            writer.write(Integer.toString(nextId));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HTMLUpdaterWithImageGUI());
    }
}
