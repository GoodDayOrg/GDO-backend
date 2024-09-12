package org.example.models;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "Job Role Application", description = "Stores information about users applications and its statuses")
public class JobRoleApplication {

    @ApiModelProperty(value = "Role's id", required = true)
    private int jobRoleId;

    @ApiModelProperty(value = "Role's name", required = true)
    private String roleName;

    @ApiModelProperty(value = "Application status name", required = true, example = "hired")
    private String statusApplicationName;

    public JobRoleApplication(final int jobRoleId, final String roleName, final String statusApplicationName) {
        this.jobRoleId = jobRoleId;
        this.roleName = roleName;
        this.statusApplicationName = statusApplicationName;
    }

    public String getStatusApplicationName() {
        return statusApplicationName;
    }

    public void setStatusApplicationName(final String statusApplicationName) {
        this.statusApplicationName = statusApplicationName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(final String roleName) {
        this.roleName = roleName;
    }

    public int getJobRoleId() {
        return jobRoleId;
    }

    public void setJobRoleId(final int jobRoleId) {
        this.jobRoleId = jobRoleId;
    }
}
