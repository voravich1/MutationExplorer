/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mutationexplorer;

import htsjdk.samtools.util.CloseableIterator;
import htsjdk.variant.variantcontext.Allele;
import htsjdk.variant.variantcontext.CommonInfo;
import htsjdk.variant.variantcontext.GenotypesContext;
import htsjdk.variant.variantcontext.StructuralVariantType;
import htsjdk.variant.variantcontext.VariantContext;
import htsjdk.variant.vcf.VCFFileReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import mutationutility.MutationCluster;
import mutationutility.MutationSuperCluster;
import mutationutility.MutationUtility;

/**
 *
 * @author worawich
 */
public class MutationExplorer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        String vcfFileName = "/Users/worawich/Download_dataset/Ratina_cancer/Mutect_dnabrick_result/set2_dnabrick/636_somatic.vcf";
//        String vcfFileName = "/Users/worawich/Download_dataset/Ratina_cancer/Mutect_dnabrick_result/set3_dnabrick/542_somatic.vcf";
//        String vcfFileName = "/Users/worawich/Download_dataset/Ratina_cancer/RB_somatic_vcf/277_somatic.vcf";
        int cluster_range = 100;
        int min_cluster_member = 5;
        int super_cluster_range = 10000;
        int min_super_cluster_member = 2;
        
        File input_file = new File(vcfFileName);
        String file_name = input_file.getName();
        String sampleName = file_name.split("_")[0];
        String path = input_file.getParent();
        String bed_saveFileName = path+"/"+sampleName+"_cluster.bed";
        String bed_SuperCluster_saveFileName = path+"/"+sampleName+"_super_cluster.bed";
        String csv_cluster_saveFileName = path+"/"+sampleName+"_cluster.csv";
        String csv_SuperCluster_saveFileName = path+"/"+sampleName+"_super_cluster.csv";
//        File vcf_File = new File(vcfFileName);
//        VCFFileReader vcfReader = new VCFFileReader(vcf_File);
//        CloseableIterator<VariantContext> vc = vcfReader.query("Chromosome", 6000, 7000);
//        List l;
//        l = vc.toList();
//        System.out.println();
//        VariantContext var;
//        var = (VariantContext) l.get(1);
//        
//        Allele al = var.getReference();
//        System.out.println("Ref is " + al.getBaseString());
//        List<Allele> listAl = var.getAlternateAlleles();
//        
//        for (Allele al2 : listAl){
//            System.out.println("ALT is " + al2.getBaseString());
//        }

//        CloseableIterator<VariantContext> vcf_info = vcfReader.iterator();
//        int count = 0;
//        while(vcf_info.hasNext()){
//            VariantContext ctx = vcf_info.next();
//            System.out.println(count++);
//            
//            List<Allele> al_list = ctx.getAlleles();
//            List<Allele> alt_al_list = ctx.getAlternateAlleles();
//            Allele al = alt_al_list.get(0);
//            
//            String base = al.getBaseString();
//            String display = al.getDisplayString();
//            int len = al.length();
//            
//            Map<String,Object> att = ctx.getAttributes();
//            int end = ctx.getEnd();
//            int start = ctx.getStart();
//            GenotypesContext gn = ctx.getGenotypes();
//            Allele ref_al = ctx.getReference();
//            Set<String> name_list = ctx.getSampleNames();
//            String src = ctx.getSource();
//            StructuralVariantType sv_type = ctx.getStructuralVariantType();
//            CommonInfo cm = ctx.getCommonInfo();
//            int num_al = ctx.getNAlleles();
//            int num_sample = ctx.getNSamples();
//            double predQ = ctx.getPhredScaledQual();
//            String mmm = ctx.toString();
//            System.out.println();
//            
//        }
        
        ArrayList<MutationCluster> list_cluster = MutationUtility.SearchMutationCluster(vcfFileName,cluster_range,min_cluster_member);
        ArrayList<MutationSuperCluster> list_super_cluster = MutationUtility.SearchSuperMutationCluster(list_cluster, super_cluster_range, min_super_cluster_member);
        MutationUtility.exportClusterToBed(list_cluster, bed_saveFileName);
        MutationUtility.exportSuperClusterToBed(list_super_cluster, bed_SuperCluster_saveFileName);
        MutationUtility.exportClusterToCSV(list_cluster, csv_cluster_saveFileName);
        MutationUtility.exportSuperClusterToCSV(list_super_cluster, csv_SuperCluster_saveFileName);
        System.out.println();
        
    }
    
}
