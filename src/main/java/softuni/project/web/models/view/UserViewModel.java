package softuni.project.web.models.view;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class UserViewModel {
    private String id;
    private String username;
    private String password;
    private String email;

    private Set<String> authorities;
}
