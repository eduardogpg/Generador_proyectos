package proyecto_base_de_datos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet; 
import java.sql.SQLException;
import java.sql.Statement;


public class Conexion {
    //Atributos de conexión
    public static final String usrBD = "root";
    public static final String passBD = "2012";
    
    private Connection conexion;
    private Statement st;
    
   
    public Conexion(String userDB, String passDB, String projectDB) throws ClassNotFoundException, SQLException {
        try {
            Class.forName("org.gjt.mm.mysql.Driver"); //Comprobar el conector
            conexion = DriverManager.getConnection("jdbc:mysql://localhost/"+projectDB, userDB, passDB);
            st=conexion.createStatement();
        } catch (ClassNotFoundException ex) {
            throw new ClassNotFoundException("Conector no encontrado. Mensaje de error: " + ex.getMessage());
        } catch (SQLException ex) {
            throw new SQLException("Error en SQL. Mensaje de error: " + ex.getMessage());
        }
    }
    
  
    public boolean insertar(String instruccion) {
        try{
            if (!conexion.isClosed()){
                st.execute(instruccion); //Ejecuta los commandos SQL
            }else{ // Error en la conexión
                System.out.println("Mensaje de error: La conexión con la Base de Datos está cerrada.");
                return false;
            }
        }catch(SQLException ex){
            System.out.println("Mensaje de error: " + ex.getMessage());
            return false;
        }
        return true;
    }
    
 
    public boolean borrar(String instruccion) {
        return insertar(instruccion);
    }
    
  
    public boolean actualizar(String instruccion) {
        try{
            if (!conexion.isClosed()){
                st.executeUpdate(instruccion); //Ejecuta el comando de actualización SQL
            }else{ // Error en la conexión
                System.out.println("Mensaje de error: La conexión con la Base de Datos está cerrada.");
                return false;
            }
        }catch(SQLException ex){
            System.out.println("Error "+ex.getMessage());
            return false;
        }
        return true;
    }
    

    public ResultSet buscar(String instruccion) {
        try{
            if (!conexion.isClosed()){
                ResultSet rs = st.executeQuery(instruccion); //Ejecuta el query de SQL
                if(!rs.next()){
                    System.out.println("No hay resultados que coincidan con la búsqueda.");
                    return null;
                }

                return rs;
            }else{ //Error en la conexión
                System.out.println("La conexión con la Base de Datos está cerrada.");
                return null;
            }
        }catch(SQLException ex){
            System.out.println("Error " + ex.getMessage());
            return null;
        }
    }
    
    public int contarRegistros(String tabla) {
        ResultSet rs = buscar("SELECT COUNT(*) FROM " + tabla);
        if (rs != null) {
            try {
                return rs.getInt(1);
            } catch (SQLException ex) {
                System.out.println("Error " + ex.getMessage());
                return -1;
            }
        } else {
            return -1;
        }
    }
    
   
    public void cerrar() {
        try {
            st.close(); // Cerrar el Statement
            conexion.close(); // Cierre de la conexión
        } catch (SQLException ex) {
            System.out.println("Error " + ex.getMessage());
        } finally {
            conexion=null;
            st=null;
        }
    }
}
