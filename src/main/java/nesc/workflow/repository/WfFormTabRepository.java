package nesc.workflow.repository;

import nesc.workflow.model.WfFormTab;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WfFormTabRepository
        extends JpaRepository<WfFormTab, Long> {


}
