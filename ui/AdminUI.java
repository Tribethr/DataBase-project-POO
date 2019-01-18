package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import server.Cliente;

@SuppressWarnings("serial")
public class AdminUI extends UiFrame implements ActionListener{;
	
	private Cliente cliente;
	private JButton botonCrear;
	private JButton botonEliminar;

	public AdminUI(Cliente cliente) {
		super("Administrador", 550, 275);
		this.cliente = cliente;
		initComponents();
	}
	
	public void initComponents() {
	  crearLabel("Sistema gestor de bases de datos", 110, 10, 575, 25, 22);
	  botonCrear = crearBoton("Crear usuario", 115, 50, 325, 75, 22);
	  botonEliminar = crearBoton("Eliminar usuario", 115, 140, 325, 75, 22);
	  botonCrear.addActionListener(this);
	  botonEliminar.addActionListener(this);
      repaint();
	}

  @Override
  public void actionPerformed(ActionEvent e) {
	  if(e.getActionCommand().equals("Crear usuario")) {
		  
	  }else {
		  
	  }
  }

}