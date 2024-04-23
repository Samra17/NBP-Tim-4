package com.nbp.tim3.repository;


import ch.qos.logback.core.encoder.EchoEncoder;
import com.nbp.tim3.model.Category;
import com.nbp.tim3.model.Token;
import com.nbp.tim3.service.DBConnectionService;
import com.nbp.tim3.util.exception.InvalidRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TokenRepository {

    private static final Logger logger = LoggerFactory.getLogger(TokenRepository.class);

    @Autowired
    DBConnectionService dbConnectionService;

    public Token findByToken(String tokenBase64) {
        String sql = "SELECT * FROM nbp_token WHERE token = ? FETCH FIRST 1 ROW ONLY";

        try {
            Connection connection = dbConnectionService.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, tokenBase64);

            ResultSet resultSet = preparedStatement.executeQuery();


            if (resultSet.next()) {
                String tokenValue = resultSet.getString("token");
                boolean expired = resultSet.getBoolean("expired");
                boolean revoked = resultSet.getBoolean("revoked");

                Token token = new Token(tokenValue,expired,revoked);
                token.setId(resultSet.getInt("id"));
                return token;
            }

            return  null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public void addToken(Token token) {
        String sql = "INSERT INTO nbp_token(token,expired,revoked,user_id) VALUES(?,?,?,?)";

        Connection connection = null;
        boolean exception = false;

        try {
            connection = dbConnectionService.getConnection();
            String returnCols[] = { "id" };

            PreparedStatement preparedStatement = connection.prepareStatement(sql,returnCols);
            preparedStatement.setString(1,token.getToken());

            if(token.isExpired())
                preparedStatement.setInt(2,1);
            else
                preparedStatement.setInt(2,0);

            if(token.isRevoked())
                preparedStatement.setInt(3,1);
            else
                preparedStatement.setInt(3,0);

            preparedStatement.setInt(4,token.getUser().getId());

            int rowCount = preparedStatement.executeUpdate();

            if(rowCount > 0) {
                ResultSet rs = preparedStatement.getGeneratedKeys();
                if(rs.next()) {
                    int generatedId = rs.getInt(1);
                    token.setId(generatedId);
                } else {
                    logger.error("No generated ID!");
                }
            }


            connection.commit();


            logger.info(String.format("Successfully inserted %d rows into Token.", rowCount));


        }

        catch (SQLException e) {
            logger.error(e.getMessage());

            exception = true;

            if(e.getSQLState().startsWith("23")) {
                 if (e.getMessage().contains("FK_TOKEN_USER")) {
                    throw new InvalidRequestException(String.format("User with id %d does not exist!", token.getUser().getId()));
                }
            }


        } catch (Exception e) {
            exception = true;
            e.printStackTrace();
            throw e;
        } finally {
            if(exception && connection!=null) {
                try {
                    connection.rollback();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }


    public List<Token> getAllValidTokensByUser(int userId) {
        List<Token> tokens = new ArrayList<>();
        String sql = "SELECT * FROM nbp_token WHERE user_id=?";

        try {
            Connection connection = dbConnectionService.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,userId);

            ResultSet resultSet = preparedStatement.executeQuery();


            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String tokenValue = resultSet.getString("token");
                boolean expired = resultSet.getBoolean("expired");
                boolean revoked = resultSet.getBoolean("revoked");

                Token token = new Token(id,tokenValue,expired,revoked,null);
                tokens.add(token);
            }

            return  tokens;
        } catch (Exception e) {
            e.printStackTrace();
            return tokens;
        }
    }

    public void revokeValidUserTokens(int userId) {
        String sql = "UPDATE nbp_token SET expired=1, revoked=1 where user_id=? AND expired=0 AND revoked=0";
        Connection connection = null;
        boolean exception = false;

        try {
            connection = dbConnectionService.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userId);


            int rowCount = preparedStatement.executeUpdate();

            connection.commit();


            logger.info(String.format("Successfully revoked %d tokens.", rowCount));

        }
        catch (SQLException e) {
            logger.error(e.getMessage());
            exception=true;
        }
        catch (Exception e) {
            e.printStackTrace();
            exception = true;
        }  finally {
        if(exception && connection!=null) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        }
    }

    public void revokeToken(int tokenId) {
        String sql = "UPDATE nbp_token SET expired=1, revoked=1 where id=?";
        Connection connection = null;
        boolean exception = false;

        try {
            connection = dbConnectionService.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, tokenId);


            int rowCount = preparedStatement.executeUpdate();

            connection.commit();


            logger.info(String.format("Successfully revoked %d tokens.", rowCount));

        }
        catch (SQLException e) {
            logger.error(e.getMessage());
            exception=true;
        }
        catch (Exception e) {
            e.printStackTrace();
            exception = true;
        }  finally {
            if(exception && connection!=null) {
                try {
                    connection.rollback();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
