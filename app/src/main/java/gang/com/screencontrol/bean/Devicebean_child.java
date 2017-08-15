package gang.com.screencontrol.bean;

import java.util.List;

/**
 * Created by xiaogangzai on 2017/8/15.
 */

public class Devicebean_child {

    /**
     * body : {"infoList":[{"URL":"","addressIp":"10.3.16.55","controlStreamingServerId":-1,"description":"","deviceExtraInfo":"","deviceFolderId":1,"deviceId":102,"deviceName":"debug","deviceStatus":5,"deviceStatusProto":"","deviceType":1,"password":"Admin","port":6028,"protoType":3,"screenInfoProto":"{\"screenInfo\":[{\"LT_x\":0,\"LT_y\":0,\"RB_x\":1920,\"RB_y\":1080}]}","sourceinfolist":{"sourceinfo":[{"URL":"C:\\Users\\Public\\Desktop\\SMART Transmitter.lnk","description":"","height":0,"sourceId":103,"sourceName":"SMART Transmitter.lnk","sourceType":46,"width":0}]},"userName":""},{"URL":"","addressIp":"10.3.8.53","controlStreamingServerId":-1,"description":"","deviceExtraInfo":"","deviceFolderId":1,"deviceId":108,"deviceName":"mail","deviceStatus":5,"deviceStatusProto":"","deviceType":1,"password":"Admin","port":6028,"protoType":3,"screenInfoProto":"{\"screenInfo\":[{\"LT_x\":0,\"LT_y\":0,\"RB_x\":1280,\"RB_y\":1024}]}","sourceinfolist":{"sourceinfo":[{"URL":"<Source ClsID = \"17CCA71B-ECD7-11D0-B908-00A0C9223196\"  DevicePath = \"\\\\?\\usb#vid_0bda&amp;pid_579c&amp;mi_00#7&amp;d4d66ce&amp;0&amp;0000#{65e8773d-8f56-11d0-a3b9-00a0c9223196}\\global\" Channel = \"0\"/><\/Source>","description":"","height":0,"sourceId":109,"sourceName":"Lenovo EasyCamera","sourceType":47,"width":0},{"URL":"C:\\Users\\Public\\Desktop\\有道词典.lnk","description":"","height":0,"sourceId":159,"sourceName":"有道词典.lnk","sourceType":46,"width":0},{"URL":"C:\\Users\\Public\\Desktop\\foobar2000.lnk","description":"","height":0,"sourceId":160,"sourceName":"foobar2000.lnk","sourceType":46,"width":0},{"URL":"C:\\Users\\Public\\Desktop\\Lenovo EasyCapture.lnk","description":"","height":0,"sourceId":161,"sourceName":"Lenovo EasyCapture.lnk","sourceType":46,"width":0}]},"userName":""}]}
     * category : 0
     * errorCode : 0
     * errorStr : OK
     * guid : M-134
     * type : GETDEVICELIST
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
        private List<InfoListBean> infoList;

        public List<InfoListBean> getInfoList() {
            return infoList;
        }

        public void setInfoList(List<InfoListBean> infoList) {
            this.infoList = infoList;
        }

        public static class InfoListBean {
            /**
             * URL :
             * addressIp : 10.3.16.55
             * controlStreamingServerId : -1
             * description :
             * deviceExtraInfo :
             * deviceFolderId : 1
             * deviceId : 102
             * deviceName : debug
             * deviceStatus : 5
             * deviceStatusProto :
             * deviceType : 1
             * password : Admin
             * port : 6028
             * protoType : 3
             * screenInfoProto : {"screenInfo":[{"LT_x":0,"LT_y":0,"RB_x":1920,"RB_y":1080}]}
             * sourceinfolist : {"sourceinfo":[{"URL":"C:\\Users\\Public\\Desktop\\SMART Transmitter.lnk","description":"","height":0,"sourceId":103,"sourceName":"SMART Transmitter.lnk","sourceType":46,"width":0}]}
             * userName :
             */

            private String URL;
            private String addressIp;
            private int controlStreamingServerId;
            private String description;
            private String deviceExtraInfo;
            private int deviceFolderId;
            private int deviceId;
            private String deviceName;
            private int deviceStatus;
            private String deviceStatusProto;
            private int deviceType;
            private String password;
            private int port;
            private int protoType;
            private String screenInfoProto;
            private SourceinfolistBean sourceinfolist;
            private String userName;

