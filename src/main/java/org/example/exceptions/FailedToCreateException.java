package org.example.exceptions;

import javax.ws.rs.core.Response;

public class FailedToCreateException extends Throwable {

    private final Response.Status status;

    public FailedToCreateException(final Entity entity) {
        super(entity.getEntity());
        this.status = Response.Status.INTERNAL_SERVER_ERROR;
    }

    public Response.Status getStatus() {
        return status;
    }
}
