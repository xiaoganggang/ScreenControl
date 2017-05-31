package gang.com.screencontrol.bean;

import java.util.List;

/**
 * Created by xiaogangzai on 2017/5/31.
 */

public class MobelBean {


    /**
     * body : {"basicInfo":[{"ID":125,"description":"","name":"few","playtime":300},{"ID":172,"description":"ff","name":"fe","playtime":300},{"ID":174,"description":"fw","name":"dew","playtime":30},{"ID":183,"description":"","name":"ge","playtime":300},{"ID":191,"description":"Test 1","name":"Test1","playtime":300},{"ID":195,"description":"Add Model","name":"Mode1","playtime":300}]}
     * category : 0
     * errorCode : 0
     * errorStr : OK
     * guid : M-18
     * type : SEARCHPROGRAMBASICINFO
     */

    private BodyBean body;
    private int category;
    private int errorCode;
    private String errorStr;
    private String guid;
    private String type;

    public BodyBean getBody() {
        return body;
    }

    public void setBody(BodyBean body) {
        this.body = body;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorStr() {
        return errorStr;
    }

    public void setErrorStr(String errorStr) {
        this.errorStr = errorStr;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static class BodyBean {
        private List<BasicInfoBean> basicInfo;

        public List<BasicInfoBean> getBasicInfo() {
            return basicInfo;
        }

        public void setBasicInfo(List<BasicInfoBean> basicInfo) {
            this.basicInfo = basicInfo;
        }

        public static class BasicInfoBean {
            /**
             * ID : 125
             * description :
             * name : few
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
}
