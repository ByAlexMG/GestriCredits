import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class PublicarAlquiler {

    private String propertyImagePath;
    private String propertyImageFileName; // Mover aquí
    private List<String> modalImagePaths = new ArrayList<>();
    private int nextId = 1; // Para manejar el ID de la propiedad

    public PublicarAlquiler() {
        loadNextId(); // Cargar el ID al iniciar

        JFrame frame = new JFrame("Publicador de Alquileres Gestri-Credits");
        frame.setSize(600, 900);
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
        JLabel descriptionLabel = new JLabel("Descripción");
        descriptionLabel.setBounds(10, 70, 80, 25);
        frame.add(descriptionLabel);

        JTextArea descriptionArea = new JTextArea();
        descriptionArea.setBounds(100, 70, 160, 75);
        frame.add(descriptionArea);

        // Campo para el título del card
        JLabel titleLabel = new JLabel("Título Anuncio:");
        titleLabel.setBounds(10, 150, 100, 25);
        frame.add(titleLabel);

        JTextField titleField = new JTextField();
        titleField.setBounds(120, 150, 160, 25);
        frame.add(titleField);

        // Campo para la imagen de la propiedad
        JLabel propertyImageLabel = new JLabel("Imagen Principal:");
        propertyImageLabel.setBounds(10, 180, 150, 25);
        frame.add(propertyImageLabel);

        JTextField propertyImageField = new JTextField();
        propertyImageField.setBounds(170, 180, 160, 25);
        propertyImageField.setEditable(false);
        frame.add(propertyImageField);

        JButton propertyBrowseButton = new JButton("Seleccionar UNA Imagen");
        propertyBrowseButton.setBounds(340, 180, 200, 25);
        frame.add(propertyBrowseButton);

        // Campo para las imágenes del modal
        JLabel modalImagesLabel = new JLabel("Selecionar ImageneS ");
        modalImagesLabel.setBounds(10, 220, 150, 25);
        frame.add(modalImagesLabel);

        JTextArea modalImagesArea = new JTextArea();
        modalImagesArea.setBounds(170, 220, 160, 75);
        modalImagesArea.setEditable(false);
        frame.add(modalImagesArea);

        JButton modalBrowseButton = new JButton("VariaS ImageneS Ctrl+click");
        modalBrowseButton.setBounds(340, 220, 200, 25);
        frame.add(modalBrowseButton);

        // Campos para el modal
        JLabel modalTitleLabel = new JLabel("Título Anuncio:");
        modalTitleLabel.setBounds(10, 300, 100, 25);
        frame.add(modalTitleLabel);

        JTextField modalTitleField = new JTextField();
        modalTitleField.setBounds(120, 300, 160, 25);
        frame.add(modalTitleField);

        JLabel modalContentLabel = new JLabel("Descripción extensa:");
        modalContentLabel.setBounds(10, 330, 140, 25);
        frame.add(modalContentLabel);

        JTextArea modalContentArea = new JTextArea();
        modalContentArea.setBounds(160, 330, 300, 100);
        frame.add(modalContentArea);

        // Botón de actualización
        JButton updateButton = new JButton("Publicar");
        updateButton.setBounds(10, 450, 150, 25);
        frame.add(updateButton);

        // Acción del botón "Seleccionar Imagen de la Propiedad"
        propertyBrowseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(frame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    propertyImagePath = selectedFile.getAbsolutePath();
                    propertyImageFileName = selectedFile.getName(); // Guardar nombre del archivo
                    propertyImageField.setText(propertyImageFileName);
                }
            }
        });

        // Acción del botón "Seleccionar Imágenes del Modal"
        modalBrowseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setMultiSelectionEnabled(true);
                int result = fileChooser.showOpenDialog(frame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File[] selectedFiles = fileChooser.getSelectedFiles();
                    modalImagePaths.clear();
                    modalImagesArea.setText("");
                    for (File file : selectedFiles) {
                        modalImagePaths.add(file.getAbsolutePath());
                        modalImagesArea.append(file.getName() + "\n");
                    }
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

                if (propertyImagePath != null && !propertyImagePath.isEmpty()) {
                    if (propertyImageFileName == null) { // Asegurarse de que el nombre del archivo esté definido
                        propertyImageFileName = new File(propertyImagePath).getName();
                    }
                    try {
                        copyImageToDirectory(propertyImagePath, "../drawable/images/" + propertyImageFileName);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                        JOptionPane.showMessageDialog(frame, "Error al guardar la imagen de la propiedad.");
                        return;
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Selecciona una imagen de la propiedad primero.");
                    return;
                }

                if (!modalImagePaths.isEmpty()) {
                    List<String> modalImageFileNames = new ArrayList<>();
                    for (String path : modalImagePaths) {
                        String modalImageFileName = new File(path).getName();
                        modalImageFileNames.add(modalImageFileName);
                        try {
                            copyImageToDirectory(path, "../drawable/images/" + modalImageFileName);
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                            JOptionPane.showMessageDialog(frame, "Error al guardar la imagen del modal: " + modalImageFileName);
                            return;
                        }
                    }
                    updateHTML(location, price, description, "../drawable/images/" + propertyImageFileName, modalImageFileNames, title, modalTitle, modalContent);
                    JOptionPane.showMessageDialog(frame, "Propiedad Publicada.");
                } else {
                    JOptionPane.showMessageDialog(frame, "Selecciona al menos una imagen para el modal.");
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

    public void updateHTML(String location, String price, String description, String propertyImagePath, List<String> modalImageFileNames, String title, String modalTitle, String modalContent) {
        String filePath = "pages\\alquilar.html"; // Reemplaza con la ruta real del archivo HTML
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
            "            <img src='" + propertyImagePath + "' alt='Imagen de la Propiedad'>\n" +
            "            <span class='card-title' id=\"nombre\">" + title + "</span>\n" +
            "        </div>\n" +
            "        <div class='card-content'>\n" +
            "            <p>Ubicaci&oacuten: " + location + "</p>\n" +
            "            <p>Precio: " + price + "&euro;</p>\n" +
            "            <p>Descripci&oacuten: " + description + "</p>\n" +
            "        </div>\n" +
            "    </div>\n" +
            "</div>\n";

        // HTML para el nuevo modal con carrusel
        StringBuilder carouselHTML = new StringBuilder();
        for (int i = 0; i < modalImageFileNames.size(); i++) {
            String modalImageFileName = modalImageFileNames.get(i);
            carouselHTML.append("            <a class='carousel-item' href='#!'><img src='../drawable/images/").append(modalImageFileName).append("' alt='Imagen ").append(i + 1).append("'></a>\n");
        }

        String newModalHTML =
            "<div id='modal" + nextId + "' class='modal'>\n" +
            "    <div class='modal-content'>\n" +
            "        <h4>" + modalTitle + "</h4>\n" +
            "        <div class='carousel carousel-slider'>\n" +
            carouselHTML.toString() +
            "        </div>\n" +
            "        <p>Ubicaci&oacuten: " + location + "</p>\n" +
            "        <p>Precio: " + price + "&euro;</p>\n" +
            "        <p>Descripci&oacuten: " + modalContent + "</p>\n" +
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
        SwingUtilities.invokeLater(() -> new PublicarAlquiler());
    }
}
