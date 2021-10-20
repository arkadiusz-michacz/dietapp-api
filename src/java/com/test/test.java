/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.test;

import com.cassette.VideoCassette;
import com.services.CassetteService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import pojos.Historia;
import pojos.My_user;
import pojos.Produkt;
import pojos.Profil;

/**
 *
 * @author Arek
 */
@Path("/test")
public class test {
    
    CassetteService cs = new CassetteService();
    
  
    
    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public List<VideoCassette> getAll()
    {
        return cs.getAllCassettes();
    }
    
    @GET
    @Path("/hql")
    @Produces(MediaType.TEXT_PLAIN)
    public String testhql()
    {
        Session s = hibernateController.Controller.getSessionFactory().openSession();
        String HQL ="select P.nazwa from Produkt as P";
        Query q = s.createQuery(HQL);
        List <String> res = q.list();
        return res.get(2);
        
    }
    
    @GET
    @Path("/produkty")
    @Produces(MediaType.APPLICATION_JSON)
    public List<pojos.Produkt> getProducts()
    {
        Session s = hibernateController.Controller.getSessionFactory().openSession();
        String HQL = "from Produkt where status = 1";
        Query q = s.createQuery(HQL);
        List res = q.list();
        return res;
    }
    
    @GET
    @Path("/trendy/{user}/{userid}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<pojos.Historia> getTrendWeek(@PathParam("userid") Long id)
    {
        Session s = hibernateController.Controller.getSessionFactory().openSession();
        String HQL = "from Historia where user_userid ="+id+" and kiedy between (current_date -7) and localtimestamp";
        Query q = s.createQuery(HQL);
        List res = q.list();
        
        return res;
    }
    
    @GET
    @Path("/historialist/{user}/{userid}/{dzien}/{miesiac}/{rok}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<pojos.Historia> getHistoria(@PathParam("userid") Long id, @PathParam("dzien") int dzien,
            @PathParam("miesiac") int miesiac, @PathParam("rok") int rok)
    {
        String msc = String.valueOf(miesiac);
        String dzn = String.valueOf(dzien);
        //String dznplus = String.valueOf(dzien+1);
        if(miesiac<10)
        {
            msc = "0"+miesiac;
        }
        if(dzien<10)
        {
            dzn = "0"+dzien;
        }
        /*if((dzien+1)<10)
        {
            dznplus = "0"+(dzien+1);
        }*/
        System.out.println(rok+"-"+miesiac+"-"+dzien);
        Session s = hibernateController.Controller.getSessionFactory().openSession();
        String HQL = "from Historia where user_userid ="+id+" and kiedy between '"+rok+"-"+msc+"-"+dzn+" 00:00:00.000' and '"+rok+"-"+msc+"-"+dzn+" 24:00:00.000'"/*" and kiedy::text like '"+rok+"-"+msc+"-"+dzn+"%'"*/;
        Query q = s.createQuery(HQL);
        List res = q.list();
        return res;
    }
    
    @GET
    @Path("/produkt/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public pojos.Produkt/*VideoCassette*/ getById(@PathParam("id") long id)
    {
        //return CassetteService.getCassetteById(Long.valueOf(id));
        Session s = hibernateController.Controller.getSessionFactory().openSession();
        pojos.Produkt pr = (pojos.Produkt)s.get(pojos.Produkt.class, id);
        return pr;
        //return videoCassettes.stream().filter(element -> element.getId() == id).findFirst().get();
    }
    
    @GET
    @Path("/profil/{user}/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Profil getProfil(@PathParam("user") String user, @PathParam("id") long id)
    {
        Profil profile;
        Session session = hibernateController.Controller.getSessionFactory().openSession();
          System.out.println(user);
          Transaction tx = session.beginTransaction();
         
            profile = (pojos.Profil)session.get(pojos.Profil.class, id);
           

            tx.commit();

            session.close();

          return profile;
    
    }
    
    @PUT
    @Path("/editprod/{user}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Produkt updateProdukt(@PathParam("user") String user, Produkt produkt)
    {
        Session session = hibernateController.Controller.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        
        Produkt pr = (pojos.Produkt)session.load(Produkt.class, produkt.getId());
        
        pr.setKalorie(produkt.getKalorie());
        pr.setBialko(produkt.getBialko());
        pr.setNazwa(produkt.getNazwa());
        pr.setTluszcz(produkt.getTluszcz());
        pr.setWegle(produkt.getWegle());
        
        
        session.update(pr);
        
        tx.commit();
        
        return pr;
    
    }
    
    
    @PUT
    @Path("/deleteprod/{user}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Produkt deleteProdukt(@PathParam("user") String user, Produkt produkt)
    {
        Session session = hibernateController.Controller.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        
        Produkt pr = (pojos.Produkt)session.load(Produkt.class, produkt.getId());
        
        pr.setStatus(produkt.getStatus());
        
        
        session.update(pr);
        
        tx.commit();
        
        return pr;
    
    }
    
    @DELETE
    @Path("/deletehistory/{user}/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public String deleteHistory(@PathParam("id") long userid)
    {
        Session session = hibernateController.Controller.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        
        try{
            Historia hist = (pojos.Historia)session.load(Historia.class, userid);
        
            session.delete(hist);
        
            tx.commit();
            
            return "YAY";
        }catch(HibernateException ex)
        {
            System.out.println(ex.getMessage());
            return "NAY";
        }
        
    }
    
    
    @PUT
    @Path("/edytujprofil/{userid}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Profil updateProfil(@PathParam("userid") long userid, Profil profil)
    {
        Session session = hibernateController.Controller.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        
        Profil pr = (pojos.Profil)session.load(Profil.class, userid);
        
        pr.setBialko(profil.getBialko());
        pr.setKalorie(profil.getKalorie());
        pr.setTluszcz(profil.getTluszcz());
        pr.setWegle(profil.getWegle());
        
        
        session.update(pr);
        
        tx.commit();
        
        System.out.println(profil.getKalorie());
        System.out.println(userid);
        
        
        
        return profil;
    
    }
    
    @GET
    @Path("/newacc/{user}/{passwd}")
    @Produces(MediaType.TEXT_PLAIN)
    public String createAcc(@PathParam("user") String user, @PathParam("passwd") String passwd)
    {
        
        Session session = hibernateController.Controller.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        
        String HQL = "from My_user where name='"+user+"'";
        Query q = session.createQuery(HQL);
        /*List res = q.list();
        tx.commit();*/
        
        if(q.list().isEmpty())
        {
            try
            {
            
                My_user newAcc = new My_user();
                newAcc.setName(user);
                newAcc.setPasswd(passwd);
                newAcc.setStatus(1);

                session.save(newAcc);

                System.out.println("Id wpisu do historii: "+newAcc.getUserId());

                //tx.commit();
                
                Profil profil = new Profil();
                profil.setId(newAcc);
                profil.setKalorie(2000);
                profil.setBialko(150);
                profil.setWegle(200);
                profil.setTluszcz(40);
                
                session.save(profil);
                
                tx.commit();

                session.close();

                return "Utworzono konto";
            }
            catch(HibernateException ex)
            {
                session.close();
                return "Error "+ex.getMessage();
            
            }
            
            
        }else
        {
                
            session.close();
            return "Takie konto intnieje";
        }
        
    }
    
    
    
    
    @GET
    @Path("/historia/{user}/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Profil getToday(@PathParam("user") String user, @PathParam("id") long id)
    {
        Profil today = new Profil();
        Session session = hibernateController.Controller.getSessionFactory().openSession();
          System.out.println(user);
          
          //today = (pojos.Profil)session.get(pojos.Profil.class, id);
              today.setBialko(0);
              today.setKalorie(0);
              today.setTluszcz(0);
              today.setWegle(0);
          
          
            Transaction tx = session.beginTransaction();

              
              
              
              String HQL = "from Historia where user_userid="+id+"and kiedy  >current_date and kiedy<(current_date+1)";
                Query q = session.createQuery(HQL);
                List res = q.list();
                for(Object hist : res)
                {
                    Historia pozycja = (Historia) hist;
                    today.setBialko(today.getBialko()+pozycja.getProdukt().getBialko());
                    today.setWegle(today.getWegle()+pozycja.getProdukt().getWegle());
                    today.setTluszcz(today.getTluszcz()+pozycja.getProdukt().getTluszcz());
                    today.setKalorie(today.getKalorie()+pozycja.getProdukt().getKalorie());
                }
              
              


              tx.commit();

              session.close();
              
              return today;
          
          
        
    }
    
    
    @POST
    @Path("/historia/{user}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Historia addHistoria(@PathParam("user") String user, Historia wpis)
    {
         String result;
        Session session = hibernateController.Controller.getSessionFactory().openSession();
          System.out.println(user);
          Transaction tx = session.beginTransaction();
          try
          {
          session.save(wpis);
          
          System.out.println("Id wpisu do historii: "+wpis.getId());
          
          
          tx.commit();
          
          session.close();
          
          result = "succes";
          
          }
          catch(HibernateException ex)
          {
              result = "error";
          }
          
          tx = null;
            
           return wpis; 
    
    }
    
    
    @GET
    @Path("/login/{user}")
    @Produces(MediaType.APPLICATION_JSON)
    public My_user getUser(@PathParam("user") String user)
    {
        
        My_user usr;
        Session session = hibernateController.Controller.getSessionFactory().openSession();
          System.out.println(user);
          Transaction tx = session.beginTransaction();
          try
          {
            String HQL = "from My_user where name||passwd like \'"+user+"\'";
            Query q = session.createQuery(HQL);
              List res = q.list();

            tx.commit();

            session.close();

                if(!res.isEmpty())
                  {
                   usr =(My_user) res.get(0);
                  }
                else
                {
                    usr = new My_user();
                    usr.setName(("Nie ma takiego usera"));

                }

          }
          catch(HibernateException ex)
          { 
              usr = new My_user();
              usr.setName(("Error"));
          }
          
          return usr;
    
    }
    
    @POST
    @Path("/dodajprodukt/{user}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Produkt dodajProdukt(@PathParam("user") String user, Produkt produkt)
    {
        String result;
        Session session = hibernateController.Controller.getSessionFactory().openSession();
          System.out.println(user);
          Transaction tx = session.beginTransaction();
          try
          {
              
              String HQL = "from Produkt where nazwa like \'"+produkt.getNazwa()+"\'";
            Query q = session.createQuery(HQL);
              List res = q.list();

            //tx.commit();

            

                if(res.isEmpty())
                  {
                      session.save(produkt);
                      tx.commit();
                   
                  }
                
          /*session.save(produkt);
          
          
          tx.commit();*/
          
          session.close();
          
          result = String.valueOf(produkt.getId());
          
          }
          catch(HibernateException ex)
          {
              result = "error";
          }
          
          tx = null;
            
           return produkt; 
            
    
    }
    
    @POST
    @Path("/dodaj")
    //@Consumes(MediaType.APPLICATION_JSON)
    //@Produces(MediaType.APPLICATION_JSON)
    public void postProdukt()
    {
        My_user user1 = new My_user();
        
        user1.setName("user2");
        user1.setPasswd("passwd");
        user1.setStatus(1);
        Produkt zupa = new Produkt();
        zupa.setId(101);
        zupa.setNazwa("Pomidorowa");
        zupa.setKalorie(250);
        zupa.setBialko(10);
        zupa.setWegle(40);
        zupa.setTluszcz(20);
        zupa.setOwner(user1);
        Produkt zupa2 = new Produkt();
        
        zupa2.setNazwa("Ogorkowa");
        zupa2.setKalorie(250);
        zupa2.setBialko(10);
        zupa2.setWegle(40);
        zupa2.setTluszcz(20);
        zupa2.setOwner(user1);
        
        Profil profil = new Profil();
        profil.setId(user1);
        profil.getId().setUserId(110);
        profil.setKalorie(2000);
        profil.setBialko(150);
        profil.setTluszcz(50);
        profil.setWegle(200);
        
        
          
          
          Session session = hibernateController.Controller.getSessionFactory().openSession();
          
          Transaction tx = session.beginTransaction();
          
          //session.save(user1);
          //session.save(zupa);
          //session.save(zupa2);
          session.save(profil);
          
          
          tx.commit();
          
          session.close();
          
          tx = null;
          
            
       
        
    }
    

    
    @GET
    @Path("/getdata")
    @Produces(MediaType.TEXT_PLAIN)
    public String getDataInJSON()
    {
        return "test pierwszej metody rest";
    }
    
}
