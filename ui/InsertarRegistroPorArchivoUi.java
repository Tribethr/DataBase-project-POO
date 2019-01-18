package ui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import datos.BaseDeDatos;
import datos.Tabla;
import server.Cliente;
import util.CSVParser;

@SuppressWarnings("serial")
public class InsertarRegistroPorArchivoUi extends UiFrame{
	private JList<String> seleccionador;
	private JLabel seleccionErrorLabel;
	private JLabel insertarErrorLabel;
	private JLabel estructuraDeLaTablaLabel;
	private JLabel nombreDeLaTablaLabel;
	private boolean sePuedeInsertar;
	private Tabla tablaActual;
	private String rutaArhivo;
	private JLabel nombreArchivoLabel;
	private ArrayList<String[]> datosAInsertar;
	private String baseDeDatos;
	private Cliente cliente;
	
	public InsertarRegistroPorArchivoUi(Cliente cliente, String baseDeDatos) {
		super("Insertar registro", 600, 400);
		this.cliente = cliente;
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.baseDeDatos = baseDeDatos;
		initComponents();
		repaint();
	}
	
	private void initComponents() {
		crearLabel("Ingresar registro", 245, 20, 600, 30, 20);
		crearLabel("Estructura de la tabla:", 20, 130, 200, 30, 16);
		nombreDeLaTablaLabel = crearLabel("", 200, 130, 200, 30, 16);
		rutaArhivo = "";
		estructuraDeLaTablaLabel = crearLabel("", 40, 170, 500, 30, 16);
		seleccionErrorLabel = crearLabel("", 10, 50, 600, 30, 20);
		seleccionErrorLabel.setForeground(Color.red);
		crearLabel("Archivo:", 20, 200, 200, 30, 14);
		nombreArchivoLabel = crearLabel("", 80, 200, 400, 30, 14);
		crearBoton("Seleccionar tabla", 20, 80, 200, 40, 14).addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				seleccionarTabla();
			}
		});
		crearBoton("Seleccionar archivo", 190, 250, 220, 40, 14).addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buscarRuta();
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
		}catch(Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void buscarRuta() {
		JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV files","CSV");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(null);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            rutaArhivo = chooser.getSelectedFile().getPath();
            nombreArchivoLabel.setText(chooser.getSelectedFile().getName());
        }
	}
	
	private void insertar() {
		if(sePuedeInsertar) {
			if(verificarArchivo() && !rutaArhivo.equals("")) {
				for(String[] dato: datosAInsertar) {
					tablaActual.insertarRegistro(dato);
				}
				try {
					cliente.inserRegistro(baseDeDatos, tablaActual);
				} catch (IOException e) {
					JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
				JOptionPane.showMessageDialog(this, "Registro insertado con éxito");
				dispose();
			}else {
				insertarErrorLabel = crearLabel("Formato inválido", 215, 220, 600, 30, 20);
				insertarErrorLabel.setForeground(Color.red);
			}
		}
	}
	
	private boolean verificarArchivo() {
		datosAInsertar = CSVParser.parseCSVFile(rutaArhivo);
		if(datosAInsertar.size() == 0) {
			return false;
		}
		for(String[] dato: datosAInsertar) {
			if(!tablaActual.verificarRegistro(dato)) {
				return false;
			}
		}
		return true;
	}
}
