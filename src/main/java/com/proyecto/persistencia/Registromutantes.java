/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.proyecto.persistencia;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Camilo
 */
@Entity
@Table(name = "registromutantes")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Registromutantes.findAll", query = "SELECT r FROM Registromutantes r")
    
    , @NamedQuery(name = "Registromutantes.findById", query = "SELECT r FROM Registromutantes r WHERE r.id = :id")
    , @NamedQuery(name = "Registromutantes.findByDna", query = "SELECT r FROM Registromutantes r WHERE r.dna = :dna")
    , @NamedQuery(name = "Registromutantes.findByEsmutante", query = "SELECT r FROM Registromutantes r WHERE r.esmutante = :esmutante")})
public class Registromutantes implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "dna")
    private String dna;
    @Basic(optional = false)
    @NotNull
    @Column(name = "esmutante")
    private int esmutante;

    public Registromutantes() {
    }

    public Registromutantes(Integer id) {
        this.id = id;
    }

    public Registromutantes(Integer id,String dna, int esmutante) {
        this.id =id;
        this.dna = dna;
        this.esmutante = esmutante;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDna() {
        return dna;
    }

    public void setDna(String dna) {
        this.dna = dna;
    }

    public int getEsmutante() {
        return esmutante;
    }

    public void setEsmutante(int esmutante) {
        this.esmutante = esmutante;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Registromutantes)) {
            return false;
        }
        Registromutantes other = (Registromutantes) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.proyecto.persistencia.Registromutantes[ id=" + id + " ]";
    }
    
}
