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
import java.util.Map;

/**
 *
 * @author worawich
 */
public class MutationSNV {
    
    private String mutationType;
    private String REF;
    private String ALT;
    private LinkedHashMap<String,LinkedHashMap<String,ArrayList<VariantContext>>> trinucleotideMatrixCA;
    private LinkedHashMap<String,LinkedHashMap<String,ArrayList<VariantContext>>> trinucleotideMatrixCG;
    private LinkedHashMap<String,LinkedHashMap<String,ArrayList<VariantContext>>> trinucleotideMatrixCT;
    private LinkedHashMap<String,LinkedHashMap<String,ArrayList<VariantContext>>> trinucleotideMatrixTA;
    private LinkedHashMap<String,LinkedHashMap<String,ArrayList<VariantContext>>> trinucleotideMatrixTC;
    private LinkedHashMap<String,LinkedHashMap<String,ArrayList<VariantContext>>> trinucleotideMatrixTG;
    private LinkedHashMap<String,ArrayList<VariantContext>> trinucleotideInfo;
    private ArrayList<VariantContext> mutationCA;
    private ArrayList<VariantContext> mutationCG;
    private ArrayList<VariantContext> mutationCT;
    private ArrayList<VariantContext> mutationTA;
    private ArrayList<VariantContext> mutationTC;
    private ArrayList<VariantContext> mutationTG;
    
    public MutationSNV(){        
        /**
         * initiate trinucleotide matrix
         * innerMap key is 3' base (left base of target position)
         * trinucleotideMatrix key is 5' base (right base of target position)
         */
        LinkedHashMap<String,ArrayList<VariantContext>> innerMap = new LinkedHashMap();
        ArrayList<VariantContext> listVarCtx = new ArrayList();
       
        innerMap.put("A", listVarCtx);
        innerMap.put("C", listVarCtx);
        innerMap.put("G", listVarCtx);
        innerMap.put("T", listVarCtx);

        this.trinucleotideMatrixCA = new LinkedHashMap();
        this.trinucleotideMatrixCA.put("A", innerMap);
        this.trinucleotideMatrixCA.put("C", innerMap);
        this.trinucleotideMatrixCA.put("G", innerMap);
        this.trinucleotideMatrixCA.put("T", innerMap);
        
        this.trinucleotideMatrixCG = new LinkedHashMap();
        this.trinucleotideMatrixCG.put("A", innerMap);
        this.trinucleotideMatrixCG.put("C", innerMap);
        this.trinucleotideMatrixCG.put("G", innerMap);
        this.trinucleotideMatrixCG.put("T", innerMap);
        
        this.trinucleotideMatrixCT = new LinkedHashMap();
        this.trinucleotideMatrixCT.put("A", innerMap);
        this.trinucleotideMatrixCT.put("C", innerMap);
        this.trinucleotideMatrixCT.put("G", innerMap);
        this.trinucleotideMatrixCT.put("T", innerMap);
        
        this.trinucleotideMatrixTA = new LinkedHashMap();
        this.trinucleotideMatrixTA.put("A", innerMap);
        this.trinucleotideMatrixTA.put("C", innerMap);
        this.trinucleotideMatrixTA.put("G", innerMap);
        this.trinucleotideMatrixTA.put("T", innerMap);
        
        this.trinucleotideMatrixTC = new LinkedHashMap();
        this.trinucleotideMatrixTC.put("A", innerMap);
        this.trinucleotideMatrixTC.put("C", innerMap);
        this.trinucleotideMatrixTC.put("G", innerMap);
        this.trinucleotideMatrixTC.put("T", innerMap);
        
        this.trinucleotideMatrixTG = new LinkedHashMap();
        this.trinucleotideMatrixTG.put("A", innerMap);
        this.trinucleotideMatrixTG.put("C", innerMap);
        this.trinucleotideMatrixTG.put("G", innerMap);
        this.trinucleotideMatrixTG.put("T", innerMap);
        /**************/
        
        this.trinucleotideInfo = new LinkedHashMap();
    }

    public String getREF() {
        return REF;
    }

    public void setREF(String REF) {
        this.REF = REF;
    }

    public String getALT() {
        return ALT;
    }

    public void setALT(String ALT) {
        this.ALT = ALT;
    }

    public String getMutationType() {
        this.mutationType = this.REF + ">" + this.ALT;
        return mutationType;
    }
    
    public void addTrinucleotideVariantContext(String threePrimeBase, String fivePrimeBase, VariantContext inVarCtx){
        
        List<Allele> list_alleles = inVarCtx.getAlleles();
        String ref_base = inVarCtx.getReference().getBaseString().toUpperCase();

        for(Allele ale : list_alleles){
            if(ale.isNonReference()){
                String alt_base = ale.getBaseString().toUpperCase();

                if(ref_base.equals("C") && alt_base.equals("A")){
                    
                    this.mutationCA.add(inVarCtx);
                    LinkedHashMap<String,ArrayList<VariantContext>> innerMap = this.trinucleotideMatrixCA.get(fivePrimeBase);
                    ArrayList<VariantContext> dummyVarList = innerMap.get(threePrimeBase);
                    dummyVarList.add(inVarCtx);
                    innerMap.put(threePrimeBase, dummyVarList);
                    this.trinucleotideMatrixCA.put(fivePrimeBase, innerMap);
                    
                    
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
    
    public void putTrinucleotideMatrix(String threePrimeBase, String fivePrimeBase, VariantContext inVarCtx){       
        LinkedHashMap<String,ArrayList<VariantContext>> innerMap = this.trinucleotideMatrix.get(fivePrimeBase);
        ArrayList<VariantContext> dummyVarList = innerMap.get(threePrimeBase);
        dummyVarList.add(inVarCtx);
        innerMap.put(threePrimeBase, dummyVarList);
        this.trinucleotideMatrixCA.put(fivePrimeBase, innerMap);
    }
    
    public void addVariantContext(String inREF, String inALT, VariantContext inCtx){
        this.mutationType = this.REF + ">" + this.ALT;
        
        List<Allele> list_alleles = inCtx.getAlleles();
        String ref_base = inCtx.getReference().getBaseString().toUpperCase();

        for(Allele ale : list_alleles){
            if(ale.isNonReference()){
                this.total_alt_alleles++;
                String alt_base = ale.getBaseString().toUpperCase();

                if(ref_base.equals("C") && alt_base.equals("A")){
                    this.count_mutationCA++;
                    this.mutationCA.add(inCtx);
                    
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
    
    public String exportTrinucleotideFrequencyCSV(){
        
        String csvExport = "x axis = 3' base,y axis = 5' base";
        
        for(Map.Entry<String, LinkedHashMap<String,ArrayList<VariantContext>>> outer_entry : this.trinucleotideMatrix.entrySet()){
            
            LinkedHashMap<String, ArrayList<VariantContext>> innerMap = outer_entry.getValue();
            for(Map.Entry<String,ArrayList<VariantContext>> inner_entry : innerMap.entrySet()){
                
            }
            
        }
//        this.trinucleotideMatrix.
        
        return null;
    }
}
