package gang.com.screencontrol.bean;


import java.io.Serializable;
import java.util.List;

/**
 * Created by xiaogangzai on 2017/6/1.
 * 每个媒体子文件中中子文件详细内容
 * 序列化的类需要传递对象
 */

public class MediaBean_childdetial implements Serializable {


    /**
     * body : {"InLocalPath":"C:\\Users\\Public\\Videos\\Sample Videos\\wildlife.wmv","InServerPath":"\\2016-05\\31423515-f9ff-4a7a-841d-187fbaf88f92.wmv","bHasThumbnailPix":true,"description":"","durationtime":"00 : 30","fileId":172,"fileName":"wildlife.wmv","folderId":1,"hasInfo":"true","height":720,"percent":1,"size":26246026,"slaveidlist":{"deviceid":[102,108]},"thumbnailPix":{"pixValue":[255,216,255,224,0,16,74,70,73,70,0,1,1,1,0,96,0,96,0,0,255,219,0,67,0,8,6,6,7,6,5,8,7,7,7,9,9,8,10,12,20,13,12,11,11,12,25,18,19,15,20,29,26,31,30,29,26,28,28,32,36,46,39,32,34,44,35,28,28,40,55,41,44,48,49,52,52,52,31,39,57,61,56,50,60,46,51,52,50,255,219,0,67,1,9,9,9,12,11,12,24,13,13,24,50,33,28,33,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,255,192,0,17,8,0,17,0,30,3,1,34,0,2,17,1,3,17,1,255,196,0,31,0,0,1,5,1,1,1,1,1,1,0,0,0,0,0,0,0,0,1,2,3,4,5,6,7,8,9,10,11,255,196,0,181,16,0,2,1,3,3,2,4,3,5,5,4,4,0,0,1,125,1,2,3,0,4,17,5,18,33,49,65,6,19,81,97,7,34,113,20,50,129,145,161,8,35,66,177,193,21,82,209,240,36,51,98,114,130,9,10,22,23,24,25,26,37,38,39,40,41,42,52,53,54,55,56,57,58,67,68,69,70,71,72,73,74,83,84,85,86,87,88,89,90,99,100,101,102,103,104,105,106,115,116,117,118,119,120,121,122,131,132,133,134,135,136,137,138,146,147,148,149,150,151,152,153,154,162,163,164,165,166,167,168,169,170,178,179,180,181,182,183,184,185,186,194,195,196,197,198,199,200,201,202,210,211,212,213,214,215,216,217,218,225,226,227,228,229,230,231,232,233,234,241,242,243,244,245,246,247,248,249,250,255,196,0,31,1,0,3,1,1,1,1,1,1,1,1,1,0,0,0,0,0,0,1,2,3,4,5,6,7,8,9,10,11,255,196,0,181,17,0,2,1,2,4,4,3,4,7,5,4,4,0,1,2,119,0,1,2,3,17,4,5,33,49,6,18,65,81,7,97,113,19,34,50,129,8,20,66,145,161,177,193,9,35,51,82,240,21,98,114,209,10,22,36,52,225,37,241,23,24,25,26,38,39,40,41,42,53,54,55,56,57,58,67,68,69,70,71,72,73,74,83,84,85,86,87,88,89,90,99,100,101,102,103,104,105,106,115,116,117,118,119,120,121,122,130,131,132,133,134,135,136,137,138,146,147,148,149,150,151,152,153,154,162,163,164,165,166,167,168,169,170,178,179,180,181,182,183,184,185,186,194,195,196,197,198,199,200,201,202,210,211,212,213,214,215,216,217,218,226,227,228,229,230,231,232,233,234,242,243,244,245,246,247,248,249,250,255,218,0,12,3,1,0,2,17,3,17,0,63,0,232,87,69,95,67,249,86,110,178,233,165,219,171,219,64,46,165,243,2,180,106,121,81,207,39,25,35,167,165,101,248,215,197,95,219,105,111,103,166,77,36,22,131,45,49,7,97,144,246,25,28,227,173,97,104,250,246,173,164,98,21,123,118,178,254,40,64,35,62,249,239,248,213,123,239,160,185,163,220,244,93,62,214,215,80,177,134,238,53,112,178,174,112,87,149,61,193,247,7,34,169,92,234,254,25,178,145,162,158,253,76,138,219,89,83,44,84,142,199,0,226,177,52,15,28,220,233,143,112,183,67,207,129,249,68,28,108,57,237,92,84,154,125,187,200,195,207,96,160,157,164,71,206,61,249,162,211,236,28,241,29,47,81,245,167,47,220,162,138,235,234,115,0,233,78,162,138,0,255,217]},"type":44,"uploadtime":"2016/05/04 18:30","width":1280}
     * category : 0
     * errorCode : 0
     * errorStr : OK
     * guid : M-48
     * type : GETMEDIAFILEINFO
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
         * InLocalPath : C:\Users\Public\Videos\Sample Videos\wildlife.wmv
         * InServerPath : \2016-05\31423515-f9ff-4a7a-841d-187fbaf88f92.wmv
         * bHasThumbnailPix : true
         * description :
         * durationtime : 00 : 30
         * fileId : 172
         * fileName : wildlife.wmv
         * folderId : 1
         * hasInfo : true
         * height : 720
         * percent : 1.0
         * size : 26246026
         * slaveidlist : {"deviceid":[102,108]}
         * thumbnailPix : {"pixValue":[255,216,255,224,0,16,74,70,73,70,0,1,1,1,0,96,0,96,0,0,255,219,0,67,0,8,6,6,7,6,5,8,7,7,7,9,9,8,10,12,20,13,12,11,11,12,25,18,19,15,20,29,26,31,30,29,26,28,28,32,36,46,39,32,34,44,35,28,28,40,55,41,44,48,49,52,52,52,31,39,57,61,56,50,60,46,51,52,50,255,219,0,67,1,9,9,9,12,11,12,24,13,13,24,50,33,28,33,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,255,192,0,17,8,0,17,0,30,3,1,34,0,2,17,1,3,17,1,255,196,0,31,0,0,1,5,1,1,1,1,1,1,0,0,0,0,0,0,0,0,1,2,3,4,5,6,7,8,9,10,11,255,196,0,181,16,0,2,1,3,3,2,4,3,5,5,4,4,0,0,1,125,1,2,3,0,4,17,5,18,33,49,65,6,19,81,97,7,34,113,20,50,129,145,161,8,35,66,177,193,21,82,209,240,36,51,98,114,130,9,10,22,23,24,25,26,37,38,39,40,41,42,52,53,54,55,56,57,58,67,68,69,70,71,72,73,74,83,84,85,86,87,88,89,90,99,100,101,102,103,104,105,106,115,116,117,118,119,120,121,122,131,132,133,134,135,136,137,138,146,147,148,149,150,151,152,153,154,162,163,164,165,166,167,168,169,170,178,179,180,181,182,183,184,185,186,194,195,196,197,198,199,200,201,202,210,211,212,213,214,215,216,217,218,225,226,227,228,229,230,231,232,233,234,241,242,243,244,245,246,247,248,249,250,255,196,0,31,1,0,3,1,1,1,1,1,1,1,1,1,0,0,0,0,0,0,1,2,3,4,5,6,7,8,9,10,11,255,196,0,181,17,0,2,1,2,4,4,3,4,7,5,4,4,0,1,2,119,0,1,2,3,17,4,5,33,49,6,18,65,81,7,97,113,19,34,50,129,8,20,66,145,161,177,193,9,35,51,82,240,21,98,114,209,10,22,36,52,225,37,241,23,24,25,26,38,39,40,41,42,53,54,55,56,57,58,67,68,69,70,71,72,73,74,83,84,85,86,87,88,89,90,99,100,101,102,103,104,105,106,115,116,117,118,119,120,121,122,130,131,132,133,134,135,136,137,138,146,147,148,149,150,151,152,153,154,162,163,164,165,166,167,168,169,170,178,179,180,181,182,183,184,185,186,194,195,196,197,198,199,200,201,202,210,211,212,213,214,215,216,217,218,226,227,228,229,230,231,232,233,234,242,243,244,245,246,247,248,249,250,255,218,0,12,3,1,0,2,17,3,17,0,63,0,232,87,69,95,67,249,86,110,178,233,165,219,171,219,64,46,165,243,2,180,106,121,81,207,39,25,35,167,165,101,248,215,197,95,219,105,111,103,166,77,36,22,131,45,49,7,97,144,246,25,28,227,173,97,104,250,246,173,164,98,21,123,118,178,254,40,64,35,62,249,239,248,213,123,239,160,185,163,220,244,93,62,214,215,80,177,134,238,53,112,178,174,112,87,149,61,193,247,7,34,169,92,234,254,25,178,145,162,158,253,76,138,219,89,83,44,84,142,199,0,226,177,52,15,28,220,233,143,112,183,67,207,129,249,68,28,108,57,237,92,84,154,125,187,200,195,207,96,160,157,164,71,206,61,249,162,211,236,28,241,29,47,81,245,167,47,220,162,138,235,234,115,0,233,78,162,138,0,255,217]}
         * type : 44
         * uploadtime : 2016/05/04 18:30
         * width : 1280
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
        private SlaveidlistBean slaveidlist;
        private ThumbnailPixBean thumbnailPix;
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

        public SlaveidlistBean getSlaveidlist() {
            return slaveidlist;
        }

        public void setSlaveidlist(SlaveidlistBean slaveidlist) {
            this.slaveidlist = slaveidlist;
        }

        public ThumbnailPixBean getThumbnailPix() {
            return thumbnailPix;
        }

        public void setThumbnailPix(ThumbnailPixBean thumbnailPix) {
            this.thumbnailPix = thumbnailPix;
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

        public static class SlaveidlistBean {
            private List<Integer> deviceid;

            public List<Integer> getDeviceid() {
                return deviceid;
            }

            public void setDeviceid(List<Integer> deviceid) {
                this.deviceid = deviceid;
            }
        }

        public static class ThumbnailPixBean {
            private List<Integer> pixValue;

            public List<Integer> getPixValue() {
                return pixValue;
            }

            public void setPixValue(List<Integer> pixValue) {
                this.pixValue = pixValue;
            }
        }
    }
}
