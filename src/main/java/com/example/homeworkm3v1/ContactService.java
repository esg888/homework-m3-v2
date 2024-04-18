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

//    @Override
//    public Contact findById(Integer id) {
//        String sql = "Select * from contact where id = ?";
//        Contact contact = jdbcTemplate.queryForObject(sql, new ContactRowMapper());
//return contact;
//    }

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
        Integer maxId = jdbcTemplate.queryForObject(sqlForId, Integer.class);
        int newId;
        if (maxId == null) {
            newId = 1;
        } else {
            newId =1+maxId;
        }

        contact.setId(newId);
        String sql = "insert into contact (id, firstname, lastname, email, phone) values (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, contact.getId(), contact.getFirstname(), contact.getLastname(), contact.getEmail(), contact.getPhone());
        return contact;
    }

    @Override
    public Contact update(Contact contact) {

        Contact existedContact=findById(contact.getId()).orElse(null);
//        Optional<Contact>= findById(contact.getId()).orElse(null);

        if (existedContact != null) {
            System.out.println("Id = " + existedContact.getId());
            String sql = "update contact set firstname = ?, lastname = ?, email = ?, phone = ? where id = ?";
            jdbcTemplate.update(sql, contact.getFirstname(), contact.getLastname(), contact.getEmail(), contact.getPhone()
                    , contact.getId()
            );
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
