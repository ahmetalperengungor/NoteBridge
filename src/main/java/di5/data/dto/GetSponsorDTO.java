package di5.data.dto;

public class GetSponsorDTO {
    private String id;
    private String commercialName;
    private String price;
    private String agreementDate;
    private String agreementPostCount;

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCommercialName() {
        return commercialName;
    }

    public void setCommercialName(String commercialName) {
        this.commercialName = commercialName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAgreementDate() {
        return agreementDate;
    }

    public void setAgreementDate(String agreementDate) {
        this.agreementDate = agreementDate;
    }

    public String getAgreementPostCount() {
        return agreementPostCount;
    }

    public void setAgreementPostCount(String agreementPostCount) {
        this.agreementPostCount = agreementPostCount;
    }
}
