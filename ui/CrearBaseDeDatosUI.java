package ui;

import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import datos.DataCenter;
import server.Cliente;

@SuppressWarnings("serial")
public class CrearBaseDeDatosUI extends UiFrame{

	private JTextField nombreBase;
	private Cliente cliente;
	
	public CrearBaseDeDatosUI(Cliente cliente) {
		super("Crear base de datos", 310, 180);
		this.cliente = cliente;
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		initComponents();
	}
	
	public void initComponents() {
		crearLabel("Crear base de datos", 70, 10, 200, 25, 16);
		crearLabel("Nombre:", 10, 50, 150, 25, 16);
		nombreBase = crearTextField(100, 50, 200, 30, 22);
		crearBoton("Crear", 100, 90, 100, 30, 22).addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					crearBaseDeDatos();
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		repaint();
	}
	
	public void crearBaseDeDatos() throws HeadlessException, ClassNotFoundException, IOException {
		if(nombreBase.getText().equals("")) {
			setError("No debe dejar campos vacíos", 10, 120, 200, 25);
			return;
		} else if(cliente.crearBaseDatos(nombreBase.getText())) {
			JOptionPane.showMessageDialog(this, "Base de datos creada!");
			return;
		}
		JOptionPane.showMessageDialog(this, "La base de datos ya existe");
		
	}
	
}
