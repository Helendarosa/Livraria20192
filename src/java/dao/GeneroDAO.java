/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import modelo.Genero;

/**
 *
 * @author dappo
 */
public class GeneroDAO extends GenericDAO<Genero, Long>{
    
    public GeneroDAO(){
        super(Genero.class);
    }
    
}
