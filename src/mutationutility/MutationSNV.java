/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mutationutility;

import htsjdk.variant.variantcontext.VariantContext;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author worawich
 */
public class MutationSNV {
    
    private String mutationType;
    private String REF;
    private String ALT;
    private LinkedHashMap<String,LinkedHashMap<String,ArrayList<VariantContext>>> trinucleotideMatrix;
    private LinkedHashMap<String,ArrayList<VariantContext>> trinucleotideInfo;
    
    
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

        this.trinucleotideMatrix = new LinkedHashMap();
        this.trinucleotideMatrix.put("A", innerMap);
        this.trinucleotideMatrix.put("C", innerMap);
        this.trinucleotideMatrix.put("G", innerMap);
        this.trinucleotideMatrix.put("T", innerMap);
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
    
    public void putTrinucleotideVariantContext(String treePrimeBase, String fivePrimeBase, VariantContext inVarCtx){
        
    }
    
    public void addVariantContext(VariantContext inCtx){
        
        
        
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
