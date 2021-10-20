/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.database;

import com.cassette.VideoCassette;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Arek
 */
public class DatabaseClass {
    
    private static Map<Long,VideoCassette> videoCassettes = new HashMap<>();
    
    public static Map<Long,VideoCassette> getCassettes()
    {
        return videoCassettes;
    }
    
}
