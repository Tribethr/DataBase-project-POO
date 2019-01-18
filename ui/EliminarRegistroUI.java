package ui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import datos.BaseDeDatos;
import datos.Fila;
import datos.Tabla;
import server.Cliente;

@SuppressWarnings("serial")
public class EliminarRegistroUi extends UiFrame{
	private JList<String> seleccionadorTabla;
	private JList<String> seleccionadorCondicion;
	private JList<String> seleccionadorDeCriterioDeBusqueda;
	private String condicionDeBusqueda;
	private String criterioDeBusqueda;
	private JLabel seleccionTablaErrorLabel;
	private JLabel seleccionCriterioErrorLabel;
	private JLabel estructuraDeLaTablaLabel;
	private JLabel nombreDeLaTablaLabel;
	private JTextArea registrosEncontradosTextArea;
	private boolean criterioSeleccionado;
	private boolean condicionSeleccionada;
	private long tiempo;
	private Tabla tablaActual;
	private JTextField textoDeBusquedaInput;
	private Cliente cliente;
	private String baseDeDatos;
	
	public EliminarRegistroUi(Cliente cliente, String baseDeDatos) {
		super("Selccionar datos", 600, 750);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.cliente = cliente;
		this.baseDeDatos = baseDeDatos;
		initComponents();
		repaint();
	}
	
