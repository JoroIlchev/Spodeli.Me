package softuni.project.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "User name is not free")
public class UserNameTakenException extends RuntimeException {
    public UserNameTakenException() {
    }
    public UserNameTakenException(String message) {
        super(message);
    }
}
