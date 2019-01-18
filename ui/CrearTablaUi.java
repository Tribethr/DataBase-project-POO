package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import datos.Fila;
import datos.Tabla;
import server.Cliente;

import javax.swing.JCheckBox;

@SuppressWarnings("serial")
public class CrearTablaUi extends UiFrame{
	private String[] opciones = {"String","Int","Float","Booleano"};
	private JList<String> seleccionador;
	private JTextField nombreDeLaTablaInput;
	private JTextField nombreDelCampoInput;
	private JCheckBox requeridoBool;
	private JTextArea textoDeRevision;
	private Tabla tablaActual;
	private Cliente cliente;
	private String baseDatos;
	
	public CrearTablaUi(Cliente cliente, String baseDatos) {
		super("Crear tabla", 600, 600);
		this.baseDatos = baseDatos;
		this.cliente = cliente;
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		initComponents();
		repaint();
	}
	
	private void initComponents() {
		crearLabel("Crear tabla	", 245, 20, 600, 30, 20);
		crearLabel("Nombre de la tabla: ", 20, 80, 200, 30, 15);
		nombreDeLaTablaInput = crearTextField(180, 80, 200, 30, 15);
		tablaActual = new Tabla("");
		crearLabel("Estructura de la tabla", 20, 130, 200, 30, 15);
		crearLabel("Nombre del campo:", 60, 180, 200, 30, 15);
		nombreDelCampoInput = crearTextField(220, 180, 200, 30, 15);
		requeridoBool = crearBoolCheck("Requerido", 60, 300, 200, 30,16);
		textoDeRevision = crearListado("", 30, 350, 550, 220, 14);
		crearBoton("Crear tabla", 411, 250, 150, 40, 14).addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				crearTabla();
			}
		});
		crearBoton("Tipo de dato", 37, 250, 150, 40, 14).addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				escogerTipoDeDato();
			}
		});
		crearBoton("Agregar campo", 224, 250, 150, 40, 14).addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				agregarCampo(false);
			}
		});
	}
	
	private void escogerTipoDeDato() {
		seleccionador = crearMultipleSeleccion("Seleccione un tipo de dato", opciones, false);
		if(seleccionador.getSelectedIndex() == -1) {
			JOptionPane.showMessageDialog(this, "Debe seleccionar un tipo de dato");
			escogerTipoDeDato();
		}
	}
	
	private void crearTabla() {
		if(agregarCampo(true)) {
			if(tablaActual.getFilas().size()<2) {
				JOptionPane.showMessageDialog(this, "La tabla debe tener al menos 2 campos");
				return;
			}
			tablaActual.setNombre(nombreDeLaTablaInput.getText());
			try {
				if(cliente.crearTabla(tablaActual, baseDatos)) {
					JOptionPane.showMessageDialog(this, "Tabla creada!");
				}else {
					JOptionPane.showMessageDialog(this, "La tabla ya existe o no se pudo conectar");
				}
			} catch (ClassNotFoundException | IOException e) {
				JOptionPane.showMessageDialog(null, e.getClass(), "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	private boolean agregarCampo(boolean creandoTabla) {
		Pattern numeros = Pattern.compile("[0-9]");
		Matcher tieneNumeros = numeros.matcher(nombreDelCampoInput.getText());
		if(nombreDelCampoInput.getText().length()<2 || nombreDelCampoInput.getText().length() > 16){
			JOptionPane.showMessageDialog(this, "El nombre del campo debe ser entre 2 y 16 caracteres");
			return false;
		}else if(tieneNumeros.find()) {
			JOptionPane.showMessageDialog(this, "El nombre del campo no debe tener números");
			return false;
		}else if(seleccionador == null) {
			JOptionPane.showMessageDialog(this, "Debe seleccionar un tipo de dato");
			escogerTipoDeDato();
			return false;
		}else{
			if(creandoTabla) {
				return true;
			}
			Fila fila;
			if(seleccionador.getSelectedIndex() == 0) {
				fila = new Fila<String>(nombreDelCampoInput.getText(), "String", requeridoBool.isSelected());
				tablaActual.annadirFila(fila);
			}else if(seleccionador.getSelectedIndex() == 1) {
				fila = new Fila<Integer>(nombreDelCampoInput.getText(), "Int", requeridoBool.isSelected());
				tablaActual.annadirFila(fila);
			}else if(seleccionador.getSelectedIndex() == 2) {
				fila = new Fila<Float>(nombreDelCampoInput.getText(), "Float", requeridoBool.isSelected());
				tablaActual.annadirFila(fila);
			}else {
				fila = new Fila<Boolean>(nombreDelCampoInput.getText(), "Booleano", requeridoBool.isSelected());
				tablaActual.annadirFila(fila);
			}
			textoDeRevision.setText(textoDeRevision.getText()+"\n"+fila.toString());
			return true;
		}
	}
}
