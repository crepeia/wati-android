package com.example.daniela.progresso.DAO;

import com.example.daniela.progresso.Entidade.Pontuacao;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

/**
 * Created by daniela on 02/05/17.
 */

public class PontuacaoDAO extends BaseDaoImpl<Pontuacao, Integer> {
    public PontuacaoDAO(ConnectionSource cs) throws SQLException {
        super(Pontuacao.class);
        setConnectionSource(cs);
        initialize();
    }
}
