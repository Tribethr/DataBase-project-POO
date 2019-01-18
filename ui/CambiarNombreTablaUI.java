package ui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import server.Cliente;

public class CambiarNombreTablaUI extends UiFrame implements ActionListener{
	
	private String[] tablas;
	private JTextField nombreTextField;
	private JButton botonCambiar;
	private JList<String> tablasSeleccion;
	private String nuevoNombre;
	private String baseDatos;
	private Cliente cliente;
	private String tabla;
	
	public CambiarNombreTablaUI(Cliente cliente, String baseDatos) {
		super("Cambiar nombre a una tabla", 390, 180);
		this.baseDatos = baseDatos;
		this.cliente = cliente;
		ArrayList<String> tablas;
		try {
			tablas = cliente.getNombreTablas(baseDatos);
			this.tablas = new String[tablas.size()];
			this.tablas = tablas.toArray(this.tablas);
		} catch (ClassNotFoundException | IOException e) {
			JOptionPane.showMessageDialog(null, "Debe selecionar una tabla", "Error", JOptionPane.ERROR_MESSAGE);
		}
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(false);
		seleccionarAlgo();
		initComponents();
	}
	
	public void initComponents() {
		crearLabel("Cambiar nombre a una tabla", 55, 10, 300, 25, 22);
		crearLabel("Nuevo nombre:", 10, 50, 150, 25, 22);
		nombreTextField = crearTextField(175, 50, 200, 30, 22);
		botonCambiar = crearBoton("Cambiar nombre", 85, 100, 200, 40, 22);
		botonCambiar.addActionListener(this);
		repaint();
	}
	
	public void actionPerformed(ActionEvent e) {
		setNuevoNombre(nombreTextField.getText());
		if(nuevoNombre.equals("")) {
			setError("Debe ingresar un nombre", 100, 100, 100, 100);
		} else
			try {
				if(cliente.cambiarNombreTabla(baseDatos, tabla, nuevoNombre)){
					JOptionPane.showMessageDialog(null, "¡Nombre cambiado!", "Nombre cambiado", JOptionPane.INFORMATION_MESSAGE);
				}else {
					JOptionPane.showMessageDialog(null, "Error al cambiar el nombre", "Error", JOptionPane.ERROR_MESSAGE);
				}
			} catch (ClassNotFoundException | IOException e1) {
				JOptionPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
	  }
	
	public void setNuevoNombre(String nuevoNombre) {
		this.nuevoNombre = nuevoNombre;
	}
	
	private void seleccionarAlgo() {
		tablasSeleccion = crearMultipleSeleccion("Seleccione la tabla", tablas, false);
		if(tablasSeleccion.getSelectedIndex() != -1) {
			this.tabla = tablasSeleccion.getSelectedValue();
			setVisible(true);
		}else {
			JOptionPane.showMessageDialog(null, "Debe selecionar una tabla", "Error", JOptionPane.ERROR_MESSAGE);
			seleccionarAlgo();
		}
		repaint();
	}
	
}
