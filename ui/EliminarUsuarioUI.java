package ui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;

@SuppressWarnings("serial")
public class EliminarUsuarioUi extends UiFrame{
	private String[] opciones = {"Usuario1","Usuario2","Usuario3","Usuario4"};
	private JList<String> seleccionador;
	private JLabel errorLabel;
	
	public EliminarUsuarioUi() {
		super("Mantenimiento de usuarios", 300, 300);
		initComponents();
		repaint();
	}
	
	private void initComponents() {
		crearLabel("Mantenimiento de usuarios", 10, 20, 600, 30, 20);
		errorLabel = crearLabel("", 10, 50, 600, 30, 20);
		errorLabel.setForeground(Color.red);
		crearBoton("Seleccionar usuario", 50, 100, 200, 50, 16).addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				seleccionar();
			}
		});
		crearBoton("Eliminar usuario", 50, 180, 200, 50, 16).addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				eliminar();
			}
		});
	}
	
	private void seleccionar() {
		errorLabel.setText("");
		seleccionador = crearMultipleSeleccion("Seleccione un usuario", opciones, false);		
	}
	
	private void eliminar() {
		if(seleccionador != null && seleccionador.getSelectedIndex() != -1) {
			int seleccion = JOptionPane.showConfirmDialog(this, "Estas seguro que deseas eliminar este usuario?");
			if(seleccion == 0) {
				JOptionPane.showMessageDialog(this, "Usuario eliminado");
			}
		}else {
			errorLabel.setText("Debe seleccionar un usuario");
			repaint();
		}
	}

}