            public String getURL() {
                return URL;
            }

            public void setURL(String URL) {
                this.URL = URL;
            }

            public String getAddressIp() {
                return addressIp;
            }

            public void setAddressIp(String addressIp) {
                this.addressIp = addressIp;
            }

            public int getControlStreamingServerId() {
                return controlStreamingServerId;
            }

            public void setControlStreamingServerId(int controlStreamingServerId) {
                this.controlStreamingServerId = controlStreamingServerId;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public String getDeviceExtraInfo() {
                return deviceExtraInfo;
            }

            public void setDeviceExtraInfo(String deviceExtraInfo) {
                this.deviceExtraInfo = deviceExtraInfo;
            }

            public int getDeviceFolderId() {
                return deviceFolderId;
            }

            public void setDeviceFolderId(int deviceFolderId) {
                this.deviceFolderId = deviceFolderId;
            }

            public int getDeviceId() {
                return deviceId;
            }

            public void setDeviceId(int deviceId) {
                this.deviceId = deviceId;
            }

            public String getDeviceName() {
                return deviceName;
            }

            public void setDeviceName(String deviceName) {
                this.deviceName = deviceName;
            }

            public int getDeviceStatus() {
                return deviceStatus;
            }

            public void setDeviceStatus(int deviceStatus) {
                this.deviceStatus = deviceStatus;
            }

            public String getDeviceStatusProto() {
                return deviceStatusProto;
            }

            public void setDeviceStatusProto(String deviceStatusProto) {
                this.deviceStatusProto = deviceStatusProto;
            }

            public int getDeviceType() {
                return deviceType;
            }

            public void setDeviceType(int deviceType) {
                this.deviceType = deviceType;
            }

            public String getPassword() {
                return password;
            }

            public void setPassword(String password) {
                this.password = password;
            }

            public int getPort() {
                return port;
            }

            public void setPort(int port) {
                this.port = port;
            }

            public int getProtoType() {
                return protoType;
            }

            public void setProtoType(int protoType) {
                this.protoType = protoType;
            }

            public String getScreenInfoProto() {
                return screenInfoProto;
            }

            public void setScreenInfoProto(String screenInfoProto) {
                this.screenInfoProto = screenInfoProto;
            }

            public SourceinfolistBean getSourceinfolist() {
                return sourceinfolist;
            }

            public void setSourceinfolist(SourceinfolistBean sourceinfolist) {
                this.sourceinfolist = sourceinfolist;
            }

            public String getUserName() {
                return userName;
            }

            public void setUserName(String userName) {
                this.userName = userName;
            }

            public static class SourceinfolistBean {
                private List<SourceinfoBean> sourceinfo;

                public List<SourceinfoBean> getSourceinfo() {
                    return sourceinfo;
                }

                public void setSourceinfo(List<SourceinfoBean> sourceinfo) {
                    this.sourceinfo = sourceinfo;
                }

                public static class SourceinfoBean {
                    /**
                     * URL : C:\Users\Public\Desktop\SMART Transmitter.lnk
                     * description :
                     * height : 0
                     * sourceId : 103
                     * sourceName : SMART Transmitter.lnk
                     * sourceType : 46
                     * width : 0
                     */

                    private String URL;
                    private String description;
                    private int height;
                    private int sourceId;
                    private String sourceName;
                    private int sourceType;
                    private int width;

                    public String getURL() {
                        return URL;
                    }

                    public void setURL(String URL) {
                        this.URL = URL;
                    }

                    public String getDescription() {
                        return description;
                    }

                    public void setDescription(String description) {
                        this.description = description;
                    }

                    public int getHeight() {
                        return height;
                    }

                    public void setHeight(int height) {
                        this.height = height;
                    }

                    public int getSourceId() {
                        return sourceId;
                    }

                    public void setSourceId(int sourceId) {
                        this.sourceId = sourceId;
                    }

                    public String getSourceName() {
                        return sourceName;
                    }

                    public void setSourceName(String sourceName) {
                        this.sourceName = sourceName;
                    }

                    public int getSourceType() {
                        return sourceType;
                    }

                    public void setSourceType(int sourceType) {
                        this.sourceType = sourceType;
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
}
