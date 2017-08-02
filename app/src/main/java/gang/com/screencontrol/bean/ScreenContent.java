package gang.com.screencontrol.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xiaogangzai on 2017/7/24.
 */

public class ScreenContent implements Serializable{

    /**
     * body : {"Content":[{"Alpha":1,"Content":[{"Type":"Image","cascadeServerId":0,"cascadeServerSourceType":0,"description":"人数.jpg","itemThumbnailPath":"","layerItemID":112,"layerItemType":43,"majorID":-1,"minorID":103,"name":"人数.jpg","playOrder":0,"playTime":30000,"refreshTime":10,"szPlayerURL":"\\2017-07\\cb17f8de-d5b2-454d-b4fb-688e88fff231.jpg","szURL":"\\2017-07\\cb17f8de-d5b2-454d-b4fb-688e88fff231.jpg","uniqueID":111,"volume":80}],"IsCreatedByUs":true,"TransitionDuration":2,"Zorder":0,"bOnlyFrame":false,"bottom":1080,"brahmen":false,"currentItemIndex":0,"guid":"111","left":0,"mute":false,"rahmenName":"","rahmenid":-1,"right":976,"strTransitionEffect":"None","strTransitionEffectDir":"","top":0,"volume":80}],"slaveID":100}
     * category : 1
     * errorCode : 0
     * errorStr : OK
     * guid :
     * type : MAXWALLSCREENCONTENT
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
         * Content : [{"Alpha":1,"Content":[{"Type":"Image","cascadeServerId":0,"cascadeServerSourceType":0,"description":"人数.jpg","itemThumbnailPath":"","layerItemID":112,"layerItemType":43,"majorID":-1,"minorID":103,"name":"人数.jpg","playOrder":0,"playTime":30000,"refreshTime":10,"szPlayerURL":"\\2017-07\\cb17f8de-d5b2-454d-b4fb-688e88fff231.jpg","szURL":"\\2017-07\\cb17f8de-d5b2-454d-b4fb-688e88fff231.jpg","uniqueID":111,"volume":80}],"IsCreatedByUs":true,"TransitionDuration":2,"Zorder":0,"bOnlyFrame":false,"bottom":1080,"brahmen":false,"currentItemIndex":0,"guid":"111","left":0,"mute":false,"rahmenName":"","rahmenid":-1,"right":976,"strTransitionEffect":"None","strTransitionEffectDir":"","top":0,"volume":80}]
         * slaveID : 100
         */

        private int slaveID;
        private List<ContentBeanX> Content;

        public int getSlaveID() {
            return slaveID;
        }

        public void setSlaveID(int slaveID) {
            this.slaveID = slaveID;
        }

        public List<ContentBeanX> getContent() {
            return Content;
        }

        public void setContent(List<ContentBeanX> Content) {
            this.Content = Content;
        }

        public static class ContentBeanX {
            /**
             * Alpha : 1.0
             * Content : [{"Type":"Image","cascadeServerId":0,"cascadeServerSourceType":0,"description":"人数.jpg","itemThumbnailPath":"","layerItemID":112,"layerItemType":43,"majorID":-1,"minorID":103,"name":"人数.jpg","playOrder":0,"playTime":30000,"refreshTime":10,"szPlayerURL":"\\2017-07\\cb17f8de-d5b2-454d-b4fb-688e88fff231.jpg","szURL":"\\2017-07\\cb17f8de-d5b2-454d-b4fb-688e88fff231.jpg","uniqueID":111,"volume":80}]
             * IsCreatedByUs : true
             * TransitionDuration : 2
             * Zorder : 0
             * bOnlyFrame : false
             * bottom : 1080
             * brahmen : false
             * currentItemIndex : 0
             * guid : 111
             * left : 0
             * mute : false
             * rahmenName :
             * rahmenid : -1
             * right : 976
             * strTransitionEffect : None
             * strTransitionEffectDir :
             * top : 0
             * volume : 80
             */

            private double Alpha;
            private boolean IsCreatedByUs;
            private int TransitionDuration;
            private int Zorder;
            private boolean bOnlyFrame;
            private int bottom;
            private boolean brahmen;
            private int currentItemIndex;
            private String guid;
            private int left;
            private boolean mute;
            private String rahmenName;
            private int rahmenid;
            private int right;
            private String strTransitionEffect;
            private String strTransitionEffectDir;
            private int top;
            private int volume;
            private List<ContentBean> Content;

            public double getAlpha() {
                return Alpha;
            }

            public void setAlpha(double Alpha) {
                this.Alpha = Alpha;
            }

            public boolean isIsCreatedByUs() {
                return IsCreatedByUs;
            }

            public void setIsCreatedByUs(boolean IsCreatedByUs) {
                this.IsCreatedByUs = IsCreatedByUs;
            }

            public int getTransitionDuration() {
                return TransitionDuration;
            }

            public void setTransitionDuration(int TransitionDuration) {
                this.TransitionDuration = TransitionDuration;
            }

            public int getZorder() {
                return Zorder;
            }

