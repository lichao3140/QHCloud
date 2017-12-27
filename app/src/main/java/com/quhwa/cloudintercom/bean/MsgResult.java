package com.quhwa.cloudintercom.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lxz on 2017/9/8 0008.
 */

public class MsgResult implements Serializable{

    /**
     * code : 1
     * message :
     * data : {"timestamp":1504829059301,"total":2,"info":[{"id":1,"title":"test","content":"111","houseId":6,"createTime":1504771213000,"updateTime":1504772565000,"status":1,"type":1,"createBy":null,"roomNo":null,"remark":null},{"id":2,"title":"test","content":"111","houseId":6,"createTime":1504771223000,"updateTime":1504772567000,"status":1,"type":1,"createBy":null,"roomNo":null,"remark":null}]}
     */

    private int code;
    private String message;
    private MsgDataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public MsgDataBean getData() {
        return data;
    }

    public void setData(MsgDataBean data) {
        this.data = data;
    }

    public static class MsgDataBean {
        /**
         * timestamp : 1504829059301
         * total : 2
         * info : [{"id":1,"title":"test","content":"111","houseId":6,"createTime":1504771213000,"updateTime":1504772565000,"status":1,"type":1,"createBy":null,"roomNo":null,"remark":null},{"id":2,"title":"test","content":"111","houseId":6,"createTime":1504771223000,"updateTime":1504772567000,"status":1,"type":1,"createBy":null,"roomNo":null,"remark":null}]
         */

        private long timestamp;
        private int total;
        private List<MsgInfoBean> info;

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public List<MsgInfoBean> getInfo() {
            return info;
        }

        public void setInfo(List<MsgInfoBean> info) {
            this.info = info;
        }

        @Override
        public String toString() {
            return "MsgDataBean{" +
                    "timestamp=" + timestamp +
                    ", total=" + total +
                    ", info=" + info +
                    '}';
        }

        public static class MsgInfoBean implements Serializable{
            /**
             * id : 1
             * title : test
             * content : 111
             * houseId : 6
             * createTime : 1504771213000
             * updateTime : 1504772565000
             * status : 1
             * type : 1
             * createBy : null
             * roomNo : null
             * remark : null
             */

            private int id;
            private String title;
            private String content;
            private int houseId;
            private long createTime;
            private long updateTime;
            private int status;
            private int type;
            private Object createBy;
            private Object roomNo;
            private Object remark;
            /**
             * 是否被选中
             */
            private boolean isChildSelected;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public int getHouseId() {
                return houseId;
            }

            public void setHouseId(int houseId) {
                this.houseId = houseId;
            }

            public long getCreateTime() {
                return createTime;
            }

            public void setCreateTime(long createTime) {
                this.createTime = createTime;
            }

            public long getUpdateTime() {
                return updateTime;
            }

            public void setUpdateTime(long updateTime) {
                this.updateTime = updateTime;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public Object getCreateBy() {
                return createBy;
            }

            public void setCreateBy(Object createBy) {
                this.createBy = createBy;
            }

            public Object getRoomNo() {
                return roomNo;
            }

            public void setRoomNo(Object roomNo) {
                this.roomNo = roomNo;
            }

            public Object getRemark() {
                return remark;
            }

            public void setRemark(Object remark) {
                this.remark = remark;
            }

            public boolean isChildSelected() {
                return isChildSelected;
            }

            public void setIsChildSelected(boolean isChildSelected) {
                this.isChildSelected = isChildSelected;
            }



            @Override
            public String toString() {
                return "MsgInfoBean{" +
                        "id=" + id +
                        ", title='" + title + '\'' +
                        ", content='" + content + '\'' +
                        ", houseId=" + houseId +
                        ", createTime=" + createTime +
                        ", updateTime=" + updateTime +
                        ", status=" + status +
                        ", type=" + type +
                        ", createBy=" + createBy +
                        ", roomNo=" + roomNo +
                        ", remark=" + remark +
                        '}';
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;

                MsgInfoBean that = (MsgInfoBean) o;

                return id == that.id;

            }

            @Override
            public int hashCode() {
                return id;
            }
        }
    }
}
