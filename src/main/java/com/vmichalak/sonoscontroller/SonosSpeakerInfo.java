package com.vmichalak.sonoscontroller;

public class SonosSpeakerInfo {
    private final String zoneName;
    private final String zoneIcon;
    private final String configuration;
    private final String localUID;
    private final String serialNumber;
    private final String softwareVersion;
    private final String softwareDate;
    private final String softwareScm;
    private final String minCompatibleVersion;
    private final String legacyCompatibleVersion;
    private final String hardwareVersion;
    private final String dspVersion;
    private final String hwFlags;
    private final String hwFeatures;
    private final String variant;
    private final String generalFlags;
    private final String ipAddress;
    private final String macAddress;
    private final String copyright;
    private final String extraInfo;
    private final String htAudioInCode;
    private final String idxTrk;
    private final String mdp2Ver;
    private final String mdp3Ver;
    private final String relBuild;
    private final String whitelistBuild;
    private final String prodUnit;
    private final String fuseCfg;
    private final String revokeFuse;
    private final String authFlags;
    private final String swFeatures;
    private final String regState;
    private final String customerID;

    public SonosSpeakerInfo(String zoneName, String zoneIcon, String configuration, String localUID,
                            String serialNumber, String softwareVersion, String softwareDate, String softwareScm,
                            String minCompatibleVersion, String legacyCompatibleVersion, String hardwareVersion,
                            String dspVersion, String hwFlags, String hwFeatures, String variant, String generalFlags,
                            String ipAddress, String macAddress, String copyright, String extraInfo,
                            String htAudioInCode, String idxTrk, String mdp2Ver, String mdp3Ver, String relBuild,
                            String whitelistBuild, String prodUnit, String fuseCfg, String revokeFuse, String authFlags,
                            String swFeatures, String regState, String customerID) {
        this.zoneName = zoneName;
        this.zoneIcon = zoneIcon;
        this.configuration = configuration;
        this.localUID = localUID;
        this.serialNumber = serialNumber;
        this.softwareVersion = softwareVersion;
        this.softwareDate = softwareDate;
        this.softwareScm = softwareScm;
        this.minCompatibleVersion = minCompatibleVersion;
        this.legacyCompatibleVersion = legacyCompatibleVersion;
        this.hardwareVersion = hardwareVersion;
        this.dspVersion = dspVersion;
        this.hwFlags = hwFlags;
        this.hwFeatures = hwFeatures;
        this.variant = variant;
        this.generalFlags = generalFlags;
        this.ipAddress = ipAddress;
        this.macAddress = macAddress;
        this.copyright = copyright;
        this.extraInfo = extraInfo;
        this.htAudioInCode = htAudioInCode;
        this.idxTrk = idxTrk;
        this.mdp2Ver = mdp2Ver;
        this.mdp3Ver = mdp3Ver;
        this.relBuild = relBuild;
        this.whitelistBuild = whitelistBuild;
        this.prodUnit = prodUnit;
        this.fuseCfg = fuseCfg;
        this.revokeFuse = revokeFuse;
        this.authFlags = authFlags;
        this.swFeatures = swFeatures;
        this.regState = regState;
        this.customerID = customerID;
    }

    public String getZoneName() {
        return zoneName;
    }

    public String getZoneIcon() {
        return zoneIcon;
    }

    public String getConfiguration() {
        return configuration;
    }

