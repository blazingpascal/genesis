package pop.plplan.algorithms;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StreamCorruptedException;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import pop.plplan.javaapi.StringToByteConvertor;

/**
 * Class for reading files containing facts, operators and goals.
 * <p>
 * PLPLAN
 * Authors : Philippe Fournier-Viger and Ludovic lebel
 * <p>
 * This work is licensed under the Creative Commons Attribution 2.5 License. To
 * view a copy of this license, visit
 * http://creativecommons.org/licenses/by/2.5/ or send a letter to Creative
 * Commons, 543 Howard Street, 5th Floor, San Francisco, California, 94105, USA.
 * <p>
 * If you use PLPLAN, we ask you to mention our names and our webpage URL in your work. 
 * The PLPLAN software is copyrighted by Philippe Fournier-Viger and Ludovic Lebel (2005). 
 * Please read carefully the license to know what you can do and cannot do with this software. 
 * You can contact Philippe Fournier-Viger for special permissions. 
 * <p>
 * This sofware is provided "as is", without warranty of any kind. 
 * The user takes the entire risk as to the quality and performance of the software. 
 * The authors accept no responsibility for any problem the user encounters using this software.
 * <p>
 * @author Philippe Fournier-Viger and Ludovic Lebel
 */
public class WorldReader {
	
	private Set<Proposition> factMap;   // Proposition, Proposition
	private Set<Action> opMap;     // Action, Action
	private List<Proposition> goalList; // contient des Propositions
    
    private StringToByteConvertor convertor;
	
	public WorldReader(StringToByteConvertor convertor)
	{
        this.convertor = convertor; 
	}
	
	public WorldReader(String filePath, StringToByteConvertor convertor) throws FileNotFoundException, IOException
	{
        this.convertor = convertor;
        
		StreamTokenizer st;
		st = getStreamTokenizer(filePath);
		
		boolean finished = false;
		do
		{
			if(st.nextToken() == '#')
			{
				st.nextToken();

				if(st.sval != null && st.sval.equals("facts"))
					factMap = parseListToMap(st);
				if(st.sval != null && st.sval.equals("operator")) 
					opMap = parseOps(st);
				if(st.sval != null && st.sval.equals("goal")) 
					goalList = parseListToList(st);
			}
			if(st.ttype == StreamTokenizer.TT_EOF) finished = true;
			
		}while(!finished);
	}

	/**
	 * @param st
	 * @return
	 * @throws IOException, StreamCorruptedException
	 */
	 private Set<Action> parseOps(StreamTokenizer iSt) 
		throws IOException, StreamCorruptedException {
		Set<Action> hash = new HashSet<Action>(50);
		
		int nextToken = iSt.nextToken();
		if(nextToken == '#' &&
		   iSt.nextToken() == StreamTokenizer.TT_WORD &&
		   iSt.sval.equals("include"))
		{
			if(iSt.nextToken() == '"')
			{
				iSt = getStreamTokenizer(iSt.sval);
				nextToken = iSt.nextToken();
			}
			else 
				throw new StreamCorruptedException("bad include directive:" + Integer.toString(iSt.lineno()));
		}
		
		if(nextToken != '(')
			throw new StreamCorruptedException("'(' expected at:" + Integer.toString(iSt.lineno()));
		
		boolean finished = false;
		do
		{
			switch(iSt.nextToken())
			{
				case ')' : 
					finished = true;
					break;
				case '(' :
					iSt.pushBack();
					parseOp(iSt, hash);
					break;
				default :
					throw new StreamCorruptedException("Op expected at:" + Integer.toString(iSt.lineno()));
			}
			
		}while(!finished);
		
		return hash;
	}
	
	/**
	 * @param st
	 * @return
	 * @throws IOException, StreamCorruptedException
	 */
	private void parseOp(StreamTokenizer iSt, Set<Action> hash) 
		throws IOException, StreamCorruptedException {
		
		if(iSt.nextToken() != '(')
			throw new StreamCorruptedException("'(' expected at:" + Integer.toString(iSt.lineno()));
		
		if(iSt.nextToken() != StreamTokenizer.TT_WORD || !iSt.sval.equals("op"))
			throw new StreamCorruptedException("'op' expected at:" + Integer.toString(iSt.lineno()));
		
		boolean finished = false;
		do
		{
			switch(iSt.nextToken())
			{
				case ')' : 
					finished = true;
					break;
				case StreamTokenizer.TT_WORD :
					Action act = new Action(convertor.getIDForActionName(iSt.sval), 
                            parseListToMap(iSt), 
                            parseListToMap(iSt), 
                            parseListToMap(iSt));
					hash.add(act);
					break;
				default :
					throw new StreamCorruptedException("Op name expected at:" + Integer.toString(iSt.lineno()));
			}
			
		}while(!finished);
	}

