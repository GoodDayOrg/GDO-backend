package org.example.models;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "Role Application Response", description = "Informs if application has been sent successfully")
public class RoleApplicationResponse {
    @ApiModelProperty(value = "Success or failure", required = true, example = "Application sent successfully")
    private String message;

    public RoleApplicationResponse(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }
}
