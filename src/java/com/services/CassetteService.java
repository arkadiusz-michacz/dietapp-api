/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.services;

import com.cassette.VideoCassette;
import com.database.DatabaseClass;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Arek
 */
public class CassetteService {
    private static Map<Long,VideoCassette> videoCassettes = DatabaseClass.getCassettes();
    
    public CassetteService(){
        videoCassettes.put(1L, new VideoCassette(1L,"Titanic"));
        videoCassettes.put(2L, new VideoCassette(2L,"Pulp Fiction"));
    
    }
    
     public List<VideoCassette> getAllCassettes(){
             return new ArrayList<VideoCassette>(videoCassettes.values());
     }
     
     public static VideoCassette getCassetteById(Long id)
     {
         return videoCassettes.get(id);
     
     }
     
     public VideoCassette addCassette(VideoCassette vc)
     {
         vc.setId(Long.valueOf(videoCassettes.size()+1));
         videoCassettes.put(vc.getId(), vc);
         return vc;
     }
     
     public VideoCassette updateCassette(VideoCassette vc)
     {
         if(vc.getId() <= 0)
         {
             return null;
         }
         videoCassettes.put(vc.getId(), vc);
         
         return vc;
     }
     
     
     public VideoCassette deleteCassette(Long id)
     {
         
         
         return videoCassettes.remove(id);
     
     }
    
}
