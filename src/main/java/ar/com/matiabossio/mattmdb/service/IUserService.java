package ar.com.matiabossio.mattmdb.service;

import ar.com.matiabossio.mattmdb.domain.User;

import java.util.Collection;

public interface IUserService {
    public Collection<User> getUsersService();
    public User getUserByIdService(Integer userId);
    public User createUserService(User userFromRequest);
    public User updateUserService(User userFromRequest);
    public User deleteUserService(Integer userId);
    public User loginUserService(User userFromRequest);
}
