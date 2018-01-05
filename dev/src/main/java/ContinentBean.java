import com.talanlabs.bean.mybatis.annotation.Column;
import com.talanlabs.bean.mybatis.annotation.Id;
import com.talanlabs.bean.mybatis.annotation.NlsColumn;
import com.talanlabs.bean.mybatis.annotation.Version;

public class ContinentBean {

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
}
