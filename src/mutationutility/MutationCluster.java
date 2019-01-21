/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mutationutility;

import htsjdk.variant.variantcontext.VariantContext;
import java.util.ArrayList;

/**
 *
 * @author worawich
 */
public class MutationCluster {
    
    ArrayList<VariantContext> varList;
    int leader_start;
    int leader_stop;
    int clusterSize;
    int cluster_start;
    int cluster_stop;
    
    String cluster_chr;
    
    public MutationCluster(){
        this.varList = new ArrayList();
    }

    public void setVarList(ArrayList<VariantContext> varList) {
        this.varList = varList;
        this.cluster_start = varList.get(0).getStart();
        this.cluster_stop = varList.get(varList.size()-1).getEnd();        
    }

    public int getCluster_start() {
        return cluster_start;
    }

    public void setCluster_start(int cluster_start) {
        this.cluster_start = cluster_start;
    }

    public int getCluster_stop() {
        return cluster_stop;
    }

    public void setCluster_stop(int cluster_stop) {
        this.cluster_stop = cluster_stop;
    }

    public String getCluster_chr() {
        return cluster_chr;
    }

    public void setCluster_chr(String leader_chr) {
        this.cluster_chr = leader_chr;
    }

    public ArrayList<VariantContext> getVarList() {
        return varList;
    }

    public int getLeader_start() {
        return leader_start;
    }

    public int getLeader_stop() {
        return leader_stop;
    }

    public int getClusterSize() {
        return this.varList.size();
    }
    
    public void addVariant(VariantContext in_var){
        this.varList.add(in_var);
        this.cluster_start = varList.get(0).getStart();
        this.cluster_stop = varList.get(varList.size()-1).getEnd();
    }

    void addLeaderStart(int leader_start) {
        this.leader_start = leader_start;
    }

    void addLeaderStop(int leader_stop) {
        this.leader_stop = leader_stop;
    }

    void removeLeader() {
        this.varList.remove(0);
        VariantContext varctx = this.varList.get(0);
        this.leader_start = varctx.getStart();
        this.leader_stop = varctx.getEnd();
        this.cluster_start = varList.get(0).getStart();
        this.cluster_stop = varList.get(varList.size()-1).getEnd();
    }
    
}
