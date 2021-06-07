package ge.bog.conversion.service;

import ge.bog.conversion.domain.User;
import ge.bog.conversion.model.UserDto;
import ge.bog.conversion.ropository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private UserRepository userRepository;

    @Override
    public String createUser(UserDto userDto) {
        Optional<User> user = userRepository.findById(userDto.getUserName());

        if (user.isEmpty()) {
            User newUser = new User();
            newUser.setUserName(userDto.getUserName());
            newUser.setFirstName(userDto.getFirstName());
            newUser.setLastName(userDto.getLastName());

            userRepository.save(newUser);
            return "Success!";
        } else {
            throw new RuntimeException("User By UserName " + userDto.getUserName() + " Already Exists!");
        }
    }
}
