/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mutationutility;

import htsjdk.variant.variantcontext.Allele;
import htsjdk.variant.variantcontext.VariantContext;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 *
 * @author worawich
 */
public class MutationSuperCluster {
    
    private int superCluster_start;
    private int superCluster_stop;
    private String superCluster_chr;
    private ArrayList<MutationCluster> list_mutation_cluster;
    private int count_mutationCA;
    private int count_mutationCG;
    private int count_mutationCT;
    private int count_mutationTA;
    private int count_mutationTC;
    private int count_mutationTG;
    private int count_mutation_other;
    private int total_alt_alleles;
    private int score_for_bed;      // score value for shade visualization. It represent number of varaint in cluster or cluster size. the value will be in range 0-1000. which varies by super cluster range divined by max variant size of cluster  both value has been set by user
    private int max_possible_cluster;   // maximum possible of number of cluster can be in one super cluster (typically equal to (super cluster range)/max variant size of cluster)
    private int super_cluster_range;    // range of super cluster defined by user
    private LinkedHashMap<Integer,VariantContext> pureVarList;         // store ID code of each variant context as key and store variant context as value. ID code has been generate by method hashcode of the object.
    
    public MutationSuperCluster(){
        this.pureVarList = new LinkedHashMap();
        this.list_mutation_cluster = new ArrayList();
        this.count_mutationCA = 0;
        this.count_mutationCG = 0;
        this.count_mutationCT = 0;
        this.count_mutationTA = 0;
        this.count_mutationTC = 0;
        this.count_mutationTG = 0;
        this.count_mutation_other = 0;
        this.total_alt_alleles = 0;
    }

    public int getCount_mutation_other() {
        return count_mutation_other;
    }

    public int getSuper_cluster_range() {
        return super_cluster_range;
    }

    public void setSuper_cluster_range(int super_cluster_range) {
        this.super_cluster_range = super_cluster_range;
    }

    public int getScore_for_bed() {
        /**
         * Score calculate by (total_cluster * 1000)/(max_possible_cluster)     : max_possible_cluster is maximum number of possible cluster calculate from (super_cluster_range)/(max_variant_size of cluster)
         * 
         * super_cluster_range is range or window that we use to search for super cluster
         * max_variant_size of cluster is range or window that we use to search for cluster (can get this info by using method getMax_variant_size with MutationCluster object)
         */
        this.max_possible_cluster = (this.super_cluster_range)/(this.list_mutation_cluster.get(0).getMax_variant_size());
        this.score_for_bed = (this.list_mutation_cluster.size()*1000)/this.max_possible_cluster;
        return score_for_bed;
    }

    public int getMax_possible_cluster() {
        return max_possible_cluster;
    }

    public void setMax_possible_cluster(int max_possible_cluster) {
        this.max_possible_cluster = max_possible_cluster;
    }

    public int getCount_mutationCA() {
        return count_mutationCA;
    }

    public int getCount_mutationCG() {
        return count_mutationCG;
    }

    public int getCount_mutationCT() {
        return count_mutationCT;
    }

    public int getCount_mutationTA() {
        return count_mutationTA;
    }

    public int getCount_mutationTC() {
        return count_mutationTC;
    }

    public int getCount_mutationTG() {
        return count_mutationTG;
    }

    public int getTotal_alt_alleles() {
        return total_alt_alleles;
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
    
    public void countMutationSubstitution(){
        
        for(MutationCluster cluster : this.list_mutation_cluster){
            cluster.getCount_mutationCA();
            this.count_mutationCA = this.count_mutationCA + cluster.getCount_mutationCA();
            this.count_mutationCG = this.count_mutationCG + cluster.getCount_mutationCG();
            this.count_mutationCT = this.count_mutationCT + cluster.getCount_mutationCT();
            this.count_mutationTA = this.count_mutationTA + cluster.getCount_mutationTA();
            this.count_mutationTC = this.count_mutationTC + cluster.getCount_mutationTC();
            this.count_mutationTG = this.count_mutationTG + cluster.getCount_mutationTG();
            this.total_alt_alleles = this.total_alt_alleles + cluster.getTotal_alt_alleles();
        }
        this.count_mutation_other = this.total_alt_alleles - (this.count_mutationCA + this.count_mutationCG + this.count_mutationCT + this.count_mutationTA + this.count_mutationTC + this.count_mutationTG);
    }
    
    public void countMutationSubstitutionV2(){
        
        for(MutationCluster cluster : this.list_mutation_cluster){
            ArrayList<VariantContext> varList = cluster.getVarList();
            for(VariantContext var_ctx : varList){
                List<Allele> list_alleles = var_ctx.getAlleles();
                String ref_base = var_ctx.getReference().getBaseString().toUpperCase();
                int var_ctx_ID = var_ctx.hashCode();
                /**
                 * Check duplicate VariantContext 
                 * In order to count mutation rate correctly
                 */
                if(!this.pureVarList.containsKey(var_ctx_ID)){
                    this.pureVarList.put(var_ctx_ID, var_ctx);
                    
                    for(Allele ale : list_alleles){
                        if(ale.isNonReference()){
                            this.total_alt_alleles++;
                            String alt_base = ale.getBaseString().toUpperCase();
                            if(ref_base.equals("C") && alt_base.equals("A")){
                                this.count_mutationCA++;
                            }else if(ref_base.equals("G") && alt_base.equals("T")){
                                this.count_mutationCA++;
                            }else if(ref_base.equals("C") && alt_base.equals("G")){
                                this.count_mutationCG++;
                            }else if(ref_base.equals("G") && alt_base.equals("C")){
                                this.count_mutationCG++;
                            }else if(ref_base.equals("C") && alt_base.equals("T")){
                                this.count_mutationCT++;
                            }else if(ref_base.equals("G") && alt_base.equals("A")){
                                this.count_mutationCT++;
                            }else if(ref_base.equals("T") && alt_base.equals("A")){
                                this.count_mutationTA++;
                            }else if(ref_base.equals("A") && alt_base.equals("T")){
                                this.count_mutationTA++;
                            }else if(ref_base.equals("T") && alt_base.equals("C")){
                                this.count_mutationTC++;
                            }else if(ref_base.equals("A") && alt_base.equals("G")){
                                this.count_mutationTC++;
                            }else if(ref_base.equals("T") && alt_base.equals("G")){
                                this.count_mutationTG++;
                            }else if(ref_base.equals("A") && alt_base.equals("C")){
                                this.count_mutationTG++;
                            }else{
                                this.count_mutation_other++;
                            }                   
                        }
                    }
                }
            } 
        }     
    }
}
