package org.example.models;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Objects;

@ApiModel(value = "Job Role Response", description = "Defines mapped JobRole object to pass to other methods")
public class JobRoleResponse {
    @ApiModelProperty(value = "unique JobRole id")
    private int jobRoleId;

    @ApiModelProperty(value = "Job role's name", required = true, example = "Delivery manager")
    private String roleName;

    @ApiModelProperty(value = "Role's location, defined by ENUM value", required = true, example = "Gdansk")
    private String jobRoleLocation;

    @ApiModelProperty(value = "Capability's name", required = true, example = "Digital Service")
    private String capabilityName;

    @ApiModelProperty(value = "Band's name", required = true, example = "Trainee")
    private String bandName;

    @ApiModelProperty(value = "Expire date of offer", required = true, example = "11/12/2024")
    private Date closingDate;

    @ApiModelProperty(value = "Open or Closed", required = true, example = "open")
    private String statusName;

    public JobRoleResponse(
            final int jobRoleId,
            final String roleName,
            final String jobRoleLocation,
            final String capability,
            final String band,
            final Date closingDate,
            final String statusName) {
        this.jobRoleId = jobRoleId;
        this.roleName = roleName;
        this.jobRoleLocation = jobRoleLocation;
        this.capabilityName = capability;
        this.bandName = band;
        this.closingDate = closingDate;
        this.statusName = statusName;
    }

    public int getJobRoleId() {
        return jobRoleId;
    }

    public void setJobRoleId(final int jobRoleId) {
        this.jobRoleId = jobRoleId;
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

    public String getClosingDate() {
        if (closingDate != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return sdf.format(closingDate);
        }
        return null;
    }

    public void setClosingDate(final Date closingDate) {
        this.closingDate = closingDate;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(final String statusName) {
        this.statusName = statusName;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        JobRoleResponse that = (JobRoleResponse) o;
        return jobRoleId == that.jobRoleId
                && Objects.equals(roleName, that.roleName)
                && Objects.equals(jobRoleLocation, that.jobRoleLocation)
                && Objects.equals(capabilityName, that.capabilityName)
                && Objects.equals(bandName, that.bandName)
                && Objects.equals(closingDate, that.closingDate)
                && Objects.equals(statusName, that.statusName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(jobRoleId, roleName, jobRoleLocation, capabilityName, bandName, closingDate, statusName);
    }
}
