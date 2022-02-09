package Clases;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

public class Cifrado {


    //C:\Users\caguilar.INFO2\Desktop\PSPRO\programas_ejemplo_libro_PSP\Tema5-TecnicasProgSegura\03-Cifrado3DES\Cifrado3DES\src\Claves\
    private static final String NOM_FICH_CLAVE = "C:\\Users\\GL512\\IdeaProjects\\AguilarJimenezClavePrivada\\";


    // Tipo de cifrado, por bloques, padding etc.
    private final static String TIPO_CIFRADO_AES
            = "AES";



    private static byte[] leerFicheroClave(String ruta) {
        byte valorClave[] = null;

        try (FileInputStream fisClave = new FileInputStream(ruta)) {
            valorClave = fisClave.readAllBytes();
        } catch (FileNotFoundException e) {
            System.out.printf("ERROR: no existe fichero de clave %s\n.", ruta);

        } catch (IOException e) {
            System.out.printf("ERROR: de E/S leyendo clave de fichero %s\n.", ruta);

        }
        return valorClave;
    }


    /**
     * Método que se encarga de introducir un flujo de datos en un fichero
     *
     * @param nomFich
     * @param modo
     * @param cifrado
     * @return
     */
    private static String escribirFichero(String nomFich, String modo, Cipher cifrado) {
        try (FileInputStream fis = new FileInputStream(nomFich);
             FileOutputStream fos = new FileOutputStream(nomFich + modo);
             BufferedInputStream is = new BufferedInputStream(fis);
             BufferedOutputStream os = new BufferedOutputStream(fos)) {
            byte[] buff = new byte[cifrado.getBlockSize()];
            while (is.read(buff) != -1) {
                os.write(cifrado.update(buff));
            }
            os.write(cifrado.doFinal());
        } catch (IllegalBlockSizeException e) {
            System.out.println("Tamaño de bloque no válido.");
            e.printStackTrace();
        } catch (BadPaddingException e) {
            System.out.println("Excepción con relleno.");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("ERROR: de E/S encriptando fichero");
        }
        return nomFich + modo;
    }

    /**
     * Método que se encaga de cifrar un archivo con una clave DESede o DES
     *
     * @param nomFich
     * @param nomFichClave
     * @param algoritmo
     * @param accion       Dirá si el modo es ecriptacion o desencriptacion
     * @return
     */
    public static String cifrarFicheroDESede(String nomFich, String nomFichClave, String algoritmo, int accion) {

        nomFichClave = NOM_FICH_CLAVE + nomFichClave + ".txt";
        String modo = null;
        byte[] valorClave = leerFicheroClave(nomFichClave);

        try {

            SecretKeySpec keySpec = new SecretKeySpec(valorClave, algoritmo);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(algoritmo);
            SecretKey clave = keyFactory.generateSecret(keySpec);




            Cipher cifrado = Cipher.getInstance(algoritmo);

            if (accion == 1) {
                cifrado.init(Cipher.ENCRYPT_MODE, clave);
                modo = ".encript";
            } else {
                cifrado.init(Cipher.DECRYPT_MODE, clave);
                modo = ".desencript";
            }


            nomFich = escribirFichero(nomFich, modo, cifrado);


        } catch (NoSuchAlgorithmException e) {
            System.out.printf("No existe algoritmo de cifrado %s.\n", algoritmo);
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            System.out.println("Especificación de clave no válida.");
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            System.out.println("Clave no válida.");
            e.printStackTrace();
        }


        return nomFich;

    }

}
