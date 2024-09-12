package org.example.models;

import io.swagger.annotations.ApiModelProperty;
import java.sql.Date;
import java.util.Objects;

public class JobRoleDetailsCSV {

    @ApiModelProperty(value = "Job role's name", required = true, example = "Delivery manager")
    private String roleName;

    @ApiModelProperty(value = "Role's location, defined by ENUM value", required = true, example = "Gdansk")
    private String jobRoleLocation;

    @ApiModelProperty(value = "Capability's id", required = true, example = "Digital Service")
    private int capabilityId;

    @ApiModelProperty(value = "Band's id", required = true, example = "Trainee")
    private int bandId;

    @ApiModelProperty(value = "Expire date of offer", required = true, example = "11/12/2024")
    private Date closingDate;

    @ApiModelProperty(value = "Open or Closed id", required = true, example = "open")
    private int statusId;

    @ApiModelProperty(value = "Job role description", required = true, example = "Description")
    private String description;

    @ApiModelProperty(value = "Job role responsibilities", required = true, example = "responsibilities")
    private String responsibilities;

    @ApiModelProperty(value = "https://url.com", required = true, example = "https://url.com")
    private String sharepointUrl;

    @ApiModelProperty(value = "Number of open positions", required = true, example = "2")
    private int numberOfOpenPositions;

    public JobRoleDetailsCSV(
            final String roleName,
            final String jobRoleLocation,
            final int capabilityId,
            final int bandId,
            final Date closingDate,
            final String description,
            final String responsibilities,
            final String sharepointUrl,
            final int statusId,
            final int numberOfOpenPositions) {
        this.roleName = roleName;
        this.jobRoleLocation = jobRoleLocation;
        this.capabilityId = capabilityId;
        this.bandId = bandId;
        this.closingDate = closingDate;
        this.description = description;
        this.responsibilities = responsibilities;
        this.sharepointUrl = sharepointUrl;
        this.statusId = statusId;
        this.numberOfOpenPositions = numberOfOpenPositions;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(final String roleName) {
        this.roleName = roleName;
    }

    public String getJobRoleLocation() {
        return jobRoleLocation;
    }

    public void setJobRoleLocation(final String jobRoleLocation) {
        this.jobRoleLocation = jobRoleLocation;
    }

    public int getCapabilityId() {
        return capabilityId;
    }

    public void setCapabilityId(final int capabilityId) {
        this.capabilityId = capabilityId;
    }

    public int getBandId() {
        return bandId;
    }

    public void setBandId(final int bandId) {
        this.bandId = bandId;
    }

    public Date getClosingDate() {
        return closingDate;
    }

    public void setClosingDate(final Date closingDate) {
        this.closingDate = closingDate;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(final int statusId) {
        this.statusId = statusId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getResponsibilities() {
        return responsibilities;
    }

    public void setResponsibilities(final String responsibilities) {
        this.responsibilities = responsibilities;
    }

    public String getSharepointUrl() {
        return sharepointUrl;
    }

    public void setSharepointUrl(final String sharepointUrl) {
        this.sharepointUrl = sharepointUrl;
    }

    public int getNumberOfOpenPositions() {
        return numberOfOpenPositions;
    }

    public void setNumberOfOpenPositions(final int numberOfOpenPositions) {
        this.numberOfOpenPositions = numberOfOpenPositions;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        JobRoleDetailsCSV that = (JobRoleDetailsCSV) o;
        return capabilityId == that.capabilityId
                && bandId == that.bandId
                && statusId == that.statusId
                && numberOfOpenPositions == that.numberOfOpenPositions
                && Objects.equals(roleName, that.roleName)
                && Objects.equals(jobRoleLocation, that.jobRoleLocation)
                && Objects.equals(closingDate, that.closingDate)
                && Objects.equals(description, that.description)
                && Objects.equals(responsibilities, that.responsibilities)
                && Objects.equals(sharepointUrl, that.sharepointUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                roleName,
                jobRoleLocation,
                capabilityId,
                bandId,
                closingDate,
                statusId,
                description,
                responsibilities,
                sharepointUrl,
                numberOfOpenPositions);
    }
}