	private void initComponents() {
		crearLabel("Eliminar registros", 235, 20, 600, 30, 20);
		crearLabel("Estructura de la tabla:", 20, 130, 200, 30, 16);
		nombreDeLaTablaLabel = crearLabel("", 200, 130, 200, 30, 16);
		estructuraDeLaTablaLabel = crearLabel("", 40, 170, 500, 30, 16);
		seleccionTablaErrorLabel = crearLabel("", 10, 50, 600, 30, 20);
		seleccionTablaErrorLabel.setForeground(Color.red);
		seleccionCriterioErrorLabel = crearLabel("", 20, 210, 600, 30, 20);
		seleccionCriterioErrorLabel.setForeground(Color.red);
		crearLabel("Registros eliminados:", 20, 400, 300, 30, 14);
		textoDeBusquedaInput = crearTextField(280, 265, 300, 30, 14);
		registrosEncontradosTextArea = crearListado("", 20, 430, 570, 310, 14);
		
		crearBoton("Seleccionar tabla", 20, 80, 200, 40, 14).addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				seleccionarTabla();
			}
		});
		crearBoton("Criterio", 20, 260, 110, 40, 14).addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				seleccionarCriterioDeBusqueda();
			}
		});
		crearBoton("Condición", 150, 260, 110, 40, 14).addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				seleccionarCondicionDebusqueda();
			}
		});
		crearBoton("Buscar", 100, 330, 150, 40, 14).addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buscar();
			}
		});
		crearBoton("Limpiar", 350, 330, 150, 40, 14).addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				limpiar();
			}
		});
	}
	
	private void limpiar() {
		textoDeBusquedaInput.setText("");
		registrosEncontradosTextArea.setText("");
	}
	
	private void buscar() {
		if(tablaActual == null) {
			seleccionCriterioErrorLabel.setText("Debe seleccionar una tabla");
		}else if(!criterioSeleccionado) {
			seleccionCriterioErrorLabel.setText("Debe seleccionar un criterio");
		}else if(!condicionSeleccionada) {
			seleccionCriterioErrorLabel.setText("Debe seleccionar una condición");
		}else {
			tiempo = System.currentTimeMillis();
			if(textoDeBusquedaInput.getText().equals("")) {
				ArrayList<Integer> posiciones = new ArrayList<Integer>();
				int max = tablaActual.getFilas().get(tablaActual.getNombresFilas().get(0)).getDatos().size();
				for(int i = 0; i< max; i++) {
					posiciones.add(i);
				}
				mostrarResultados(posiciones);
			}else if(condicionDeBusqueda.equals("igual")) {
				mostrarResultados(buscarPorIgualdad());
			}else if(condicionDeBusqueda.equals("menor que")) {
				mostrarResultados(buscarPorMenorQue());
			}else if(condicionDeBusqueda.equals("mayor que")) {
				mostrarResultados(buscarPorMayorQue());
			}else {
				mostrarResultados(buscarPorDesIgualdad());
			}
		}
		repaint();
	}
	
	private void mostrarResultados(ArrayList<Integer> posiciones) {
		if(posiciones == null || posiciones.size() == 0) {
			registrosEncontradosTextArea.setText("No se encontraron registros");
			return;
		}
		try {
			cliente.eliminarRegistros(baseDeDatos, tablaActual.getNombre(), posiciones);
		} catch (IOException e) {
			e.printStackTrace();
		}
		registrosEncontradosTextArea.setText("Cantidad de registros eliminados: "+posiciones.size()+".\n"+
		"Tiempo de ejecución: "+(System.currentTimeMillis()-tiempo)+"ms");
	}
	
	private ArrayList<Integer> buscarPorIgualdad() {
		Fila fila = tablaActual.getFilas().get(criterioDeBusqueda);
		if(!fila.verificarDato(textoDeBusquedaInput.getText())) {
			registrosEncontradosTextArea.setText("El tipo de dato suministrado no coincide con el criterio de búsqueda");
			return null;
		}
		ArrayList<Integer> posiciones = new ArrayList<Integer>();
		Object dato = null;
		if(fila.getTipo().equals("String")) {
			dato = textoDeBusquedaInput.getText();
		}else if(fila.getTipo().equals("Int")) {
			dato = Integer.valueOf(textoDeBusquedaInput.getText());
		}else if (fila.getTipo().equals("Float")) {
			dato = Float.valueOf(textoDeBusquedaInput.getText());
		}else {
			dato = Boolean.valueOf(textoDeBusquedaInput.getText());
		}
		int i = 0;
		for(Object posibilidad: fila.getDatos()) {
			if(dato.equals(posibilidad)) {
				posiciones.add(i);
			}
			i++;
		}
		return posiciones;
	}
	private ArrayList<Integer> buscarPorDesIgualdad() {
		Fila fila = tablaActual.getFilas().get(criterioDeBusqueda);
		if(!fila.verificarDato(textoDeBusquedaInput.getText())) {
			registrosEncontradosTextArea.setText("El tipo de dato suministrado no coincide con el criterio de búsqueda");
			return null;
		}
		ArrayList<Integer> posiciones = new ArrayList<Integer>();
		Object dato = null;
		if(fila.getTipo().equals("String")) {
			dato = textoDeBusquedaInput.getText();
		}else if(fila.getTipo().equals("Int")) {
			dato = Integer.valueOf(textoDeBusquedaInput.getText());
		}else if (fila.getTipo().equals("Float")) {
			dato = Float.valueOf(textoDeBusquedaInput.getText());
		}else {
			dato = Boolean.valueOf(textoDeBusquedaInput.getText());
		}
		int i = 0;
		for(Object posibilidad: fila.getDatos()) {
			if(!dato.equals(posibilidad)) {
				posiciones.add(i);
			}
			i++;
		}
		return posiciones;
	}
	private ArrayList<Integer> buscarPorMayorQue() {
		Fila fila = tablaActual.getFilas().get(criterioDeBusqueda);
		if(!fila.verificarDato(textoDeBusquedaInput.getText())) {
			registrosEncontradosTextArea.setText("El tipo de dato suministrado no coincide con el criterio de búsqueda");
			return null;
		}
		ArrayList<Integer> posiciones = new ArrayList<Integer>();
		if(fila.getTipo().equals("Int")) {
			int dato = Integer.valueOf(textoDeBusquedaInput.getText());
			int i = 0;
			for(Object posibilidad: fila.getDatos()) {
				int conv = (int)posibilidad;
				System.out.println(dato+" "+conv);
				if(dato < conv) {
					posiciones.add(i);
				}
				i++;
			}
			return posiciones;
		}else {
			float dato = Float.valueOf(textoDeBusquedaInput.getText());
			int i = 0;
			for(Object posibilidad: fila.getDatos()) {
				int conv = Math.round((Float)posibilidad);
				System.out.println(dato+" "+conv);
				if(dato < conv) {
					posiciones.add(i);
				}
				i++;
			}
			return posiciones;
		}
		
	}
	private ArrayList<Integer> buscarPorMenorQue() {
		Fila fila = tablaActual.getFilas().get(criterioDeBusqueda);
		if(!fila.verificarDato(textoDeBusquedaInput.getText())) {
			registrosEncontradosTextArea.setText("El tipo de dato suministrado no coincide con el criterio de búsqueda");
			return null;
		}
		ArrayList<Integer> posiciones = new ArrayList<Integer>();
		if(fila.getTipo().equals("Int")) {
			int dato = Integer.valueOf(textoDeBusquedaInput.getText());
			int i = 0;
			for(Object posibilidad: fila.getDatos()) {
				int conv = (int)posibilidad;
				if(dato > conv) {
					posiciones.add(i);
				}
				i++;
			}
			return posiciones;
		}else {
			float dato = Float.valueOf(textoDeBusquedaInput.getText());
			int i = 0;
			for(Object posibilidad: fila.getDatos()) {
				int conv = Math.round((Float)posibilidad);
				if(dato > conv) {
					posiciones.add(i);
				}
				i++;
			}
			return posiciones;
		}
	}
	
	private String [] crearCondicionesDeBusqueda(String tipo) {
		if(tipo.equals("Int")|| tipo.equals("Float")) {
			return new String[]{"igual","diferente","mayor que","menor que"};
		}else {
			return new String[] {"igual","diferente"};
		}
	}
	
	private void seleccionarCondicionDebusqueda() {
		if(criterioSeleccionado) {
			Fila filaActual = tablaActual.getFilas().get(criterioDeBusqueda);
			String[] condiciones = crearCondicionesDeBusqueda(filaActual.getTipo());			
			seleccionadorCondicion = crearMultipleSeleccion("Seleccione una condición de busqueda", condiciones, false);
			if(seleccionadorCondicion.getSelectedIndex() != -1) {
				condicionDeBusqueda = condiciones[seleccionadorCondicion.getSelectedIndex()];
				seleccionCriterioErrorLabel.setText("");
				condicionSeleccionada = true;
			}else {
				seleccionCriterioErrorLabel.setText("Debe seleccionar una condición");
				condicionSeleccionada = false;
			}
		}else {
			seleccionCriterioErrorLabel.setText("Debe seleccionar un criterio");
		}
		repaint();
	}
	
	private void seleccionarCriterioDeBusqueda() {
		if(tablaActual != null) {
			String[] criterios = new String[tablaActual.getNombresFilas().size()];
			for(int i = 0; i<criterios.length; i++) {
				criterios[i] = tablaActual.getNombresFilas().get(i);
			}
			seleccionadorDeCriterioDeBusqueda = crearMultipleSeleccion("Seleccione un criterio de busqueda", criterios, false);
			if(seleccionadorDeCriterioDeBusqueda.getSelectedIndex() != -1) {
				criterioDeBusqueda = criterios[seleccionadorDeCriterioDeBusqueda.getSelectedIndex()];
				seleccionCriterioErrorLabel.setText("");
				criterioSeleccionado = true;
			}else {
				seleccionCriterioErrorLabel.setText("Debe seleccionar un criterio");
				criterioSeleccionado = false;
			}
		}else {
			seleccionCriterioErrorLabel.setText("Debe seleccionar una tabla");
		}
		repaint();
	}
	
	private void seleccionarTabla() {
		ArrayList<String> tablasActuales = new ArrayList<String>();
		try {
			tablasActuales = cliente.getNombreTablas(baseDeDatos);
		} catch (ClassNotFoundException | IOException e1) {
			e1.printStackTrace();
		}
		String[] tablas = new String[tablasActuales.size()];
		for(int i = 0; i<tablas.length; i++) {
			tablas[i] = tablasActuales.get(i);
		}
		seleccionadorTabla = crearMultipleSeleccion("Seleccione una tabla", tablas, false);
		if(seleccionadorTabla.getSelectedIndex() != -1) {
			seleccionTablaErrorLabel.setText("");
			seleccionCriterioErrorLabel.setText("");
			try {
				tablaActual = cliente.getTabla(baseDeDatos,tablas[seleccionadorTabla.getSelectedIndex()]);
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}
			nombreDeLaTablaLabel.setText(tablaActual.getNombre());
			estructuraDeLaTablaLabel.setText(tablaActual.estrucutraDeLaTabla());
		}else {
			seleccionTablaErrorLabel.setText("Debe seleccionar una tabla");
			estructuraDeLaTablaLabel.setText("");
			nombreDeLaTablaLabel.setText("");
		}
		repaint();
	}
	
}
