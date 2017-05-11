package com.example.daniela.progresso.DAO;

import com.example.daniela.progresso.Entidade.Desafios;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

/**
 * Created by daniela on 02/05/17.
 */

public class DesafioDAO extends BaseDaoImpl<Desafios, Integer> {
    public DesafioDAO(ConnectionSource cs) throws SQLException {
        super(Desafios.class);
        setConnectionSource(cs);
        initialize();
    }
}
