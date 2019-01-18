package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import datos.Tabla;


public class Cliente {
	
	Socket socket;
	private ObjectInputStream in;
    private ObjectOutputStream out;
	
	public Boolean accesar(String nombre, String contrasenia) throws ClassNotFoundException, IOException {
		conectar();
		out.writeObject("Acceso");
		out.writeObject(nombre + ";" + contrasenia);
		return (Boolean) in.readObject();
	}
	
	public Boolean esAdmin() throws UnknownHostException, IOException, ClassNotFoundException {
		conectar();
		out.writeObject("Admin");
		return (Boolean)in.readObject();
	}
	
	public Boolean crearBaseDatos(String nombre) throws ClassNotFoundException, IOException {
		conectar();
		out.writeObject("CrearBase");
		out.writeObject(nombre);
		return (Boolean)in.readObject();
	}
	
	public Boolean crearTabla(Tabla tabla, String nombreBase) throws ClassNotFoundException, IOException {
		conectar();
		out.writeObject("CrearTabla");
		out.writeObject(nombreBase);
		out.writeObject(tabla);
		return (Boolean)in.readObject();
	}
	
	public ArrayList<String> getBasesDeDatos() throws UnknownHostException, IOException, ClassNotFoundException{
		conectar();
		out.writeObject("NombresBases");
		return (ArrayList<String>)in.readObject();
	}
	
	public Boolean eliminarTabla(String nombreBase, String nombreTabla) throws IOException, ClassNotFoundException {
		conectar();
		out.writeObject("EliminarTabla");
		out.writeObject(nombreBase);
		out.writeObject(nombreTabla);
		return (Boolean)in.readObject();
	}
	
	/**
	 * Envia la informacin necesaria para registrar un usuario en la base de datos.
	 * @param nombre
	 * @param contrasenna
	 * @param bases, las bases de datos a las cuales tendr acceso el usuario
	 */
	public void registrarUsuario(String nombre, String contrasenna, String bases) {
		try {
			out.writeObject("registrarUsuario");
			out.writeObject(nombre+";"+contrasenna);
			out.writeObject(bases);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Elimina un usuario de la base de datos
	 * @param nombre
	 */
	public void eliminarUsuario(String nombre) {
		try {
			out.writeObject("eliminarUsuario");
			out.writeObject(nombre);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Consigue los nombres de los usuarios
	 * @return String[] con  los nombres de los usuarios
	 */
	public String[] getNombresUsuarios() {
		try {
			out.writeObject("getNombreUsuarios");
			return (String[])in.readObject();
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private void conectar() throws UnknownHostException, IOException {
		socket = new Socket("", 8086);
		in = new ObjectInputStream(socket.getInputStream());
	    out = new ObjectOutputStream(socket.getOutputStream());
	}
	
	public ArrayList<String> getNombreTablas(String nombreBase) throws UnknownHostException, IOException, ClassNotFoundException{
		conectar();
		out.writeObject("getTablas");
		out.writeObject(nombreBase);
		return (ArrayList<String>) in.readObject();
	}
	
	public Boolean cambiarNombreTabla(String baseDatos, String tabla, String nuevoNombre) throws IOException, ClassNotFoundException {
		conectar();
		out.writeObject("CambiarNombreTabla");
		out.writeObject(baseDatos);
		out.writeObject(tabla);
		out.writeObject(nuevoNombre);
		return (Boolean)in.readObject();
	}
	
	public Tabla getTabla(String baseDatos, String tabla) throws IOException, ClassNotFoundException {
		conectar();
		out.writeObject("GetTabla");
		out.writeObject(baseDatos);
		out.writeObject(tabla);
		return (Tabla)in.readObject();
	}
	
	public void inserRegistro(String baseDatos, Tabla tabla) throws IOException {
		conectar();
		out.writeObject("InsertarRegistro");
		out.writeObject(baseDatos);
		out.writeObject(tabla);
	}
	
	public void eliminarRegistros(String baseDeDatos,String tabla, ArrayList<Integer> posiciones) throws UnknownHostException, IOException {
		conectar();
		out.writeObject("eliminarRegistros");
		out.writeObject(baseDeDatos);
		out.writeObject(tabla);
		out.writeObject(posiciones);
	}

	public String reporte(String baseDatos) throws IOException, ClassNotFoundException {
		conectar();
		out.writeObject("Reporte");
		out.writeObject(baseDatos);
		return (String)in.readObject();
	}

	public void ordenar(String baseDatos, String tabla, String campo, String ordemiento) throws IOException {
		conectar();
		out.writeObject("Ordenar");
		out.writeObject(baseDatos);
		out.writeObject(tabla);
		out.writeObject(campo);
		out.writeObject(ordemiento);
	}
	
}
