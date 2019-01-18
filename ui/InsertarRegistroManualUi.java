package ui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import datos.BaseDeDatos;
import datos.Tabla;
import server.Cliente;

@SuppressWarnings("serial")
public class InsertarRegistroManualUi extends UiFrame{
	private JList<String> seleccionador;
	private JLabel seleccionErrorLabel;
	private JLabel insertarErrorLabel;
	private JLabel estructuraDeLaTablaLabel;
	private JLabel nombreDeLaTablaLabel;
	private boolean sePuedeInsertar;
	private Tabla tablaActual;
	private JTextField datosAIngresarInput;
	private String baseDeDatos;
	private Cliente cliente;
	
	public InsertarRegistroManualUi(Cliente cliente, String baseDeDatos) {
		super("Insertar registro", 600, 400);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.baseDeDatos = baseDeDatos;
		this.cliente = cliente;
		initComponents();
		repaint();
	}
	
	private void initComponents() {
		crearLabel("Ingresar registro", 245, 20, 600, 30, 20);
		crearLabel("Estructura de la tabla:", 20, 130, 200, 30, 16);
		nombreDeLaTablaLabel = crearLabel("", 200, 130, 200, 30, 16);
		estructuraDeLaTablaLabel = crearLabel("", 40, 170, 500, 30, 16);
		seleccionErrorLabel = crearLabel("", 10, 50, 600, 30, 20);
		seleccionErrorLabel.setForeground(Color.red);
		crearLabel("Datos a ingresar: ", 20, 210, 200, 30, 15);
		datosAIngresarInput = crearTextField(40, 250, 450, 30, 15);
		crearBoton("Seleccionar tabla", 20, 80, 200, 40, 14).addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				seleccionarTabla();
			}
		});
		crearBoton("Insertar", 225, 310, 150, 40, 14).addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				insertar();
			}
		});
	}
	
	private void seleccionarTabla() {
		ArrayList<String> nombreTablas;
		try {
			nombreTablas = cliente.getNombreTablas(baseDeDatos);
			String[] tablas = new String[nombreTablas.size()];
			tablas = nombreTablas.toArray(tablas);
			seleccionador = crearMultipleSeleccion("Seleccione una tabla", tablas, false);
			if(seleccionador.getSelectedIndex() != -1) {
				seleccionErrorLabel.setText("");
				sePuedeInsertar = true;
				tablaActual = cliente.getTabla(baseDeDatos, seleccionador.getSelectedValue());
				nombreDeLaTablaLabel.setText(tablaActual.getNombre());
				estructuraDeLaTablaLabel.setText(tablaActual.estrucutraDeLaTabla());
			}else {
				seleccionErrorLabel.setText("Debe seleccionar una tabla");
				estructuraDeLaTablaLabel.setText("");
				nombreDeLaTablaLabel.setText("");
				sePuedeInsertar = false;
			}
			repaint();
		}catch (ClassNotFoundException | IOException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void insertar() {
		if(sePuedeInsertar) {
			String texto = datosAIngresarInput.getText();
			String[] datos = texto.split(",");
			if(tablaActual.verificarRegistro(datos)) {
				tablaActual.insertarRegistro(datos);
				try {
					cliente.inserRegistro(baseDeDatos, tablaActual);
				} catch (IOException e) {
					JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
				JOptionPane.showMessageDialog(this, "Registro insertado con éxito");
				System.out.println(tablaActual.registrosToString());
			}else {
				insertarErrorLabel = crearLabel("Formato invalido", 215, 220, 600, 30, 20);
				insertarErrorLabel.setForeground(Color.red);
			}
		}
	}
}
