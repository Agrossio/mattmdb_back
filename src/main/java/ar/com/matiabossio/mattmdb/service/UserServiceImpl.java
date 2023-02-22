package ar.com.matiabossio.mattmdb.service;

import ar.com.matiabossio.mattmdb.dao.implementations.UserDaoImpl;
import ar.com.matiabossio.mattmdb.domain.User;
import jakarta.inject.Named;

import java.util.Collection;

@Named
public class UserServiceImpl implements IUserService {

     /*
         Aca solamente el servicio esta haciendo un pasamanos, pero si necesito
         agregar logica a lo que obtuve del DAO corresponde hacerla aca. Si tengo una capa de controlador,
         la logica de negocio iria ahi, y en el servicio combino las diferentes logicas de negocio.
         En este caso habria que hacer la logica de negocio aca ya que no uso la capa controller.
     */

    @Override
    public Collection<User> getUsersService() {

        Collection<User> users = new UserDaoImpl().getUsers();

         /*
            Genero una instancia de UserDaoImpl y llamo a getUsers de esa
            instancia que retorna una coleccion de usuarios (un Array List).
            Es lo mismo que hacer:
             UserDaoImpl userDaoImpl = new UserDaoImpl();
             Collection<User> users = userDaoImpl.getUsers();
         */

        return users;
    }

    @Override
    public User getUserByIdService(Integer userId) {

        User user = new UserDaoImpl().getUserById(userId);

        // aca sigue siendo un pasamanos, pero puedo meter logica aca

        return user;
    }

    @Override
    public User createUserService(User userFromRequest) {

        User createdUser = new UserDaoImpl().createUser(userFromRequest);

        return createdUser;
    }

    @Override
    public User updateUserService(User userFromRequest) {
        User updatedUser = new UserDaoImpl().upateUser(userFromRequest);
        return updatedUser;
    }

    @Override
    public User deleteUserService(Integer userId) {

        User deletedUser = new UserDaoImpl().deleteUserById(userId);

        return deletedUser;
    }

    @Override
    public User loginUserService(User userFromRequest) {

        // CONSULTAR: Esta bien hacer esta logica aca o donde iria??

        // userFromRequest tiene solo email y password

        // tambien consultar porque no anda con => User loggedUser = null;
        User loggedUser = new User(null, null,null, null);

        // probar con User loggedUser = new User(); en vez de new User(null, null, null..)

        // busco el usuario con ese email en la BD para ver si coinciden los passwords
        User foundUser = new UserDaoImpl().getUserByEmail(userFromRequest.getEmail());

        // sino existe en la BD retorno null
        if( foundUser == null){
            return loggedUser = null;
        }

        // si los passwords coinciden retorno los datos del usuario de la base de datos:
        if (userFromRequest.getPassword().equals(foundUser.getPassword())){

            // cargo de a uno los datos para no retornar al front la contraseña:
            loggedUser.setUserId(foundUser.getUserId());
            loggedUser.setUsername(foundUser.getUsername());
            loggedUser.setEmail(foundUser.getEmail());

        } else {
            // si los passwords no coinciden tambien retorno null
            loggedUser = null;
        }

        return loggedUser;
    }
}
