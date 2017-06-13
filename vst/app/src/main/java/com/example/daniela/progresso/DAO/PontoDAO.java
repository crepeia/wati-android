package com.example.daniela.progresso.DAO;

import com.example.daniela.progresso.Entidade.Pontos;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

/**
 * Created by daniela on 12/06/17.
 */

public class PontoDAO extends BaseDaoImpl<Pontos, Integer> {
    public PontoDAO(ConnectionSource cs) throws SQLException {
        super(Pontos.class);
        setConnectionSource(cs);
        initialize();
    }
}
