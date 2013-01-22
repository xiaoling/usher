package sf.filler.regex;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import sf.SFConstants;
import sf.SFEntity;
import sf.filler.Filler;
import tackbp.KbEntity.EntityType;

/**
 * A filler for the per:stateorprovince_of_death slot
 * @author sjonany
 */
public class PerStateorprovinceOfDeathFiller extends Filler{
	private static final String SLOT_NAME = "per:stateorprovince_of_death";
	//words that indicate death
	private static final List<String> DEATH_VERBS = Collections.unmodifiableList(
			Arrays.asList(new String[]{
					"ATTACK", "DIE", "PASSED AWAY", "KILL"
					}));
	
	//list of US states, abbreviations and full names in upper case
	//TODO: Hardcoded here because we are only supposed to submit 1 file
	private static final List<String> US_STATES = Collections.unmodifiableList(
			Arrays.asList((new String[]{
					"ALABAMA", "AL",
					"ALASKA", "AK",
					"ARIZONA", "AZ", 
					"ARKANSAS", "AR", 
					"CALIFORNIA", "CA", 
					"COLORADO", "CO",
					"CONNECTICUT", "CT", 
					"DELAWARE", "DE", 
					"FLORIDA", "FL", 
					"GEORGIA", "GA", 
					"HAWAII", "HI", 
					"IDAHO", "ID", 
					"ILLINOIS", "IL",
					"INDIANA", "IN", 
					"IOWA", "IA", 
					"KANSAS", "KS", 
					"KENTUCKY", "KY", 
					"LOUISIANA", "LA", 
					"MAINE", "ME",
					"MARYLAND", "MD", 
					"MASSACHUSETTS", "MA", 
					"MICHIGAN", "MI", 
					"MINNESOTA", "MN", 
					"MISSISSIPPI", "MS", 
					"MISSOURI", "MO", 
					"MONTANA", "MT", 
					"NEBRASKA", "NE", 
					"NEVADA", "NV",
					"NEW HAMPSHIRE", "NH", 
					"NEW JERSEY", "NJ", 
					"NEW MEXICO", "NM", 
					"NEW YORK", "NY",
					"NORTH CAROLINA", "NC", 
					"NORTH DAKOTA", "ND", 
					"OHIO", "OH", 
					"OKLAHOMA", "OK",
					"OREGON", "OR", 
					"PENNSYLVANIA", "PA",
					"RHODE ISLAND", "RI", 
					"SOUTH CAROLINA", "SC", 
					"SOUTH DAKOTA", "SD", 
					"TENNESSEE", "TN", 
					"TEXAS", "TX", 
					"UTAH", "UT",
					"VERMONT", "VT", 
					"VIRGINIA", "VA",
					"WASHINGTON", "WA", 
					"WEST VIRGINIA", "WV", 
					"WISCONSIN", "WI", 
					"WYOMING", "WY"})));
	
	public PerStateorprovinceOfDeathFiller() {
		slotName = SLOT_NAME;
	}
	
	
	@Override
	public void predict(SFEntity mention, Map<String, String> annotations) {
		//the query needs to be of PER type, since the slot to be filled is related to a person
		if(mention.ignoredSlots.contains(slotName) 
				|| mention.entityType != EntityType.PER){
			return;
		}
		
		// get the filename of this sentence.
		String[] meta = annotations.get(SFConstants.META).split("\t");
		String filename = meta[2];
		
		//TODO: what if you get a pronoun? - Coreference info is not provided yet
		String tokens = annotations.get(SFConstants.TOKENS).split("\t")[1];
		
		//get the segment related to that person
		String segmentTokens = getRelatedSegmentToken(tokens, mention.mentionString);
		if(segmentTokens == null){
			//if the person name isn't even mentioned in the sentence, stop processing
			return;
		}
		
		//Assertion: This sentence is talking about the right person
		
		//the grammatical info is important to make sure that it
		//part of speech line
		String posLine = annotations.get(SFConstants.STANFORDPOS);
		String[] posLineTokens = posLine.split("\t");
		if(posLineTokens.length <= 1){
			return;
		}
		
		String posAnnotationForSegment = getAnnotationForSegment(posLineTokens[1], segmentTokens, tokens);
		if(!isDeathRelated(segmentTokens, posAnnotationForSegment)){
			//current sentence is talking about the right person, but is not about his/her death
			return;
		}
		
		//Assertion: this sentence is talking about the death of the right person
		String nerAnnotationLine = annotations.get(SFConstants.STANFORDNER);
		String[] nerAnnotationLineTokens = nerAnnotationLine.split("\t");
		if(nerAnnotationLineTokens.length <= 1){
			return;
		}
		
		//get the NER for just the segmentTokens
		String nerAnnotation = getAnnotationForSegment(nerAnnotationLineTokens[1], segmentTokens, tokens);
		
		Scanner nerReader = new Scanner(nerAnnotation);
		Scanner tokenReader = new Scanner(segmentTokens);
		
		while(nerReader.hasNext()){
			String curEntityType = nerReader.next();
			String curToken = tokenReader.next();
			
			if(curEntityType.equals("LOCATION")){
				//this might be the start of a multi-word state/province
				//continue consuming tokens to get the entire location name
				String locationName = curToken;
				while(nerReader.hasNext()){
					String entityType = nerReader.next();
					String token = tokenReader.next();
					if(entityType.equals("LOCATION")){
						locationName += " " + token;
					}else{
						break;
					}
				}
				
				if(isUSState(locationName) || isProvince(locationName)){
					fillSlot(mention, filename, locationName);
					nerReader.close();
					tokenReader.close();
					return;
				}
			}
		}
		
		nerReader.close();
		tokenReader.close();
	}
	
