/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hibernateController;

import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.SessionFactory;
import pojos.Historia;
import pojos.My_user;
import pojos.Produkt;
import pojos.Profil;


/**
 * Hibernate Utility class with a convenient method to get Session Factory
 * object.
 *
 * @author Arek
 */
public class Controller {

    private static final SessionFactory sessionFactory;
    
    static {
        try {
            // Create the SessionFactory from standard (hibernate.cfg.xml) 
            // config file.
            sessionFactory = new AnnotationConfiguration()
                    .addAnnotatedClass(My_user.class)
                    .addAnnotatedClass(Produkt.class)
                    .addAnnotatedClass(Profil.class)
                    .addAnnotatedClass(Historia.class)
                    .configure()
                    .buildSessionFactory();
        } catch (Throwable ex) {
            // Log the exception. 
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }
    
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
