package datos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class Fila<T extends Comparable> implements Serializable{
	private static final long serialVersionUID = 4575713096565059784L;
	private String nombre;
	private String tipo;
	private ArrayList<T> datos;
	private boolean requerido;
	
	public Fila(String nombre, String tipo, boolean requerido) {
		this.nombre = nombre;
		this.tipo = tipo;
		this.requerido = requerido;
		datos = new ArrayList<T>();
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public String getTipo() {
		return tipo;
	}
	
	public ArrayList<T> getDatos() {
		return datos;
	}
	
	public boolean isRequerido() {
		return requerido;
	}
	
	public void addDato(T dato) {
		datos.add(dato);
	}

	public boolean verificarDato(String dato) {
		try {
			if(tipo.equals("String")) {
			}else if(tipo.equals("Int")) {
				Integer.valueOf(dato);
			}else if (tipo.equals("Float")) {
				Float.valueOf(dato);
			}else {
				Boolean.valueOf(dato);
			}
			return true;
		}catch(Exception e) {
			return false;
		}
	}
	
	public String registroPorColumnaToString(int numeroDeColumna) {
		T dato = null;
		if(numeroDeColumna < datos.size()) {
			dato = datos.get(numeroDeColumna);
		}
		if(dato != null) {
			if(dato.getClass() == String.class) {
				return (String)dato;
			}
			return String.valueOf(dato);
		}
		return "";
	}
	
	public String toString() {
		return "["+nombre+","+tipo+","+(requerido?"Si":"No")+"]";
	}
	
	public void ordenar(String ordemiento) {
		if(ordemiento.equals("Ascendente")) {
			Collections.sort(datos);
		}else {
			Collections.sort(datos, Collections.reverseOrder());
		}
	}
}
