import com.talanlabs.bean.mybatis.annotation.Association;
import com.talanlabs.bean.mybatis.annotation.Collection;
import com.talanlabs.bean.mybatis.annotation.Column;
import com.talanlabs.bean.mybatis.annotation.Id;
import com.talanlabs.bean.mybatis.annotation.NlsColumn;
import com.talanlabs.bean.mybatis.annotation.OrderBy;
import com.talanlabs.bean.mybatis.annotation.Version;
import com.talanlabs.bean.mybatis.helper.IId;

import java.util.List;

public class CountryBean {

    @Id
    @Column(name = "ID")
    private String id;
    @Version
    @Column(name = "VERSION")
    private int version;
    @Column(name = "CODE")
    private String code;
    @NlsColumn(name = "NAME")
    private String name;
    @Column(name = "CONTINENT_ID")
    private IId continentId;
    @Association(propertySource = "continentId")
    private ContinentBean continent;
    @Collection(propertyTarget = "countryId", orderBy = @OrderBy("code"))
    private List<StateBean> states;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public IId getContinentId() {
        return continentId;
    }

    public void setContinentId(IId continentId) {
        this.continentId = continentId;
    }

    public ContinentBean getContinent() {
        return continent;
    }

    public void setContinent(ContinentBean continent) {
        this.continent = continent;
    }

    public List<StateBean> getStates() {
        return states;
    }

    public void setStates(List<StateBean> states) {
        this.states = states;
    }
}
