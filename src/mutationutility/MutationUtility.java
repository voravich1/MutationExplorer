/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mutationutility;

import htsjdk.samtools.util.CloseableIterator;
import htsjdk.variant.variantcontext.VariantContext;
import htsjdk.variant.variantcontext.writer.VariantContextWriter;
import htsjdk.variant.vcf.VCFFileReader;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author worawich
 */
public class MutationUtility {
    
    public static ArrayList<MutationCluster> SearchMutationCluster(String vcfFile, int range, int cluster_size_filter){
        
        File vcf_File = new File(vcfFile);
        VCFFileReader vcfReader = new VCFFileReader(vcf_File);
        CloseableIterator<VariantContext> vcf_info = vcfReader.iterator();
        
        ArrayList<MutationCluster> list_cluster = new ArrayList();
        
        int leader_start = 0;
        int leader_stop = 0;
        
        int member_start = 0;
        int member_stop = 0;
        
        String leader_chr = "";
        String member_chr = "";
        
        int diff = 0;
        
        int count = 0;
        
        MutationCluster dummy_cluster = new MutationCluster();
        
        while(vcf_info.hasNext()){  // loop all variant
            
            VariantContext varctx = vcf_info.next();
            
            if(count==0){
                /**
                 * For first time
                 */
                leader_start = varctx.getStart();
                leader_stop = varctx.getEnd();
                leader_chr = varctx.getContig();
                dummy_cluster.addVariant(varctx);
                dummy_cluster.addLeaderStart(leader_start);
                dummy_cluster.addLeaderStop(leader_stop);
                dummy_cluster.setCluster_chr(leader_chr);
                count=1;
            }else{
                /**
                 * For other line
                 */
                member_start = varctx.getStart();
                member_stop = varctx.getEnd();
                member_chr = varctx.getContig();
                diff = member_start - leader_start;
                
                if(leader_chr.equals(member_chr)){
                    /**
                     * leader and current var has the same chromosome
                     * Check position
                     */
                    if(diff <= range){
                        dummy_cluster.addVariant(varctx);
                    }else{
                        /*
                            New var not match with leader of cluster
                            Save old cluster and create new cluster (save only cluster that has member more than 1)
                        */
                        if(dummy_cluster.getClusterSize() >= cluster_size_filter){
                            MutationCluster save_cluster = new MutationCluster();
                            ArrayList<VariantContext> save_cluster_list = new ArrayList<VariantContext>(dummy_cluster.getVarList());
                            save_cluster.setVarList(save_cluster_list);
                            save_cluster.addLeaderStart(dummy_cluster.getLeader_start());
                            save_cluster.addLeaderStop(dummy_cluster.getLeader_stop());
                            save_cluster.setCluster_chr(dummy_cluster.getCluster_chr());
                            list_cluster.add(save_cluster);
                        }

                        if(diff > (2*range)){
                            /*
                                New var is to far from every member in old cluster
                                Initiate new cluster and use current var as leader
                                No need to compare with other member
                            */
                            leader_start = member_start;
                            leader_stop = member_stop;
                            leader_chr = member_chr;
                            dummy_cluster = new MutationCluster();
                            dummy_cluster.addVariant(varctx);
                            dummy_cluster.addLeaderStart(leader_start);
                            dummy_cluster.addLeaderStop(leader_stop);
                            dummy_cluster.setCluster_chr(leader_chr);
                        }else{
                            /*
                                New var is far from leader but not far from other member
                                Use other member as leader (start from second)
                            */
                            while(dummy_cluster.getClusterSize()>1){
                                /**
                                 * loop all member and compare with new var
                                 */
                                dummy_cluster.removeLeader();
                                leader_start = dummy_cluster.getLeader_start();
                                leader_stop = dummy_cluster.getLeader_stop();

                                diff = member_start - leader_start;

                                if(diff <= range){
                                    /**
                                     * current var is in range when compare to one of member
                                     * add current var to the cluster
                                     */
                                    dummy_cluster.addVariant(varctx);
                                    break;
                                }                                                            
                            }

                            if(dummy_cluster.getClusterSize()==1){
                                /*
                                    Current cluster has no member
                                    Create new cluster, use current var as leader
                                */
                                leader_start = member_start;
                                leader_stop = member_stop;
                                leader_chr = member_chr;
                                dummy_cluster = new MutationCluster();
                                dummy_cluster.addVariant(varctx);
                                dummy_cluster.addLeaderStart(leader_start);
                                dummy_cluster.addLeaderStop(leader_stop);
                                dummy_cluster.setCluster_chr(leader_chr);
                            }
                        }  
                    }
                }else{
                    /**
                     * leader and current var has different in chromosome
                     * Save cluster and create new cluster using current var as leader
                     */
                    if(dummy_cluster.getClusterSize() >= cluster_size_filter){
                        MutationCluster save_cluster = new MutationCluster();
                        ArrayList<VariantContext> save_cluster_list = new ArrayList<VariantContext>(dummy_cluster.getVarList());
                        save_cluster.setVarList(save_cluster_list);
                        save_cluster.addLeaderStart(dummy_cluster.getLeader_start());
                        save_cluster.addLeaderStop(dummy_cluster.getLeader_stop());
                        save_cluster.setCluster_chr(dummy_cluster.getCluster_chr());
                        list_cluster.add(save_cluster);
                    }
                    
                    leader_start = member_start;
                    leader_stop = member_stop;
                    leader_chr = member_chr;
                    dummy_cluster = new MutationCluster();
                    dummy_cluster.addVariant(varctx);
                    dummy_cluster.addLeaderStart(leader_start);
                    dummy_cluster.addLeaderStop(leader_stop);
                    dummy_cluster.setCluster_chr(leader_chr);
                }
                       
            }
        }
        /**
         * Save last cluster
         */
        if(dummy_cluster.getClusterSize() >= cluster_size_filter){
            list_cluster.add(dummy_cluster);
        }
        
        System.out.print("done");
        return list_cluster; 
    }
    
