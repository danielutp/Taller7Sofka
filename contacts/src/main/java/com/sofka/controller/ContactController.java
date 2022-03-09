package com.sofka.controller;

import com.sofka.domain.Contact;
import com.sofka.service.ContactService;
import com.sofka.util.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.regex.Pattern;

@Slf4j
@RestController
public class ContactController {

    @Autowired
    private ContactService contactService;
    private Response response = new Response();

    /**
     * Metodo para mostrar la lista de contactos
     * @return
     */
    @GetMapping(path ="/contacts")
    @CrossOrigin()
    public Response list(){
        try {
            response.data= contactService.list();
        } catch (Exception exc) {
            response.error = true;
            response.message = exc.getMessage();
            response.status = "ERROR";
        }
            return response;
    }

    /**
     * Metodo para crear un contacto
     * @param contact
     * @return
     */
    @PostMapping(path ="/contact/create")
    @CrossOrigin()
    public ResponseEntity<Response> create(@RequestBody Contact contact) {
        response.data = contact;
        try {
            log.info("Usuario a crear: {}", contact);
            contactService.save(contact);
            return new ResponseEntity<Response>(response, HttpStatus.CREATED);
        } catch (Exception exc) {
            response.status = exc.getCause().toString();
            response.error = true;
            if (Pattern.compile("(usuario.tipo_documento_and_documento_UNIQUE)").matcher(exc.getMessage()).find()) {
                response.message = "documento duplicado";
                return new ResponseEntity<Response>(response, HttpStatus.CONFLICT);
            } else {
                response.message = exc.getMessage();
                return new ResponseEntity<Response>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    /**
     * Metodo para borrar un contacto por su id
     * @param contact
     * @return
     */
    @DeleteMapping(path ="/contact/deletetotal/{id}")
    @CrossOrigin()
    public ResponseEntity<Contact> delete(@RequestBody Contact contact){
        log.info("Contacto a borrar {}",contact);
        contactService.delete(contact);
        return new ResponseEntity<>(contact, HttpStatus.OK);
    }

    /**
     * Metodo para actualizar contacto
     * @param contact
     * @param id
     * @return
     */
    @PutMapping(path ="/contact/update/{id}")
    @CrossOrigin()
    public ResponseEntity update(@RequestBody Contact contact, @PathVariable("id") Long id){
        contactService.update(id,contact);
        log.info("Contacto a modificar {}",contact);
        return new ResponseEntity<>(contact, HttpStatus.OK);
    }

    /**
     * Metodo para hacer borrado logico
     * @param contact
     * @param id
     * @return
     */
    @PatchMapping (path ="/contact/updatepartial/{id}")
    @CrossOrigin()
    public ResponseEntity updatedelete(@RequestBody Contact contact, @PathVariable("id") Long id){
        contactService.deletePartial(id,contact);
        log.info("Contacto a modificar {}",contact);
        return new ResponseEntity<>(contact, HttpStatus.OK);
    }
}
