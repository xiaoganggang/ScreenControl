package gang.com.screencontrol.bean;

import java.util.List;

/**
 * Created by xiaogangzai on 2017/8/15.
 */

public class Devicebean_childfolder {

    /**
     * body : {"deviceFolderInfo":[{"Description":"","Name":"Controller","bDisplay":true,"folderID":184,"hasChild":false,"hasDevice":false,"parentID":1,"readOnly":false,"type":1}]}
     * category : 0
     * errorCode : 0
     * errorStr : OK
     * guid : M-117
     * type : GETDEVICETYPEFOLDERLIST
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
        private List<DeviceFolderInfoBean> deviceFolderInfo;

        public List<DeviceFolderInfoBean> getDeviceFolderInfo() {
            return deviceFolderInfo;
        }

        public void setDeviceFolderInfo(List<DeviceFolderInfoBean> deviceFolderInfo) {
            this.deviceFolderInfo = deviceFolderInfo;
        }

        public static class DeviceFolderInfoBean {
            /**
             * Description :
             * Name : Controller
             * bDisplay : true
             * folderID : 184
             * hasChild : false
             * hasDevice : false
             * parentID : 1
             * readOnly : false
             * type : 1
             */

            private String Description;
            private String Name;
            private boolean bDisplay;
            private int folderID;
            private boolean hasChild;
            private boolean hasDevice;
            private int parentID;
            private boolean readOnly;
            private int type;

            public String getDescription() {
                return Description;
            }

            public void setDescription(String Description) {
                this.Description = Description;
            }

            public String getName() {
                return Name;
            }

            public void setName(String Name) {
                this.Name = Name;
            }

            public boolean isBDisplay() {
                return bDisplay;
            }

            public void setBDisplay(boolean bDisplay) {
                this.bDisplay = bDisplay;
            }

            public int getFolderID() {
                return folderID;
            }

            public void setFolderID(int folderID) {
                this.folderID = folderID;
            }

            public boolean isHasChild() {
                return hasChild;
            }

            public void setHasChild(boolean hasChild) {
                this.hasChild = hasChild;
            }

            public boolean isHasDevice() {
                return hasDevice;
            }

            public void setHasDevice(boolean hasDevice) {
                this.hasDevice = hasDevice;
            }

            public int getParentID() {
                return parentID;
            }

            public void setParentID(int parentID) {
                this.parentID = parentID;
            }

            public boolean isReadOnly() {
                return readOnly;
            }

            public void setReadOnly(boolean readOnly) {
                this.readOnly = readOnly;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }
        }
    }
}