    public static ArrayList<MutationSuperCluster> SearchSuperMutationCluster(ArrayList<MutationCluster> list_cluster, int range, int cluster_size_filter){
        
        boolean firstFlag = true;
        int diff = 0;
        MutationSuperCluster super_cluster = new MutationSuperCluster();
        ArrayList<MutationSuperCluster> list_super_cluster = new ArrayList();
        
        for(MutationCluster mutation_cluster : list_cluster){
            
            if(firstFlag == true){
                /**
                 * First Time 
                 * create new super cluster and add current mutation cluster as leader
                 */
                super_cluster.addMutationCluster(mutation_cluster);
                firstFlag = false;
            }else{
                if(super_cluster.getSuperCluster_chr().equals(mutation_cluster.getCluster_chr())){
                    /**
                     * SuperCluster and current mutationCluster has same chromosome
                     * Check diff btw SuperCluster and current mutation Cluster 
                     */
                    diff = mutation_cluster.cluster_start - super_cluster.getSuperCluster_start();
                    if(diff <= range){
                        /**
                         * Current mutation cluster is in rage of super cluster
                         * add to super cluster
                         */
                        super_cluster.addMutationCluster(mutation_cluster);
                    }else{
                        /**
                         * Current mutation cluster is not in range of super cluster
                         * Save super cluster
                         * remove first member of super cluster 
                         * use second member as leader and recheck diff 
                         */
                        if(super_cluster.getClusterSize() >= cluster_size_filter){
                            MutationSuperCluster save_super_cluster = new MutationSuperCluster();
                            ArrayList<MutationCluster> dummy = new ArrayList<MutationCluster>(super_cluster.getList_mutation_cluster());
                            save_super_cluster.setList_mutation_cluster(dummy);
                            list_super_cluster.add(save_super_cluster);
                        }
                        
                        if(diff > (2*range)){
                            /**
                             * Current mutation has 2 time differ from super cluster
                             * no need to iterate to check with the other member in super cluster
                             * create new super cluster using current cluster as leader
                             */
                            super_cluster = new MutationSuperCluster();
                            super_cluster.addMutationCluster(mutation_cluster);
                        }else{
                            /**
                             * Current mutation does not have diff exceed 2 time from super cluster
                             */
                             while(super_cluster.getClusterSize()>1){
                                /**
                                 * remove first member and use second as leader
                                 * do like this until found leader that has diff not exceed range
                                 * or until super cluster has single member
                                 */
                                super_cluster.removeMutationCluster();
                                diff = mutation_cluster.cluster_start - super_cluster.getSuperCluster_start();
                                if(diff <= range){
                                    super_cluster.addMutationCluster(mutation_cluster);
                                    break;
                                }
                            }
                             
                            if(super_cluster.getClusterSize() == 1){
                                /**
                                 * Super cluster has single member
                                 * create new super cluster using current mutation cluster as leader
                                 */
                                super_cluster = new MutationSuperCluster();
                                super_cluster.addMutationCluster(mutation_cluster);
                            }
                        }
                    }
                }else{
                    /**
                     * SuperCluster and current mutationCluster has different chromosome
                     * Save Super cluster (if it has member more that cluster size filter)
                     * and Create new Super Cluster and add current cluster as leader
                     */
                    if(super_cluster.getClusterSize() >= cluster_size_filter){
                        MutationSuperCluster save_super_cluster = new MutationSuperCluster();
                        ArrayList<MutationCluster> dummy = new ArrayList<MutationCluster>(super_cluster.getList_mutation_cluster());
                        save_super_cluster.setList_mutation_cluster(dummy);
                        list_super_cluster.add(save_super_cluster);
                    }

                    super_cluster = new MutationSuperCluster();
                    super_cluster.addMutationCluster(mutation_cluster);
                }               
            }
        }
        
        if(super_cluster.getClusterSize() >= cluster_size_filter){
            list_super_cluster.add(super_cluster);
        }
        return list_super_cluster;
    } 
    
}
