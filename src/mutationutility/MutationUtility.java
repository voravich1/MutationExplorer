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
import htsjdk.samtools.reference.FastaSequenceFile;
import htsjdk.samtools.reference.IndexedFastaSequenceFile;
import htsjdk.samtools.reference.ReferenceSequence;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
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
        dummy_cluster.setMax_variant_size(range);
        
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
                            save_cluster.setMax_variant_size(range);
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
                            dummy_cluster.setMax_variant_size(range);
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
                                dummy_cluster.setMax_variant_size(range);
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
                        save_cluster.setMax_variant_size(range);
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
                    dummy_cluster.setMax_variant_size(range);
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
        super_cluster.setSuper_cluster_range(range);
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
                    diff = mutation_cluster.getCluster_start() - super_cluster.getSuperCluster_start();
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
                            save_super_cluster.setSuper_cluster_range(range);
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
                            super_cluster.setSuper_cluster_range(range);
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
                                diff = mutation_cluster.getCluster_start() - super_cluster.getSuperCluster_start();
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
                                super_cluster.setSuper_cluster_range(range);
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
                        save_super_cluster.setSuper_cluster_range(range);
                        list_super_cluster.add(save_super_cluster);
                    }

                    super_cluster = new MutationSuperCluster();
                    super_cluster.addMutationCluster(mutation_cluster);
                    super_cluster.setSuper_cluster_range(range);
                }               
            }
        }
        
        if(super_cluster.getClusterSize() >= cluster_size_filter){
            list_super_cluster.add(super_cluster);
        }
        return list_super_cluster;
    }
    
    public static void exportClusterToBed(ArrayList<MutationCluster> in_list_cluster, String fullPath_saveFileName) throws IOException{
        
        PrintStream ps;
        FileWriter writer;        
        /**
         * Check File existing
         */
        
        File f = new File(fullPath_saveFileName); //File object        
        if(f.exists()){
//            ps = new PrintStream(new FileOutputStream(filename,true));
            writer = new FileWriter(fullPath_saveFileName,true);
        }else{
//            ps = new PrintStream(filename);
            writer = new FileWriter(fullPath_saveFileName);
        }
        int count = 0;
        writer.write("track useScore=1\n");
        for(MutationCluster cluster : in_list_cluster){
            String chr = cluster.getCluster_chr();
            int start = cluster.getCluster_start();
            int stop = cluster.getCluster_stop();
            int score = cluster.getScore_for_bed();
            count++;
            String clusterName = "cluster"+count;
            writer.write(chr + "\t" + start + "\t" + stop + "\t" + clusterName + "\t" + score +"\t" + "." + "\t" + start + "\t" + stop +"\n");
        }

        writer.flush();
        writer.close();
    }
    
    public static void exportSuperClusterToBed(ArrayList<MutationSuperCluster> in_list_super_cluster, String fullPath_saveFileName) throws IOException{
        PrintStream ps;
        FileWriter writer;        
        /**
         * Check File existing
         */
        
        File f = new File(fullPath_saveFileName); //File object        
        if(f.exists()){
//            ps = new PrintStream(new FileOutputStream(filename,true));
            writer = new FileWriter(fullPath_saveFileName,true);
        }else{
//            ps = new PrintStream(filename);
            writer = new FileWriter(fullPath_saveFileName);
        }
        int count = 0;
        writer.write("track useScore=1\n");
        for(MutationSuperCluster superCluster : in_list_super_cluster){
            String chr = superCluster.getSuperCluster_chr();
            int start = superCluster.getSuperCluster_start();
            int stop = superCluster.getSuperCluster_stop();
            int score = superCluster.getScore_for_bed();
            count++;
            String superClusterName = "super_cluster"+count;
            writer.write(chr + "\t" + start + "\t" + stop + "\t" + superClusterName + "\t" + score +"\t" + "." + "\t" + start + "\t" + stop +"\n");
        }

        writer.flush();
        writer.close();
    }
    
    public static void exportClusterToCSV(ArrayList<MutationCluster> list_cluster,String fullPath_saveFileName) throws IOException{
        PrintStream ps;
        FileWriter writer;        
        /**
         * Check File existing
         */
        
        File f = new File(fullPath_saveFileName); //File object        
        if(f.exists()){
//            ps = new PrintStream(new FileOutputStream(filename,true));
            writer = new FileWriter(fullPath_saveFileName,true);
        }else{
//            ps = new PrintStream(filename);
            writer = new FileWriter(fullPath_saveFileName);
        }
        
        String header = "ID,Chromosome,Start,Stop,Mutation Score,C>A,C>G,C>T,T>A,T>C,T>G,Other,Total mutation\n";
        writer.write(header);
        int count=0;
        for(MutationCluster cluster : list_cluster){
            String ID = "cluster"+(++count);
            String chr = cluster.getCluster_chr();
            int start = cluster.getCluster_start();
            int stop = cluster.getCluster_stop();
            int score = cluster.getScore_for_bed();
            
            cluster.countMutationSubstitution();
            
            int totalMutation = cluster.getTotal_alt_alleles();
            int countCA = cluster.getCount_mutationCA();
            int countCG = cluster.getCount_mutationCG();
            int countCT = cluster.getCount_mutationCT();
            int countTA = cluster.getCount_mutationTA();
            int countTC = cluster.getCount_mutationTC();
            int countTG = cluster.getCount_mutationTG();
            int countOther = cluster.getCount_mutation_other();
            
            writer.write(ID+","+chr+","+start+","+stop+","+score+","+countCA+","+countCG+","+countCT+","+countTA+","+countTC+","+countTG+","+countOther+","+totalMutation+"\n");
        }
        
        writer.flush();
        writer.close();
    }
    
    public static void exportSuperClusterToCSV(ArrayList<MutationSuperCluster> list_super_cluster,String fullPath_saveFileName) throws IOException{
        PrintStream ps;
        FileWriter writer;        
        /**
         * Check File existing
         */
        
        File f = new File(fullPath_saveFileName); //File object        
        if(f.exists()){
//            ps = new PrintStream(new FileOutputStream(filename,true));
            writer = new FileWriter(fullPath_saveFileName,true);
        }else{
//            ps = new PrintStream(filename);
            writer = new FileWriter(fullPath_saveFileName);
        }
        
        String header = "ID,Chromosome,Start,Stop,Mutation Score,C>A,C>G,C>T,T>A,T>C,T>G,Other,Total mutation\n";
        writer.write(header);
        int count=0;
        for(MutationSuperCluster superCluster : list_super_cluster){
            String ID = "superCluster"+(++count);
            String chr = superCluster.getSuperCluster_chr();
            int start = superCluster.getSuperCluster_start();
            int stop = superCluster.getSuperCluster_stop();
            int score = superCluster.getScore_for_bed();
            
            superCluster.countMutationSubstitutionV2();
            
            int totalMutation = superCluster.getTotal_alt_alleles();
            int countCA = superCluster.getCount_mutationCA();
            int countCG = superCluster.getCount_mutationCG();
            int countCT = superCluster.getCount_mutationCT();
            int countTA = superCluster.getCount_mutationTA();
            int countTC = superCluster.getCount_mutationTC();
            int countTG = superCluster.getCount_mutationTG();
            int countOther = superCluster.getCount_mutation_other();
            
            writer.write(ID+","+chr+","+start+","+stop+","+score+","+countCA+","+countCG+","+countCT+","+countTA+","+countTC+","+countTG+","+countOther+","+totalMutation+"\n");
        }
        
        writer.flush();
        writer.close();
    }
    
    public static void readMantaVCF(String vcfFileName){
        File vcf_File = new File(vcfFileName);
        VCFFileReader vcf = new VCFFileReader(vcf_File);
        
        CloseableIterator<VariantContext> vcf_Iter = vcf.iterator();
        
        while(vcf_Iter.hasNext()){      // loop all variant
            VariantContext vcfContext = vcf_Iter.next();    
            
            vcfContext.getAllele(vcfFileName);
            
        }
    }
    
    public static MutationSNV analyseTrinucleotide(String vcfFilePath, String refFilePath) throws FileNotFoundException{
        
        /**
         * Read reference fasta file
         */
        File ref_File = new File(refFilePath);
        IndexedFastaSequenceFile ref = new IndexedFastaSequenceFile(ref_File);
//        FastaSequenceFile ref = new FastaSequenceFile(ref_File,true);
        /****************************/
        
        /**
         * Read vcf file
         */
        File vcf_File = new File(vcfFilePath);
        VCFFileReader vcfReader = new VCFFileReader(vcf_File);
        /****************************/
        String name_of_file = vcf_File.getName();
        
        MutationSNV muSNV = new MutationSNV(name_of_file);
       
        CloseableIterator<VariantContext> vcf_info = vcfReader.iterator();
        while(vcf_info.hasNext()){  // loop all variant
            
            VariantContext varctx = vcf_info.next();
            
            int POS = varctx.getStart();
            String contig = varctx.getContig();
            ReferenceSequence leftBase = ref.getSubsequenceAt(contig, POS-1, POS-1);
            ReferenceSequence rightBase = ref.getSubsequenceAt(contig, POS+1, POS+1);
            
            muSNV.addTrinucleotideVariantContext(leftBase.getBaseString(), rightBase.getBaseString(), varctx);
        }
        return muSNV;
    }
    
}
