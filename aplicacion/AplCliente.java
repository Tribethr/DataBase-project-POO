package aplicacion;

import java.io.IOException;

import javax.swing.JOptionPane;

import server.Cliente;
import ui.AdminUI;
import ui.UiIngresar;
import ui.UsuarioUI;

public abstract class AplCliente {
	
	private static void esperarBoton(UiIngresar logIn) throws InterruptedException {
		while(!logIn.botonPresionado()) {
			Thread.sleep(100);
		}
	}
	
	private static Boolean puedeIngresar(UiIngresar logIn, Cliente cliente) throws ClassNotFoundException, IOException, InterruptedException {
		esperarBoton(logIn);
		return cliente.accesar(logIn.getTextoUsuario(), logIn.getTextoContrasenia());
	}
	
	public static void main(String[] args) {
		UiIngresar logIn = new UiIngresar();
		try {
			Cliente cliente = new Cliente();
			while(!puedeIngresar(logIn, cliente)) {				
				logIn.setError("Usuario o contrase�a no v�lidos.");
			}
			logIn.cerrar();
			if(cliente.esAdmin()) {
				new AdminUI(cliente);
			}else {
				new UsuarioUI(cliente);
			}
		} catch (NullPointerException e) {
			JOptionPane.showMessageDialog(null, e.getClass(), "Error", JOptionPane.ERROR_MESSAGE);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
}
