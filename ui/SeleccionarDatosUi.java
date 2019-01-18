/**
 * La clase Insertar registro manual ui provee la interfáz gráfica, para hacer la inserción de un registro. 
 * @author Abraham Meza Vega, Giancarlos Fonseca Esquivel, Tribeth Rivas Pérez
 * @version (1.0 - 25/11/18)
 */

package ui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import datos.BaseDeDatos;
import datos.Fila;
import datos.Tabla;
import server.Cliente;

@SuppressWarnings("serial")
public class SeleccionarDatosUi extends UiFrame{
	private JList<String> seleccionadorTabla;
	private JList<String> seleccionadorCondicion;
	private JList<String> seleccionadorDeCriterioDeBusqueda;
	private JList<String> seleccionadorDeCamposVisibles;
	private String condicionDeBusqueda;
	private String[] camposVisibles;
	private String criterioDeBusqueda;
	private JLabel seleccionTablaErrorLabel;
	private JLabel seleccionCriterioErrorLabel;
	private JLabel estructuraDeLaTablaLabel;
	private JLabel nombreDeLaTablaLabel;
	private JTextArea registrosEncontradosTextArea;
	private boolean criterioSeleccionado;
	private boolean condicionSeleccionada;
	private boolean camposVisiblesSeleccionados;
	private Tabla tablaActual;
	private JTextField textoDeBusquedaInput;
	private String baseDeDatos;
	private Cliente cliente;
	
	/**
	 * Constructor, Se le ingresa la base de datos en la cual se está trabajando.
	 * @param baseDeDatos, base de datos con la cual se va a trabajar.
	 */
	public SeleccionarDatosUi(Cliente cliente, String baseDeDatos) {
		super("Selccionar datos", 600, 800);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.cliente = cliente;
		this.baseDeDatos = baseDeDatos;
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		initComponents();
		repaint();
	}
	
