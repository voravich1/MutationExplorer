/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mutationexplorer;

import java.io.IOException;
import mutationutility.*;
/**
 *
 * @author worawich
 */
public class TestBed {
    
    public static void main(String[] args) throws IOException {
        
        String vcfFileName = "/Users/worawich/Download_dataset/Ratina_cancer/Mutect_dnabrick_result/set1_dnabrick/555_somatic.vcf";
        String refFileName = "/Users/worawich/Reference/hg19_ucsc/ucsc.hg19.fa";
        String saveFile = "/Users/worawich/Download_dataset/Ratina_cancer/Mutect_dnabrick_result/set1_dnabrick/555_trinucleotide.csv";
        
        MutationSNV muSNV = MutationUtility.analyseTrinucleotide(vcfFileName, refFileName);
        muSNV.exportTrinucleotideFrequencyToCSVFile(saveFile);
        
    }
}
