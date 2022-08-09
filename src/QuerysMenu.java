import java.sql.Connection;
import java.sql.ResultSet;
import java.util.*;

import com.mysql.jdbc.Statement;
public class QuerysMenu {
	RegistroYLoginBD bd = new RegistroYLoginBD();
	Connection cn;
	Statement statement;
	ResultSet rs;
	String sql = "";
	public QuerysMenu() {
		this.cn = bd.conectar();
	}
	
	
	public ArrayList<String> escribirPost(String idUsuario, String post) {
		ArrayList<String> ultimoId = new ArrayList<String>(); //Se necesita el ID de la ultima publicacion generada
															//para guardar los hashtags en una tabla
		try {
			sql = "CALL escribirPost('" + idUsuario + "', '" + post + "')";
			statement = (Statement) this.cn.createStatement();
			statement.execute(sql);
			sql = "SELECT LAST_INSERT_ID()"; //Se obtiene el ID de la ultima publicacion generada
			statement = (Statement) this.cn.createStatement();
			rs = statement.executeQuery(sql);
			while(rs.next()) {
				ultimoId.add(rs.getString(1));
			}
			System.out.println("La publicacion ha sido realizada con exito");
			
		}catch(Exception e) {
			System.out.println("Error al escribir el post");
		}
		return ultimoId;
	}
	public void insertarHashtag(String idPost, String idUsuario, ArrayList<String> listaHashtag) {
		try {
			for(int i = 0; i < listaHashtag.size(); i++) {
				sql = "CALL insertarHashtags('" + idPost + "', '" + idUsuario + "', '" + listaHashtag.get(i) + "')";
				statement = (Statement) this.cn.createStatement();
				statement.execute(sql);
			}
		}catch(Exception e) {
			System.out.println("Error al registrar el hashtag");
		}
	}
	public ArrayList<String> verHashtagsPopulares() {
		ArrayList<String> hashtags = new ArrayList<String>();
		try {
			sql = "SELECT * FROM hashtagsPopulares";
			statement = (Statement) this.cn.createStatement();
			rs = statement.executeQuery(sql);
			while(rs.next()) {
				hashtags.add(rs.getString(1));
			}
		}catch(Exception e) {
			System.out.println("Error al recuperar los hashtags");
		}
		return hashtags;
	}
	public ArrayList<String> buscarHashtag(String hashtag) {
		ArrayList<String> hashtags = new ArrayList<String>();
		try {
			sql = "CALL buscarHashtag('" + hashtag + "')";
			statement = (Statement) this.cn.createStatement();
			rs = statement.executeQuery(sql);
			while(rs.next()) {
				for(int i = 1; i < 4; i++) { //Se itera 3 veces y se guardan los campos en la
											//lista hashtags
					hashtags.add(rs.getString(i));
				}
			}
		}catch(Exception e) {
			System.out.println("Error al recuperar los hashtags");
		}
		return hashtags;
	}
	public ArrayList<String> verTodosLosPosts() {
		ArrayList<String> posts = new ArrayList<String>();
		try {
			sql = "SELECT * FROM vistaPublicacion";
			statement = (Statement) this.cn.createStatement();
			rs = statement.executeQuery(sql);
			while(rs.next()) {
				for(int i = 2; i < 5; i++) { //El indice 1 es el id, por eso empiezo en 2
											//Son 4 colunnas, por eso itero 4 veces (no llega al 5)
					posts.add(rs.getString(i));
				}
			}
		}catch(Exception e) {
			System.out.println("Error al recuperar los posts");
		}
		return posts;
	}
	public ArrayList<String> buscarUsuarios(String usuario){
		ArrayList<String> posts = new ArrayList<String>();
		try {
			sql = "CALL buscarUsuario('" + usuario + "')";
			statement = (Statement) this.cn.createStatement();
			rs = statement.executeQuery(sql);
			while(rs.next()) {
				for(int i = 1; i < 4; i++) {
					posts.add(rs.getString(i));
				}
			}
		}catch(Exception e) {
			System.out.println("Error al recuperar los posts");
		}
		return posts;
	}
	public ArrayList<String> verTusPosts(String usuario, String orden){
		ArrayList<String> posts = new ArrayList<String>();
		try {
			sql = "SELECT * FROM vistaPublicacion WHERE nombre ='" + usuario + "'";
			statement = (Statement) this.cn.createStatement();
			rs = statement.executeQuery(sql);
			while(rs.next()) {
				if(orden.equals("recientes")) {
					for(int i = 1; i < 5; i++) {//Son 4 columnas. Se itera 4 veces. Registros aparecen del mas nuevo al mas antiguo
						posts.add(rs.getString(i));
					}
				}else {
					for(int i = 4; i > 0; i--) { //Los registros aparecen del mas antiguo al mas nuevo
						posts.add(rs.getString(i));
					}
				}
				
			}
		}catch(Exception e) {
			System.out.println("Error al recuperar los posts");
		}
		return posts;
	}
	public void eliminarPost(String idPublicacion) {
		try {
			//Primero eliminamos la publicacion de la tabla de los hashtags (en caso de haber hashtags)
			sql = "DELETE FROM publicacionesXhashtags WHERE idPublicacion ='" + idPublicacion + "'";
			statement = (Statement) this.cn.createStatement();
			statement.execute(sql);
			//Despues, eliminamos la publicacion de la tabla de publicaciones
			String sql2 = "DELETE FROM publicaciones WHERE id = '" + idPublicacion + "'";
			statement = (Statement) this.cn.createStatement();
			statement.execute(sql2);
			System.out.println("El post ha sido eliminado con exito");
		}catch(Exception e) {
			System.out.println("Error al eliminar el post");
		}
	}
	public void eliminarTodo(String idUsuario) {
		try {
			//Primero eliminamos las publicaciones de la tabla de los hashtags (en caso de haber hashtags)
			sql = "DELETE FROM publicacionesXhashtags WHERE idUsuario = '" + idUsuario + "'";
			statement = (Statement) this.cn.createStatement();
			statement.execute(sql);
			//Despues, eliminamos las publicaciones de la tabla de publicaciones
			String sql2 = "DELETE FROM publicaciones WHERE idUsuario = '" + idUsuario + "'";
			statement = (Statement) this.cn.createStatement();
			statement.execute(sql2);
			System.out.println("Todos los posts han sido eliminados con exito");
		}catch(Exception e) {
			System.out.println("Error al eliminar todos los posts");
		}
	}
}
