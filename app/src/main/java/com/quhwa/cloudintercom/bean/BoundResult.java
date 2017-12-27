package com.quhwa.cloudintercom.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by lxz on 2017/10/13 0013.
 */

public class BoundResult implements Serializable{


    /**
     * code : 1
     * message : SUCESS
     * data : {"deviceId":424,"districtId":"1","areaId":"1","buildingId":"2","unitId":"2","roomId":"29","createdTime":1504770800000,"createdUser":null,"deviceAlias":"1001010101020501","deviceCode":"c20170901092018o","deviceName":"1001010101020501","devicePwd":null,"deviceStatus":"0","deviceType":"01","districtGateFlag":null,"deviceMac":"00:0D:00:00:1F:95","updatedTime":1504770800000,"updatedUser":null,"deviceIp":"192.168.1.14","unitDoorNo":null,"position":null,"coordinate":null,"version":null,"sipid":"18824600691","roomNo":null}
     */

    private int code;
    private String message;
    @SerializedName("data")
    private Device device;

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

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public static class Device implements Serializable{
        /**
         * deviceId : 424
         * districtId : 1
         * areaId : 1
         * buildingId : 2
         * unitId : 2
         * roomId : 29
         * createdTime : 1504770800000
         * createdUser : null
         * deviceAlias : 1001010101020501
         * deviceCode : c20170901092018o
         * deviceName : 1001010101020501
         * devicePwd : null
         * deviceStatus : 0
         * deviceType : 01
         * districtGateFlag : null
         * deviceMac : 00:0D:00:00:1F:95
         * updatedTime : 1504770800000
         * updatedUser : null
         * deviceIp : 192.168.1.14
         * unitDoorNo : null
         * position : null
         * coordinate : null
         * version : null
         * sipid : 18824600691
         * roomNo : null
         */

        private int deviceId;
        private String districtId;
        private String areaId;
        private String buildingId;
        private String unitId;
        private String roomId;
        private long createdTime;
        private Object createdUser;
        private String deviceAlias;
        private String deviceCode;
        private String deviceName;
        private Object devicePwd;
        private String deviceStatus;
        private String deviceType;
        private Object districtGateFlag;
        private String deviceMac;
        private long updatedTime;
        private Object updatedUser;
        private String deviceIp;
        private String unitDoorNo;
        private Object position;
        private Object coordinate;
        private Object version;
        private String sipid;
        private String roomNo;

        private int shieldStatus;
        /**用户名*/
        private String username;
        private boolean isCheck;
        /**2 屏蔽 0取消屏蔽*/
        private String status;

        public void setCheck(boolean isCheck) {
            this.isCheck = isCheck;
        }
        public boolean getCheck() {
            return isCheck;
        }
        public int getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(int deviceId) {
            this.deviceId = deviceId;
        }

        public String getDistrictId() {
            return districtId;
        }

        public void setDistrictId(String districtId) {
            this.districtId = districtId;
        }

        public String getAreaId() {
            return areaId;
        }

        public void setAreaId(String areaId) {
            this.areaId = areaId;
        }

        public String getBuildingId() {
            return buildingId;
        }

        public void setBuildingId(String buildingId) {
            this.buildingId = buildingId;
        }

        public String getUnitId() {
            return unitId;
        }

        public void setUnitId(String unitId) {
            this.unitId = unitId;
        }

        public String getRoomId() {
            return roomId;
        }

        public void setRoomId(String roomId) {
            this.roomId = roomId;
        }

        public long getCreatedTime() {
            return createdTime;
        }

        public void setCreatedTime(long createdTime) {
            this.createdTime = createdTime;
        }

        public Object getCreatedUser() {
            return createdUser;
        }

        public void setCreatedUser(Object createdUser) {
            this.createdUser = createdUser;
        }

        public String getDeviceAlias() {
            return deviceAlias;
        }

        public void setDeviceAlias(String deviceAlias) {
            this.deviceAlias = deviceAlias;
        }

        public String getDeviceCode() {
            return deviceCode;
        }

        public void setDeviceCode(String deviceCode) {
            this.deviceCode = deviceCode;
        }

        public String getDeviceName() {
            return deviceName;
        }

        public void setDeviceName(String deviceName) {
            this.deviceName = deviceName;
        }

        public Object getDevicePwd() {
            return devicePwd;
        }

        public void setDevicePwd(Object devicePwd) {
            this.devicePwd = devicePwd;
        }

        public String getDeviceStatus() {
            return deviceStatus;
        }

        public void setDeviceStatus(String deviceStatus) {
            this.deviceStatus = deviceStatus;
        }

        public String getDeviceType() {
            return deviceType;
        }

        public void setDeviceType(String deviceType) {
            this.deviceType = deviceType;
        }

        public Object getDistrictGateFlag() {
            return districtGateFlag;
        }

        public void setDistrictGateFlag(Object districtGateFlag) {
            this.districtGateFlag = districtGateFlag;
        }

        public String getDeviceMac() {
            return deviceMac;
        }

        public void setDeviceMac(String deviceMac) {
            this.deviceMac = deviceMac;
        }

        public long getUpdatedTime() {
            return updatedTime;
        }

        public void setUpdatedTime(long updatedTime) {
            this.updatedTime = updatedTime;
        }

        public Object getUpdatedUser() {
            return updatedUser;
        }

        public void setUpdatedUser(Object updatedUser) {
            this.updatedUser = updatedUser;
        }

        public String getDeviceIp() {
            return deviceIp;
        }

        public void setDeviceIp(String deviceIp) {
            this.deviceIp = deviceIp;
        }

        public String getUnitDoorNo() {
            return unitDoorNo;
        }

        public void setUnitDoorNo(String unitDoorNo) {
            this.unitDoorNo = unitDoorNo;
        }

        public Object getPosition() {
            return position;
        }

        public void setPosition(Object position) {
            this.position = position;
        }

        public Object getCoordinate() {
            return coordinate;
        }

        public void setCoordinate(Object coordinate) {
            this.coordinate = coordinate;
        }

        public Object getVersion() {
            return version;
        }

        public void setVersion(Object version) {
            this.version = version;
        }

        public String getSipid() {
            return sipid;
        }

        public void setSipid(String sipid) {
            this.sipid = sipid;
        }

        public String getRoomNo() {
            return roomNo;
        }

        public void setRoomNo(String roomNo) {
            this.roomNo = roomNo;
        }

        public int getShieldStatus() {
            return shieldStatus;
        }

        public void setShieldStatus(int shieldStatus) {
            this.shieldStatus = shieldStatus;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        @Override
        public String toString() {
            return "Device{" +
                    "deviceId=" + deviceId +
                    ", districtId='" + districtId + '\'' +
                    ", areaId='" + areaId + '\'' +
                    ", buildingId='" + buildingId + '\'' +
                    ", unitId='" + unitId + '\'' +
                    ", roomId='" + roomId + '\'' +
                    ", createdTime=" + createdTime +
                    ", createdUser=" + createdUser +
                    ", deviceAlias='" + deviceAlias + '\'' +
                    ", deviceCode='" + deviceCode + '\'' +
                    ", deviceName='" + deviceName + '\'' +
                    ", devicePwd=" + devicePwd +
                    ", deviceStatus='" + deviceStatus + '\'' +
                    ", deviceType='" + deviceType + '\'' +
                    ", districtGateFlag=" + districtGateFlag +
                    ", deviceMac='" + deviceMac + '\'' +
                    ", updatedTime=" + updatedTime +
                    ", updatedUser=" + updatedUser +
                    ", deviceIp='" + deviceIp + '\'' +
                    ", unitDoorNo=" + unitDoorNo +
                    ", position=" + position +
                    ", coordinate=" + coordinate +
                    ", version=" + version +
                    ", sipid='" + sipid + '\'' +
                    ", roomNo=" + roomNo +
                    ", shieldStatus=" + shieldStatus +
                    ", username='" + username + '\'' +
                    ", status='" + status + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "BoundResult{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", device=" + device +
                '}';
    }
}
