/**
 * La clase CrearUsuarioUI provee la interfáz gráfica para la creación de un usuario.
 * @author Abraham Meza Vega, Giancarlos Fonseca Esquivel, Tribeth Rivas Pérez
 * @version (1.0 - 25/11/18)
 */
package ui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import server.Cliente;

@SuppressWarnings("serial")
public class CrearUsuarioUI extends UiFrame{

	private JLabel errorLabel;
	private JTextField usuarioInput;
	private JPasswordField contrasennaInput;
	private JList<String> seleccionador;
	private Cliente cliente;
	private String[] basesDisponibles;
	
	/**
	 * Constructor
	 * @param cliente, el cliente que controlará las peticiones con el servidor
	 */
	public CrearUsuarioUI(Cliente cliente) {
		super("Crear usuario", 550, 270);
		this.cliente = cliente;
		conseguirBasesDeDatosDisponibles();
		initComponents();
		repaint();
	}
	
	/**
	 * inicia los componentes de la interfáz gráfica
	 */
	public void initComponents() {
		crearLabel("Crear usuario", 175, 10, 575, 25, 16);
		crearLabel("Nombre de usuario:", 10, 50, 200, 25, 16);
		crearLabel("Contraseña:", 10, 100, 200, 25, 16);
		errorLabel = crearLabel("", 10, 140, 600, 25, 16);
		errorLabel.setForeground(Color.RED);
		usuarioInput = crearTextField(200, 50, 300, 30, 16);
		contrasennaInput = crearPasswordField(200, 100, 300, 30, 16);
		crearBoton("Registrar", 160, 180, 200, 40, 22).addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				validar();
			}
		});
	}
	
	/**
	 * valida si es necesario intentar crear el usuario
	 */
	public void validar() {
		if(!estaVacio() && usuarioEsValido() && contrasennaEsValida()) {
			seleccionarBases();
		}
	}
	
	/**
	 * crea un objeto de múltiple selección para trabajar con la tabla deseada
	 */
	private void seleccionarBases() {
		seleccionador = crearMultipleSeleccion("Seleccione las bases a las cuales el usuario tendrá acceso", basesDisponibles, true);
		if(seleccionador.getSelectedIndex() != -1) {
			try {
				if(cliente.accesar(usuarioInput.getText(), String.valueOf(contrasennaInput.getPassword()))) {
					String bases = "";
					for(int base : seleccionador.getSelectedIndices()) {
						bases += basesDisponibles[base]+";";
					}
					cliente.registrarUsuario(usuarioInput.getText(), String.valueOf(contrasennaInput.getPassword()), bases.substring(0,bases.length()-1));
					JOptionPane.showMessageDialog(this, "Se ha registrado el usuario!");
					dispose();
				}
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}
		}else {
			JOptionPane.showMessageDialog(this, "Debe seleccionar al menos una base de datos");
			seleccionarBases();
		}
	}
	
	/**
	 * Consigue los nombres de las bases de datos
	 */
	private void conseguirBasesDeDatosDisponibles() {
		ArrayList<String> nombresBases = cliente.conseguirNombresDeBasesDeDatos();
		if(nombresBases != null) {
			basesDisponibles = new String[nombresBases.size()];
			int i = 0;
			for(String base : nombresBases) {
				basesDisponibles[i] = base;
			}
		}
		basesDisponibles = new String[1];
		basesDisponibles[0] = "No se encontraron bases de datos";
	}
	
	/**
	 * Revisa si la contraseña cumple con el formáto requerido
	 * @return true en caso de cumplirlo, de lo contrario false
	 */
	private boolean contrasennaEsValida() {
		if(contrasennaInput.getPassword().length<7 || contrasennaInput.getPassword().length > 15) {
			errorLabel.setText("La contraseña debe tener entre 7 y 15 caracteres");
			return false;
		}
		Pattern letter = Pattern.compile("[a-zA-Z]");
		Matcher hasLetter = letter.matcher(String.valueOf(contrasennaInput.getPassword()));
		if(!hasLetter.find()) {
			errorLabel.setText("La contraseña debe tener al menos una letra");
			return false;
		}
		Pattern digit = Pattern.compile("[0-9]");
		Matcher hasDigit = digit.matcher(String.valueOf(contrasennaInput.getPassword()));
		if(!hasDigit.find()) {
			errorLabel.setText("La contraseña debe tener al menos un número");
			return false;
		}
		Pattern special = Pattern.compile ("[?$#@!]");
        Matcher hasSpecial = special.matcher(String.valueOf(contrasennaInput.getPassword()));
        if(!hasSpecial.find()) {
        	errorLabel.setText("La contraseña debe tener al menos uno de estos caracteres: ?$#@!");
			return false;
        }
        return true;
	}
	
	/**
	 * Revisa si el usuario cumple con el formáto requerido
	 * @return true en caso de cumplirlo, de lo contrario false
	 */
	private boolean usuarioEsValido() {
		if(usuarioInput.getText().length()<7 || usuarioInput.getText().length() > 12) {
			errorLabel.setText("El nombre de usuario debe tener entre 7 y 12 caracteres");
			return false;
		}
		return true;
	}
	
	/**
	 * Revisa si los campos están vacíos
	 * @return true en caso de cumplirlo, de lo contrario false
	 */
	public boolean estaVacio() {
		if(contrasennaInput.getPassword().length == 0 || usuarioInput.getText().length() == 0) {
			errorLabel.setText("No debe dejar campos vacíos");
			return true;
		}
		return false;
	}
	
}
