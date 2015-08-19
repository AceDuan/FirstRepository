package com.china.acetech.ToolPackage.data.repo.greenDao;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table BRACELET_INFO.
 */
public class BraceletInfo {

    private Long id;
    private String bracelet;
    private String version;
    private String MD5;
    private String MacAddress;
    private String fileName;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public BraceletInfo() {
    }

    public BraceletInfo(Long id) {
        this.id = id;
    }

    public BraceletInfo(Long id, String bracelet, String version, String MD5, String MacAddress, String fileName) {
        this.id = id;
        this.bracelet = bracelet;
        this.version = version;
        this.MD5 = MD5;
        this.MacAddress = MacAddress;
        this.fileName = fileName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBracelet() {
        return bracelet;
    }

    public void setBracelet(String bracelet) {
        this.bracelet = bracelet;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getMD5() {
        return MD5;
    }

    public void setMD5(String MD5) {
        this.MD5 = MD5;
    }

    public String getMacAddress() {
        return MacAddress;
    }

    public void setMacAddress(String MacAddress) {
        this.MacAddress = MacAddress;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}
