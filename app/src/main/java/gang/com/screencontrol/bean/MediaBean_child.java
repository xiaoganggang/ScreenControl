package gang.com.screencontrol.bean;


import java.util.List;

/**
 * Created by xiaogangzai on 2017/6/1.
 * 每个媒体子文件中中子文件
 */

public class MediaBean_child {

    /**
     * body : {"infolist":[{"InLocalPath":"C:\\Users\\Public\\Videos\\Sample Videos\\wildlife.wmv","InServerPath":"","bHasThumbnailPix":false,"description":"","durationtime":"","fileId":172,"fileName":"wildlife.wmv","folderId":1,"hasInfo":"false","height":0,"percent":0,"size":0,"type":0,"uploadtime":"2016/05/04 18:30","width":0}]}
     * category : 0
     * errorCode : 0
     * errorStr : OK
     * guid : M-42
     * type : GETMEDIAFILELIST
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
        private List<InfolistBean> infolist;

        public List<InfolistBean> getInfolist() {
            return infolist;
        }

        public void setInfolist(List<InfolistBean> infolist) {
            this.infolist = infolist;
        }

        public static class InfolistBean {
            /**
             * InLocalPath : C:\Users\Public\Videos\Sample Videos\wildlife.wmv
             * InServerPath :
             * bHasThumbnailPix : false
             * description :
             * durationtime :
             * fileId : 172
             * fileName : wildlife.wmv
             * folderId : 1
             * hasInfo : false
             * height : 0
             * percent : 0.0
             * size : 0
             * type : 0
             * uploadtime : 2016/05/04 18:30
             * width : 0
             */

            private String InLocalPath;
            private String InServerPath;
            private boolean bHasThumbnailPix;
            private String description;
            private String durationtime;
            private int fileId;
            private String fileName;
            private int folderId;
            private String hasInfo;
            private int height;
            private double percent;
            private int size;
            private int type;
            private String uploadtime;
            private int width;

            public String getInLocalPath() {
                return InLocalPath;
            }

            public void setInLocalPath(String InLocalPath) {
                this.InLocalPath = InLocalPath;
            }

            public String getInServerPath() {
                return InServerPath;
            }

            public void setInServerPath(String InServerPath) {
                this.InServerPath = InServerPath;
            }

            public boolean isBHasThumbnailPix() {
                return bHasThumbnailPix;
            }

            public void setBHasThumbnailPix(boolean bHasThumbnailPix) {
                this.bHasThumbnailPix = bHasThumbnailPix;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public String getDurationtime() {
                return durationtime;
            }

            public void setDurationtime(String durationtime) {
                this.durationtime = durationtime;
            }

            public int getFileId() {
                return fileId;
            }

            public void setFileId(int fileId) {
                this.fileId = fileId;
            }

            public String getFileName() {
                return fileName;
            }

            public void setFileName(String fileName) {
                this.fileName = fileName;
            }

            public int getFolderId() {
                return folderId;
            }

            public void setFolderId(int folderId) {
                this.folderId = folderId;
            }

            public String getHasInfo() {
                return hasInfo;
            }

            public void setHasInfo(String hasInfo) {
                this.hasInfo = hasInfo;
            }

            public int getHeight() {
                return height;
            }

            public void setHeight(int height) {
                this.height = height;
            }

            public double getPercent() {
                return percent;
            }

            public void setPercent(double percent) {
                this.percent = percent;
            }

            public int getSize() {
                return size;
            }

            public void setSize(int size) {
                this.size = size;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public String getUploadtime() {
                return uploadtime;
            }

            public void setUploadtime(String uploadtime) {
                this.uploadtime = uploadtime;
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
