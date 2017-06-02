package gang.com.screencontrol.bean;

/**
 * Created by Administrator on 2017/5/31.
 */

public class GroupBean {


    /**
     * folderDescription :
     * folderID : 291
     * folderName : 测试会议一
     * hasChild : false
     * parentID : 0
     * readOnly : false
     */

    private String folderDescription;
    private int folderID;
    private String folderName;
    private boolean hasChild;
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