    public String getLocalUID() {
        return localUID;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public String getSoftwareVersion() {
        return softwareVersion;
    }

    public String getSoftwareDate() {
        return softwareDate;
    }

    public String getSoftwareScm() {
        return softwareScm;
    }

    public String getMinCompatibleVersion() {
        return minCompatibleVersion;
    }

    public String getLegacyCompatibleVersion() {
        return legacyCompatibleVersion;
    }

    public String getHardwareVersion() {
        return hardwareVersion;
    }

    public String getDspVersion() {
        return dspVersion;
    }

    public String getHwFlags() {
        return hwFlags;
    }

    public String getHwFeatures() {
        return hwFeatures;
    }

    public String getVariant() {
        return variant;
    }

    public String getGeneralFlags() {
        return generalFlags;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public String getCopyright() {
        return copyright;
    }

    public String getExtraInfo() {
        return extraInfo;
    }

    public String getHtAudioInCode() {
        return htAudioInCode;
    }

    public String getIdxTrk() {
        return idxTrk;
    }

    public String getMdp2Ver() {
        return mdp2Ver;
    }

    public String getMdp3Ver() {
        return mdp3Ver;
    }

    public String getRelBuild() {
        return relBuild;
    }

    public String getWhitelistBuild() {
        return whitelistBuild;
    }

    public String getProdUnit() {
        return prodUnit;
    }

    public String getFuseCfg() {
        return fuseCfg;
    }

    public String getRevokeFuse() {
        return revokeFuse;
    }

    public String getAuthFlags() {
        return authFlags;
    }

    public String getSwFeatures() {
        return swFeatures;
    }

    public String getRegState() {
        return regState;
    }

    public String getCustomerID() {
        return customerID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SonosSpeakerInfo that = (SonosSpeakerInfo) o;

        if (zoneName != null ? !zoneName.equals(that.zoneName) : that.zoneName != null) return false;
        if (zoneIcon != null ? !zoneIcon.equals(that.zoneIcon) : that.zoneIcon != null) return false;
        if (configuration != null ? !configuration.equals(that.configuration) : that.configuration != null)
            return false;
        if (localUID != null ? !localUID.equals(that.localUID) : that.localUID != null) return false;
        if (serialNumber != null ? !serialNumber.equals(that.serialNumber) : that.serialNumber != null) return false;
        if (softwareVersion != null ? !softwareVersion.equals(that.softwareVersion) : that.softwareVersion != null)
            return false;
        if (softwareDate != null ? !softwareDate.equals(that.softwareDate) : that.softwareDate != null) return false;
        if (softwareScm != null ? !softwareScm.equals(that.softwareScm) : that.softwareScm != null) return false;
        if (minCompatibleVersion != null ? !minCompatibleVersion.equals(that.minCompatibleVersion) :
                that.minCompatibleVersion != null)
            return false;
        if (legacyCompatibleVersion != null ? !legacyCompatibleVersion.equals(that.legacyCompatibleVersion) :
                that.legacyCompatibleVersion != null)
            return false;
        if (hardwareVersion != null ? !hardwareVersion.equals(that.hardwareVersion) : that.hardwareVersion != null)
            return false;
        if (dspVersion != null ? !dspVersion.equals(that.dspVersion) : that.dspVersion != null) return false;
        if (hwFlags != null ? !hwFlags.equals(that.hwFlags) : that.hwFlags != null) return false;
        if (hwFeatures != null ? !hwFeatures.equals(that.hwFeatures) : that.hwFeatures != null) return false;
        if (variant != null ? !variant.equals(that.variant) : that.variant != null) return false;
        if (generalFlags != null ? !generalFlags.equals(that.generalFlags) : that.generalFlags != null) return false;
        if (ipAddress != null ? !ipAddress.equals(that.ipAddress) : that.ipAddress != null) return false;
        if (macAddress != null ? !macAddress.equals(that.macAddress) : that.macAddress != null) return false;
        if (copyright != null ? !copyright.equals(that.copyright) : that.copyright != null) return false;
        if (extraInfo != null ? !extraInfo.equals(that.extraInfo) : that.extraInfo != null) return false;
        if (htAudioInCode != null ? !htAudioInCode.equals(that.htAudioInCode) : that.htAudioInCode != null)
            return false;
        if (idxTrk != null ? !idxTrk.equals(that.idxTrk) : that.idxTrk != null) return false;
        if (mdp2Ver != null ? !mdp2Ver.equals(that.mdp2Ver) : that.mdp2Ver != null) return false;
        if (mdp3Ver != null ? !mdp3Ver.equals(that.mdp3Ver) : that.mdp3Ver != null) return false;
        if (relBuild != null ? !relBuild.equals(that.relBuild) : that.relBuild != null) return false;
        if (whitelistBuild != null ? !whitelistBuild.equals(that.whitelistBuild) : that.whitelistBuild != null)
            return false;
        if (prodUnit != null ? !prodUnit.equals(that.prodUnit) : that.prodUnit != null) return false;
        if (fuseCfg != null ? !fuseCfg.equals(that.fuseCfg) : that.fuseCfg != null) return false;
        if (revokeFuse != null ? !revokeFuse.equals(that.revokeFuse) : that.revokeFuse != null) return false;
        if (authFlags != null ? !authFlags.equals(that.authFlags) : that.authFlags != null) return false;
        if (swFeatures != null ? !swFeatures.equals(that.swFeatures) : that.swFeatures != null) return false;
        if (regState != null ? !regState.equals(that.regState) : that.regState != null) return false;
        return customerID != null ? customerID.equals(that.customerID) : that.customerID == null;
    }

    @Override
    public int hashCode() {
        int result = zoneName != null ? zoneName.hashCode() : 0;
        result = 31 * result + (zoneIcon != null ? zoneIcon.hashCode() : 0);
        result = 31 * result + (configuration != null ? configuration.hashCode() : 0);
        result = 31 * result + (localUID != null ? localUID.hashCode() : 0);
        result = 31 * result + (serialNumber != null ? serialNumber.hashCode() : 0);
        result = 31 * result + (softwareVersion != null ? softwareVersion.hashCode() : 0);
        result = 31 * result + (softwareDate != null ? softwareDate.hashCode() : 0);
        result = 31 * result + (softwareScm != null ? softwareScm.hashCode() : 0);
        result = 31 * result + (minCompatibleVersion != null ? minCompatibleVersion.hashCode() : 0);
        result = 31 * result + (legacyCompatibleVersion != null ? legacyCompatibleVersion.hashCode() : 0);
        result = 31 * result + (hardwareVersion != null ? hardwareVersion.hashCode() : 0);
        result = 31 * result + (dspVersion != null ? dspVersion.hashCode() : 0);
        result = 31 * result + (hwFlags != null ? hwFlags.hashCode() : 0);
        result = 31 * result + (hwFeatures != null ? hwFeatures.hashCode() : 0);
        result = 31 * result + (variant != null ? variant.hashCode() : 0);
        result = 31 * result + (generalFlags != null ? generalFlags.hashCode() : 0);
        result = 31 * result + (ipAddress != null ? ipAddress.hashCode() : 0);
        result = 31 * result + (macAddress != null ? macAddress.hashCode() : 0);
        result = 31 * result + (copyright != null ? copyright.hashCode() : 0);
        result = 31 * result + (extraInfo != null ? extraInfo.hashCode() : 0);
        result = 31 * result + (htAudioInCode != null ? htAudioInCode.hashCode() : 0);
        result = 31 * result + (idxTrk != null ? idxTrk.hashCode() : 0);
        result = 31 * result + (mdp2Ver != null ? mdp2Ver.hashCode() : 0);
        result = 31 * result + (mdp3Ver != null ? mdp3Ver.hashCode() : 0);
        result = 31 * result + (relBuild != null ? relBuild.hashCode() : 0);
        result = 31 * result + (whitelistBuild != null ? whitelistBuild.hashCode() : 0);
        result = 31 * result + (prodUnit != null ? prodUnit.hashCode() : 0);
        result = 31 * result + (fuseCfg != null ? fuseCfg.hashCode() : 0);
        result = 31 * result + (revokeFuse != null ? revokeFuse.hashCode() : 0);
        result = 31 * result + (authFlags != null ? authFlags.hashCode() : 0);
        result = 31 * result + (swFeatures != null ? swFeatures.hashCode() : 0);
        result = 31 * result + (regState != null ? regState.hashCode() : 0);
        result = 31 * result + (customerID != null ? customerID.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SonosSpeakerInfo{" +
                "zoneName='" + zoneName + '\'' +
                ", zoneIcon='" + zoneIcon + '\'' +
                ", configuration='" + configuration + '\'' +
                ", localUID='" + localUID + '\'' +
                ", serialNumber='" + serialNumber + '\'' +
                ", softwareVersion='" + softwareVersion + '\'' +
                ", softwareDate='" + softwareDate + '\'' +
                ", softwareScm='" + softwareScm + '\'' +
                ", minCompatibleVersion='" + minCompatibleVersion + '\'' +
                ", legacyCompatibleVersion='" + legacyCompatibleVersion + '\'' +
                ", hardwareVersion='" + hardwareVersion + '\'' +
                ", dspVersion='" + dspVersion + '\'' +
                ", hwFlags='" + hwFlags + '\'' +
                ", hwFeatures='" + hwFeatures + '\'' +
                ", variant='" + variant + '\'' +
                ", generalFlags='" + generalFlags + '\'' +
                ", ipAddress='" + ipAddress + '\'' +
                ", macAddress='" + macAddress + '\'' +
                ", copyright='" + copyright + '\'' +
                ", extraInfo='" + extraInfo + '\'' +
                ", htAudioInCode='" + htAudioInCode + '\'' +
                ", idxTrk='" + idxTrk + '\'' +
                ", mdp2Ver='" + mdp2Ver + '\'' +
                ", mdp3Ver='" + mdp3Ver + '\'' +
                ", relBuild='" + relBuild + '\'' +
                ", whitelistBuild='" + whitelistBuild + '\'' +
                ", prodUnit='" + prodUnit + '\'' +
                ", fuseCfg='" + fuseCfg + '\'' +
                ", revokeFuse='" + revokeFuse + '\'' +
                ", authFlags='" + authFlags + '\'' +
                ", swFeatures='" + swFeatures + '\'' +
                ", regState='" + regState + '\'' +
                ", customerID='" + customerID + '\'' +
                '}';
    }
}
