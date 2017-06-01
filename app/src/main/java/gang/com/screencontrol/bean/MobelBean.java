package gang.com.screencontrol.bean;

import java.util.List;

/**
 * Created by xiaogangzai on 2017/5/31.
 */

public class MobelBean {


    private List<BasicInfoBean> basicInfo;

    public List<BasicInfoBean> getBasicInfo() {
        return basicInfo;
    }

    public void setBasicInfo(List<BasicInfoBean> basicInfo) {
        this.basicInfo = basicInfo;
    }

    public static class BasicInfoBean {
        /**
         * ID : 266
         * description :
         * name : 测试
         * playtime : 300
         */

        private int ID;
        private String description;
        private String name;
        private int playtime;

        public int getID() {
            return ID;
        }

        public void setID(int ID) {
            this.ID = ID;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getPlaytime() {
            return playtime;
        }

        public void setPlaytime(int playtime) {
            this.playtime = playtime;
        }
    }
}
