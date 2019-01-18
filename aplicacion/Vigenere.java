/**
 * La clase Vigenere se dedica a cifrar y descifrar datos.
 * @author Abraham Meza Vega, Giancarlos Fonseca Esquivel, Tribeth Rivas Pérez
 * @version (1.0 - 25/11/18)
 */
package aplicacion;

import java.lang.StringBuilder;

public class Vigenere {

  private String frase;
  private static final String codigo = "66";
	
  /**
   * Constructor
   * @param frase, frase a cifrar o descifrar
   */
  public Vigenere(String frase) {
    this.frase = frase;
  }

  /**
   * cifra la frase actual y la retorna
   * @return la frase ya cifrada
   */
  public String cifrar() {
	String codigoR;
	String fraseFinal = "";
	for(int i = 0; i < frase.length(); i++) {
      codigoR = new StringBuilder(codigo).reverse().toString();
      int valorASCII = frase.charAt(i);
      valorASCII += (codigoR.charAt(0) - '0');
      fraseFinal += (char)valorASCII;
	}
	frase = fraseFinal;
    return fraseFinal; 
  }
  
  /**
   * descifra la frase actual y la retorna
   * @return la frase ya descifrada
   */
  public String descifrar() {
    String codigoR;
    String fraseFinal = "";
    for(int i = 0; i < frase.length(); i++) {
      codigoR = new StringBuilder(codigo).reverse().toString();
	  int valorASCII = frase.charAt(i);
	  valorASCII -= (codigoR.charAt(0) - '0');
	  fraseFinal += (char)valorASCII;
    }
    frase = fraseFinal;
    return fraseFinal; 
  }
  
  public void setFrase(String frase) {
	  this.frase = frase;
  }

}
