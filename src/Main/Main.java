package Main;

import Clases.Cifrado;
import Clases.Clave;

import java.util.Scanner;

public class Main {

    private static final String ALGORITMO_CLAVE_SIMETRICA_DESede = "DESede";// 3DES
    private static final String ALGORITMO_CLAVE_SIMETRICA_DES = "DES";
    private static final String ALGORITMO_CLAVE_SIMETRICA_AES = "AES/ECB/PKCS5Padding";
    public static final String MENSAJE_ERROR = "La elección debe ser un número entre 0 y 3";
    public static final String PEDIR_NOMARCHIVO = "Escribe el nombre del fichero donde quieres que se genere la clave";
    public static final String PEDIR_RUTA_ARCHIVO = "Escriba la ruta completa del archivo a encriptar";
    public static final String PEDIR_TIPO_CLAVE = "Escribe: \n" +
            "1 -> AES \n" +
            "2 -> DES \n" +
            "3 -> DESede\n" +
            "0 -> Atrás";
   //C:\Users\GL512\IdeaProjects\AguilarJimenezClavePrivada\src\Claves\
    public static final String RUTA_CLAVES = "C:\\\\Users\\\\caguilar\\\\Downloads\\\\AguilarJimenezClavePrivada\\\\src\\\\Claves\\\\";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        menu(sc);
    }


    /**
     * @param sc
     * @param peticion
     * @return
     */
    private static String pedir(Scanner sc, String peticion) {
        System.out.println(peticion);
        return sc.nextLine();
    }

    /**
     * @param sc
     * @return
     */
    private static int eleccionTipoClave(Scanner sc) {
        String eleccionString = "";
        int eleccion = -1;
        do {
            eleccionString = pedir(sc, PEDIR_TIPO_CLAVE);
            try {
                eleccion = Integer.parseInt(eleccionString);
            } catch (Exception e) {
                System.out.println(MENSAJE_ERROR);
                eleccion = -1;
            }
        } while (eleccion == -1);
        return eleccion;
    }

    private static String tipoClaveCadena(Scanner sc, int tipo) {
        String eleccionString = "";
        switch (tipo) {
            case 1:
                eleccionString = ALGORITMO_CLAVE_SIMETRICA_AES;
                break;
            case 2:
                eleccionString = ALGORITMO_CLAVE_SIMETRICA_DES;
                break;
            case 3:
                eleccionString = ALGORITMO_CLAVE_SIMETRICA_DESede;
        }
        return eleccionString;
    }


    /**
     * Desde aquí se llama al método que pedirá al usuario
     * introducir el tipo de clave de cifrado que prefiere
     * para su fichero.
     *
     * @param sc
     */
    private static void opcion1(Scanner sc, int tipoClave) {
        switch (tipoClave) {
            case 1:
                Clave.generarClaveAES(pedir(sc, PEDIR_NOMARCHIVO));
                break;
            case 2:
            case 3:
                Clave.generarClaveDESyDESede(tipoClave, pedir(sc, PEDIR_NOMARCHIVO));
                break;
        }


    }



    private static void opcion2( int accion, Scanner sc) {


        cifrarDESede(accion, sc);
    }

    private static void cifrarAES(int accion, Scanner sc){

    }

    private static void cifrarDESede(int accion, Scanner sc) {
        String nomFich = pedir(sc, PEDIR_NOMARCHIVO);
        String ruta = pedir(sc, PEDIR_RUTA_ARCHIVO);
        String algoritmo = tipoClaveCadena(sc, eleccionTipoClave(sc));
        String nom = Cifrado.cifrarFicheroDESede(ruta, nomFich, algoritmo, accion);


        if ((nom).endsWith(".encript") || (nom).endsWith(".desencript") ) {
            if (accion == 1) {
                System.out.println("Se generó el archivo encriptado en la ruta " + nom);
            }else{
                System.out.println("Se generó el archivo encriptado en la ruta " + nom);
            }
        } else {
            if (accion == 1)
                System.out.println("No se pudo generar el archivo encriptado");
            else
                System.out.println("No se pudo descifrar el archivo");

        }


    }


    /**
     * Menu de la app
     *
     * @param sc
     */
    private static void menu(Scanner sc) {
        int eleccion = 0;
        do {
            System.out.println("Elige una opcion \n" +
                    "1 -> Generar Clave \n" +
                    "2 -> Encriptar \n" +
                    "3 -> Desencriptar \n" +
                    "0 -> Salir");
            String input = sc.nextLine();
            try {
                eleccion = Integer.parseInt(input);
            } catch (NumberFormatException ex) {
                System.out.println("No es una opcion válida");
                eleccion = -1;
            }
            switch (eleccion) {
                case 0:
                    System.out.println("Bye!");
                    break;
                case 1:
                    opcion1(sc, eleccionTipoClave(sc));
                    break;
                case 2:
                    opcion2(1, sc);
                    break;
                case 3:
                    opcion2(2, sc);
            }
        } while (eleccion != 0);

    }
}
