package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;

import server.Cliente;

@SuppressWarnings("serial")
public class UsuarioUI extends UiFrame implements ActionListener{;
	
	private Cliente cliente;
	private JButton botonCrearBase;
	private JButton botonCrearTabla;
	private JButton botonEliminarTabla;
	private JButton botonCambiarNombre;
	private JButton botonInsertarRegistroManual;
	private JButton botonInsertarRegistroArchivo;
	private JButton botonSeleccionarDatos;
	private JButton botonEliminarRegistro;
	private JButton botonOrdenarTabla;
	private JButton botonGenerarReporte;
	private JList<String> seleccionador;
	private String[] basesNombres;
	private String baseDatos;

	public UsuarioUI(Cliente cliente) throws UnknownHostException, ClassNotFoundException, IOException {
		super("Usuario", 690, 575);
		this.cliente = cliente;
		ArrayList<String> bases = cliente.getBasesDeDatos();
		basesNombres = new String[bases.size()];
		basesNombres = bases.toArray(basesNombres);
		seleccionarBase();
		initComponents();
	}
	
	public void seleccionarBase() throws UnknownHostException, ClassNotFoundException, IOException {
		setVisible(false);
		seleccionador = crearMultipleSeleccion("Seleccione una base de datos", basesNombres, false);
		if(seleccionador.getSelectedIndex() != -1) {
			baseDatos = seleccionador.getSelectedValue();
			setVisible(true);
		}else {
			JOptionPane.showMessageDialog(null, "Debe seleccionar una base de datos", "Error", JOptionPane.ERROR_MESSAGE);
			seleccionarBase();
		}
	}
	
	private void initComponents() {
	  crearLabel("Sistema gestor de bases de datos", 200, 10, 575, 25, 22);
	  botonCrearBase = crearBoton("Crear base de datos", 10, 50, 325, 75, 22);
	  botonCrearTabla = crearBoton("Crear tabla", 350, 50, 325, 75, 22);
	  botonEliminarTabla = crearBoton("Eliminar tabla", 10, 150, 325, 75, 22);
	  botonCambiarNombre = crearBoton("Cambiar nombre a una tabla", 350, 150, 325, 75, 22);
	  botonInsertarRegistroManual = crearBoton("Insertar registro manualmente", 10, 250, 325, 75, 22);
	  botonInsertarRegistroArchivo = crearBoton("Insertar registro por archivo", 350, 250, 325, 75, 22);
	  botonSeleccionarDatos = crearBoton("Seleccionar datos", 10, 350, 325, 75, 22);
	  botonEliminarRegistro = crearBoton("Eliminar registros", 350, 350, 325, 75, 22);
	  botonOrdenarTabla = crearBoton("Ordenar una tabla por campo", 10, 450, 325, 75, 22);
	  botonGenerarReporte = crearBoton("Generar reporte", 350, 450, 325, 75, 22);
	  botonCrearBase.addActionListener(this);
	  botonCrearTabla.addActionListener(this);
	  botonEliminarTabla.addActionListener(this);
	  botonCambiarNombre.addActionListener(this);
	  botonInsertarRegistroManual.addActionListener(this);
	  botonInsertarRegistroArchivo.addActionListener(this);
	  botonSeleccionarDatos.addActionListener(this);
	  botonEliminarRegistro.addActionListener(this);
	  botonOrdenarTabla.addActionListener(this);
	  botonGenerarReporte.addActionListener(this);
      repaint();
	}

  @Override
  public void actionPerformed(ActionEvent e) {
	  if(e.getActionCommand().equals("Crear base de datos")) {
		  new CrearBaseDeDatosUI(cliente);
	  }else if(e.getActionCommand().equals("Crear tabla")) {
		  new CrearTablaUi(cliente, baseDatos);
	  }else if(e.getActionCommand().equals("Eliminar tabla")) {
		  new EliminarTablaUi(cliente, baseDatos);
	  }else if(e.getActionCommand().equals("Cambiar nombre a una tabla")) {
		  new CambiarNombreTablaUI(cliente, baseDatos);
	  }else if(e.getActionCommand().equals("Insertar registro manualmente")) {
		  new InsertarRegistroManualUi(cliente, baseDatos);
	  }else if(e.getActionCommand().equals("Insertar registro por archivo")) {
		  new InsertarRegistroPorArchivoUi(cliente, baseDatos);
	  }else if(e.getActionCommand().equals("Seleccionar datos")) {
		  new SeleccionarDatosUi(cliente, baseDatos);
	  }else if(e.getActionCommand().equals("Eliminar registros")) {
		  new EliminarRegistroUi(cliente,baseDatos);
	  }else if(e.getActionCommand().equals("Ordenar una tabla por campo")) {
		  new OrdenarDatosUi(cliente, baseDatos);
	  }else {
		  try {
			  	Calendar calendario = Calendar.getInstance();
		  		Date fecha = calendario.getTime();
		  		String fechaString = fecha.toString().replace(' ', '-');
		  		fechaString = fechaString.replace(':', '-');
		  		FileOutputStream archivo = new FileOutputStream("reporte-" + fechaString + ".html");
		  		DataOutputStream objeto = new DataOutputStream(archivo);
				objeto.writeUTF(cliente.reporte(baseDatos));
				objeto.close();
				JOptionPane.showMessageDialog(null, "Reporte creado!", "Base Creada", JOptionPane.INFORMATION_MESSAGE);
		  } catch (ClassNotFoundException | IOException e1) {
			  JOptionPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		  }
		  
	  }
  }

}