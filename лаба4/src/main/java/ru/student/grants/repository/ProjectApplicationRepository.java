package ru.student.grants.repository;
import ru.student.grants.model.ProjectApplication;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ProjectApplicationRepository extends JpaRepository<ProjectApplication, Long> {}