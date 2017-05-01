package com.example.daniela.progresso.DAO;

import com.example.daniela.progresso.Entidade.Acoes;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

/**
 * Created by daniela on 29/04/17.
 */

public class AcoesDAO extends BaseDaoImpl<Acoes, Integer> {
    public AcoesDAO(ConnectionSource cs) throws SQLException {
        super(Acoes.class);
        setConnectionSource(cs);
        initialize();
    }
}