            public void setZorder(int Zorder) {
                this.Zorder = Zorder;
            }

            public boolean isBOnlyFrame() {
                return bOnlyFrame;
            }

            public void setBOnlyFrame(boolean bOnlyFrame) {
                this.bOnlyFrame = bOnlyFrame;
            }

            public int getBottom() {
                return bottom;
            }

            public void setBottom(int bottom) {
                this.bottom = bottom;
            }

            public boolean isBrahmen() {
                return brahmen;
            }

            public void setBrahmen(boolean brahmen) {
                this.brahmen = brahmen;
            }

            public int getCurrentItemIndex() {
                return currentItemIndex;
            }

            public void setCurrentItemIndex(int currentItemIndex) {
                this.currentItemIndex = currentItemIndex;
            }

            public String getGuid() {
                return guid;
            }

            public void setGuid(String guid) {
                this.guid = guid;
            }

            public int getLeft() {
                return left;
            }

            public void setLeft(int left) {
                this.left = left;
            }

            public boolean isMute() {
                return mute;
            }

            public void setMute(boolean mute) {
                this.mute = mute;
            }

            public String getRahmenName() {
                return rahmenName;
            }

            public void setRahmenName(String rahmenName) {
                this.rahmenName = rahmenName;
            }

            public int getRahmenid() {
                return rahmenid;
            }

            public void setRahmenid(int rahmenid) {
                this.rahmenid = rahmenid;
            }

            public int getRight() {
                return right;
            }

            public void setRight(int right) {
                this.right = right;
            }

            public String getStrTransitionEffect() {
                return strTransitionEffect;
            }

            public void setStrTransitionEffect(String strTransitionEffect) {
                this.strTransitionEffect = strTransitionEffect;
            }

            public String getStrTransitionEffectDir() {
                return strTransitionEffectDir;
            }

            public void setStrTransitionEffectDir(String strTransitionEffectDir) {
                this.strTransitionEffectDir = strTransitionEffectDir;
            }

            public int getTop() {
                return top;
            }

            public void setTop(int top) {
                this.top = top;
            }

            public int getVolume() {
                return volume;
            }

            public void setVolume(int volume) {
                this.volume = volume;
            }

            public List<ContentBean> getContent() {
                return Content;
            }

            public void setContent(List<ContentBean> Content) {
                this.Content = Content;
            }

            public static class ContentBean {
                /**
                 * Type : Image
                 * cascadeServerId : 0
                 * cascadeServerSourceType : 0
                 * description : 人数.jpg
                 * itemThumbnailPath :
                 * layerItemID : 112
                 * layerItemType : 43
                 * majorID : -1
                 * minorID : 103
                 * name : 人数.jpg
                 * playOrder : 0
                 * playTime : 30000
                 * refreshTime : 10
                 * szPlayerURL : \2017-07\cb17f8de-d5b2-454d-b4fb-688e88fff231.jpg
                 * szURL : \2017-07\cb17f8de-d5b2-454d-b4fb-688e88fff231.jpg
                 * uniqueID : 111
                 * volume : 80
                 */

                private String Type;
                private int cascadeServerId;
                private int cascadeServerSourceType;
                private String description;
                private String itemThumbnailPath;
                private int layerItemID;
                private int layerItemType;
                private int majorID;
                private int minorID;
                private String name;
                private int playOrder;
                private int playTime;
                private int refreshTime;
                private String szPlayerURL;
                private String szURL;
                private int uniqueID;
                private int volume;

                public String getType() {
                    return Type;
                }

                public void setType(String Type) {
                    this.Type = Type;
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

                public String getDescription() {
                    return description;
                }

                public void setDescription(String description) {
                    this.description = description;
                }

                public String getItemThumbnailPath() {
                    return itemThumbnailPath;
                }

                public void setItemThumbnailPath(String itemThumbnailPath) {
                    this.itemThumbnailPath = itemThumbnailPath;
                }

                public int getLayerItemID() {
                    return layerItemID;
                }

                public void setLayerItemID(int layerItemID) {
                    this.layerItemID = layerItemID;
                }

                public int getLayerItemType() {
                    return layerItemType;
                }

                public void setLayerItemType(int layerItemType) {
                    this.layerItemType = layerItemType;
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

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
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

                public int getRefreshTime() {
                    return refreshTime;
                }

                public void setRefreshTime(int refreshTime) {
                    this.refreshTime = refreshTime;
                }

                public String getSzPlayerURL() {
                    return szPlayerURL;
                }

                public void setSzPlayerURL(String szPlayerURL) {
                    this.szPlayerURL = szPlayerURL;
                }

                public String getSzURL() {
                    return szURL;
                }

                public void setSzURL(String szURL) {
                    this.szURL = szURL;
                }

                public int getUniqueID() {
                    return uniqueID;
                }

                public void setUniqueID(int uniqueID) {
                    this.uniqueID = uniqueID;
                }

                public int getVolume() {
                    return volume;
                }

                public void setVolume(int volume) {
                    this.volume = volume;
                }
            }
        }
    }
}
