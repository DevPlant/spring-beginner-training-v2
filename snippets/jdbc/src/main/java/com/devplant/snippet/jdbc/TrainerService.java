package com.devplant.snippet.jdbc;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.devplant.snippet.jdbc.model.Trainer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TrainerService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    protected void postConstruct() {
        log.info("Creating table ... ");
        jdbcTemplate.execute("DROP TABLE IF EXISTS trainers ");
        jdbcTemplate.execute("CREATE TABLE trainers(id NUMBER, name VARCHAR(255))");
    }

    public void testJdbc() {

        List<Object[]> data = new ArrayList<>();
        Object[] timo = {1, "Timo"};
        Object[] radu = {2, "Radu"};
        data.add(timo);
        data.add(radu);

        jdbcTemplate.batchUpdate("INSERT INTO trainers(id, name) VALUES (?,?)", data);


        List<Trainer> result = jdbcTemplate.query("SELECT * FROM trainers", (rs, rowNum) -> {

            return Trainer.builder().id(rs.getInt("id"))
                    .name(rs.getString("name")).build();
        });

        result.forEach(res -> {
            log.info("We have: {}", res);
        });
    }


}
