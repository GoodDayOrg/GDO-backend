package org.example.mappers;

import org.example.daos.JobRoleDao;
import org.example.models.JobRole;
import org.example.models.JobRoleDetails;
import org.example.models.JobRoleDetailsCSV;
import org.example.models.JobRoleResponse;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class JobRoleMapper {


    public static List<JobRoleResponse> toResponse(final List<JobRole> jobRoles) {
        List<JobRoleResponse> jobRoleResponses = new ArrayList<>();
        for (JobRole j : jobRoles) {
            jobRoleResponses.add(toResponse(j));
        }
        return jobRoleResponses;
    }

    private static JobRoleResponse toResponse(final JobRole jobRole) {
        return new JobRoleResponse(
                jobRole.getJobRoleId(),
                jobRole.getRoleName(),
                jobRole.getJobRoleLocation(),
                jobRole.getCapabilityName(),
                jobRole.getBandName(),
                jobRole.getClosingDate(),
                jobRole.getStatusName()
        );
    }
    public JobRoleDetailsCSV toJobRolesCSV(final JobRoleDetails jobRoleDetails, final JobRoleDao jobRoleDao) throws SQLException {
        return new JobRoleDetailsCSV(
                jobRoleDetails.getRoleName(),
                jobRoleDetails.getJobRoleLocation(),
                jobRoleDao.getCapabilityIdByName(jobRoleDetails.getCapabilityName()),
                jobRoleDao.getBandIdByName(jobRoleDetails.getBandName()),
                jobRoleDetails.getClosingDate(),
                jobRoleDetails.getDescription(),
                jobRoleDetails.getResponsibilities(),
                jobRoleDetails.getSharepointUrl(),
                jobRoleDao.getStatusIdByName(jobRoleDetails.getStatusName()),
                jobRoleDetails.getNumberOfOpenPositions()
        );
    }

}
