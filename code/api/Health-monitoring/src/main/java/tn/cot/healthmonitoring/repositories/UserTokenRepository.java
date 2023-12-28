package tn.cot.healthmonitoring.repositories;


import jakarta.data.repository.CrudRepository;
import jakarta.data.repository.Param;
import jakarta.data.repository.Query;
import jakarta.enterprise.context.ApplicationScoped;
import tn.cot.healthmonitoring.security.UserToken;

import java.util.Optional;


public interface UserTokenRepository extends CrudRepository<UserToken,String> {


    Optional<UserToken> findByEmail(String  email ) ;
    @Query("select * from UserToken where tokens.token = @refreshToken")
    Optional<UserToken> findByRefreshToken(@Param("refreshToken") String token);

    @Query("select * from UserToken where tokens.accessToken.token = @accessToken")
    Optional<UserToken> findByAccessToken(@Param("accessToken") String token);



}