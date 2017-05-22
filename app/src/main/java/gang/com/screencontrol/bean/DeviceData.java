package gang.com.screencontrol.bean;

/**
 * Created by xiaogangzai on 2017/5/22.
 */

public class DeviceData {
    private int img;
    private String type;

    public DeviceData(int img, String type) {
        this.img = img;
        this.type = type;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
