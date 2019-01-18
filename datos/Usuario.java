package datos;

import java.io.Serializable;
import java.util.ArrayList;

public class Usuario implements Serializable {

	private static final long serialVersionUID = 1L;
	private String nombre;
	private String contrasenna;
	private String[] basesDeDatos;
	
	public Usuario() {
		basesDeDatos = new String[1];
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getContrasenna() {
		return contrasenna;
	}
	public void setContrasenna(String contrasenna) {
		this.contrasenna = contrasenna;
	}
	public String[] getBasesDeDatos() {
		return basesDeDatos;
	}
	public void setBasesDeDatos(String[] basesDeDatos) {
		this.basesDeDatos = basesDeDatos;
	}
	
	public boolean equals(Usuario usuario) {
		if(nombre.equals(usuario.getNombre()) && contrasenna.equals(usuario.getContrasenna())) {
			return true;
		}
		return false;
	}
	
}
