/**
 * Librera para el maneja de intefaz
 * 
 * @author Abraham Meza y Tribeth Rivas
 * @version 08/09/18
 */

package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;


@SuppressWarnings("serial")
public abstract class UiFrame extends JFrame{
	protected String returnText;
	
  public UiFrame(String pTitulo, int ancho, int alto) {
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	int x = (int)screenSize.getWidth()/2-ancho/2;
	int y = (int)screenSize.getHeight()/2-alto/2;
	setLayout(null);
    setBounds(x,y,ancho,alto);
  	setTitle(pTitulo);
  	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  	setResizable(false);
  	setVisible(true);
  	repaint();
  }
  
	/**
	 * Crea un label
	 * @param texto que mostrar el label
	 * @param coordenada x donde se posicionar el label
	 * @param coordenada y donde se posicionar el label 
	 * @param ancho del label
	 * @param alto del label
	 * @param tamao de la fuente del label
	 * @return label creado
	 */
  public JLabel crearLabel(String texto,int x, int y, int ancho, int alto,int tamannoFuente) {
    JLabel label = new JLabel(texto);
    label.setBounds(x,y,ancho,alto);
    label.setFont(new Font(null,Font.PLAIN,tamannoFuente));
    add(label);
    return label;
  }
	
  /**
   * Crea un cuadro de texto
   * @param coordena x donde se ubicar el cuadro de texto
   * @param coordena y donde se ubicar el cuadro de texto
   * @param ancho del cuadro de texto
   * @param alto del cuadro de texto
   * @param tamao de la fuente del cuadro de texto
   * @return cuadro de texto creado
   */
  public JTextField crearTextField(int x, int y, int ancho, int alto,int tamannoFuente) {
  	JTextField textField = new JTextField();
  	textField.setBounds(x,y,ancho,alto);
  	textField.setFont(new Font(null,Font.PLAIN,tamannoFuente));
  	add(textField);
  	return textField;
  }
  
  /**
   * Crea un boton
   * @param texto del boton
   * @param coordena x donde se ubicar el boton
   * @param coordena y donde se ubicar el boton
   * @param ancho del boton
   * @param alto del boton
   * @param tamao de la fuente del boton
   * @return boton creado
   */
  public JButton crearBoton(String texto, int x, int y, int ancho, int alto, int tamannoFuente) {
    JButton button = new JButton(texto);
    button.setBounds(x, y, ancho, alto);
    button.setFont(new Font(null,Font.PLAIN,tamannoFuente));
    add(button);
    return button;
  }
  
  /**
   * Crea un cuadro de seleccion multiple
   * @param pregunta del cuadro
   * @param opciones a elegir
   * @param True si el cuadro a tener multiples opciones o False si es solo un objeto de seleccin
   * @return lista de opciones
   */
  public JList<String> crearMultipleSeleccion(String pregunta,String[] opciones, boolean seleccionMultiple) {
		JList<String> lista = new JList<String>(opciones);
		lista.setSelectionMode(seleccionMultiple?ListSelectionModel.MULTIPLE_INTERVAL_SELECTION: ListSelectionModel.SINGLE_SELECTION);
		lista.setBounds(400,450,200,200);
		lista.setVisible(true);
		JOptionPane.showMessageDialog(this, new JScrollPane(lista), pregunta, 3);
		return lista;
  }
  /* Ejemplo de codigo para crear una múltiple seleccion
   * private void seleccionarAlgo() {
		seleccionador = crearMultipleSeleccion("Seleccione algo", Arreglo de string con las opciones, true);
		if(seleccionador.getSelectedIndex() != -1) {
		// Se puede seleccionar basado en los indices 
		}else {
		// El usuario no escogió nada
		}
		repaint();
	}
   */
  
  /**
   * Crea un listado
   * @param pregunta del listado
   * @param coordena x donde se ubicar el listado
   * @param coordena y donde se ubicar el listado
   * @param ancho del listado
   * @param alto del listado
   * @param tamao de la fuente del listado
   * @return Area de texto creado
   */
  public JTextArea crearListado(String pregunta,int x,int y,int ancho,int alto, int tamannoFuente) {
		JTextArea area = new JTextArea(pregunta);
		JScrollPane pane = new JScrollPane(area);
		pane.setBounds(x, y, ancho-10, alto-35);
		area.setFont(new Font(null,Font.PLAIN, tamannoFuente));
		add(pane);
		pane.repaint();
		area.repaint();
		return area;
  }
  
	public String getReturnText() {
		return returnText;
	}

	public void setReturnText(String pReturnText) {
		returnText = pReturnText;
	}
	
	public JCheckBox crearBoolCheck(String texto,int x, int y, int ancho, int alto, int tamannoFuente) {
		JCheckBox boolCheck = new JCheckBox(texto);
		boolCheck.setBounds(x,y, ancho, alto);
		boolCheck.setFont(new Font(null,Font.PLAIN, tamannoFuente));
		add(boolCheck);
		return boolCheck;
	}
	
	/**
	 * Crea un campo para la contraseña
	 * @param coordena x donde se ubicar el cuadro
   * @param coordena y donde se ubicar el cuadro
   * @param ancho del cuadro
   * @param alto del cuadro
   * @param tamao de la fuente del cuadro
	 * @return cuadro de texto creado
	 */
	public JPasswordField crearPasswordField(int x, int y, int ancho, int alto,int tamannoFuente) {
		JPasswordField textField = new JPasswordField();
		textField.setBounds(x,y,ancho,alto);
		textField.setFont(new Font(null,Font.PLAIN,tamannoFuente));
		add(textField);
	    return textField;
	  }
	
	public void setError(String textoError, int x, int y, int ancho, int alto) {
		JLabel error = crearLabel(textoError, x, y, ancho, alto, 14);
		error.setForeground(Color.red);
	    error.setText(textoError);
	 }
  
}
