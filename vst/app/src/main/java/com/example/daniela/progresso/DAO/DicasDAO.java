package com.example.daniela.progresso.DAO;

import com.example.daniela.progresso.Entidade.Dicas;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

/**
 * Created by daniela on 03/06/17.
 */

public class DicasDAO extends BaseDaoImpl<Dicas, Integer> {
    public DicasDAO(ConnectionSource cs) throws SQLException {
        super(Dicas.class);
        setConnectionSource(cs);
        initialize();
    }
}