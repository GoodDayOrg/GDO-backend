package org.example.models;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.sql.Date;
import java.util.Objects;

@ApiModel(value = "Job Role Response", description = "Defines mapped JobRole object to pass to other methods")
public class JobRoleResponse {
    @ApiModelProperty(
            value = "Job role's name",
            required = true,
            example = "Delivery manager")
    private String roleName;

    @ApiModelProperty(
            value = "Role's location, defined by ENUM value",
            required = true,
            example = "Gdańsk")
    private String jobRoleLocation;

    @ApiModelProperty(
            value = "Capability's name",
            required = true,
            example = "Digital Service")
    private String capabilityName;

    @ApiModelProperty(
            value = "Band's name",
            required = true,
            example = "Trainee")
    private String bandName;

    @ApiModelProperty(
            value = "Expire date of offer",
            required = true,
            example = "11/12/2024")
    private Date closingDate;

    @ApiModelProperty(
            value = "Open or Closed",
            required = true,
            example = "open")
    private String status;

    public JobRoleResponse(final String roleName,
                           final String jobRoleLocation,
                           final String capability,
                           final String band,
                           final Date closingDate,
                           final String status) {
        this.roleName = roleName;
        this.jobRoleLocation = jobRoleLocation;
        this.capabilityName = capability;
        this.bandName = band;
        this.closingDate = closingDate;
        this.status = status;
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

    public String getCapabilityName() {
        return capabilityName;
    }

    public void setCapabilityName(final String capabilityName) {
        this.capabilityName = capabilityName;
    }

    public String getBandName() {
        return bandName;
    }

    public void setBandName(final String bandName) {
        this.bandName = bandName;
    }

    public Date getClosingDate() {
        return closingDate;
    }

    public void setClosingDate(final Date closingDate) {
        this.closingDate = closingDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JobRoleResponse that = (JobRoleResponse) o;
        return Objects.equals(roleName, that.roleName)
                && Objects.equals(jobRoleLocation, that.jobRoleLocation)
                && Objects.equals(capabilityName, that.capabilityName)
                && Objects.equals(bandName, that.bandName)
                && Objects.equals(closingDate, that.closingDate)
                && Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roleName, jobRoleLocation, capabilityName, bandName, closingDate, status);
    }
}
