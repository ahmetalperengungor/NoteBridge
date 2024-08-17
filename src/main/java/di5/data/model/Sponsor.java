package di5.data.model;

public class Sponsor extends BaseEntity {
    private String commercialName;
    private String price;
    private String agreementDate;
    private String agreementPostCount;

    public Sponsor() {
        commercialName = null;
        price = null;
        agreementDate = null;
        agreementPostCount = null;
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

    public void setAgreementPostCount(String agreementPostcount) {
        this.agreementPostCount = agreementPostcount;
    }
}
