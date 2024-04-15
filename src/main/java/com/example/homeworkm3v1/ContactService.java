package com.example.homeworkm3v1;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.ArgumentPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j

public class ContactService implements  Repository{

    private final JdbcTemplate jdbcTemplate;


    @Override
    public List<Contact> findAll() {
        String sql="select * from contact";
        return jdbcTemplate.query(sql, new ContactRowMapper());
    }




    @Override
    public Optional<Contact> findById(Integer id) {
        String sql = "Select * from contact where id = ?";
        Contact contact=  DataAccessUtils.singleResult(
                jdbcTemplate.query(sql, new ArgumentPreparedStatementSetter(new Object[]{id}),
                        new RowMapperResultSetExtractor<>(new ContactRowMapper(), 1)
                )
        );
        return Optional.ofNullable(contact);
    }

    @Override
    public Contact save(Contact contact) {
        String sqlForId="select max(id) from contact";
        int maxId = jdbcTemplate.queryForObject(sqlForId, Integer.class);
        int newId = maxId + 1;
        contact.setId(newId);
        String sql = "insert into contact (firstName, lastName, email, phone, id) values (?, ?, ?, ?)";
        jdbcTemplate.update(sql, contact.getFirstName(), contact.getLastName(), contact.getEmail(), contact.getPhone(), contact.getId());
        return contact;
    }

    @Override
    public Contact update(Contact contact) {
        
        Contact existedContact=findById(contact.getId()).orElse(null);
        if (existedContact != null) {
            String sql = "update contact set firstName = ?, lastName = ?, email = ?, phone = ? where id = ?";
            jdbcTemplate.update(sql, contact.getFirstName(), contact.getLastName(), contact.getEmail(), contact.getPhone(), contact.getId());
            return contact;
        }
          throw new ContactNotFoundException("contact for update not found! ID: " + contact.getId());
    }

    @Override
    public void deleteById(Integer id) {
        String sql = "delete from contact where id = ?";
        jdbcTemplate.update(sql, id);

    }



}