	/**
	 * Inicializa los componentes necesarios para dibujar la ui.
	 */
	private void initComponents() {
		crearLabel("Buscar datos", 235, 20, 600, 30, 20);
		crearLabel("Estructura de la tabla:", 20, 130, 200, 30, 16);
		nombreDeLaTablaLabel = crearLabel("", 200, 130, 200, 30, 16);
		estructuraDeLaTablaLabel = crearLabel("", 40, 170, 500, 30, 16);
		seleccionTablaErrorLabel = crearLabel("", 10, 50, 600, 30, 20);
		seleccionTablaErrorLabel.setForeground(Color.red);
		seleccionCriterioErrorLabel = crearLabel("", 20, 280, 600, 30, 20);
		seleccionCriterioErrorLabel.setForeground(Color.red);
		crearLabel("Campos a seleccionar:", 20, 210, 200, 30, 15);
		crearLabel("Registros encontrados:", 20, 450, 300, 30, 14);
		textoDeBusquedaInput = crearTextField(280, 315, 300, 30, 14);
		registrosEncontradosTextArea = crearListado("", 20, 480, 570, 310, 14);
		crearBoton("Campos visibles",20, 250, 200, 30, 15).addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				seleccionarCamposVisibles();
			}
		});;
		crearBoton("Seleccionar tabla", 20, 80, 200, 40, 14).addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				seleccionarTabla();
			}
		});
		crearBoton("Criterio", 20, 310, 110, 40, 14).addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				seleccionarCriterioDeBusqueda();
			}
		});
		crearBoton("Condición", 150, 310, 110, 40, 14).addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				seleccionarCondicionDebusqueda();
			}
		});
		crearBoton("Buscar", 100, 380, 150, 40, 14).addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buscar();
			}
		});
		crearBoton("Limpiar", 350, 380, 150, 40, 14).addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				limpiar();
			}
		});
	}
	
	/**
	 * limpia los cuadros de ingreso de texto.
	 */
	private void limpiar() {
		textoDeBusquedaInput.setText("");
		registrosEncontradosTextArea.setText("");
	}
	
	/**
	 * Se encarga de buscar los datos basado en los parámetros en caso de que estos sean correctos.
	 */
	private void buscar() {
		if(tablaActual == null) {
			seleccionCriterioErrorLabel.setText("Debe seleccionar una tabla");
		}else if(!criterioSeleccionado) {
			seleccionCriterioErrorLabel.setText("Debe seleccionar un criterio");
		}else if(!condicionSeleccionada) {
			seleccionCriterioErrorLabel.setText("Debe seleccionar una condiciÃ³n");
		}else if(!camposVisiblesSeleccionados) {
			seleccionCriterioErrorLabel.setText("Debe seleccionar al menos un campo");
		}else {
			if(textoDeBusquedaInput.getText().equals("")) {
				Fila fila = tablaActual.getFilas().get(criterioDeBusqueda);
				ArrayList<Integer> posiciones = new ArrayList<Integer>();
				for(int i = 0; i< fila.getDatos().size(); i++) {
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
	
	/**
	 * Se encarga de mostrar en pantalla los resultados de la búsqueda de los registros.
	 * @param posiciones, las posiciones de los datos bajo los correspondientes criterios de búsqueda.
	 */
	private void mostrarResultados(ArrayList<Integer> posiciones) {
		String resultados = "";
		if(posiciones == null || posiciones.size() == 0) {
			registrosEncontradosTextArea.setText("No se encontraron registros");
			return;
		}
		for(int posicion: posiciones) {
			System.out.println(posicion);
			for(String campo: camposVisibles) {
				if(posicion < tablaActual.getFilas().get(campo).getDatos().size()) {
					resultados += String.valueOf(tablaActual.getFilas().get(campo).registroPorColumnaToString(posicion))+", ";					
				}else {
					resultados += ", ";
				}
			}
			resultados = resultados.substring(0, resultados.length()-2)+"\n";
		}
		registrosEncontradosTextArea.setText(resultados);
	}
	
	/**
	 * Busca elementos que coincidan sobre los parametros búsqueda.
	 * @return Arreglo de enteros con las posiciones de los datos que concuerden.
	 */
	private ArrayList<Integer> buscarPorIgualdad() {
		Fila fila = tablaActual.getFilas().get(criterioDeBusqueda);
		if(!fila.verificarDato(textoDeBusquedaInput.getText())) {
			registrosEncontradosTextArea.setText("El tipo de dato suministrado no coincide con el criterio de bÃºsqueda");
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
	
	/**
	 * Busca elementos que no coincidan sobre los parametros búsqueda.
	 * @return Arreglo de enteros con las posiciones de los datos que concuerden.
	 */
	private ArrayList<Integer> buscarPorDesIgualdad() {
		Fila fila = tablaActual.getFilas().get(criterioDeBusqueda);
		if(!fila.verificarDato(textoDeBusquedaInput.getText())) {
			registrosEncontradosTextArea.setText("El tipo de dato suministrado no coincide con el criterio de bÃºsqueda");
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
	
	/**
	 * Busca elementos que sean mayores sobre los parametros búsqueda.
	 * @return Arreglo de enteros con las posiciones de los datos que concuerden.
	 */
	private ArrayList<Integer> buscarPorMayorQue() {
		Fila fila = tablaActual.getFilas().get(criterioDeBusqueda);
		if(!fila.verificarDato(textoDeBusquedaInput.getText())) {
			registrosEncontradosTextArea.setText("El tipo de dato suministrado no coincide con el criterio de bÃºsqueda");
			return null;
		}
		ArrayList<Integer> posiciones = new ArrayList<Integer>();
		if(fila.getTipo().equals("Int")) {
			int dato = Integer.valueOf(textoDeBusquedaInput.getText());
			int i = 0;
			for(Object posibilidad: fila.getDatos()) {
				int conv = (int)posibilidad;
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
				long conv =  Math.round((Float) posibilidad);
				if(dato < conv) {
					posiciones.add(i);
				}
				i++;
			}
			return posiciones;
		}
		
	}
	
	/**
	 * Busca elementos que sean menores sobre los parametros búsqueda.
	 * @return Arreglo de enteros con las posiciones de los datos que concuerden.
	 */
	private ArrayList<Integer> buscarPorMenorQue() {
		Fila fila = tablaActual.getFilas().get(criterioDeBusqueda);
		if(!fila.verificarDato(textoDeBusquedaInput.getText())) {
			registrosEncontradosTextArea.setText("El tipo de dato suministrado no coincide con el criterio de bÃºsqueda");
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
				long conv =  Math.round((Float) posibilidad);
				if(dato > conv) {
					posiciones.add(i);
				}
				i++;
			}
			return posiciones;
		}
	}
	
	/**
	 * genera las condiciones de búsqueda sobre los tipos de datos.
	 * @param tipo, el tipo de dato a evaluar.
	 * @return arreglo de string con las codiciones de búsqueda adecuadas para ese tipo de dato.
	 */
	private String [] crearCondicionesDeBusqueda(String tipo) {
		if(tipo.equals("Int")|| tipo.equals("Float")) {
			return new String[]{"igual","diferente","mayor que","menor que"};
		}else {
			return new String[] {"igual","diferente"};
		}
	}
	
	/**
	 * Crea un obejeto de selección múltiple para seleccionar los campos visibles en la búsqueda.
	 */
	private void seleccionarCamposVisibles() {
		if(tablaActual != null) {
			String[] criterios = new String[tablaActual.getNombresFilas().size()];
			for(int i = 0; i<criterios.length; i++) {
				criterios[i] = tablaActual.getNombresFilas().get(i);
			}
			seleccionadorDeCamposVisibles = crearMultipleSeleccion("Seleccione un criterio de busqueda", criterios, true);
			if(seleccionadorDeCamposVisibles.getSelectedIndex() != -1) {
				camposVisibles = new String[seleccionadorDeCamposVisibles.getSelectedIndices().length];
				int[] opciones = seleccionadorDeCamposVisibles.getSelectedIndices();
				int j = 0;
				for(int i :opciones) {
					camposVisibles[j++] = criterios[i];
				}
				seleccionCriterioErrorLabel.setText("");
				camposVisiblesSeleccionados = true;
			}else {
				seleccionCriterioErrorLabel.setText("Debe seleccionar al menos un campo");
				camposVisiblesSeleccionados = false;
			}
		}else {
			seleccionCriterioErrorLabel.setText("Debe seleccionar una tabla");
		}
		repaint();
	}
	
	/**
	 * Crea un obejeto de selección múltiple para seleccionar las condiciones de la búsqueda.
	 */
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
	
	/**
	 * Crea un obejeto de selección múltiple para seleccionar los criterios de la búsqueda.
	 */

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
	
	/**
	 * Crea un objeto de múltiple seleccion en el cual se selecciona una tabla.
	 */
	private void seleccionarTabla() {
		ArrayList<String> nombreTablas;
		try{
			nombreTablas = cliente.getNombreTablas(baseDeDatos);
			String[] tablas = new String[nombreTablas.size()];
			tablas = nombreTablas.toArray(tablas);
			seleccionadorTabla = crearMultipleSeleccion("Seleccione una tabla", tablas, false);
			if(seleccionadorTabla.getSelectedIndex() != -1) {
				seleccionTablaErrorLabel.setText("");
				seleccionCriterioErrorLabel.setText("");
				tablaActual = cliente.getTabla(baseDeDatos, seleccionadorTabla.getSelectedValue());
				nombreDeLaTablaLabel.setText(tablaActual.getNombre());
				estructuraDeLaTablaLabel.setText(tablaActual.estrucutraDeLaTabla());
			}else {
				seleccionTablaErrorLabel.setText("Debe seleccionar una tabla");
				estructuraDeLaTablaLabel.setText("");
				nombreDeLaTablaLabel.setText("");
			}
		}catch(Exception e) {
			JOptionPane.showMessageDialog(null, e.getClass(), "Error", JOptionPane.ERROR_MESSAGE);
		}
		repaint();
	}
	
}
