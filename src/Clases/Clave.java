package Clases;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;

public class Clave {
    private static final String ALGORITMO_CLAVE_SIMETRICA_DESede = "DESede";// 3DES
    private static final String ALGORITMO_CLAVE_SIMETRICA_DES = "DES";
    private static final String ALGORITMO_CLAVE_SIMETRICA_AES = "AES";
    //C:\Users\caguilar.INFO2\Desktop\PSPRO\programas_ejemplo_libro_PSP\Tema5-TecnicasProgSegura\03-Cifrado3DES\Cifrado3DES\src\Claves\
    private static final String NOM_FICH_CLAVE = "C:\\Users\\GL512\\IdeaProjects\\AguilarJimenezClavePrivada\\";
    private static final String ALGORITMO_GEN_NUM_ALEAT = "SHA1PRNG";


    /**
     * Método que se encarga de generar una clave AES
     * y devuelve la ruta del archivo donde se encuentra esta
     *
     * @param ficheroClave
     * @return
     */
    public static SecretKeySpec generarClaveAES(String ficheroClave) {
        SecretKeySpec secretKey = null;
        try {
            //Creo la instancia del generador de claves
            KeyGenerator genClaves = KeyGenerator.getInstance(ALGORITMO_CLAVE_SIMETRICA_AES);
            //Creo la instancia del random
            SecureRandom srand = SecureRandom.getInstance(ALGORITMO_GEN_NUM_ALEAT);
            //inicializo el generador de claves con el número random creado anteriormente
            genClaves.init(srand);
            //Inistancio una clave secreta que sera generada por genClaves
            //a partir del número aleatorio dado anteriormente
            SecretKey clave = genClaves.generateKey();
            //Muestro el formato del fichero que contendra la clave
            System.out.printf("Formato de clave: %s\n", clave.getFormat());
            //Convierto esta clave en un array de bytes y lo guardo
            byte[] claveEncriptacion = clave.toString().getBytes("UTF-8");


            MessageDigest sha = MessageDigest.getInstance(ALGORITMO_GEN_NUM_ALEAT);
            claveEncriptacion = sha.digest(claveEncriptacion);
            claveEncriptacion = Arrays.copyOf(claveEncriptacion, 16);
            secretKey = new SecretKeySpec(claveEncriptacion, ALGORITMO_CLAVE_SIMETRICA_AES);
            byte[] valorClave = secretKey.getEncoded();
            try (FileOutputStream fos = new FileOutputStream(NOM_FICH_CLAVE + ficheroClave + ".txt")) {
                fos.write(valorClave);
            } catch (IOException e) {
                System.out.println("Error de E/S escribiendo clave en fichero");
                e.printStackTrace();
            }
            System.out.printf("Longitud de clave: %d bits\n", valorClave.length * 8);
            System.out.printf("Valor de la clave: [%s] \nen fichero %s\n",
                    valorHexadecimal(valorClave), NOM_FICH_CLAVE + ficheroClave);
        } catch (NoSuchAlgorithmException e) {
            System.out.println("No se encuentra el algoritmo");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return secretKey;
    }


    /**
     * Método que genera una clave DESede.
     * Devuelve la ruta completa del fichero creado con la clave DESede
     *
     * @param ficheroClave
     * @return
     */
    public static String generarClaveDESyDESede(int tipoEncriptado, String ficheroClave) {
        String algoritmo = "";
        switch (tipoEncriptado) {
            case 2:
                algoritmo = ALGORITMO_CLAVE_SIMETRICA_DES;
                break;
            case 3:
                algoritmo = ALGORITMO_CLAVE_SIMETRICA_DESede;
        }

        try {
            KeyGenerator genClaves = KeyGenerator.getInstance(algoritmo);

            SecureRandom srand = SecureRandom.getInstance(ALGORITMO_GEN_NUM_ALEAT);
            genClaves.init(srand);


            SecretKey clave = genClaves.generateKey();
            System.out.printf("Formato de clave: %s\n", clave.getFormat());

            SecretKeyFactory keySpecFactory = SecretKeyFactory.getInstance(algoritmo);
            KeySpec keySpec = null;
            byte[] valorClave = null;
            if (algoritmo.equals(ALGORITMO_CLAVE_SIMETRICA_DESede)) {
                keySpec = keySpecFactory.getKeySpec(clave, DESedeKeySpec.class);
                valorClave = ((DESedeKeySpec) keySpec).getKey();
            } else if (algoritmo.equals(ALGORITMO_CLAVE_SIMETRICA_DES)) {
                keySpec = keySpecFactory.getKeySpec(clave, DESKeySpec.class);
                valorClave = ((DESKeySpec) keySpec).getKey();
            }
            System.out.printf("Longitud de clave: %d bits\n", valorClave.length * 8);
            System.out.printf("Valor de la clave: [%s] en fichero %s",
                    valorHexadecimal(valorClave), NOM_FICH_CLAVE + ficheroClave);
            try (FileOutputStream fos = new FileOutputStream(NOM_FICH_CLAVE + ficheroClave + ".txt")) {
                fos.write(valorClave);
            } catch (IOException e) {
                System.out.println("Error de E/S escribiendo clave en fichero");
                e.printStackTrace();
            }


        } catch (NoSuchAlgorithmException e) {
            System.out.println("Algoritmo de generación de claves no soportado");
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return NOM_FICH_CLAVE + ficheroClave;
    }


    static String valorHexadecimal(byte[] bytes) {
        String result = "";
        for (byte b : bytes) {
            result += String.format(String.format("%x", b));
        }
        return result;
    }

}
