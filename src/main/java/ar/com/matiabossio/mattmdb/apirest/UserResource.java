package ar.com.matiabossio.mattmdb.apirest;

// La documentacion que hay que revisar es la de rest easy www.resteasy.dev/docs


import ar.com.matiabossio.mattmdb.util.Message;
import ar.com.matiabossio.mattmdb.domain.User;
import ar.com.matiabossio.mattmdb.service.IUserService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Collection;


    /*************************************
     *            /api/v1/users          *
     *************************************/
@Path("/users")
public class UserResource {

// inyectando la interface ya no hay que importar e instanciar la clase UserServiceImpl, la misma ya queda instanciada y lista para usar (usar la interface y no la clase hace que pueda tener disponibles instancias de todas las clases que implementan la Interface):
@Inject
private IUserService userService;
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsers() {

        // al tener la inyeccion de dependencia no hace falta hacer la instancia de userService:
        Collection<User> userList = userService.getUsersService();
        return Response.ok().status(Response.Status.OK).entity(userList).build();
    }


    /*************************************
     *      /api/v1/users/{userId}       *
     *************************************/
    @Path("/{userId}")      // entre {} hago que sea dinamico (es el params de express)
    @GET
    // @Consumes(MediaType.APPLICATION_JSON)    // no consume porque no lo recibe en el body, sino en la URL.
    @Produces(MediaType.APPLICATION_JSON)

    // como lo que recibo por params es un String lo tengo que castear a Integer:
    public Response getUserById(@PathParam("userId") Integer userId){

        User user = userService.getUserByIdService(userId);

        /*
           sin inyeccion de dependendias seria asi:
           User user = new UserServiceImpl().getUserByIdService(userId);
        */

        // genero la logica para retornar un mensaje y poder mostarlo en el front:

        if (user == null) {

            Message message = new Message("User id " + userId + " not found", 404, false, user);

            return Response.ok().status(Response.Status.NOT_FOUND).entity(message).build();
        }

        Message message = new Message("User id " + userId + " found OK", 200, true, user);

        return Response.ok().status(Response.Status.OK).entity(message).build();

    }


    // ----- BUG: esta ruta entra en conflicto con la del getUserById a veces anda y a veces no --------

        /*************************************
         *    LOGIN  /api/v1/users/login     *
         *************************************/
    @Path("/login")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    // recibe por parametro un User que viene en el body del request gracias al @Consumes:
    public Response loginUser(User userFromRequest){

        // gracias al injection ya tengo la instancia userService para usarla:
        User loggedUser = userService.loginUserService(userFromRequest);

        if (loggedUser == null){
            Message message = new Message("Please check your login credentials", 401, false, loggedUser);

            return Response.ok().status(Response.Status.UNAUTHORIZED).entity(message).build();
        }

        Message message = new Message("User logged OK", 200, true, loggedUser);

        // si respondo con NO_CONTENT (204) no retorna los datos del usuario loggeado
        return Response.ok().status(Response.Status.OK).entity(message).build();


    }


        /*************************************
         *  REGISTER  /api/v1/users          *
         *************************************/

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)

    // recibe por parametro un User que viene en el body del request gracias al @Consumes:
    public Response createUser(User userFromRequest){

        // gracias al injection ya tengo la instancia userService para usarla:
        User createdUser = userService.createUserService(userFromRequest);

        Message message = new Message("User created OK", 201, true, createdUser);

        return Response.ok().status(Response.Status.CREATED).entity(message).build();
    }

    /*************************************
    *      /api/v1/users/{userId}       *
    *************************************/

    @Path("/{userId}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)

    // recibe por parametro un User que viene en el body del request gracias al @Consumes:
    public Response updateUser(@PathParam("userId") Integer userId, User userFromRequest){

        // agrego al user que viene en el body el id que viene por params:

        userFromRequest.setUserId(userId);

        // gracias al injection ya tengo la instancia userService para usarla:
        User updatedUser = userService.updateUserService(userFromRequest);

        if (updatedUser == null){
            Message message = new Message("User not found", 404, false, updatedUser);

            return Response.ok().status(Response.Status.NOT_FOUND).entity(message).build();
        }

        Message message = new Message("User updated OK", 200, true, updatedUser);
        return Response.ok().status(Response.Status.OK).entity(message).build();
    }


        /*************************************
         *      /api/v1/users/{userId}       *
         *************************************/

        @Path("/{userId}")
        @DELETE
        // @Consumes(MediaType.APPLICATION_JSON)    // no consume porque no lo recibe en el body, sino en la URL.
        @Produces(MediaType.APPLICATION_JSON)

        public Response deleteUser(@PathParam("userId") Integer userId){

            // gracias al injection ya tengo la instancia userService para usarla:
            User deletedUser = userService.deleteUserService(userId);

            if (deletedUser == null){
                Message message = new Message("User not found", 404, false, deletedUser);

                return Response.ok().status(Response.Status.NOT_FOUND).entity(message).build();
            }

            Message message = new Message("User deleted OK", 200, true, deletedUser);

            // si respondo con NO_CONTENT (204) no retorna los datos del usuario eliminado
            return Response.ok().status(Response.Status.OK).entity(message).build();

        }

}