package softuni.project.services.implementations;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.project.data.entities.Role;
import softuni.project.data.repositories.RoleRepository;
import softuni.project.services.interfaces.RoleService;
import softuni.project.services.models.RoleServiceModel;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository, ModelMapper modelMapper) {
        this.roleRepository = roleRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void seedRoles() {
        if (roleRepository.count() == 0) {
            roleRepository.saveAndFlush(new Role("ROLE_USER"));
            roleRepository.saveAndFlush(new Role("ROLE_ADMIN"));
            roleRepository.saveAndFlush(new Role("ROLE_MODERATOR"));
            roleRepository.saveAndFlush(new Role("ROLE_ROOT"));
        }
    }

    @Override
    public Set<RoleServiceModel> findAll() {
        return roleRepository.findAll().stream()
                .map(r -> modelMapper.map(r, RoleServiceModel.class))
                .collect(Collectors.toSet());
    }

    @Override
    public RoleServiceModel findByAuthority(String authority) {
        return modelMapper.map(roleRepository.findByAuthority(authority), RoleServiceModel.class);
    }
}
