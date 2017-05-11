package com.example.daniela.progresso.DAO;

import com.example.daniela.progresso.Entidade.Acao;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

/**
 * Created by daniela on 29/04/17.
 */

public class AcaoDAO extends BaseDaoImpl<Acao, Integer> {
    public AcaoDAO(ConnectionSource cs) throws SQLException {
        super(Acao.class);
        setConnectionSource(cs);
        initialize();
    }
}
