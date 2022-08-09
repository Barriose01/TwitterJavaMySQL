import java.util.*;

public class TwitterMain {
	static Scanner sc = new Scanner(System.in);
	static RegistroYLoginBD bd = new RegistroYLoginBD();
	static String opcion = "";
	static int parseOpcion = 0;
	
	private static void registrar() {
		while(true) {
			System.out.println("Presione (q) en cualquier momento para volver al menu principal");
			System.out.print("Nombre de usuario: ");
			String usuario = sc.nextLine().strip();
			if(usuario.toLowerCase().equals("q")) {break;}
			System.out.print("Clave: ");
			String clave = sc.nextLine().strip();
			if(clave.toLowerCase().equals("q")) {break;}
			System.out.print("Vuelva a introducir la clave: ");
			String clave2 = sc.nextLine().strip();
			if(clave2.toLowerCase().equals("q")) {break;}
			if(clave.equals(clave2)) {
				if(usuario.length() > 20 || clave.length() > 20) {
					System.out.println("El nombre de usuario y la clave deben tener un maximo de 20 caracteres");
				}else if(usuario.length() < 1 || clave.length() < 1) {
					System.out.println("El nombre de usuario y la clave deben tener un minimo de 1 caracter");
				}else {
					ArrayList<String>  listaUsuario = bd.obtenerListaUsuarios(usuario.toLowerCase());
					if(listaUsuario.size() > 0) {
						System.out.println("El usuario ingresado ya existe. Intente con otro");
					}else {
						bd.registrarUsuario(usuario, clave);
					}
				}
			}else {
				System.out.println("Las claves no coinciden");
			}
			break;
		}
	}
	private static void login() {
		while(true) {
			System.out.println("Presione (q) en cualquier momento para volver al menu principal");
			System.out.print("Nombre de usuario: ");
			String usuario = sc.nextLine().strip();
			if(usuario.toLowerCase().equals("q")) {break;}
			System.out.print("Clave: ");
			String clave = sc.nextLine().strip();
			if(clave.toLowerCase().equals("q")) {break;}
			if(usuario.length() > 20 || clave.length() > 20) {
				System.out.println("El nombre de usuario y la clave deben tener un maximo de 20 caracteres");
			}else if(usuario.length() < 1 || clave.length() < 1) {
				System.out.println("El nombre de usuario y la clave deben tener un minimo de 1 caracter");
			}else {
					ArrayList<String> listaUsuarios = bd.obtenerListaUsuarios(usuario);
					boolean usuarioExiste = bd.verificarUsuarioYClave(usuario.toLowerCase(), clave);
					if(usuarioExiste) {
						String id = listaUsuarios.get(0); //El id esta en el indice 0. Se usara para operaciones
														// en la clase MenuPrincipal y QuerysMenu
						MenuPrincipal menu = new MenuPrincipal(id,usuario);
						menu.menu();
					}else {
						System.out.println("Usuario o clave incorrecta. Intentelo de nuevo");
					}
			}
			break;
		}
	}

	public static void main(String[] args) {
		int salida = 1;
		while(salida == 1) {
			System.out.println("\nElige una opcion: ");
			System.out.println("(1): Registro");
			System.out.println("(2): Iniciar Sesion");
			System.out.println("(3): Salir");
			
			opcion = sc.nextLine();
			try {
				parseOpcion = Integer.parseInt(opcion);
			}catch(Exception e) {
				System.out.println("Ingrese una opcion valida");
				continue;
			}
			switch(parseOpcion) {
			case 1:
				registrar();
				break;
			case 2:
				login();
				break;
			case 3:
				salida = 0;
				break;
			default:
				System.out.println("Ingrese una opcion valida");
				break;
				
			}
		}

	}

}
