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
                    
                    /**
                     * update trinucleotide matrix
                     */
                    LinkedHashMap<String,ArrayList<VariantContext>> innerMap = this.trinucleotideMatrixCA.get(fivePrimeBase);
                    ArrayList<VariantContext> dummyVarList = innerMap.get(threePrimeBase);
                    dummyVarList.add(inVarCtx);
                    innerMap.put(threePrimeBase, dummyVarList);
                    this.trinucleotideMatrixCA.put(fivePrimeBase, innerMap);
                    /******************************/
                    
                }else if(ref_base.equals("G") && alt_base.equals("T")){

                    this.mutationCA.add(inVarCtx);
                    
                    /**
                     * update trinucleotide matrix
                     */
                    LinkedHashMap<String,ArrayList<VariantContext>> innerMap = this.trinucleotideMatrixCA.get(fivePrimeBase);
                    ArrayList<VariantContext> dummyVarList = innerMap.get(threePrimeBase);
                    dummyVarList.add(inVarCtx);
                    innerMap.put(threePrimeBase, dummyVarList);
                    this.trinucleotideMatrixCA.put(fivePrimeBase, innerMap);
                    /******************************/
                    
                }else if(ref_base.equals("C") && alt_base.equals("G")){
                   
                    this.mutationCG.add(inVarCtx);
                    
                    /**
                     * update trinucleotide matrix
                     */
                    LinkedHashMap<String,ArrayList<VariantContext>> innerMap = this.trinucleotideMatrixCG.get(fivePrimeBase);
                    ArrayList<VariantContext> dummyVarList = innerMap.get(threePrimeBase);
                    dummyVarList.add(inVarCtx);
                    innerMap.put(threePrimeBase, dummyVarList);
                    this.trinucleotideMatrixCG.put(fivePrimeBase, innerMap);
                    /******************************/
                    
                }else if(ref_base.equals("G") && alt_base.equals("C")){
                    
                    this.mutationCG.add(inVarCtx);
                    
                    /**
                     * update trinucleotide matrix
                     */
                    LinkedHashMap<String,ArrayList<VariantContext>> innerMap = this.trinucleotideMatrixCG.get(fivePrimeBase);
                    ArrayList<VariantContext> dummyVarList = innerMap.get(threePrimeBase);
                    dummyVarList.add(inVarCtx);
                    innerMap.put(threePrimeBase, dummyVarList);
                    this.trinucleotideMatrixCG.put(fivePrimeBase, innerMap);
                    /******************************/
                    
                }else if(ref_base.equals("C") && alt_base.equals("T")){
                    
                    this.mutationCT.add(inVarCtx);
                    
                    /**
                     * update trinucleotide matrix
                     */
                    LinkedHashMap<String,ArrayList<VariantContext>> innerMap = this.trinucleotideMatrixCT.get(fivePrimeBase);
                    ArrayList<VariantContext> dummyVarList = innerMap.get(threePrimeBase);
                    dummyVarList.add(inVarCtx);
                    innerMap.put(threePrimeBase, dummyVarList);
                    this.trinucleotideMatrixCT.put(fivePrimeBase, innerMap);
                    /******************************/
                    
                }else if(ref_base.equals("G") && alt_base.equals("A")){
                    
                    this.mutationCT.add(inVarCtx);
                    
                    /**
                     * update trinucleotide matrix
                     */
                    LinkedHashMap<String,ArrayList<VariantContext>> innerMap = this.trinucleotideMatrixCT.get(fivePrimeBase);
                    ArrayList<VariantContext> dummyVarList = innerMap.get(threePrimeBase);
                    dummyVarList.add(inVarCtx);
                    innerMap.put(threePrimeBase, dummyVarList);
                    this.trinucleotideMatrixCT.put(fivePrimeBase, innerMap);
                    /******************************/
                    
                }else if(ref_base.equals("T") && alt_base.equals("A")){
                    
                    this.mutationTA.add(inVarCtx);
                    
                    /**
                     * update trinucleotide matrix
                     */
                    LinkedHashMap<String,ArrayList<VariantContext>> innerMap = this.trinucleotideMatrixTA.get(fivePrimeBase);
                    ArrayList<VariantContext> dummyVarList = innerMap.get(threePrimeBase);
                    dummyVarList.add(inVarCtx);
                    innerMap.put(threePrimeBase, dummyVarList);
                    this.trinucleotideMatrixTA.put(fivePrimeBase, innerMap);
                    /******************************/
                    
                }else if(ref_base.equals("A") && alt_base.equals("T")){
                    
                    this.mutationTA.add(inVarCtx);
                    
                    /**
                     * update trinucleotide matrix
                     */
                    LinkedHashMap<String,ArrayList<VariantContext>> innerMap = this.trinucleotideMatrixTA.get(fivePrimeBase);
                    ArrayList<VariantContext> dummyVarList = innerMap.get(threePrimeBase);
                    dummyVarList.add(inVarCtx);
                    innerMap.put(threePrimeBase, dummyVarList);
                    this.trinucleotideMatrixTA.put(fivePrimeBase, innerMap);
                    /******************************/
                    
                }else if(ref_base.equals("T") && alt_base.equals("C")){
                    
                    this.mutationTC.add(inVarCtx);
                    
                    /**
                     * update trinucleotide matrix
                     */
                    LinkedHashMap<String,ArrayList<VariantContext>> innerMap = this.trinucleotideMatrixTC.get(fivePrimeBase);
                    ArrayList<VariantContext> dummyVarList = innerMap.get(threePrimeBase);
                    dummyVarList.add(inVarCtx);
                    innerMap.put(threePrimeBase, dummyVarList);
                    this.trinucleotideMatrixTC.put(fivePrimeBase, innerMap);
                    /******************************/
                    
                }else if(ref_base.equals("A") && alt_base.equals("G")){
                    
                    this.mutationTC.add(inVarCtx);
                    
                    /**
                     * update trinucleotide matrix
                     */
                    LinkedHashMap<String,ArrayList<VariantContext>> innerMap = this.trinucleotideMatrixTC.get(fivePrimeBase);
                    ArrayList<VariantContext> dummyVarList = innerMap.get(threePrimeBase);
                    dummyVarList.add(inVarCtx);
                    innerMap.put(threePrimeBase, dummyVarList);
                    this.trinucleotideMatrixTC.put(fivePrimeBase, innerMap);
                    /******************************/
                    
                }else if(ref_base.equals("T") && alt_base.equals("G")){
                    
                    this.mutationTG.add(inVarCtx);
                    
                    /**
                     * update trinucleotide matrix
                     */
                    LinkedHashMap<String,ArrayList<VariantContext>> innerMap = this.trinucleotideMatrixTG.get(fivePrimeBase);
                    ArrayList<VariantContext> dummyVarList = innerMap.get(threePrimeBase);
                    dummyVarList.add(inVarCtx);
                    innerMap.put(threePrimeBase, dummyVarList);
                    this.trinucleotideMatrixTG.put(fivePrimeBase, innerMap);
                    /******************************/
                    
                }else if(ref_base.equals("A") && alt_base.equals("C")){
                    
                    this.mutationTG.add(inVarCtx);
                    
                    /**
                     * update trinucleotide matrix
                     */
                    LinkedHashMap<String,ArrayList<VariantContext>> innerMap = this.trinucleotideMatrixTG.get(fivePrimeBase);
                    ArrayList<VariantContext> dummyVarList = innerMap.get(threePrimeBase);
                    dummyVarList.add(inVarCtx);
                    innerMap.put(threePrimeBase, dummyVarList);
                    this.trinucleotideMatrixTG.put(fivePrimeBase, innerMap);
                    /******************************/
                    
                }                
            }
        }  
    }
    
    //public void putTrinucleotideMatrix(String threePrimeBase, String fivePrimeBase, VariantContext inVarCtx){       
        //LinkedHashMap<String,ArrayList<VariantContext>> innerMap = this.trinucleotideMatrix.get(fivePrimeBase);
        //ArrayList<VariantContext> dummyVarList = innerMap.get(threePrimeBase);
        //dummyVarList.add(inVarCtx);
        //innerMap.put(threePrimeBase, dummyVarList);
        //this.trinucleotideMatrixCA.put(fivePrimeBase, innerMap);
    //}

    public String exportTrinucleotideFrequencyCSV(String mutationType){
        /**
         * 6 mutationType : C>A => CA, C>G => CG, C>T => CT, T>A => TA, T>C => TC=>, T>G => TG
         */
        LinkedHashMap<String,LinkedHashMap<String,ArrayList<VariantContext>>> selectedMatrix = new LinkedHashMap();
        
        if(mutationType.equals("CA")){
            selectedMatrix = this.trinucleotideMatrixCA;
        }else if(mutationType.equals("CG")){
            selectedMatrix = this.trinucleotideMatrixCG;
        }else if(mutationType.equals("CT")){
            selectedMatrix = this.trinucleotideMatrixCT;
        }else if(mutationType.equals("TA")){
            selectedMatrix = this.trinucleotideMatrixTA;
        }else if(mutationType.equals("TC")){
            selectedMatrix = this.trinucleotideMatrixTC;
        }else if(mutationType.equals("TG")){
            selectedMatrix = this.trinucleotideMatrixTG;
        }
        
        String csvData = "Mutation type = " + mutationType + " [x axis = 3' base,y axis = 5' base]"
                + "\n5'\3',A,C,G,T";
        
        for(Map.Entry<String, LinkedHashMap<String,ArrayList<VariantContext>>> outer_entry : selectedMatrix.entrySet()){
            String fivePrimeBase = outer_entry.getKey();
            csvData = csvData + "\n" + fivePrimeBase;
            
            LinkedHashMap<String, ArrayList<VariantContext>> innerMap = outer_entry.getValue();
            for(Map.Entry<String,ArrayList<VariantContext>> inner_entry : innerMap.entrySet()){
                String threePrimeBase = inner_entry.getKey();
                ArrayList<VariantContext> varList = inner_entry.getValue();
                csvData = csvData + "," + varList.size();
            }   
        }      
        return csvData;
    }
}
