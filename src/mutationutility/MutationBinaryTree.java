/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mutationutility;

import htsjdk.variant.variantcontext.VariantContext;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 *
 * @author worawich
 */
public class MutationBinaryTree {
    
    ArrayList<Long> mutationBinaryTree;                 // Sorted array of code [code is combination of chr|position 32bit|32bit]
    LinkedHashMap<Long,VariantContext> mutationMap;
    LinkedHashMap<String,Integer> mapContigToChrCode = new LinkedHashMap();
    LinkedHashMap<Integer,String> mapChrCodeToContig = new LinkedHashMap();
        
    public MutationBinaryTree(){
        this.mutationBinaryTree = new ArrayList();
        this.mutationMap = new LinkedHashMap();
        this.mapContigToChrCode = new LinkedHashMap();
        this.mapChrCodeToContig = new LinkedHashMap();
    }

    public ArrayList<Long> getMutationBinaryTree() {
        return mutationBinaryTree;
    }

    public void setMutationBinaryTree(ArrayList<Long> mutationBinaryTree) {
        this.mutationBinaryTree = mutationBinaryTree;
    }

    public LinkedHashMap<Long, VariantContext> getMutationMap() {
        return mutationMap;
    }

    public void setMutationMap(LinkedHashMap<Long, VariantContext> mutationMap) {
        this.mutationMap = mutationMap;
    }
    
    public VariantContext findNearestVariantContext(Long code){
        return null;
    }

    public LinkedHashMap<String, Integer> getMapContigToChrCode() {
        return mapContigToChrCode;
    }

    public void setMapContigToChrCode(LinkedHashMap<String, Integer> mapContigToChrCode) {
        this.mapContigToChrCode = mapContigToChrCode;
    }

    public LinkedHashMap<Integer, String> getMapChrCodeToContig() {
        return mapChrCodeToContig;
    }

    public void setMapChrCodeToContig(LinkedHashMap<Integer, String> mapChrCodeToContig) {
        this.mapChrCodeToContig = mapChrCodeToContig;
    }
    
