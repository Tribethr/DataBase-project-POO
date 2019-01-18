/**
 * La clase tabla está encargada de crear y administrar las filas que sean necesarias para dar riqueza de información a la base de datos.
 * @author Abraham Meza Vega, Giancarlos Fonseca Esquivel, Tribeth Rivas Pérez
 * @version (1.0 - 25/11/18)
 */
package datos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Tabla implements Serializable{
	private static final long serialVersionUID = 5836620666848271423L;
	private String nombre;
	private HashMap<String,Fila> filas;
	private ArrayList<String> nombresFilas;
	private String estructuraDeLaTablaMSG;
	private int cantRegistros;
	
	/**
	 * Constructor que inicializa las variables necesarias para el manejo de la tabla.
	 * @param nombre, este parámetro indicará el nombre que identificará a la tabla en la base de datos.
	 */
	public Tabla(String nombre) {
		this.nombre = nombre;
		filas = new HashMap<String,Fila>();
		nombresFilas = new ArrayList<String>();
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public HashMap<String,Fila> getFilas() {
		return filas;
	}
	
	public ArrayList<String> getNombresFilas(){
		return nombresFilas;
	}
	
	/**
	 * El método intenta insertar un registro el cual se le pasa como un arreglo de string.
	 * @param datos, son los datos a ingresar en la tabla.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void insertarRegistro(String[] datos) {
		int i = 0;
		Fila filaActual = null;
		for(String llave: nombresFilas) {
			filaActual = filas.get(llave);
			if(!filaActual.isRequerido()&&datos[i].equals(" ")) {
				i++;
				continue;
			}
			if(filaActual.getTipo().equals("String")) {
				filaActual.addDato(datos[i]);
			}else if(filaActual.getTipo().equals("Int")) {
				filaActual.addDato(Integer.valueOf(datos[i]));
			}else if (filaActual.getTipo().equals("Float")) {
				filaActual.addDato(Float.valueOf(datos[i]));
			}else {
				filaActual.addDato(Boolean.valueOf(datos[i]));
			}
			i++;
		}
		cantRegistros++;
	}
	
	/**
	 * Verifica por cada fila de la tabla si el tipo de dato que se le está suministrando es válido para posteriormente hacer o no hacer el registro de dichos datos.
	 * @param datos, los datos a validar en forma de arreglo de string.
	 * @return true en el caso de que los datos tengan en formato correcto, false de lo contrario.
	 */
	public boolean verificarRegistro(String []datos) {
		int i = 0;
		Fila filaActual = null;
		for(String llave: nombresFilas) {
			filaActual = filas.get(llave);
			if(i == datos.length) {
				return false;
			}
			if(!filaActual.isRequerido()&&datos[i].equals(" ")) {
				i++;
				continue;
			}
			if(!filaActual.verificarDato(datos[i])) {
				return false;
			}
			i++;
		}
		return true;
	}
	
	/**
	 * Se encarga de crear el mensaje de la estructura actual cuando ya la tabla está lista y no se le agregarán más filas.
	 */
	public void crearMensajeDeLaEstructura() {
		String mensaje = "";
		for(String llave: nombresFilas){
			mensaje += filas.get(llave).toString()+"-";
		}
		estructuraDeLaTablaMSG = mensaje.substring(0,mensaje.length()-1); 
	}
	
	public String registrosToString() {
		String registros = "";
		for(int i = 0; i<cantRegistros; i++) {	
			for(String llave : nombresFilas) {
				registros += filas.get(llave).registroPorColumnaToString(i)+", ";
			}
			registros = registros.substring(0,registros.length()-2)+"\n";
		}
		return registros;
	}
	
	public void disminuirRegistros() {
		cantRegistros--;
	}
	
	/**
	 * Inserta la fila indicada por parámetro.
	 * @param fila, la fila a insertar.
	 */
	public <T> void annadirFila(Fila<T> fila) {
		filas.put(fila.getNombre(), fila);
		nombresFilas.add(fila.getNombre());
	}
	
	/**
	 * Retorna la estructura de la tabla en forma de string para facilitar su lectura.
	 * @return String, la estructura de la tabla.
	 */
	public String estrucutraDeLaTabla() {
		return estructuraDeLaTablaMSG;
	}

	public void ordenar(String campo, String ordemiento) {
		filas.get(campo).ordenar(ordemiento);
	}

}
