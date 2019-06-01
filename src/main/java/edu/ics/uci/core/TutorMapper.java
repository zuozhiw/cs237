package edu.ics.uci.core;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class TutorMapper implements RowMapper<TutorBean> {

    @Override
    public TutorBean map(ResultSet rs, StatementContext ctx) throws SQLException {
        return new TutorBean(rs.getString("email_id"), rs.getString("skills"),
                Optional.ofNullable(rs.getTimestamp("reserved"))
                        .map(t -> t.toLocalDateTime()).orElse(null), rs.getBoolean("available"), rs.getDouble("score"));
    }

}
