package gang.com.screencontrol.bean;

import java.io.Serializable;

/**
 * Created by xiaogangzai on 2017/8/7.
 * 再一次操作层的时候需要传递的元素数组
 */

public class CengYuansuBean implements Serializable{
    private int ID ;
    private String name ;
    private int type ;
    private int majorID ;
    private int minorID ;
    private int playOrder ;
    private int playTime ;
    private int validSource;
    private int refreshTime;
    private int protoType ;
    private String description ;
    private String urlList;
    private int  cascadeServerId ;
    private int  cascadeServerSourceType ;
    private String reserved ;
    private String itemThumbnialPath;
    private int    sourceTotalTime ;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getMajorID() {
        return majorID;
    }

    public void setMajorID(int majorID) {
        this.majorID = majorID;
    }

    public int getMinorID() {
        return minorID;
    }

    public void setMinorID(int minorID) {
        this.minorID = minorID;
    }

    public int getPlayOrder() {
        return playOrder;
    }

    public void setPlayOrder(int playOrder) {
        this.playOrder = playOrder;
    }

    public int getPlayTime() {
        return playTime;
    }

    public void setPlayTime(int playTime) {
        this.playTime = playTime;
    }

    public int getValidSource() {
        return validSource;
    }

    public void setValidSource(int validSource) {
        this.validSource = validSource;
    }

    public int getRefreshTime() {
        return refreshTime;
    }

    public void setRefreshTime(int refreshTime) {
        this.refreshTime = refreshTime;
    }

    public int getProtoType() {
        return protoType;
    }

    public void setProtoType(int protoType) {
        this.protoType = protoType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrlList() {
        return urlList;
    }

    public void setUrlList(String urlList) {
        this.urlList = urlList;
    }

    public int getCascadeServerId() {
        return cascadeServerId;
    }

    public void setCascadeServerId(int cascadeServerId) {
        this.cascadeServerId = cascadeServerId;
    }

    public int getCascadeServerSourceType() {
        return cascadeServerSourceType;
    }

    public void setCascadeServerSourceType(int cascadeServerSourceType) {
        this.cascadeServerSourceType = cascadeServerSourceType;
    }

    public String getReserved() {
        return reserved;
    }

    public void setReserved(String reserved) {
        this.reserved = reserved;
    }

    public String getItemThumbnialPath() {
        return itemThumbnialPath;
    }

    public void setItemThumbnialPath(String itemThumbnialPath) {
        this.itemThumbnialPath = itemThumbnialPath;
    }

    public int getSourceTotalTime() {
        return sourceTotalTime;
    }

    public void setSourceTotalTime(int sourceTotalTime) {
        this.sourceTotalTime = sourceTotalTime;
    }
}
