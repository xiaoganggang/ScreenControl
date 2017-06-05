package gang.com.screencontrol.bean;


/**
 * Created by xiaogangzai on 2017/6/1.
 * 每个媒体子文件中中子文件详细内容
 */

public class MediaBean_childdetial {
    private int fileId;
    private int folderId;
    private String InLocalPath;
    private String fileName;
    private String InServerPath;
    private int type;
    private int size;
    private int width;
    private int height;
    private String durationtime;
    private double percent;
    private Boolean bDownloadFinished;
    private String uploadtime;
    private String description;
    private Boolean bHasThumbnailPix;
    private String hasInfo;
    private int whatContentChanged;  // 0 for not changed, 1 for name changed, 2 for volume changed, 4 for ShowNameProperty changed

    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    public int getFolderId() {
        return folderId;
    }

    public void setFolderId(int folderId) {
        this.folderId = folderId;
    }

    public String getInLocalPath() {
        return InLocalPath;
    }

    public void setInLocalPath(String inLocalPath) {
        InLocalPath = inLocalPath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getInServerPath() {
        return InServerPath;
    }

    public void setInServerPath(String inServerPath) {
        InServerPath = inServerPath;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getDurationtime() {
        return durationtime;
    }

    public void setDurationtime(String durationtime) {
        this.durationtime = durationtime;
    }

    public double getPercent() {
        return percent;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }

    public Boolean getbDownloadFinished() {
        return bDownloadFinished;
    }

    public void setbDownloadFinished(Boolean bDownloadFinished) {
        this.bDownloadFinished = bDownloadFinished;
    }

    public String getUploadtime() {
        return uploadtime;
    }

    public void setUploadtime(String uploadtime) {
        this.uploadtime = uploadtime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getbHasThumbnailPix() {
        return bHasThumbnailPix;
    }

    public void setbHasThumbnailPix(Boolean bHasThumbnailPix) {
        this.bHasThumbnailPix = bHasThumbnailPix;
    }

    public String getHasInfo() {
        return hasInfo;
    }

    public void setHasInfo(String hasInfo) {
        this.hasInfo = hasInfo;
    }

    public int getWhatContentChanged() {
        return whatContentChanged;
    }

    public void setWhatContentChanged(int whatContentChanged) {
        this.whatContentChanged = whatContentChanged;
    }
}
