package com.example.daniela.progresso.DAO;

import com.example.daniela.progresso.Entidade.Cigarros;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

/**
 * Created by daniela on 02/05/17.
 */

public class CigarrosDAO extends BaseDaoImpl<Cigarros, Integer> {
    public CigarrosDAO(ConnectionSource cs) throws SQLException {
        super(Cigarros.class);
        setConnectionSource(cs);
        initialize();
    }
}
