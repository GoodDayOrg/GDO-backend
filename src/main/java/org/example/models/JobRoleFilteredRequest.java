package org.example.models;

import io.swagger.annotations.ApiModelProperty;
import java.sql.Date;
import javax.ws.rs.QueryParam;

public class JobRoleFilteredRequest {
    @ApiModelProperty(value = "Job role's name", example = "Delivery manager")
    @QueryParam("roleName")
    private String roleName;

    @ApiModelProperty(value = "Role's location, defined by ENUM value", example = "Gdansk,Helsinki")
    @QueryParam("jobRoleLocation")
    private String jobRoleLocation;

    @ApiModelProperty(value = "Capability's name", example = "Cyber Security,Data & AI")
    @QueryParam("capabilityName")
    private String capabilityName;

    @ApiModelProperty(value = "Band's name", example = "Trainee")
    @QueryParam("bandName")
    private String bandName;

    @ApiModelProperty(value = "Expire date of offer", example = "2024-12-30")
    @QueryParam("closingDate")
    private Date closingDate;

    public String getRoleName() {
        return roleName;
    }

    public String getLikeRoleName() {
        if (roleName != null && !roleName.isEmpty()) {
            return "%" + roleName + "%";
        }
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
}
