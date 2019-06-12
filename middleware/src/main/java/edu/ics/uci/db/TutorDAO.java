package edu.ics.uci.db;

import edu.ics.uci.core.TutorBean;
import edu.ics.uci.core.TutorMapper;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import javax.xml.ws.Response;
import java.util.List;

@RegisterRowMapper(TutorMapper.class)
public interface TutorDAO {

    @SqlQuery("select * from tutor")
    List<TutorBean> listAllTutors();

    @SqlQuery("select * from tutor where skills like '%' || :skill || '%' AND available = true")
    List<TutorBean> findAvailableTutorsWithSkill(@Bind("skill") String skill);

    @SqlQuery("select * from tutor where email_id = :email_id")
    TutorBean findTutor(@Bind("email_id") String email_id);

    @SqlUpdate("delete from tutor where email_id = :email_id")
    void deleteTutor(@Bind("email_id") String email_id);

    @SqlUpdate("insert into tutor values (:email_id, :skills, null, :available, 0);")
    void insertTutor(@Bind("email_id") String email_id, @Bind("skills") String skills, @Bind("available") Boolean available);

    @SqlUpdate("update tutor set skills = :skills, available = :available where email_id = :email_id;")
    void updateTutor(@Bind("email_id") String email_id, @Bind("skills") String skills, @Bind("available") Boolean available);

    @SqlUpdate("update tutor set score = :score, available = :available where email_id = :email_id;")
    void updateTutorAvailability(@Bind("email_id") String email_id, @Bind("score") Double score, @Bind("available") Boolean available);
}
