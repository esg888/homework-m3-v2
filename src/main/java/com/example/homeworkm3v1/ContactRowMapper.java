package com.example.homeworkm3v1;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class ContactRowMapper implements RowMapper<Contact> {
    @Override
    public Contact mapRow(ResultSet rs, int rowNum) throws SQLException {

        Contact contact = new Contact();
        contact.setId(rs.getInt(Contact.Fields.id));
        contact.setFirstname(rs.getString(Contact.Fields.firstname));
        contact.setLastname(rs.getString(Contact.Fields.lastname));
        contact.setEmail(rs.getString(Contact.Fields.email));
        contact.setPhone(rs.getString(Contact.Fields.phone));
        return contact;

    }
}
