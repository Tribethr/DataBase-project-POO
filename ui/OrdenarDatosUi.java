package ui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import datos.Tabla;
import server.Cliente;

public class OrdenarDatosUi extends UiFrame implements ActionListener{
	private String[] ordenamientos = {"Ascendente","Descendente"};
	private String[] tablas;
	private String[] campos;
	private JList<String> seleccionador;
	private String tabla;
	private String ordenamiento;
	private JButton botonTabla;
	private JButton botonCampo;
	private JButton botonOrdenamiento;
	private JButton botonOrdenar;
	private Cliente cliente;
	private String baseDatos;
	private String campo;
	
	public OrdenarDatosUi(Cliente cliente, String baseDatos) {
		super("Ordenar datos", 660, 250);
		this.cliente = cliente;
		this.baseDatos = baseDatos;
		ArrayList<String> tablas;
		try {
			tablas = cliente.getNombreTablas(baseDatos);
			this.tablas = new String[tablas.size()];
			this.tablas = tablas.toArray(this.tablas);
		} catch (ClassNotFoundException | IOException e) {
			JOptionPane.showMessageDialog(null, "Debe selecionar una tabla", "Error", JOptionPane.ERROR_MESSAGE);
		}
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		initComponents();
		repaint();
	}
	
	private void initComponents() {
		crearLabel("Ordenar datos", 250, 20, 600, 30, 20);
		botonTabla = crearBoton("Seleccionar tabla", 20, 80, 200, 40, 14);
		botonCampo = crearBoton("Seleccionar campo", 230, 80, 200, 40, 14);
		botonOrdenamiento = crearBoton("Seleccionar ordenamiento", 440, 80, 200, 40, 14);
		botonOrdenar = crearBoton("Ordenar", 230, 150, 200, 40, 14);
		botonTabla.addActionListener(this);
		botonCampo.addActionListener(this);
		botonOrdenamiento.addActionListener(this);
		botonOrdenar.addActionListener(this);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("Seleccionar tabla")) {
			seleccionarTabla();
		}else if(e.getActionCommand().equals("Seleccionar campo")) {
			seleccionarCampo();
		}else if(e.getActionCommand().equals("Seleccionar ordenamiento")) {
			seleccionarOrdenamiento();
		}else if(e.getActionCommand().equals("Ordenar")) {
			ordenar();
		}
	}
	
	private void ordenar() {
		try {
			cliente.ordenar(baseDatos, tabla, campo, ordenamiento);
			JOptionPane.showMessageDialog(null, "¡Tabla Ordenada!", "Ordenamiento", JOptionPane.INFORMATION_MESSAGE);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void seleccionarOrdenamiento() {
		seleccionador = crearMultipleSeleccion("Seleccione un ordenamiento", ordenamientos, false);
		if(seleccionador.getSelectedIndex() != -1) {
			ordenamiento = seleccionador.getSelectedValue();
		}else {
			JOptionPane.showMessageDialog(null, "Debe seleccionar un ordenamiento.", "Error", JOptionPane.ERROR_MESSAGE);
			seleccionarOrdenamiento();
		}
	}
	
	private void seleccionarCampo(){
		if(tabla == null) {
			JOptionPane.showMessageDialog(null, "Debe seleccionar una tabla.", "Error", JOptionPane.ERROR_MESSAGE);
			seleccionarTabla();
		}else {
			seleccionador = crearMultipleSeleccion("Seleccione un campo", campos, false);
			if(seleccionador.getSelectedIndex() != -1) {
				campo = seleccionador.getSelectedValue();
			}else {
				JOptionPane.showMessageDialog(null, "Debe seleccionar un campo.", "Error", JOptionPane.ERROR_MESSAGE);
				seleccionarCampo();
			}
		}
	}
	
	private void seleccionarTabla() {
		seleccionador = crearMultipleSeleccion("Seleccione una tabla", tablas, false);
		if(seleccionador.getSelectedIndex() != -1) {
			tabla = seleccionador.getSelectedValue();
			Tabla tablaServer;
			try {
				tablaServer = cliente.getTabla(baseDatos, tabla);
				ArrayList<String> nombreCampos = tablaServer.getNombresFilas();
				campos = new String[nombreCampos.size()];
				campos = nombreCampos.toArray(campos);
			} catch (ClassNotFoundException | IOException e) {
				JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
		}else {
			JOptionPane.showMessageDialog(null, "Debe seleccionar una tabla.", "Error", JOptionPane.ERROR_MESSAGE);
			seleccionarTabla();
		}
	}
	
}
