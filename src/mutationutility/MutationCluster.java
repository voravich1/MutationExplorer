/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mutationutility;

import htsjdk.variant.variantcontext.Allele;
import htsjdk.variant.variantcontext.VariantContext;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author worawich
 */
public class MutationCluster {
    
    private ArrayList<VariantContext> varList;
    private int leader_start;
    private int leader_stop;
    private int clusterSize;
    private int cluster_start;
    private int cluster_stop;
    private int count_mutationCA;
    private int count_mutationCG;
    private int count_mutationCT;
    private int count_mutationTA;
    private int count_mutationTC;
    private int count_mutationTG;
    private int count_mutation_other;
    private int total_alt_alleles;
    private int score_for_bed;      // score value for shade visualization. It represent number of varaint in cluster or cluster size. the value will be in range 0-1000. which varies by cluster range (max possible occurance of SNV when window is 100) that set by user
    private int max_variant_size;   // maximum possible of number of variant can be in one cluster (typically equal to cluster range (window size or range of cluster))
    
    String cluster_chr;
    
    public MutationCluster(){
        this.varList = new ArrayList();
        this.count_mutationCA = 0;
        this.count_mutationCG = 0;
        this.count_mutationCT = 0;
        this.count_mutationTA = 0;
        this.count_mutationTC = 0;
        this.count_mutationTG = 0;
        this.count_mutation_other = 0;
        this.total_alt_alleles = 0;
    }

    public void setVarList(ArrayList<VariantContext> varList) {
        this.varList = varList;
        this.cluster_start = varList.get(0).getStart();
        this.cluster_stop = varList.get(varList.size()-1).getEnd();        
    }

    public int getCount_mutation_other() {
        return count_mutation_other;
    }

    public int getScore_for_bed() {
        /**
         * Score calculate by (totalMutation*1000)/(50% of max_variant_size)   ;max_variant_size is a range value or a size of window that we use to search for cluster.
         */
        int half_max_variant_size = (this.max_variant_size*50)/100;
        this.score_for_bed = (getClusterSize()*1000)/half_max_variant_size;
        return score_for_bed;
    }

    public int getMax_variant_size() {
        return max_variant_size;
    }

    public void setMax_variant_size(int max_variant_size) {
        this.max_variant_size = max_variant_size;
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

    public void addLeaderStart(int leader_start) {
        this.leader_start = leader_start;
    }

    public void addLeaderStop(int leader_stop) {
        this.leader_stop = leader_stop;
    }

    public void removeLeader() {
        this.varList.remove(0);
        VariantContext varctx = this.varList.get(0);
        this.leader_start = varctx.getStart();
        this.leader_stop = varctx.getEnd();
        this.cluster_start = varList.get(0).getStart();
        this.cluster_stop = varList.get(varList.size()-1).getEnd();
    }
    
    public void countMutationSubstitution(){
        /**
         * count occurrence of each mutation substitution type
         * 
         *  C>A|G>T, C>G|G>C, C>T|G>A, T>A|A>T, T>C|A>G, and T>G|A>C
         */
        
        for(VariantContext var_ctx : this.varList){
            List<Allele> list_alleles = var_ctx.getAlleles();
            String ref_base = var_ctx.getReference().getBaseString().toUpperCase();
            
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
