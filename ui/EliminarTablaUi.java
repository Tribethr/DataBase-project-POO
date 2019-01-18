package ui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;

import server.Cliente;

@SuppressWarnings("serial")
public class EliminarTablaUi extends UiFrame{
	private String[] opciones;
	private JList<String> seleccionador;
	private JLabel errorLabel;
	private Cliente cliente;
	private String baseDatos;
	
	public EliminarTablaUi(Cliente cliente, String baseDatos) {
		super("Mantenimiento de tablas", 300, 300);
		this.cliente = cliente;
		this.baseDatos = baseDatos;
		try {
			ArrayList<String> tablas = cliente.getNombreTablas(baseDatos);
			opciones = new String[tablas.size()];
			opciones = tablas.toArray(opciones);
		}catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getClass(), "Error", JOptionPane.ERROR_MESSAGE);
		}
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		initComponents();
	}
	
	private void initComponents() {
		crearLabel("Mantenimiento de tablas", 10, 20, 600, 30, 20);
		crearBoton("Seleccionar tabla", 50, 100, 200, 50, 16).addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				seleccionar();
			}
		});
		crearBoton("Eliminar tabla", 50, 180, 200, 50, 16).addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				eliminar();
			}
		});
	}
	
	private void seleccionar() {
		seleccionador = crearMultipleSeleccion("Seleccione una tabla", opciones, false);		
	}
	
	private void eliminar() {
		if(seleccionador != null && seleccionador.getSelectedIndex() != -1) {
			try {
				cliente.eliminarTabla(baseDatos, seleccionador.getSelectedValue());
			}catch (ClassNotFoundException | IOException e) {
				JOptionPane.showMessageDialog(null, e.getClass(), "Error", JOptionPane.ERROR_MESSAGE);
			}
		}else {
			if(errorLabel == null) {
				errorLabel = crearLabel("Debe seleccionar una tabla", 10, 50, 600, 30, 20);
				errorLabel.setForeground(Color.red);
			}
			repaint();
		}
	}

}

