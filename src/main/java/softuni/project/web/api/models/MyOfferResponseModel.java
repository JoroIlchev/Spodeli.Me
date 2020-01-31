package softuni.project.web.api.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

@Getter
@Setter
@NoArgsConstructor
public class MyOfferResponseModel {

    private String id;
    private String image;
    private String name;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private String startsOn;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private String endsOn;
    @NumberFormat(pattern = "##0.00")
    private String price;
    private String isActive;
    private String isApproved;
    private String categoryUrl;
}
