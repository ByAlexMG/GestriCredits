import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class HTMLUpdaterGUI {

    // Constructor de la interfaz gráfica
    public HTMLUpdaterGUI() {
        // Crear el marco de la aplicación
        JFrame frame = new JFrame("Actualizador de HTML");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        // Crear componentes de la interfaz
        JLabel locationLabel = new JLabel("Ubicación:");
        locationLabel.setBounds(10, 10, 80, 25);
        frame.add(locationLabel);

        JTextField locationField = new JTextField();
        locationField.setBounds(100, 10, 160, 25);
        frame.add(locationField);

        JLabel priceLabel = new JLabel("Precio:");
        priceLabel.setBounds(10, 40, 80, 25);
        frame.add(priceLabel);

        JTextField priceField = new JTextField();
        priceField.setBounds(100, 40, 160, 25);
        frame.add(priceField);

        JLabel descriptionLabel = new JLabel("Descripción:");
        descriptionLabel.setBounds(10, 70, 80, 25);
        frame.add(descriptionLabel);

        JTextArea descriptionArea = new JTextArea();
        descriptionArea.setBounds(100, 70, 160, 75);
        frame.add(descriptionArea);

        JButton updateButton = new JButton("Actualizar");
        updateButton.setBounds(10, 160, 150, 25);
        frame.add(updateButton);

        // Acción del botón de actualización
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String location = locationField.getText();
                String price = priceField.getText();
                String description = descriptionArea.getText();

                updateHTML(location, price, description);
                JOptionPane.showMessageDialog(frame, "Archivo HTML actualizado.");
            }
        });

        // Mostrar el marco
        frame.setVisible(true);
    }

    // Método para actualizar el HTML
    public void updateHTML(String location, String price, String description) {
        String filePath = "pages\\venta.html"; // Reemplaza con la ruta real del archivo HTML
        StringBuilder htmlContent = new StringBuilder();

        // Leer el archivo HTML
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                htmlContent.append(line).append(System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
            return; // Salir si ocurre un error al leer el archivo
        }

        // Generar el nuevo HTML de la propiedad
        String newPropertyHTML =
            "<div class='col s12 m6 card-item' data-ubicacion='" + location + "' data-precio='" + price + "'>\n" +
            "    <div class='card' onclick='openModal(" + getNextId() + ")'>\n" +
            "        <div class='card-image'>\n" +
            "            <img src='ruta/a/imagen.jpg' alt='Imagen de la Propiedad'>\n" + // Cambia la ruta de la imagen si es necesario
            "            <span class='card-title'>Propiedad</span>\n" +
            "        </div>\n" +
            "        <div class='card-content'>\n" +
            "            <p>Ubicación: " + location + "</p>\n" +
            "            <p>Precio: " + price + "€</p>\n" +
            "            <p>Descripción: " + description + "</p>\n" +
            "        </div>\n" +
            "    </div>\n" +
            "</div>\n";

        // Buscar el lugar de inserción
        int insertIndex = htmlContent.indexOf("<!-- INSERTAR AQUI -->");
        if (insertIndex == -1) {
            System.out.println("Marcador de posición no encontrado en el archivo HTML.");
            return;
        }

        // Insertar el nuevo contenido
        htmlContent.insert(insertIndex, newPropertyHTML);

        // Escribir el archivo HTML actualizado
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(htmlContent.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método para obtener el siguiente ID para las propiedades
    public int getNextId() {
        // Implementar lógica para obtener el siguiente ID de propiedad
        // Aquí solo devolvemos un número fijo por simplicidad
        // En una implementación real, podrías leer el archivo HTML o una base de datos para encontrar el siguiente ID disponible
        return 1;
    }

    public static void main(String[] args) {
        // Crear la interfaz gráfica
        SwingUtilities.invokeLater(() -> new HTMLUpdaterGUI());
    }
}
