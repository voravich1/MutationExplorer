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
        
        String vcfFileName = "/Users/worawich/Download_dataset/Ratina_cancer/Manta_Normal_Tumor_Result/sumResult/208_somaticSV.vcf.gz";
        String refFileName = "";
        
        MutationUtility.analyseTrinucleotide(vcfFileName, refFileName);
       
        
    }
}
