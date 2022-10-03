package rikkei.academy.service.user;

import rikkei.academy.config.ConnectMySQL;
import rikkei.academy.model.Role;
import rikkei.academy.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class UserServiceIMPL implements IUserService {
    private Connection connection = ConnectMySQL.getConnection();
    private static final String CREATE_USER = "INSERT INTO users(name,username,email,password) VALUES (?,?,?,?);";
    private static final String INSERT_ROLE = "INSERT INTO user_role(user_id,role_id) VALUES (?,?);";

    private static final String FIND_ALL_USER = "SELECT username FROM users;";
    private static final String FIND_ALL_EMAIL = "SELECT email FROM  users;";
    private static final String SELECT_USER = "SELECT * FROM users WHERE username = ? and password = ?";

    @Override

    public boolean exitedByUserName(String username) {
        List<String> listUserName = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_USER);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                listUserName.add(resultSet.getString("username"));
                for (int i = 0; i < listUserName.size(); i++) {
                    if (username.equals(listUserName.get(i))) {
                        return true;
                    }
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return false;
    }

    @Override
    public boolean exitedByEmail(String email) {
        List<String> listEmail = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_EMAIL);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                listEmail.add(resultSet.getString("email"));
                for (int i = 0; i < listEmail.size(); i++) {
                    if (email.equals(listEmail.get(i))) {
                        return true;
                    }

                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    @Override
    public User findByUserNameAndPassWord(String username, String password) {
        try {
            PreparedStatement statement = connection.prepareStatement(SELECT_USER);
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            User user = null;
            if (resultSet.next()) {
                user = new User();

                user.setUsername(username);
                user.setPassword(password);
                return user;


            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;


    }

    @Override
    public void save(User user) {
        try {
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement(CREATE_USER, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getUsername());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setString(4, user.getPassword());
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            int user_id = 0;
            while (resultSet.next()) {
                user_id = resultSet.getInt(1);
            }
            PreparedStatement preparedStatement1 = connection.prepareStatement(INSERT_ROLE);
            Set<Role> roles = user.getRoles();
            List<Role> roleList = new ArrayList<>(roles);
            for (int i = 0; i < roleList.size(); i++) {
                preparedStatement1.setInt(1, user_id);
                preparedStatement1.setInt(2, roleList.get(i).getId());
                preparedStatement1.executeUpdate();
            }
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
