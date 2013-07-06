
package proyecto_base_de_datos;

import com.mysql.jdbc.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;


public class Conexion_Base_de_Datos {
  
  private Connection conexion;
  private Statement statemen;
  private final String base ="base_ephyon";
  private final String usuario="root";
 private final String password="2012";
  String Estado = "Exitoso";
    

    
    public Conexion_Base_de_Datos(){
    
        
        try {
            
            Class.forName("org.gjt.mm.mysql.Driver"); //Colocamos e Driver
            conexion = (Connection) DriverManager.getConnection("jdbc:mysql://localhost/"+base, usuario, password);//Crea la conexion ala base de datos
          statemen = conexion.createStatement();    //se crea al Statement
           System.out.println("Conexion "+Estado);
           
        } catch (ClassNotFoundException ex) {//En caso que no se encuentre la base de datos , se manejara una execepcion
            Logger.getLogger(Conexion_Base_de_Datos.class.getName()).log(Level.SEVERE, null, ex);
            Estado = "Error de Driver"; 
            System.out.println("Conexion "+Estado);//Un mensaje del tipo de error
        }catch (SQLException ex) {
            Estado = "Error de Base de datos";
            Logger.getLogger(Conexion_Base_de_Datos.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Conexion "+Estado);//Un mensaje del tipo de error 
        }     
    }
    
    //******************************************************************\\
    //Se necesita que el metodo regrese un verdadero o falso para poder porseguir con las operaciones
    public boolean registrarte(int id,String name,String password,String correo,String lenguaje,String proyect){
    try{//El metodo registarse tiene recive como parametros todos los datos necesarios para poder registrarse
           
           this.statemen.execute("INSERT INTO `usuarios_2` (`id`,`nombre`, `password`, `correo`, `proyecto`, `lenguaje`) VALUES('"+id+"','"+name+"','"+password+"','"+correo+"','"+lenguaje+"','"+proyect+"')"); //Ejecuta los commandos SQL
          //la clase statement tiene un metodo execute el cual necesita como parametro un sentencia de SQL
           System.out.println("Se guardo de Manera Exitosa ");
        }catch(SQLException ex){
            System.out.println("Mensaje de error: " + ex.getMessage());
            return false;
        }
        return true; //En caso que todo salga bien returnomos un true
    }
    

  //Metdo para poder registrarte la cual tiene como parametro el nombre del usuario
     public ResultSet loggin(String name) {
        try{
         //Se busca l nombre del usuario y se busca la columna passwor para poder trajarla en otra clase
                ResultSet result = this.statemen.executeQuery("SELECT password,nombre FROM usuarios_2 WHERE nombre='"+name+"'"); 
                if(!result.next()){//Condicional para saber si aun se encuentran datos en la base
                    System.out.println("No hay resultados que coincidan con la búsqueda.");
                    return null;
                }
                return result;//regresamos el objeto resultset
         
        }catch(SQLException ex){
            System.out.println("Error " + ex.getMessage());
            return null;
        }
    }
   
      //para poder crear un proyecto este metodo recibe como parametro todos los datos necesiarios para la creacion de un proyecto
     public void newProyect(int id,String creador,String nombre_p,int personas, int dia, int mes, int año,String lenguaje,String descripcion){
     try{
            
              this.statemen.execute("INSERT INTO `proyecto` (`id`,`creador`,`nombre`,`integrantes`,`dia`,`mes`,`año`,`lenguaje`,`descripcion`) VALUES('"+id+"','"+creador+"','"+nombre_p+"','"+personas+"','"+dia+"','"+mes+"','"+año+"','"+lenguaje+"','"+descripcion+"')");
                  System.out.println("Se guardo de Manera Exitosa "); 
        }catch(SQLException ex){
            System.out.println("Mensaje de error: " + ex.getMessage());
            
        }
        
    }
     //este metodo recibe como parametro un string el cual es el proyecto a buscar
     public ResultSet buscarProyecto(String proyect){
      try{
         ResultSet result = this.statemen.executeQuery("SELECT nombre,creador,integrantes,dia,mes,año,lenguaje,descripcion FROM proyecto WHERE nombre='"+proyect+"'"); 
        //el objeto result obtiene las columnas de la tabla donde se encuentre el proyecto       
         if(!result.next()){
                    System.out.println("No hay resultados que coincidan con la búsqueda.");
                    return null;
                }
                return result;
         
        }catch(SQLException ex){
            System.out.println("Error " + ex.getMessage());
            return null;
        }
     
     }
    //Al igual  que buscar
      public ResultSet buscarProyectoTabla(String nombre_user){
      try{
         ResultSet result = this.statemen.executeQuery("SELECT nombre,integrantes,lenguaje FROM proyecto WHERE nombre='"+nombre_user+"'"); 
                if(!result.next()){
                    System.out.println("No hay resultados que coincidan con la búsqueda.");
                    return null;
                }
                return result;
         
        }catch(SQLException ex){
            System.out.println("Error " + ex.getMessage());
            return null;
        }
     
     }
      //Metdo que regresa un boolean para poder saber si se ejecuta de manera correcta
      //recibe como parametro todos lo datos que creemes necesarios actualizar
       public boolean actualizarProyecto(String proyect,String lenguaje,int integrantes,String descripcion) {
        try{
        //statemen tiene un metodo el cual tiene como parametro un String de SQL   
        this.statemen.executeUpdate("UPDATE proyecto SET integrantes='"+integrantes+"',lenguaje='"+lenguaje+"',descripcion='"+descripcion+"' WHERE nombre='"+proyect+"'"); //Ejecuta el comando de actualización SQL
        }catch(SQLException ex){
            System.out.println("Error "+ex.getMessage());
            return false;
        }
        //Mensaje exitoso
        JOptionPane.showMessageDialog(null, "Se actualizo el proyecto");
        return true;
    }
       
       //******************************************
       //Metodo que regresa un boolean para saber si se ejecuto de manera correcta y tiene comp parametro el nombre del royecto
        public boolean borrar(String nombre) {
        try{
          this.statemen.execute("DELETE FROM proyecto WHERE nombre='"+nombre+"'"); //metodo execute de statement con sentencia SQL
         }catch(SQLException ex){
             //se maneja el excepcion con mensaje
            System.out.println("Mensaje de error: " + ex.getMessage());
            return false;
        }
        return true;
    }
      
    //*
        public void proyecto_usuario(String creador,String proyecto){
         try{
            if (!conexion.isClosed()){
              //metodo de statement que recibe como parametro una sentencia SQl
              this.statemen.execute("INSERT INTO `proyecto_usuario` (`proyecto`,`usuario`) VALUES('"+proyecto+"','"+creador+"')");
             //Mensaje de Exitoso
              System.out.println("Se guardo de Manera Exitosa  Exitosa");
          }else{ 
                
                System.out.println("Mensaje de error: La conexión con la Base de Datos está cerrada.");
                
            }
        }catch(SQLException ex){
            System.out.println("Mensaje de error: " + ex.getMessage());
            
        }
        }
        

       
        
     }
     
