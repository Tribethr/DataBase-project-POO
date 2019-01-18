/**
 * La clase Cliente se dedica a hacer las consultas necesarias al servidor.
 * @author Abraham Meza Vega, Giancarlos Fonseca Esquivel, Tribeth Rivas Pérez
 * @version (1.0 - 25/11/18)
 */
package aplicacion;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class LogInServidor {
	
	private ArrayList<String> usuarios;
	
	/**
	 * carga la lista de usuarios de la base de datos
	 */
	@SuppressWarnings("unchecked")
	public LogInServidor() {
		try {
			FileInputStream archivo = new FileInputStream("Users.kiwi");
			ObjectInputStream objeto = new ObjectInputStream(archivo);
			usuarios = (ArrayList<String>) objeto.readObject();
			objeto.close();
		}catch(Exception e) {
			usuarios = new ArrayList<String>();
		}
	}
	
	/**
	 * revisa si un usuario está registrado
	 * @param nombre
	 * @param contrasenia
	 * @return true en caso de estar registrado
	 */
	public boolean estaRegistrado(String nombre, String contrasenia) {
		Vigenere cifrador = new Vigenere(nombre);
		nombre = cifrador.cifrar();
		cifrador.setFrase(contrasenia);
		contrasenia = cifrador.cifrar();
		String datos = nombre + ";" + contrasenia;
		for(String usuario : usuarios) {
			if(usuario.equals(datos)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * registra un usuario en la base de datos y lo guarda
	 * @param nombre
	 * @param contrasenia
	 * @throws IOException
	 */
	public void registarUsuario(String nombre, String contrasenia) throws IOException {
		aniadirUsuario(nombre, contrasenia);
		FileOutputStream archivo = new FileOutputStream("Users.kiwi");
		ObjectOutputStream objeto = new ObjectOutputStream(archivo);
		objeto.writeObject(usuarios);
		objeto.close();
	}
	
	/**
	 * registra un usuario
	 * @param nombre
	 * @param contrasenia
	 */
	private void aniadirUsuario(String nombre, String contrasenia) {
		Vigenere cifrador = new Vigenere(nombre);
		nombre = cifrador.cifrar();
		cifrador.setFrase(contrasenia);
		contrasenia = cifrador.cifrar();
		usuarios.add(nombre + ";" + contrasenia);
	}
	
}
