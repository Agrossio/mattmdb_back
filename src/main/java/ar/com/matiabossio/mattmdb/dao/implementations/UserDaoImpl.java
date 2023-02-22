package ar.com.matiabossio.mattmdb.dao.implementations;

import ar.com.matiabossio.mattmdb.dao.IUserDao;
import ar.com.matiabossio.mattmdb.domain.User;
import ar.com.matiabossio.mattmdb.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

public class UserDaoImpl implements IUserDao {

    // DAO: La API contra la Base de Datos, va la logica de obtener los datos de la DB
    @Override
    public Collection<User> getUsers() {

        Collection<User> users = new ArrayList<>();     // uso Collection en el tipo de dato que es el mas generico de todos, pero puedo usar ArrayList.

        Connection connection = DatabaseConnection.getDatabaseConnection();

        String query = "SELECT * FROM users";

        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()){
                User user = getUserFromResultSet(rs);   // guardo en user un usuario del resultSet que retorna executeQuery
                users.add(user);
                /*
                sin getUserFromResultSet:

                Integer id = rs.getInt(1);
                String username = rs.getString(2);
                String email = rs.getString(3);
                String password = rs.getString(4);

                User user = new User(id, username, email, password);
                users.add(user);

                */

            }

            stmt.close();   // cuando ya no uso mas el stmt lo tengo que cerrar

            return users;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public User getUserById(Integer userId) {

        Connection connection = DatabaseConnection.getDatabaseConnection();

        String query = "SELECT * FROM users WHERE id_user = ?";

        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            //creo un user null porque sino lo encuentro devuelvo un null:
            User user = null;       // reserva el espacio en memoria para un objeto de tipo User, pero sin nada dentro

            if (rs.next()){

           /*
              Uso el metodo creado abajo para obtener el unico usuario que esta
              en el rs y guardarlo en la instancia user:
           */
                user = getUserFromResultSet(rs);    // deja de estar nulo el espacio reservado de tipo User

             /* sin getUserFromResultSet:

                user.setUserId(rs.getInt(1));
                user.setUsername(rs.getString(2));
                user.setEmail(rs.getString(3));
                user.setPassword(rs.getString(4));*/

            }

            stmt.close();   // cuando ya no uso mas el stmt lo tengo que cerrar

            return user;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public User getUserByEmail(String email) {

        Connection connection = DatabaseConnection.getDatabaseConnection();

        String query = "SELECT * FROM users WHERE email = ?";

        PreparedStatement stmt = null;
        try {
            stmt = connection.prepareStatement(query);
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            //creo un user null porque sino lo encuentro devuelvo un null:
            User user = null;   // reserva el espacio en memoria para un objeto de tipo User, pero sin nada dentro

            if (rs.next()){

           /*
              Uso el metodo creado abajo para obtener el unico usuario que esta
              en el rs y guardarlo en la instancia user:
           */
                user = getUserFromResultSet(rs);    // deja de estar nulo el espacio reservado de tipo User

             /* sin getUserFromResultSet:

                user.setUserId(rs.getInt(1));
                user.setUsername(rs.getString(2));
                user.setEmail(rs.getString(3));
                user.setPassword(rs.getString(4));*/

            }

            stmt.close();   // cuando ya no uso mas el stmt lo tengo que cerrar

            return user;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public User createUser(User user) {

        Connection connection = DatabaseConnection.getDatabaseConnection();

        // FALTA VALIDAR SI EL EMAIL/USERNAME YA EXISTEN!!


        String query = "INSERT INTO users (username, email, password) VALUES (?,?,?)";

        try {

        // uso el PreparedStatement.RETURN_GENERATED_KEYS para que me retorne la key que se genera al crear el User:
            PreparedStatement stmt = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);

        // meto en la query los datos del usuario que me llegan por parametro (vienen en el body del request sin id):
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());

            stmt.execute();

            // obtengo lo(s) ids generados:
            ResultSet rs = stmt.getGeneratedKeys();     // el metodo restorna un ResultSet pero con un solo id

            // recorro el rs guarando en el usuario recibido, la key generada por la DB (vino sin id desde el front)
            if (rs.next()){
                Integer key = rs.getInt(1);
                user.setUserId(key);
            }

            stmt.close();       // cierro el statement

            // retorno el usuario generado con el id
            return user;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public User upateUser(User userFromRequest) {

        Connection connection = DatabaseConnection.getDatabaseConnection();

        String query = "UPDATE users SET username = ?, email = ?, password = ? WHERE id_user = ?";

        try {
            PreparedStatement stmt = connection.prepareStatement(query);

            // guardo en el stmt los datos de usuario que vienen en el request:

            stmt.setString(1, userFromRequest.getUsername());
            stmt.setString(2, userFromRequest.getEmail());
            stmt.setString(3, userFromRequest.getPassword());
            stmt.setInt(4, userFromRequest.getUserId());

            int updatedRows = stmt.executeUpdate();     // retorna la cantidad de registros actualizados
            stmt.close();

            User updatedUser = null;    // sino se actualizó => updatedRows = 0 => retorno null

            if (updatedRows != 0){
            // Busco el usuario actualizado en la BD (el this es
            // instancia de UserDaoImpl que llama al metodo getUserById):
                updatedUser = this.getUserById(userFromRequest.getUserId());
            }
            return updatedUser;


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public User deleteUserById(Integer userId) {

        // busco en la DB el usuario a eliminar, utilizando el userId del usuario que llega del Request:
        User user = this.getUserById(userId);

        if (user == null){  // sino encuentra el usuario en la BD retorna null
            return user;
        }

        Connection connection = DatabaseConnection.getDatabaseConnection();

        String query = "DELETE FROM users WHERE id_user = ?";

        try {
            PreparedStatement stmt = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);

            // guardo en el stmt los datos de usuario que vienen en el request:
            stmt.setInt(1, userId);

            int deletedRows = stmt.executeUpdate();

            stmt.close();

            User deletedUser = null;

            if (deletedRows != 0){
                // si es != 0 se borro, retorno el usuario que el front mando a borrar
                deletedUser = user;
            }
            // si deletedRows == 0 => no se borró => retorno el deletedUser = null
            return  deletedUser;

        } catch (SQLException e) {
            throw new RuntimeException(e);

            // probar reemplazarlo con :
            // e.printStackTrace();
            // return null;
        }
    }

    private User getUserFromResultSet(ResultSet rs) throws SQLException {   // con el throws lanzo la excepcion a la funcion que la llama para que la maneje ella (ahi tiene que estar el try/catch).

        // genero el user en base a lo que llega en el rs

        Integer id = rs.getInt(1);
        String username = rs.getString(2);
        String email = rs.getString(3);
        String password = rs.getString(4);

        User user = new User(id, username, email, password);

        return user;

    }
}
