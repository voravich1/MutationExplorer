/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mutationutility;

import java.util.ArrayList;

/**
 *
 * @author worawich
 */
public class MutationSuperCluster {
    
    int superCluster_start;
    int superCluster_stop;
    String superCluster_chr;
    ArrayList<MutationCluster> list_mutation_cluster;
    
    public MutationSuperCluster(){
        this.list_mutation_cluster = new ArrayList();
    }

    public int getSuperCluster_start() {
        return superCluster_start;
    }

    public void setSuperCluster_start(int superCluster_start) {
        this.superCluster_start = superCluster_start;
    }

    public int getSuperCluster_stop() {
        return superCluster_stop;
    }

    public void setSuperCluster_stop(int superCluster_stop) {
        this.superCluster_stop = superCluster_stop;
    }

    public String getSuperCluster_chr() {
        return superCluster_chr;
    }

    public void setSuperCluster_chr(String superCluster_chr) {
        this.superCluster_chr = superCluster_chr;
    }

    public void setList_mutation_cluster(ArrayList<MutationCluster> list_mutation_cluster) {
        this.list_mutation_cluster = list_mutation_cluster;
        updateData();
    }

    public ArrayList<MutationCluster> getList_mutation_cluster() {
        return list_mutation_cluster;
    }
    
    public void addMutationCluster(MutationCluster in_cluster){
        this.list_mutation_cluster.add(in_cluster);
        updateData();
    }
    
    public void removeMutationCluster(){
        this.list_mutation_cluster.remove(0);
        updateData();
    }
    
    public void updateData(){
        this.superCluster_chr = this.list_mutation_cluster.get(0).getCluster_chr();
        this.superCluster_start = this.list_mutation_cluster.get(0).getCluster_start();
        this.superCluster_stop = this.list_mutation_cluster.get(this.list_mutation_cluster.size()-1).getCluster_stop();
    }
    
     public int getClusterSize() {
        return this.list_mutation_cluster.size();
    }
}
