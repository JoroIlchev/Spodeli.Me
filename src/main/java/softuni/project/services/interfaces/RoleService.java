package softuni.project.services.interfaces;

import softuni.project.services.models.RoleServiceModel;

import java.util.Set;

public interface RoleService {

    void seedRoles();
//    void assignUserRole(UserServiceModel userServiceModel, long numberOfUsers);

    Set<RoleServiceModel> findAll();
    RoleServiceModel findByAuthority(String authority);
}
