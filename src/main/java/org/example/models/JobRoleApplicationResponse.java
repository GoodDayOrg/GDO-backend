package org.example.models;


import io.swagger.annotations.ApiModelProperty;

public class JobRoleApplicationResponse {

    @ApiModelProperty(value = "Email of user applying for given role", required = true, example = "john.doe@example.com")
    private final String userEmail;
    @ApiModelProperty(value = "Name of role user is applying for", required = true, example = "Test Automation Engineer")
    private final String jorRoleName;
    @ApiModelProperty(value = "Status of an application", required = true, example = "in progress")
    private final String applicationStatus;

    public JobRoleApplicationResponse(String userEmail, String jorRoleName, String applicationStatus) {
        this.userEmail = userEmail;
        this.jorRoleName = jorRoleName;
        this.applicationStatus = applicationStatus;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getJorRoleName() {
        return jorRoleName;
    }

    public String getApplicationStatus() {
        return applicationStatus;
    }
}
