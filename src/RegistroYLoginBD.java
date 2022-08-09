import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.*;

import com.mysql.jdbc.Statement;

public class RegistroYLoginBD {
	Connection cn;
	
	public RegistroYLoginBD() {
		this.cn = this.conectar();
	}
	
	public Connection conectar() {
		Connection con = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");  
			con=DriverManager.getConnection("jdbc:mysql://localhost:3306/twitter","root",""); 
		}catch(Exception e) {
			System.out.println("Error al realizar la conexion con la base de datos");
		}
		return con;
		
	}
	//Este metodo verifica si el usuario esta registrado. Usado para registro y login
	public boolean verificarUsuarioYClave(String usuario,String clave) {
		boolean usuarioExiste = false;
		ArrayList<String> listaUsuarios = new ArrayList<String>();
		try {
			String sql = "CALL verificarUsuarioYClave('" + usuario + "', '" + clave + "')";
			Statement statement = (Statement) this.cn.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			while(rs.next()) {
				listaUsuarios.add(rs.getString(1)); //Se inserta a la lista cualquier campo.
													//Lo que se quiere verificar es que exista
			}
			if(listaUsuarios.size() > 0) {usuarioExiste = true;}
		}catch(Exception e) {
			System.out.println("Error al verificar la cuenta");

		}
		return usuarioExiste;

	}
	public ArrayList<String>  obtenerListaUsuarios(String usuario) {
		ArrayList<String> listaUsuarios = new ArrayList<String>();
		try {
			String sql = "SELECT * FROM usuario WHERE nombre = '" + usuario + "'";
			Statement statement = (Statement) this.cn.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			while(rs.next()) {
				for(int i = 1; i < 4; i++) { //son 3 columnas. Se itera 3 veces obteniendo todos los
											//campos  y guardandolos en listaUsuarios
					listaUsuarios.add(rs.getString(i));
				}
			}
		}catch(Exception e) {
			System.out.println("Error al acceder a las cuentas");
		}
		return listaUsuarios;
	}
	public void registrarUsuario(String usuario, String clave) {
		try {
			String sql = "CALL registrarUsuario('" + usuario + "', '" + clave + "')";
			Statement statement = (Statement) this.cn.createStatement();
			statement.execute(sql);
			System.out.println("Usuario registrado con exito");
		}catch(Exception e) {
			System.out.println("Error al registrar el usuario");
		}
	}

}
