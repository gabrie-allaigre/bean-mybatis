import com.talanlabs.bean.mybatis.annotation.Column;
import com.talanlabs.bean.mybatis.annotation.Id;
import com.talanlabs.bean.mybatis.annotation.Version;

public class StateBean {

    @Id
    @Column(name = "ID")
    private String id;
    @Version
    @Column(name = "VERSION")
    private int version;
    @Column(name = "CODE")
    private String code;
    @Column(name = "COUNTRY_ID")
    private String countryId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }
}
