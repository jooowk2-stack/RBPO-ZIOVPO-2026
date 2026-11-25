package ru.student.grants.repository;
import ru.student.grants.model.GrantCompetition;
import org.springframework.data.jpa.repository.JpaRepository;
public interface GrantCompetitionRepository extends JpaRepository<GrantCompetition, Long> {}