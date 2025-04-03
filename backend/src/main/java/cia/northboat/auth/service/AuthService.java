package cia.northboat.auth.service;


import cia.northboat.auth.pojo.Pair;
import cia.northboat.auth.crypto.Schnorr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AuthService {

    private final Schnorr schnorr;

    @Autowired
    public AuthService(Schnorr schnorr){
        this.schnorr = schnorr;
    }


    public List<Pair> auth(String algo){
        if(algo.equals("Schnorr")){
            return schnorr.simulate();
        }
        return null;
    }


    public List<Pair> auth(Map<String, String> params){
        String algo = params.get("algo");

        return auth(algo);
    }



}
