/**
 * La clase Cliente se dedica a hacer las consultas necesarias al servidor.
 * @author Abraham Meza Vega, Giancarlos Fonseca Esquivel, Tribeth Rivas Prez
 * @version (1.0 - 25/11/18)
 */
package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import aplicacion.LogInServidor;
import datos.DataCenter;
import datos.Tabla;

public class Servidor{
	
	private static Servidor server = new Servidor();
	private static final int PUERTO = 8086;
	private ServerSocket listener;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private Boolean esAdmin = false;
	private DataCenter dataCenter;

	private Servidor() {
		dataCenter = new DataCenter();
	}
	
	public static Servidor getInstance() {
		return server;
	}
	
	/**
	 * Espera por la conexion de un cliente, filtra lo que el cliente quiere hacer y lo hace.
	 * @throws IOException
	 * @throws ClassNotFoundException 
	 */
	public void esperarConexion() throws IOException, ClassNotFoundException {
		listener = new ServerSocket(PUERTO);
		while(true) {
			System.out.println("Esperando conexin...");
			Socket socket = listener.accept();
			System.out.println("Se conecto: " + socket.getInetAddress());
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
		    String entrada = (String)in.readObject();
		    if(entrada.equals("Acceso")) {
		    	System.out.println("Se solicit acceso al sistema.");
		    	entrada = (String)in.readObject();
		    	boolean puedeEntrar = validarAcceso(entrada);
		    	out.writeObject(puedeEntrar);
		    	if(puedeEntrar) {
		    		System.out.println("Se ha consedido en acceso.");
		    	}else {
		    		System.out.println("Se ha denegado al acceso.");
		    	}
		    }else if(entrada.equals("Admin")) {
		    	out.writeObject(esAdmin);
		    }else if(entrada.equals("CrearBase")) {
		    	entrada = (String)in.readObject();
		    	Boolean existe = dataCenter.crearBaseDeDatos(entrada);
		    	out.writeObject(existe);
		    	dataCenter.guardar();
		    }else if(entrada.equals("CrearTabla")) {
		    	try {
		    		entrada = (String)in.readObject();
					Tabla tabla = (Tabla)in.readObject();
					dataCenter.annadirTabla(entrada, tabla);
					dataCenter.guardar();
					out.writeObject(true);
				} catch (ClassNotFoundException e) {
					out.writeObject(false);
				}
		    }else if (entrada.equals("NombresBases")) {
		    	out.writeObject(dataCenter.getNombresBasesDatos());
		    }else if(entrada.equals("EliminarTabla")) {
		    	String nombreBase = (String)in.readObject();
		    	entrada = (String)in.readObject();
		    	out.writeObject(eliminarTabla(nombreBase, entrada));
		    	dataCenter.guardar();
		    }else if(entrada.equals("getTablas")) {
		    	entrada = (String) in.readObject();
		    	out.writeObject(getNombreTablas(entrada));
		    }else if(entrada.equals("CambiarNombreTabla")) {
		    	String baseDatos = (String)in.readObject();
		    	String tabla = (String)in.readObject();
		    	entrada = (String)in.readObject();
		    	out.writeObject(cambiarNombreTabla(baseDatos, tabla, entrada));
		    	dataCenter.guardar();
		    }else if(entrada.equals("GetTabla")) {
		    	String baseDatos = (String)in.readObject();
		    	entrada = (String)in.readObject();
		    	out.writeObject(getTabla(baseDatos, entrada));
		    }else if(entrada.equals("InsertarRegistro")) {
		    	entrada = (String)in.readObject();
		    	Tabla tabla = (Tabla)in.readObject();
		    	insertarRegistro(entrada, tabla);
		    	dataCenter.guardar();
		    }else if(entrada.equals("eliminarRegistros")){
		    	eliminarRegistros((String)in.readObject(),(String)in.readObject() , (ArrayList<Integer>)in.readObject());
		    	dataCenter.guardar();
		    }else if(entrada.equals("Reporte")) {
		    	entrada = (String)in.readObject();
		    	System.out.println(entrada);
		    	out.writeObject(new Reporte(dataCenter.getBasesDeDatos().get(entrada)).generar());
		    }else if(entrada.equals("Ordenar")) {
		    	String nombreBase = (String)in.readObject();
		    	String nombreTabla = (String)in.readObject();
		    	String nombreCampo = (String)in.readObject();
		    	entrada = (String)in.readObject();
		    	ordenar(nombreBase, nombreTabla, nombreCampo, entrada);
		    	dataCenter.guardar();
		    }
		}
	}
	
	/**
	 * revisa si el cliente que quiere conectarse tiene o no acceso
	 * @param datos
	 * @return
	 */
	private Boolean validarAcceso(String datos) {
		LogInServidor logIn = new LogInServidor();
		String[] info = datos.split(";");
		Boolean puedeIngresar = logIn.estaRegistrado(info[0], info[1]);
		if(puedeIngresar && info[0].equals("Administrador")) {
			esAdmin = true;
		}else {
			esAdmin = false;
		}
		return puedeIngresar;
	}
	
	public void eliminarRegistros(String baseDeDatos, String tabla, ArrayList<Integer> posiciones) {
		Tabla tablaActual = dataCenter.getBasesDeDatos().get(baseDeDatos).getTabla(tabla);
		for(String campo: tablaActual.getNombresFilas()) {
			System.out.println(campo);
			for(int i = posiciones.size()-1; i>-1 ; i--) {
				tablaActual.getFilas().get(campo).getDatos().remove((int)posiciones.get(i));
				tablaActual.disminuirRegistros();
			}
		}
		for(int i = 0; i<posiciones.size(); i++) {
			tablaActual.disminuirRegistros();
		}
	}
	
	public Boolean eliminarTabla(String nombreBase, String nombreTabla) {
		return dataCenter.eliminarTabla(nombreBase, nombreTabla);
	}
	
	public ArrayList<String> getNombreTablas(String nombreBase){
		return dataCenter.getBasesDeDatos().get(nombreBase).getNombresTablas();
	}
	
	public Boolean cambiarNombreTabla(String baseDatos, String tabla, String nombreNuevo) {
		return dataCenter.cambiarNombreTabla(baseDatos, tabla, nombreNuevo);
	}
	
	public Tabla getTabla(String baseDatos, String tabla) {
		return dataCenter.getBasesDeDatos().get(baseDatos).getTabla(tabla);
	}
	
	public void insertarRegistro(String baseDatos, Tabla tabla) {
		dataCenter.insertarRegistro(baseDatos, tabla.getNombre(), tabla);
	}

	public void ordenar(String baseDatos, String tabla, String campo, String ordemiento) {
		dataCenter.ordenar(baseDatos, tabla, campo, ordemiento);
	}
	
}