    public VariantContext mapToAnotationBinaryTreeWithPosStart(long chrPosStart, long chrPosStop){
        /**
         * This function try to map chrPosStart With Binary search to the Array of chrPosAnnoIndex
         * then return the Annotation index that this chrPosStart has match or in the rage of start and stop point of that annotation
         * 
         * If chrPosStart did not match to any annotation range the program will move to use chrPosStop instead
         * If chrPosStop not match, we will return -1 (this match pattern is not in the range of any annotation)
         * 
         */
        VariantContext varCtx;
        int posMask = 268435455;        // 28 bit mask (AND operation with this mask to get position)
        int iniPoint = 0;
        int lastPoint = this.mutationBinaryTree.size()-1;
        int midPoint = (lastPoint + iniPoint)/2;
        long compareChrPos;
        
        int chrStart = (int)(chrPosStart>>28);
        int posStart = (int)(chrPosStart&posMask);
        int chrStop = (int)(chrPosStop>>28);
        int posStop = (int)(chrPosStop&posMask);
        
        VariantContext varCtxStart;
        VariantContext varCtxStop;
        
        int annotationIndex = 0;
        char status = 'n';              // status indicate that the input is lower or higher than compareChrPos 'l' is lower |  'h' is higher (if 'l' it mean the chrPosStart is lower than that mid point)
        
        while(true){
            
            
            compareChrPos = this.mutationBinaryTree.get(midPoint);
            
            if(compareChrPos == chrPosStart){
                varCtxStart = this.mutationMap.get(midPoint);
                
            }else if(chrPosStart < compareChrPos){
                status = 'l';
                lastPoint = midPoint-1;
                if(iniPoint>lastPoint){
                    break;
                }else{
                    midPoint = (lastPoint + iniPoint)/2;
                }
            }else if(chrPosStart > compareChrPos){
                status = 'h';
                iniPoint = midPoint+1;
                if(iniPoint>lastPoint){
                    break;
                }else{
                    midPoint = (lastPoint + iniPoint)/2;
                }               
            }
        }
        
        
        
        if(status=='l'){
            
            long codeLeft = this.mutationBinaryTree.get(midPoint-1);
            int chrLeft = (int)(codeLeft>>28);
            int posLeft = (int)(codeLeft&posMask);
            int diffLeft = Math.abs(posLeft - posStart);
            
            long codeRight = this.mutationBinaryTree.get(midPoint);
            int chrRight = (int)(codeRight>>28);
            int posRight = (int)(codeRight&posMask);
            int diffRight = Math.abs(posRight - posStart);
            
            if(chrLeft == chrStart && chrRight == chrStart){
                
                if(diffLeft > diffRight){
                    // right SV is nearest to cluster. save right SV
                    varCtx = this.mutationMap.get(codeRight);
                }else if(diffLeft < diffRight){
                    // left SV is nearest to cluster. save left SV
                    varCtx = this.mutationMap.get(codeLeft);
                }else if(diffLeft == diffRight){
                    // Both right and left SV is nearest to cluster. save both
                    ArrayList<VariantContext> add two var
                }//***************** Hold all code HERE. Stuck on logic for nearest finding
                
            }else if(chrLeft == chrStart && chrRight != chrStart){
                // right SV is on different chromosome
                
            }else if(chrLeft != chrStart && chrRight == chrStart){
                
            }
            
            if(midPoint == 0){
                
                /**
                 * midPoint is Start Point
                 * it's lower than start
                 * it have a chance that back part of it may overlap with this annotation. So, we have to check with chrPosStop.
                 * So we compare chrPosstop with chrPos of this annotation (midPoint)
                 */
                compareChrPos = annoBinaryTree.get(midPoint) & this.chrPosCompareMask;
                if(chrPosStop < compareChrPos){
                    // chrPosStop has lower than compareChrPos. This mean back part is not in the range of this annotation
                    return -1;
                }else if(chrPosStop>=compareChrPos){
                    // chrPosStop is higher than compareChrPos this mean it's back part is overlap in the range of this annotation. So we return this anootation index.
                    int midPointAnnotationIndex = (int)(this.annoBinaryTree.get(midPoint) & this.indexAnnoMask);
                    annotationIndex = midPointAnnotationIndex;
                    return annotationIndex;
                }
                
                return -1;

            }else if(midPoint == annoBinaryTree.size()-1){
                int midPointAnnotationIndex = (int)(this.annoBinaryTree.get(midPoint) & this.indexAnnoMask);
                /**
                * midpoint is Stop Point
                * It's lower than stop
                */
                annotationIndex = midPointAnnotationIndex;
                return annotationIndex;
                
            }else{
            
                int midPointAnnotationIndex = (int)(this.annoBinaryTree.get(midPoint) & this.indexAnnoMask);
                int leftPointAnnotationIndex = (int)(this.annoBinaryTree.get(midPoint-1) & this.indexAnnoMask);
                int rightPointAnnotationIndex = (int)(this.annoBinaryTree.get(midPoint+1) & this.indexAnnoMask);

                if(leftPointAnnotationIndex == midPointAnnotationIndex){
                    /**
                     * midpoint is Stop Point
                     * It lower than stop
                     */

                    annotationIndex = midPointAnnotationIndex;
                    return annotationIndex;

                }else if(rightPointAnnotationIndex == midPointAnnotationIndex){
                    /**
                     * midPoint is Start Point
                     * it's lower than start
                     * it have a chance that back part of it may overlap with this annotation. So, we have to check with chrPosStop.
                     * So we compare chrPosstop with chrPos of this annotation (midPoint)
                     */
                    compareChrPos = annoBinaryTree.get(midPoint) & this.chrPosCompareMask;
                    if(chrPosStop < compareChrPos){
                        // chrPosStop has lower than compareChrPos. This mean back part is not in the range of this annotation
                        return -1;
                    }else if(chrPosStop>=compareChrPos){
                        // chrPosStop is higher than compareChrPos this mean it's back part is overlap in the range of this annotation. So we return this anootation index.
                        annotationIndex = midPointAnnotationIndex;
                        return annotationIndex;
                    }

                }
            }
        }else if(status=='h'){
            
            if(midPoint == 0){
                int midPointAnnotationIndex = (int)(this.annoBinaryTree.get(midPoint) & this.indexAnnoMask);
                /**
                * midPoint is Start point
                * it's higher than start
                */
                
                annotationIndex = midPointAnnotationIndex;
                return annotationIndex;

            }else if(midPoint == annoBinaryTree.size()-1){
                /**
                * midpoint is Stop Point
                * It's higher than stop
                */
                
                return -1;
                
            }else{

                int midPointAnnotationIndex = (int)(this.annoBinaryTree.get(midPoint) & this.indexAnnoMask);
                int leftPointAnnotationIndex = (int)(this.annoBinaryTree.get(midPoint-1) & this.indexAnnoMask);
                int rightPointAnnotationIndex = (int)(this.annoBinaryTree.get(midPoint+1) & this.indexAnnoMask);

                if(leftPointAnnotationIndex == midPointAnnotationIndex){
                    /**
                     * midPoint is Stop point
                     * It's higher than stop
                     * it have a chance that back part of it may overlap with next annotation. So, we have to check with chrPosStop.
                     * So we compare chrPosstop with chrPos of next annotation (next anno start point)
                     */
                    
                    compareChrPos = annoBinaryTree.get(midPoint+1) & this.chrPosCompareMask;
                    if(chrPosStop < compareChrPos){
                        // chrPosStop has lower than or equal to compareChrPos. This mean back part is not in the range of next annotation index
                        return -1;
                    }else if(chrPosStop>=compareChrPos){
                        // chrPosStop higher or equal with compareChrPos. this mean back part is overlap with next annotation
                        midPointAnnotationIndex = (int)(this.annoBinaryTree.get(midPoint+1) & this.indexAnnoMask);
                        annotationIndex = midPointAnnotationIndex;
                        return annotationIndex;
                    }
                    
                    annotationIndex = midPointAnnotationIndex;
                    return annotationIndex;
                }else if(rightPointAnnotationIndex == midPointAnnotationIndex){
                    /**
                     * midPoint is Start point
                     * it's higher than start
                     */
                    
                    annotationIndex = midPointAnnotationIndex;
                    return annotationIndex;
                }
            }
        }
        
//        check correction of this logic 
        return -1;
    } 
}
