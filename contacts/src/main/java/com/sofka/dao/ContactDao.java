package com.sofka.dao;

import com.sofka.domain.Contact;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.List;


public interface ContactDao extends CrudRepository<Contact, Long> {
    /**
     * Metodo para hacer el borrado logico
     * @param id
     * @param delete_at
     */
    @Modifying
    @Column(name = "delete_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    @Query("update Contact cont set cont.delete_at = :delete_at where cont.id = :id")
    public void deletePartial(
            @Param(value = "id") Long id,
            @Param(value = "delete_at") Date delete_at
    );

    @Modifying
    @Query(value = "SELECT * FROM contacts where contacts.delete_at IS NULL",
    nativeQuery = true)
    public List<Contact> findAllNoDeleteLogic();
}