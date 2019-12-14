package com.example.Gerrymender.web;


import com.example.Gerrymender.Abstractions.*;
import com.example.Gerrymender.exception.ResourceNotFoundException;
import com.example.Gerrymender.model.*;
import com.example.Gerrymender.repository.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import reactor.util.function.Tuple2;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;


@Controller
public class MainController {
    @Autowired
    StateRepository stateRepository;
    @Autowired
    DistrictRepository districtRepository;
    @Autowired
    PrecinctRepository precinctRepository;
    @Autowired
    VoteRepository voteRepository;
    @Autowired
    VoteDisRepository voteDisRepository;

    Algorithm alg;

    @RequestMapping("/")
    public String welcome(){
        return "Homepage";
    }

    @RequestMapping(value="/getSelectArea",method = RequestMethod.POST)
    public @ResponseBody String updateSmallInfoWindow(String stateName, String id, String mapLevel, String year, String electionType) {
        ObjectMapper obj = new ObjectMapper();
        List res = new ArrayList();
        switch(mapLevel){
            case "district":
                District founddis = districtRepository.findByDistrictidAndStatename(id, stateName);
                res.add(founddis);
                VoteDis dv = voteDisRepository.findByVoteid(new VoteDisId(id, founddis.getStateName(), "" + year, electionType));
                res.add(dv);
                break;
            default:
                Precinct foundpre = precinctRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("precinct", "id", id));
                res.add(foundpre);
                Vote v = voteRepository.findByVoteid(new VoteId(foundpre.getNameID(), "" + year, electionType));
                res.add(v);
        }
        try {
            String r = obj.writeValueAsString(res);
            return r;
        } catch (IOException e) {
            return "";
        }


    }

    @ResponseBody
    @RequestMapping("/Test")
    public String stateList(){
        ObjectMapper obj = new ObjectMapper();
        try {
            String ret = obj.writeValueAsString(alg.getBaseState());
            return ret;
        } catch (IOException e) {
            return "";
        }
    }


    @RequestMapping(value="/phase0",method = RequestMethod.POST)
    public @ResponseBody String phase0(String electionYear, String electionType, double popThreshold, double voteThreshold) {
        alg.lock.lock();
        List<VotingBlocInfo> r = alg.phase0(popThreshold / 100.0, voteThreshold / 100.0, electionYear + electionType);
        alg.lock.unlock();
        ObjectMapper obj = new ObjectMapper();
        try {
            String ret = obj.writeValueAsString(r);
            return ret;
        } catch (IOException e) {
            return "";
        }
    }

    @RequestMapping(value = "/phase1", method = RequestMethod.POST)
    public @ResponseBody String phase1(String[] whichRaces, double minPopPerc, double maxPopPerc, int numDistricts) {
        alg.lock.lock();
        String ret = "";
        if(alg.isRunning()) {
            List<Tuple2<String, String>> r = null;
            if(!alg.getPhase1Queue().isEmpty()) {
                r = alg.getPhase1Queue().remove();
            }
            ObjectMapper obj = new ObjectMapper();
            try {
                if(r != null) {
                    ret = obj.writeValueAsString(r);
                }
                alg.lock.unlock();
                return ret;
            } catch (IOException e) {
                alg.lock.unlock();
                return "";
            }
        }
        else{
            Race[] races = new Race[whichRaces.length];
            for(int i = 0; i < whichRaces.length; i++) {
                    races[i] = (Race.valueOf(whichRaces[i]));
            }
            Runnable run = () -> {
                alg.phase1(races, minPopPerc / 100.0, maxPopPerc / 100.0, numDistricts);
            };
            new Thread(run).start();
        }
        alg.lock.unlock();
        return ret;
    }

    @RequestMapping(value="/updateState", method=RequestMethod.POST)
    public @ResponseBody void updateState(String id) {
        if(alg == null) {
            alg = new Algorithm();
        }
        alg.lock.lock();
        State s = stateRepository.findById(id).orElse(null);
        List<Precinct> precincts = precinctRepository.findByStatename(s.getNameID());
        Map<String, BasePrecinct> basePrecincts = new HashMap<>();
        for(Precinct p : precincts) {
            Map<String, Votes> votes = new HashMap<>();
            votes.put("2016PRESIDENT", new Votes(voteRepository.findByVoteid(new VoteId(p.getNameID(), "2016", "PRESIDENT"))));
            votes.put("2016CONGRESSIONAL", new Votes(voteRepository.findByVoteid(new VoteId(p.getNameID(), "2016", "CONGRESSIONAL"))));
            votes.put("2018CONGRESSIONAL", new Votes(voteRepository.findByVoteid(new VoteId(p.getNameID(), "2018", "CONGRESSIONAL"))));
            basePrecincts.put(p.getNameID(), new BasePrecinct(p, votes));
        }
        for(String key : basePrecincts.keySet()) {
            BasePrecinct basePrecinct = basePrecincts.get(key);
            for(String neighborId : basePrecinct.getNeighborIDs()) {
                if(basePrecincts.get(neighborId) != null) {
                    basePrecinct.getEdges().add(basePrecincts.get(neighborId));
                }
            }
        }
        BaseState baseState = new BaseState(s);
        baseState.setPrecincts(basePrecincts);
        alg.setBaseState(baseState);
        alg.lock.unlock();
        System.out.println("State " + id + " has been updated.");
    }
}
