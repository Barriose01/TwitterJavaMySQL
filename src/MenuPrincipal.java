import java.util.*;
public class MenuPrincipal {
	Scanner sc = new Scanner(System.in);
	QuerysMenu q = new QuerysMenu();
	String id, usuario, opcion;
	int parseOpcion;
	
	public MenuPrincipal(String id, String usuario) {
		this.id = id;
		this.usuario = usuario;
	}
	
	public void menu() {
		int salida = 1;
		while(salida == 1) {
			System.out.println("\nBienvenid@, " + this.usuario);
			System.out.println("(1): Escribir post");
			System.out.println("(2): Ver hashtags mas populares");
			System.out.println("(3): Buscar posts por hashtag");
			System.out.println("(4): Ver posts");
			System.out.println("(5): Buscar posts por usuario");
			System.out.println("(6): Ver tus posts");
			System.out.println("(7): Ver tus posts (de mas antiguo a mas reciente)");
			System.out.println("(8): Borrar un determinado post");
			System.out.println("(9): Borrar todos los posts");
			System.out.println("(10): Cerrar sesion");
			
			opcion = sc.nextLine().strip();
			try {
				parseOpcion = Integer.parseInt(opcion);
			}catch(Exception e) {
				System.out.println("Ingrese una opcion valida");
				continue;
			}
			if(parseOpcion == 10) {break;} 
			else {this.menuOpciones(parseOpcion);}
		}
		
	}
	private void menuOpciones(int opcion) {
		switch(opcion) {
		case 1:
			this.escribirPost();
			break;
		case 2:
			this.verHashtagsPopulares();
			break;
		case 3:
			this.buscarHashtag();
			break;
		case 4:
			this.verTodosLosPosts();
			break;
		case 5:
			this.buscarPost();
			break;
		case 6:
			this.verTusPosts("recientes");
			break;
		case 7:
			this.verTusPosts("antiguos");
			break;
		case 8:
			this.eliminarPost();
			break;
		case 9:
			this.eliminarTodo();
			break;
		default:
			System.out.println("Ingrese una opcion valida");
			break;
		}
	}
	//Este metodo solo se usa en caso de incluir un hashtag (#) al escribir un post
	private ArrayList<String> obtenerHashtag(String post) {
		ArrayList<String> hashtags = new ArrayList<String>();
		for(int i = 0; i < post.length(); i++) {
			if(post.charAt(i) == '#') {
				String tag = post.substring(i).split(" ")[0]; //Se corta el string, partiendo desde el hashtag (#)
															//Se guarda el string en un array, sin contar con los espacios (" ")
															//Se obtiene el primer valor (El hashtag con su palabra)
				hashtags.add(tag);
			}
		}
		return hashtags;
	}
	private void escribirPost() {
		System.out.println("Presione (q) para volver al menu principal");
		System.out.print("Escriba su post: ");
		String post = sc.nextLine();
		if(!post.toLowerCase().strip().equals("q")) {
			if(post.length() > 180 || post.length() < 1) {
				System.out.println("Los posts deben tener un minimo de 1 caracter y un maximo de 180");
			}else {
				try {
					ArrayList<String> hashtags = this.obtenerHashtag(post);
					if(hashtags.size() < 1) { //Si no hay hashtags
						q.escribirPost(this.id, post);
					}else {
						String idPost = q.escribirPost(this.id, post).get(0); //Se obtiene el ID de la publicacion generada
						q.insertarHashtag(idPost, this.id, hashtags); //Se le pasa el ID para poder guardar el hashtag
					}
				}catch(Exception e) {
					System.out.println("Error al registrar el hashtag");
				}
			}
		}
	}
	private void verHashtagsPopulares() {
		ArrayList<String> hashtagsPopulares = q.verHashtagsPopulares();
		if(hashtagsPopulares.size() > 0) {
			for(int i = 0; i < hashtagsPopulares.size(); i++) {
				System.out.println("(" + (i + 1) + "): " + hashtagsPopulares.get(i));
			}
		}else {
			System.out.println("No hay hashtags para mostrar");
		}
	}
	private void buscarHashtag() {
		System.out.println("Presione (q) para volver al menu principal");
		System.out.print("Ingrese un hashtag para buscar publicaciones relacionadas: ");
		opcion = sc.nextLine();
		if(!opcion.toLowerCase().strip().equals("q")) {
			ArrayList<String> hashtags = q.buscarHashtag(opcion);
			if(hashtags.size() > 0) {
				Collections.reverse(hashtags); //La lista se devuelve primero con la fecha, luego con la publicacion
												//y al final con el usuario. Lo queremos al contrario
				int contadorResultados = 0;
				for(int i = 0; i < hashtags.size(); i +=3) {contadorResultados +=1;} //Forma para obtener la cantidad de registros encontrados
				
				System.out.println("\nResultados para '" + opcion + "': " + contadorResultados);
				for(int i = 0; i < hashtags.size(); i+=3) { //Se tiene una lista continua en donde vuelve a 
															//la primera columna por cada 3 registros
					
					//						usuario							publicacion
					System.out.println("-" + hashtags.get(i + 2) + ": " + hashtags.get(i + 1) + ". Publicado en " 
				+ hashtags.get(i)); //fecha
				}
				
			}else {
				System.out.println("No hay hashtags para mostrar");
			}
			
		}
	}
	private void verTodosLosPosts() {
		ArrayList<String> posts = q.verTodosLosPosts();
		if(posts.size() > 0) {
			Collections.reverse(posts);
			for(int i = 0; i < posts.size(); i+= 3) {
				System.out.println("-" + posts.get(i + 2) + ": " + posts.get(i + 1) + ". Publicado en " 
			+ posts.get(i));
			}
			
		}else {
			System.out.println("No hay posts para mostrar");
		}
		
	}
	private void buscarPost() {
		System.out.println("Presione (q) para volver al menu principal");
		System.out.print("Ingrese el nombre del usuario que desea buscar para ver sus posts: ");
		opcion = sc.nextLine().toLowerCase().strip();
		if(!opcion.equals("q")) {
			ArrayList<String> posts = q.buscarUsuarios(opcion); 
			if(posts.size() > 0) {
				Collections.reverse(posts);
				int contadorResultados = 0;
				for(int i = 0; i < posts.size(); i +=3) {contadorResultados +=1;}
				
				System.out.println("\nResultados para '" + opcion + "': " + contadorResultados);
				for(int i = 0; i < posts.size(); i +=3) {
					System.out.println("-" + posts.get(i + 2) + ": " + posts.get(i + 1) + ". Publicado en " 
					+ posts.get(i));
				}
			}else {
				System.out.println("\nNo se encontraron posts para el usuario '" + opcion + "'");
			}
		}
	}
	private void verTusPosts(String orden) {
		ArrayList<String> posts = q.verTusPosts(this.usuario, orden);
		if(posts.size() > 0) {
			if(orden.equals("recientes")) {Collections.reverse(posts);} //Si queremos ver de mas nuevo a mas antiguo,
																		//nos devolvera la lista al contrario,
																		//por eso le hacemos un reverse
			int contadorResultados = 0;
			for(int i = 0; i < posts.size(); i +=4) {contadorResultados +=1;}
			
			System.out.println("\nCantidad de posts realizados: " + contadorResultados);
			for(int i = 0; i < posts.size(); i +=4) { //Son 4 columnas. El metodo devuelve la columna del id
				System.out.println("-" + posts.get(i + 2) + ": " + posts.get(i + 1) + ". Publicado en " 
				+ posts.get(i));
			}
		}else {
			System.out.println("No has realizado ningun post");
		}
	}
	private void eliminarPost() {
		while(true) {
			ArrayList<String> posts = q.verTusPosts(this.usuario, "recientes");
			ArrayList<String> listaID = new ArrayList<String>(); //Esta lista es para obtener todos los ID de las publicaciones
			Collections.reverse(posts);
			if(posts.size() > 0) {
				listaID.clear(); //Si no lo limpio al comenzar, aumenta la lista mas adelante
				System.out.println("Ingrese el indice de la publicacion que desea eliminar");
				int fila = 0; //Representa la fila (o indice) de los posts que aparecen
				for(int i = 0; i < posts.size(); i +=4) {
					System.out.println("(" + fila + ") " + posts.get(i + 2) + ": " + posts.get(i + 1) 
					+ ". Publicado en " + posts.get(i));
					listaID.add(posts.get(i + 3)); //El id siempre nos aparecera a 3 posiciones despues de la primera columna
					fila++;
				}
				System.out.println("(q): Volver");
				Collections.reverse(posts); //Hago esto para que, al volver al loop, vuelva a quedar en orden
				opcion = sc.nextLine().toLowerCase().strip();
				if(opcion.equals("q")) {break;}
				else {
					try {
						parseOpcion = Integer.parseInt(opcion);
					}catch(Exception e) {
						System.out.println("Ingrese una opcion valida");
						continue;
					}
					if(parseOpcion < 0 || parseOpcion > listaID.size() - 1) {
						System.out.println("Debe ingresar una opcion valida");
					}else {
						q.eliminarPost(listaID.get(parseOpcion)); //Le pasamos al metodo la posicion que elegimos.
																//listaID.get(parseOpcion) devolvera el ID de la publicacion
																//de la cual elegimos su posicion 
					}
				}
			}else {
				System.out.println("No hay posts para eliminar");
				break;
			}
			
		}
	}
	public void eliminarTodo() {
		ArrayList<String> posts = q.verTusPosts(this.usuario, "recientes");
		int salida = 0;
		if(posts.size() > 0) {
			while(salida == 0) {
				System.out.println("Esta seguro de que quiere eliminar todos los posts?");
				System.out.println("(1): Si");
				System.out.println("(2): No");
				opcion = sc.nextLine().strip();
				try {
					parseOpcion = Integer.parseInt(opcion);
				}catch(Exception e) {
					System.out.println("Ingrese una opcion valida");
					continue;
				}
				switch(parseOpcion) {
				case 1:
					q.eliminarTodo(this.id);
					salida = 1;
					break;
				case 2:
					salida = 1;
					break;
				default:
					System.out.println("Ingrese una opcion valida");
					break;
				}
			}
		}else {
			System.out.println("No hay posts para eliminar");
		}
	}
}