	/**
	 * @param st
	 * @return
	 */
	Set<Proposition> parseListToMap(StreamTokenizer iSt) 
		throws IOException, StreamCorruptedException {
		Set<Proposition> map = new HashSet<Proposition>();
		
		if(iSt.nextToken() != '(')
			throw new StreamCorruptedException("'(' expected at:" + Integer.toString(iSt.lineno()));
		
		boolean finished = false;
		do
		{
			switch(iSt.nextToken())
			{
				case ',' : break;
				case ')' : 
					finished = true;
					break;
				case StreamTokenizer.TT_WORD :
					Proposition p = new Proposition(convertor.getIDForPropositionName(iSt.sval));
					map.add(p);
					break;
				default :
					throw new StreamCorruptedException("Proposition expected at:" + Integer.toString(iSt.lineno()));
			}
			
		}while(!finished);
		
		return map;
	}
	
	/**
	 * @param st
	 * @return
	 */
	List<Proposition> parseListToList(StreamTokenizer iSt) 
		throws IOException, StreamCorruptedException {
		List<Proposition> list = new ArrayList<Proposition>();
		
		if(iSt.nextToken() != '(')
			throw new StreamCorruptedException("'(' expected at:" + Integer.toString(iSt.lineno()));
		
		boolean finished = false;
		do
		{
			switch(iSt.nextToken())
			{
				case ',' : break;
				case ')' : 
					finished = true;
					break;
				case StreamTokenizer.TT_WORD :
					Proposition p = new Proposition(convertor.getIDForPropositionName(iSt.sval));
					list.add(p);
					break;
				default :
					throw new StreamCorruptedException("Proposition expected at:" + Integer.toString(iSt.lineno()));
			}
			
		}while(!finished);
		
		return list;
	}

	private StreamTokenizer getStreamTokenizer(String filePath)
		throws FileNotFoundException, IOException
	{
		StreamTokenizer st = null;
		File file = new File(filePath);
		FileInputStream fStream;

		fStream = new FileInputStream(file.getCanonicalPath());
		Reader r = new BufferedReader(new InputStreamReader(fStream));
		st = new StreamTokenizer(r);
		st.slashSlashComments(true);
		st.wordChars((int)'a', (int)'Z');
		
		return st;
	}
	
	/**
	 * @return
	 */
	public Set<Proposition> getFactSet() {
		return factMap;
	}

	/**
	 * @return
	 */
	public Set<Action> getOpSet() {
		return opMap;
	}
	
	/**
	 * @return
	 */
	public List<Proposition> getGoalList() {
		return goalList;
	}

	/**
	 * @return
	 */
	public String getFactStr() {
		String str = "(";
		Iterator<Proposition> itr = factMap.iterator();
		while (itr.hasNext()) {
			Proposition p = itr.next();
			str += p.toString();
			if(itr.hasNext()) str += " ";
		}
		str += ")";
		return str;
	}

	/**
	 * @return
	 */
	public String getGoalStr() {
		String str = "(";
		for(int i = 0; i < goalList.size(); i++){
			Proposition p = (Proposition) goalList.get(i);
			str += p.toString();
			if(i < goalList.size()) str += " ";
		}
		str += ")";
		return str;
	}

	/**
	 * @param operatorFileName
	 * @throws IOException
	 * @throws StreamCorruptedException
	 */
	public void parseOpsFile(String operatorFileName) throws StreamCorruptedException, IOException {
		StreamTokenizer st = getStreamTokenizer(operatorFileName);
		
		boolean finished = false;
		do
		{
			if(st.nextToken() == '#')
			{
				st.nextToken();
				if(st.sval != null && st.sval.equals("operator")) 
					opMap = parseOps(st);
			}
			if(st.ttype == StreamTokenizer.TT_EOF) finished = true;
			
		}while(!finished);
	}

	/**
	 * @param problemFileName
	 * @throws IOException
	 * @throws StreamCorruptedException
	 */
	public void parseProbFile(String problemFileName) throws StreamCorruptedException, IOException {
		StreamTokenizer st = getStreamTokenizer(problemFileName);
		
		boolean finished = false;
		do
		{
			if(st.nextToken() == '#')
			{
				st.nextToken();
				if(st.sval != null && st.sval.equals("facts")) 
					factMap = parseListToMap(st);
				if(st.sval != null && st.sval.equals("goal")) 
					goalList = parseListToList(st);
			}
			if(st.ttype == StreamTokenizer.TT_EOF) finished = true;
			
		}while(!finished);
	}

	/**
	 * @param text
	 * @throws IOException
	 * @throws StreamCorruptedException
	 */
	public void parseFacts(String text) throws StreamCorruptedException, IOException {
		Reader r = new BufferedReader(new StringReader(text));
		StreamTokenizer st = new StreamTokenizer(r);
		st.slashSlashComments(true);
		st.wordChars((int)'a', (int)'Z');
		
		factMap = parseListToMap(st);
	}
	
	/**
	 * @param text
	 * @throws IOException
	 * @throws StreamCorruptedException
	 */
	public void parseGoal(String text) throws StreamCorruptedException, IOException {
		Reader r = new BufferedReader(new StringReader(text));
		StreamTokenizer st = new StreamTokenizer(r);
		st.slashSlashComments(true);
		st.wordChars((int)'a', (int)'Z');
		
		goalList = parseListToList(st);
	}

}
