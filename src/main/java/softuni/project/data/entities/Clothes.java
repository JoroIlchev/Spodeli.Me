package softuni.project.data.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "clothes")
public class Clothes extends BaseOffer {


    @Column(name = "size", nullable = false)
    private String size;
    @Column(name = "entity_condition", nullable = false)
    private String entityCondition;
    @Column(name = "deposit_needed")
    private Boolean depositNeeded;



}
