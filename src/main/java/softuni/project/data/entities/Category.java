package softuni.project.data.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "categories")
public class Category extends BaseEntity {

    @Column(name = "category_name", nullable = false, unique = true)
    private String name;
    @Column(name = "description", nullable = false, unique = true, columnDefinition = "text")
    private String description;
    @Column(name = "image_url", nullable = false)
    private String image;
    @Column(name = "category_url", nullable = false, unique = true)
    private String categoryUrl;


}
