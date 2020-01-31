package softuni.project.web.api.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

@Getter
@Setter
@NoArgsConstructor
public class AllOffersResponseModel {
    private String id;
    private String name;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private String startsOn;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private String endsOn;
    @NumberFormat(pattern = "##0.00")
    private String price;
    private Boolean isActive;
    private Boolean isApproved;
    private String categoryUrl;
}
