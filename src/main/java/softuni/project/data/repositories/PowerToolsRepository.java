package softuni.project.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.project.data.entities.PowerTools;

import java.util.List;

@Repository
public interface PowerToolsRepository extends JpaRepository<PowerTools, String > {

    List<PowerTools> findAllByIsActiveTrueAndIsApprovedIsTrueOrderByTimeStampDesc();
    List<PowerTools> findAllByOrderByTimeStampDesc();
    List<PowerTools> findAllByUser_IdOrderByTimeStampDesc(String userId);

}
