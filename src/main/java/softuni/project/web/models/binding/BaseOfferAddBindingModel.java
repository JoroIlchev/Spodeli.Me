package softuni.project.web.models.binding;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
abstract class BaseOfferAddBindingModel {


    private String id;
    private String name;
    private String description;
    private MultipartFile image;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startsOn;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate endsOn;
    private BigDecimal price;
    private String region;
    private String ownerPhoneNumber;
}
