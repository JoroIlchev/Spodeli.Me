package softuni.project.data.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import softuni.project.data.entities.enums.Region;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
abstract class BaseOffer extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "description", nullable = false, columnDefinition = "text")
    private String description;
    @Column(name = "image_url")
    private String image;
    @Column(name = "starts_on", nullable = false)
    private LocalDate startsOn;
    @Column(name = "ends_on", nullable = false)
    private LocalDate endsOn;
    @Column(name = "price", nullable = false)
    private BigDecimal price;
    @Enumerated(EnumType.STRING)
    @Column(name = "region", nullable = false)
    private Region region;
    @Column(name = "is_active")
    private Boolean isActive;
    @Column(name = "is_approved")
    private Boolean isApproved;

    @Column(name = "owner_phone_number", nullable = false)
    private String ownerPhoneNumber;

    @OneToOne()
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne()
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "time_stamp",nullable = false, updatable = false)
    @CreationTimestamp
    private Date timeStamp;

}
