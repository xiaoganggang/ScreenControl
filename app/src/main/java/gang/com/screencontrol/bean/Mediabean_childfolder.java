package gang.com.screencontrol.bean;

import java.util.List;

/**
 * Created by xiaogangzai on 2017/6/5.
 */

public class Mediabean_childfolder {

    /**
     * body : {"folderInfo":[{"folderDescription":"video","folderID":11,"folderName":"Video","hasChild":false,"hasMedia":true,"parentID":0,"readOnly":true},{"folderDescription":"image","folderID":12,"folderName":"Image","hasChild":false,"hasMedia":true,"parentID":0,"readOnly":true},{"folderDescription":"office","folderID":13,"folderName":"Office","hasChild":false,"hasMedia":true,"parentID":0,"readOnly":true},{"folderDescription":"Web","folderID":14,"folderName":"Web","hasChild":false,"hasMedia":true,"parentID":0,"readOnly":true},{"folderDescription":"Frame","folderID":15,"folderName":"Frame","hasChild":false,"hasMedia":true,"parentID":0,"readOnly":true},{"folderDescription":"Empty Layer","folderID":18,"folderName":"Empty Layer","hasChild":true,"hasMedia":false,"parentID":0,"readOnly":true}],"parentFolderId":0}
     * category : 0
     * errorCode : 0
     * errorStr : OK
     * guid : M-36
     * type : GETCHILDMEDIAFOLDERLIST
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
        /**
         * folderInfo : [{"folderDescription":"video","folderID":11,"folderName":"Video","hasChild":false,"hasMedia":true,"parentID":0,"readOnly":true},{"folderDescription":"image","folderID":12,"folderName":"Image","hasChild":false,"hasMedia":true,"parentID":0,"readOnly":true},{"folderDescription":"office","folderID":13,"folderName":"Office","hasChild":false,"hasMedia":true,"parentID":0,"readOnly":true},{"folderDescription":"Web","folderID":14,"folderName":"Web","hasChild":false,"hasMedia":true,"parentID":0,"readOnly":true},{"folderDescription":"Frame","folderID":15,"folderName":"Frame","hasChild":false,"hasMedia":true,"parentID":0,"readOnly":true},{"folderDescription":"Empty Layer","folderID":18,"folderName":"Empty Layer","hasChild":true,"hasMedia":false,"parentID":0,"readOnly":true}]
         * parentFolderId : 0
         */

        private int parentFolderId;
        private List<FolderInfoBean> folderInfo;

        public int getParentFolderId() {
            return parentFolderId;
        }

        public void setParentFolderId(int parentFolderId) {
            this.parentFolderId = parentFolderId;
        }

        public List<FolderInfoBean> getFolderInfo() {
            return folderInfo;
        }

        public void setFolderInfo(List<FolderInfoBean> folderInfo) {
            this.folderInfo = folderInfo;
        }

        public static class FolderInfoBean {
            /**
             * folderDescription : video
             * folderID : 11
             * folderName : Video
             * hasChild : false
             * hasMedia : true
             * parentID : 0
             * readOnly : true
             */

            private String folderDescription;
            private int folderID;
            private String folderName;
            private boolean hasChild;
            private boolean hasMedia;
            private int parentID;
            private boolean readOnly;

            public String getFolderDescription() {
                return folderDescription;
            }

            public void setFolderDescription(String folderDescription) {
                this.folderDescription = folderDescription;
            }

            public int getFolderID() {
                return folderID;
            }

            public void setFolderID(int folderID) {
                this.folderID = folderID;
            }

            public String getFolderName() {
                return folderName;
            }

            public void setFolderName(String folderName) {
                this.folderName = folderName;
            }

            public boolean isHasChild() {
                return hasChild;
            }

            public void setHasChild(boolean hasChild) {
                this.hasChild = hasChild;
            }

            public boolean isHasMedia() {
                return hasMedia;
            }

            public void setHasMedia(boolean hasMedia) {
                this.hasMedia = hasMedia;
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
        }
    }
}
