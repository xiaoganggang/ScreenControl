package gang.com.screencontrol.bean;

/**
 * Created by xiaogangzai on 2017/6/1.
 */

public class DeviceBean {


    /**
     * Description : Matrix
     * Name : Matrix
     * bDisplay : true
     * folderID : 2
     * hasChild : false
     * hasDevice : false
     * parentID : 0
     * readOnly : true
     * type : 2
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