	/**
	 * fill in the slot given that you obtain the answer for the slot from 'filename'
	 * @param mention contains the slot to be filled
	 * @param filename the name of the file you found the answer from
	 * @param answer the answer to fill the slot with
	 */
	private void fillSlot(SFEntity mention, String filename, String answer){
		SFEntity.SingleAnswer ans = new SFEntity.SingleAnswer();
		ans.answer = answer;
		ans.doc = filename;
		mention.answers.put(slotName, ans);
	}
	
	/**
	 * Check if the string is a US state
	 * @param s the string to check
	 * @return true iff 'locationName' is a US state, abbreviated or not
	 */
	private boolean isUSState(String locationName){
		return US_STATES.contains(locationName.toUpperCase());
	}
	
	/**
	 * check if the string is the name of a province
	 * since I don't have access to any database, this is the best I can do for now
	 * @param locationNAme
	 * @return true iff 'locationName' is a province
	 */
	private boolean isProvince(String locationName){
		return locationName.toUpperCase().contains("PROVINCE");
	}
	
	/**
	 * @param tokens the sentence to examine
	 * @param posAnnotation the part of speech annotation corresponding to the token
	 * @return true iff this sentence is talking about somebody's death
	 */
	private boolean isDeathRelated(String tokens, String posAnnotation){
		Scanner posLineReader = new Scanner(posAnnotation);
		Scanner sentenceReader = new Scanner(tokens);
		while(posLineReader.hasNext()){
			String curPos = posLineReader.next();
			String curWord = sentenceReader.next();
			
			//to capture any kind of verb - .eg. VBZ, VBG
			if(curPos.contains("VB")){
				for(String verb : DEATH_VERBS){
					//ideally, I want to get the stemmed form, but I don't have access to it right now
					//so I just use string.contains
					if(curWord.toUpperCase().contains(verb)){
						posLineReader.close();
						sentenceReader.close();
						return true;
					}
				}
			}
		}
		
		posLineReader.close();
		sentenceReader.close();
		
		return false;
	}
	
	/**
	 * get the segment that is related to the name
	 * a 'segment' is a sequence of tokens that are not divided by any double quotes
	 * @param tokens the sentence to examine
	 * @param name the name of the interesting object to search for
	 * @return the segment related to the name
	 * 	null if there is no such segment
	 */
	private String getRelatedSegmentToken(String tokens, String name){
		String[] segments = tokens.split("(''|``)");
		for(String segment : segments){
			if(segment.contains(name)){
				return segment;
			}
		}
		return null;
	}
	
	/**
	 * e.g. If annotation  = "A B C D", segmentTokens = "Baby cat", tokens = "Another Baby cat danced"
	 * "B C" will be returned
	 * @requires segmentTokens is contained in tokens
	 * @param annotation the original notation corresponding to the entire tokens
	 * @param segmentTokens a subset of tokens that we are interested in 
	 * @param tokens the tokens corresponding to the annotations
	 * @return the NER corresponding to the segment tokens, as opposed to the entire tokens
	 */
	private static String getAnnotationForSegment(String annotation, String segmentTokens, String tokens){
		segmentTokens = segmentTokens.trim();
		tokens = tokens.trim();
		int startIndex = tokens.indexOf(segmentTokens);
		String skippedWords = tokens.substring(0, startIndex);
		int numSkippedWords = skippedWords.trim().length() == 0 ? 0 : skippedWords.split(" ").length;
		int numSegmentTokens = segmentTokens.trim().length() == 0? 0 : segmentTokens.split(" ").length;
		
		Scanner nerReader = new Scanner(annotation);
		for(int i=1; i<=numSkippedWords; i++){
			nerReader.next();
		}
		
		String relevantNer = "";
		for(int i=1; i<=numSegmentTokens; i++){
			relevantNer += nerReader.next() + " ";
		}
		
		nerReader.close();
		
		return relevantNer;
	}
}
