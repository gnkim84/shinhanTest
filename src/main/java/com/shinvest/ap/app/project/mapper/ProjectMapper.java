package com.shinvest.ap.app.project.mapper;

import com.shinvest.ap.app.project.model.ProjectModel;
import com.shinvest.ap.app.project.model.ProjectCriteria;
import com.shinvest.ap.common.annotation.ConnMapperFirst;

import java.util.List;

@ConnMapperFirst
public interface ProjectMapper {

	List<ProjectModel> selectProjectList(ProjectCriteria criteria);
	int selectProjectListCount(ProjectCriteria criteria);
	ProjectModel selectProject(ProjectModel model);
	long deleteProject(ProjectModel model);
	long insertProject(ProjectModel model);
	long updateProject(ProjectModel model);
	
	long insertProjectHst(ProjectModel model);
}
