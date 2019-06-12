package edu.ics.uci.db;

import edu.ics.uci.core.TutorBean;
import edu.ics.uci.core.TutorMapper;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

import java.util.List;

@RegisterRowMapper(TutorMapper.class)
public interface TutorDAO {

    @SqlQuery("select * from tutor")
    List<TutorBean> listAllTutors();

    @SqlQuery("select * from tutor where skills like '%' || :skill || '%' AND available = true")
    List<TutorBean> findAvailableTutorsWithSkill(@Bind("skill") String skill);

}
