/**
 * La clase base de datos permite crear diferentes tipos de tablas para almacenar información que se relaciona entre sí 
 * @author Abraham Meza Vega, Giancarlos Fonseca Esquivel, Tribeth Rivas Pérez
 * @version (1.0 - 25/11/18)
 */
package datos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

@SuppressWarnings("serial")
public class BaseDeDatos implements Serializable{
	private static final long serialVersionUID = 7950153768127472605L;
	private String nombre;
	private HashMap<String,Tabla> tablas;
	private ArrayList<String> nombresTablas;
	
	/**
	 * Constructor que se encarga de inicializar las variables para las tablas y para sus nombres.
	 * @param nombre, en este parámetro se indica el nombre que va tomar la base de datos con el cual se identificará en el datacenter.
	 */
	public BaseDeDatos(String nombre){
		this.nombre = nombre;
		tablas = new HashMap<String,Tabla>();
		nombresTablas = new ArrayList<String>();
	}

	public ArrayList<String> getNombresTablas() {
		return nombresTablas;
	}
	
	public HashMap<String, Tabla> getTablas(){
		return tablas;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public boolean cambiarNombreTabla(String nombreActual, String nombreNuevo) {
		if(tablas.containsKey(nombreActual)) {
			Tabla actual = tablas.get(nombreActual);
			actual.setNombre(nombreNuevo);
			tablas.put(nombreNuevo, tablas.remove(nombreActual));
			nombresTablas.remove(nombreActual);
			nombresTablas.add(nombreNuevo);
			return true;
		}
		return false;
	}
	
	public Tabla getTabla(String nombre) {
		return tablas.get(nombre);
	}
	/**
	 * Se encarga de recibir una tabla y verificar que esta no esté en la base de datos, en caso de existir simplemente no la agrega.
	 * @param tabla, esta va a ser la tabla que se intentará añadir.
	 */
	public void annadirTabla(Tabla tabla) {
		if(!tablas.containsKey(tabla.getNombre())) {
			tablas.put(tabla.getNombre(), tabla);
			nombresTablas.add(tabla.getNombre());
		}
	}
	
	public boolean eliminarTabla(String nombreTabla) {
		if(tablas.containsKey(nombreTabla)) {
			tablas.remove(nombreTabla);
			return true;
		}else {
			return false;
		}
	}
	
	public void insertarRegistro(String nombreTabla, Tabla tabla) {
		tablas.replace(nombreTabla, tabla);
	}

	public void ordenar(String tabla, String campo, String ordemiento) {
		tablas.get(tabla).ordenar(campo, ordemiento);
	}
	
}
