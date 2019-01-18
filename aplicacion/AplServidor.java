package aplicacion;

import java.io.IOException;

import server.Servidor;

public class AplServidor {

	public static void main(String[] args) {
		System.out.println("Servidor iniciado.");
		Servidor server = Servidor.getInstance();
		try {
			server.esperarConexion();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
	
}
