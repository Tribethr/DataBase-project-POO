package datos;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class Reporte {

	private BaseDeDatos base;
	
	public Reporte(BaseDeDatos base) {
		this.base = base;
	}
	
	public String generar() {
		String reporte;
		reporte = "<!DOCTYPE html>\n";
		reporte += generarHTML();
		return reporte;
	}
	
	private String generarHTML() {
		String reporte;
		reporte = "<html>\n";
		reporte += generarHeader();
		reporte += "\t<body>\n";
		reporte += "\t\t<h1>Instituto Tecnol&oacute;gico de Costa Rica</h1>\n";
		reporte += "\t\t<h2>Tarea Programada 3</h2>\n";
		reporte += "\t\t<h2 class = 'cursiva'>Reporte de tablas</h2>\n";
		reporte += "\t\t<div class = 'right tamano'><strong>Nombre de la BD: </strong>" + base.getNombre() + "</div>\n";
		Collection<Tabla> tablas = base.getTablas().values();
		for(Tabla tabla: tablas) {
			reporte += "\t\t<div class = 'left tamano'><strong>Nombre de la tabla: </strong>" + tabla.getNombre() + "</div>\n";
			reporte += "\t\t<div class = 'left tamano'><strong>Estructura:</strong></div>\n";
			reporte += "\t\t<table>\n";
			reporte += "\t\t\t<tr>\n";
			reporte += "\t\t\t\t<td>\n";
			reporte += "\t\t\t\t\t<strong class = 'cursiva tamano'>Nombre del Campo</strong>\n";
			reporte += "\t\t\t\t</td>\n";
			reporte += "\t\t\t\t<td>\n";
			reporte += "\t\t\t\t\t<strong class = 'cursiva tamano'>Tipo de Dato</strong>\n";
			reporte += "\t\t\t\t</td>\n";
			reporte += "\t\t\t\t<td>\n";
			reporte += "\t\t\t\t\t<strong class = 'cursiva tamano'>Requirido</strong>";
			reporte += "\t\t\t\t</td>\n";
			reporte += "\t\t\t</tr>\n";
			ArrayList<String> nombreFilas = tabla.getNombresFilas();
			HashMap<String, Fila> filas = tabla.getFilas();
			for(int i = 0; i < filas.size(); i++) {
				String nombre = nombreFilas.get(i);
				Fila fila = filas.get(nombre);
				reporte += "\t\t\t<tr>\n";
				reporte += "\t\t\t\t<td class = 'tamano'>\n";
				reporte += "\t\t\t\t\t" + nombre + "\n";
				reporte += "\t\t\t\t</td>\n";
				reporte += "\t\t\t\t<td class = 'tamano'>\n";
				reporte += "\t\t\t\t\t" + fila.getTipo() + "\n";
				reporte += "\t\t\t\t</td>\n";
				reporte += "\t\t\t\t<td class = 'tamano'>\n";
				reporte += "\t\t\t\t\t" + (fila.isRequerido() ? "Si" : "No") + "\n";
				reporte += "\t\t\t\t</td>\n";
				reporte += "\t\t\t</tr>\n";
			}
			reporte += "\t\t\t</table>\n";
			reporte += "\t\t\t<div class = 'left tamano'><strong>Datos:</strong></div>\n";
			reporte += "\t\t\t<table>\n";
			reporte += "\t\t\t<tr>\n";
			for(int i = 0; i < filas.size(); i++) {
				String nombre = nombreFilas.get(i);
				reporte += "\t\t\t\t<td class = 'tamano'>\n";
				reporte += "\t\t\t\t\t" + nombre + "\n";
				reporte += "\t\t\t\t</td>\n";
			}
			reporte += "\t\t\t</tr>\n";
			for(int i = 0; i < filas.size(); i++) {
				String nombre = nombreFilas.get(i);
				Fila fila = filas.get(nombre);
				ArrayList datos = fila.getDatos();
				reporte += "\t\t\t<tr>\n";
				for (int j = 0; j < datos.size(); j++) {
					reporte += "\t\t\t\t<td class = 'tamano'>\n";
					reporte += "\t\t\t\t\t" + datos.get(j) + "\n";
					reporte += "\t\t\t\t</td>\n";
				}
				reporte += "\t\t\t</tr>\n";
			}
			reporte += "\t\t</table>\n";
		}
		reporte += "\t\t<div class = 'right tamano'><strong>Creado por:</strong></div>\n";
		reporte += "\t\t<div class = 'right tamano'><strong>Giancarlos Fonseca</strong></div>\n";
		reporte += "\t\t<div class = 'right tamano'><strong>Abraham Meza</strong></div>\n";
		reporte += "\t\t<div class = 'right tamano'><strong>Tribeth Rivas</strong></div>\n";
		reporte += "\t</body>\n";
		reporte += "</html>";
		return reporte;
	}
	
	private String generarHeader() {
		String reporte;
		reporte = "\t<header>\n";
		reporte += "\t\t<title>Reporte</title>\n";
		reporte += "\t\t<meta charset = \"UTF-8\">\n";
		reporte += generarStyleSheet();
		reporte += "\t</header>\n";
		return reporte;
	}
	
	private String generarStyleSheet() {
		String reporte;
		reporte = "\t\t<style>\n";
		reporte += generarCSS();
		reporte += "\t\t</style>\n";
		return reporte;
	}
	
	private String generarCSS() {
		String reporte;
		reporte = generarH1H2();
		reporte += generarCursiva();
		reporte += generarTamano();
		reporte += generarRight();
		reporte += generarLeft();
		reporte += generarTable();
		reporte += "\t\t\ttable, td{\n";
		reporte += "\t\t\t\tborder-collapse: collapse;\n";
		reporte += "\t\t\t\tborder: solid 1px #000;\n";
		reporte += "\t\t\t}\n";
		return reporte;
	}
	
	private String generarH1H2() {
		String reporte;
		reporte = "\t\t\th1, h2{\n";
		reporte += "\t\t\t\ttext-align: center;\n";
		reporte += "\t\t\t}\n";
		return reporte;
	}
	
	private String generarCursiva() {
		String reporte;
		reporte = "\t\t\t.cursiva{\n";
		reporte += "\t\t\t\tfont-style: italic\n";
		reporte += "\t\t\t}\n";
		return reporte;
	}
	
	private String generarTamano() {
		String reporte;
		reporte = "\t\t\t.tamano{\n";
		reporte += "\t\t\t\tfont-size: 22px;\n";
		reporte += "\t\t\t}\n";
		return reporte;
	}
	
	private String generarRight() {
		String reporte;
		reporte = "\t\t\t.right{\n";
		reporte += "\t\t\t\ttext-align: right;\n";
		reporte += "\t\t\t}\n";
		return reporte;
	}
	
	private String generarLeft() {
		String reporte;
		reporte = "\t\t\t.left{\n";
		reporte += "\t\t\t\ttext-align: left;\n";
		reporte += "\t\t\t}\n";
		return reporte;
	}
	
	private String generarTable() {
		String reporte;
		reporte = "\t\t\ttable{\n";
		reporte += "\t\t\t\twidth: 90%;\n";
		reporte += "\t\t\t\tmargin: 5px 20px;\n";
		reporte += "\t\t\t}\n";
		return reporte;
	}
	
}
