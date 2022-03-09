package com.sofka.service;

import com.sofka.dao.ContactDao;
import com.sofka.domain.Contact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ContactService implements IContactService {

    @Autowired //Para inyectar ContactDao
    private ContactDao contactDao;

    /**
     * Servicio de listar contactos
     * @return (List<Contact>)
     */
    @Override
    @Transactional(readOnly = true)
    public List<Contact> list() {
        return (List<Contact>) contactDao.findAllNoDeleteLogic();
    }

    /**
     * Servicio para guardar un contacto
     * @param contact
     * @return contact
     */
    @Override
    @Transactional
    public Contact save(Contact contact) {
        return contactDao.save(contact);
    }

    /**
     * Servicio para actualizar contacto
     * @param id
     * @param contact
     * @return contact
     */
    @Override
    @Transactional
    public Contact update(Long id, Contact contact) {
        contact.setId(id);
        return contactDao.save(contact);
    }

    /**
     * Servicio para borrar logicamente un contacto
     * @param id
     * @param contact
     */
    @Transactional
    public void deletePartial (Long id, Contact contact){
        contactDao.deletePartial(id,contact.getDelete_at());
    }

    /**
     * Servicio para borrar contacto
     * @param contact
     */
    @Override
    @Transactional
    public void delete(Contact contact) {
        contactDao.delete(contact);
    }

    /**
     * Servicio para encontrar un contacto
     * @param contact
     * @return contact
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Contact> findContact(Contact contact) {
        return contactDao.findById(contact.getId());
    }
}
