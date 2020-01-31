package softuni.project.web.models.view;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
@NoArgsConstructor
public class OfferViewModel {
    private String id;
    private String name;
    private String image;
    private String price;
    private String region;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private String startsOn;

    private String endsOn;
}
