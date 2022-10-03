package rikkei.academy.service.user;

import rikkei.academy.model.User;
import rikkei.academy.service.IGenericService;

public interface IUserService extends IGenericService<User> {
boolean exitedByUserName (String username);
boolean exitedByEmail (String email);
User findByUserNameAndPassWord(String username,String password);
}
