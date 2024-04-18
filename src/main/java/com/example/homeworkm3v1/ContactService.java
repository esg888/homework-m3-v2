package com.example.homeworkm3v1;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.ArgumentPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import java.util.List;
import java.util.Optional;

//@Service
@org.springframework.stereotype.Repository
@RequiredArgsConstructor
@Slf4j

public class ContactService implements  Repository{


    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Contact> findAll() {
        String sql="select * from contact";
        return jdbcTemplate.query(sql, new ContactRowMapper());
    }

    @Override //https://javarush.com/quests/lectures/questspring.level03.lecture08
    public Contact findById(Integer id) {
        String sql = "SELECT id, firstname, lastname, email, phone FROM contact WHERE id = ?";

        return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> {
            Contact contactFind = new Contact();
            contactFind.setId(rs.getInt("id"));
            contactFind.setFirstname(rs.getString("firstname"));
            contactFind.setLastname(rs.getString("lastname"));
            contactFind.setEmail(rs.getString("email"));
            contactFind.setPhone(rs.getString("phone"));
            return contactFind;
        });
    }


    @Override
    public Contact save(Contact contact) {
        String sqlForId="select max(id) from contact";
        Integer maxId = jdbcTemplate.queryForObject(sqlForId, Integer.class);
        int newId;
        if (maxId == null) {
            newId = 1;
        } else {
            newId =1+maxId;
        }

        contact.setId(newId);
        String sqlSave = "insert into contact (id, firstname, lastname, email, phone) values (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sqlSave, contact.getId(), contact.getFirstname(), contact.getLastname(), contact.getEmail(), contact.getPhone());
        return contact;
    }

    @Override // то, по чему ищем - сзади!contact.getId - jdbcTemplate.update
    public Contact update(Contact contact) {
        String sqlUp = "update contact set firstname = ?, lastname = ?, email = ?, phone = ? where id = ?";
        jdbcTemplate.update(sqlUp,  contact.getFirstname(), contact.getLastname(), contact.getEmail(), contact.getPhone(), contact.getId());

            return contact;

    }

    @Override
    public void deleteById(Integer id) {
        String sqlDel = "delete from contact where id = ?";
        jdbcTemplate.update(sqlDel, id);
    }
}
