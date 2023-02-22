package ar.com.matiabossio.mattmdb.dao;

import ar.com.matiabossio.mattmdb.domain.User;

import java.util.Collection;

public interface IUserDao {
    public Collection<User> getUsers();
    public User getUserById(Integer userId);
    public User getUserByEmail(String email);
    public User createUser(User user);
    public User upateUser(User user);
    public User deleteUserById(Integer userId);

}
