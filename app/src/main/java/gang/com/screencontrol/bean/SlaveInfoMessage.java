package gang.com.screencontrol.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xiaogangzai on 2017/7/13.
 */

public class SlaveInfoMessage implements Serializable{

    /**
     * body : {"slaveInfo":[{"captureCount":0,"deviceType":1,"flag":"J1900-PC","groupid":101,"id":101,"ip":"192.168.12.4","left":0,"mac":"00-ee-99-55-44-de","monitorInfo":[{"height":1080,"left":0,"top":0,"width":1920}],"online":true,"top":0}]}
     * category : 0
     * errorCode : 0
     * errorStr : OK
     * guid : M-44
     * type : LOADVIDEOWALLINFO
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
        private List<SlaveInfoBean> slaveInfo;

        public List<SlaveInfoBean> getSlaveInfo() {
            return slaveInfo;
        }

        public void setSlaveInfo(List<SlaveInfoBean> slaveInfo) {
            this.slaveInfo = slaveInfo;
        }

        public static class SlaveInfoBean {
            /**
             * captureCount : 0
             * deviceType : 1
             * flag : J1900-PC
             * groupid : 101
             * id : 101
             * ip : 192.168.12.4
             * left : 0
             * mac : 00-ee-99-55-44-de
             * monitorInfo : [{"height":1080,"left":0,"top":0,"width":1920}]
             * online : true
             * top : 0
             */

            private int captureCount;
            private int deviceType;
            private String flag;
            private int groupid;
            private int id;
            private String ip;
            private int left;
            private String mac;
            private boolean online;
            private int top;
            private List<MonitorInfoBean> monitorInfo;

            public int getCaptureCount() {
                return captureCount;
            }

            public void setCaptureCount(int captureCount) {
                this.captureCount = captureCount;
            }

            public int getDeviceType() {
                return deviceType;
            }

            public void setDeviceType(int deviceType) {
                this.deviceType = deviceType;
            }

            public String getFlag() {
                return flag;
            }

            public void setFlag(String flag) {
                this.flag = flag;
            }

            public int getGroupid() {
                return groupid;
            }

            public void setGroupid(int groupid) {
                this.groupid = groupid;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getIp() {
                return ip;
            }

            public void setIp(String ip) {
                this.ip = ip;
            }

            public int getLeft() {
                return left;
            }

            public void setLeft(int left) {
                this.left = left;
            }

            public String getMac() {
                return mac;
            }

            public void setMac(String mac) {
                this.mac = mac;
            }

            public boolean isOnline() {
                return online;
            }

            public void setOnline(boolean online) {
                this.online = online;
            }

            public int getTop() {
                return top;
            }

            public void setTop(int top) {
                this.top = top;
            }

            public List<MonitorInfoBean> getMonitorInfo() {
                return monitorInfo;
            }

            public void setMonitorInfo(List<MonitorInfoBean> monitorInfo) {
                this.monitorInfo = monitorInfo;
            }

            public static class MonitorInfoBean {
                /**
                 * height : 1080
                 * left : 0
                 * top : 0
                 * width : 1920
                 */

                private int height;
                private int left;
                private int top;
                private int width;

                public int getHeight() {
                    return height;
                }

                public void setHeight(int height) {
                    this.height = height;
                }

                public int getLeft() {
                    return left;
                }

                public void setLeft(int left) {
                    this.left = left;
                }

                public int getTop() {
                    return top;
                }

                public void setTop(int top) {
                    this.top = top;
                }

                public int getWidth() {
                    return width;
                }

                public void setWidth(int width) {
                    this.width = width;
                }
            }
        }
    }
}
