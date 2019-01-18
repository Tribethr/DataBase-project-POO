/**
 * La calse datacenter permite tener una estructura que soporta crear y acceder a mltiples bases de datos
 * @author Abraham Meza Vega, Giancarlos Fonseca Esquivel, Tribeth Rivas Prez
 * @version (1.0 - 25/11/18)
 */
package datos;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class DataCenter {
	
	private HashMap<String, BaseDeDatos> basesDeDatos;
	
	public DataCenter() {
		basesDeDatos = new HashMap<String,BaseDeDatos>();
		cargar();
		for(String base: basesDeDatos.keySet()) {
			System.out.println(base);
		}
	}
	
	/**
	 * Intenta crear una base de datos, si esta existe, avisa que ya existe una, de lo contrario la crea.
	 * @param nombre, Nombre de la base de datos a crear.
	 * @return true en caso de que pudo crear la base de datos, false de lo contrario.
	 */
	public boolean crearBaseDeDatos(String nombre) {
		if(basesDeDatos.containsKey(nombre)) {
			return false;
		}
		basesDeDatos.put(nombre, new BaseDeDatos(nombre));
		return true;
	}
	
	/**
	 * Guarda los datos de las bases de datos en un archivo llamado DataCenter.kiwi
	 */
	public void guardar() {
		try {
			FileOutputStream archivo = new FileOutputStream("DataCenter.kiwi");
			ObjectOutputStream objeto = new ObjectOutputStream(archivo);
			ArrayList<BaseDeDatos> bases = new ArrayList<BaseDeDatos>();
			for(BaseDeDatos base : basesDeDatos.values()) {
				bases.add(base);
			}
			objeto.writeObject(bases);
			objeto.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Carga los datos de las bases de datos en un archivo llamado DataCenter.kiwi
	 */
	@SuppressWarnings("unchecked")
	public void cargar() {
		try {
			FileInputStream archivo = new FileInputStream("DataCenter.kiwi");
			ObjectInputStream objeto = new ObjectInputStream(archivo);
			ArrayList<BaseDeDatos> bases = (ArrayList<BaseDeDatos>) objeto.readObject();
			for(BaseDeDatos base : bases) {
				basesDeDatos.put(base.getNombre(),base);
			}
			objeto.close();
		}catch(Exception e) {
			e.printStackTrace();
			basesDeDatos = new HashMap<String,BaseDeDatos>();
		}
	}
	
	/**
	 * aade una tabla a una base de datos
	 * @param nombreBase
	 * @param tabla
	 * @return
	 */
	public boolean annadirTabla(String nombreBase, Tabla tabla) {
		if(basesDeDatos.containsKey(nombreBase)) {
			tabla.crearMensajeDeLaEstructura();
			basesDeDatos.get(nombreBase).annadirTabla(tabla);
			return true;
		}
		return false;
	}
	
	public HashMap<String, BaseDeDatos> getBasesDeDatos(){
		return basesDeDatos;
	}
	
	/**
	 * Intenta agregar una base de datos, si esta existe, avisa que ya existe una, de lo contrario la agrega.
	 * @param baseDeDatos, la base de datos a agregar.
	 * @return true en caso de que pudo agregar la base de datos, false de lo contrario.
	 */
	public boolean agregarBaseDeDatos(BaseDeDatos baseDeDatos) {
		if(basesDeDatos.containsKey(baseDeDatos.getNombre())) {
			return false;
		}
		basesDeDatos.put(baseDeDatos.getNombre(), baseDeDatos);
		return true;
	}
	
	public ArrayList<String> getNombresBasesDatos(){
		ArrayList<String> nombres = new ArrayList<String>();
		for (String base: basesDeDatos.keySet()) {
			nombres.add(base);
		}
		return nombres;
	}
	
	public boolean eliminarTabla(String nombreBase, String nombreTabla) {
		boolean eliminada = basesDeDatos.get(nombreBase).eliminarTabla(nombreTabla);
		if(eliminada) {
			basesDeDatos.get(nombreBase).getNombresTablas().remove(nombreTabla);
		}
		return eliminada;
	}
	
	public boolean cambiarNombreTabla(String nombreBase, String nombreTabla, String nombreNuevo) {
		return basesDeDatos.get(nombreBase).cambiarNombreTabla(nombreTabla, nombreNuevo);
	}
	
	public void insertarRegistro(String base, String nombreTabla, Tabla tabla) {
		basesDeDatos.get(base).insertarRegistro(nombreTabla, tabla);
	}
	
	public void ordenar(String baseDatos, String tabla, String campo, String ordemiento) {
		basesDeDatos.get(baseDatos).ordenar(tabla, campo, ordemiento);
	}

}

