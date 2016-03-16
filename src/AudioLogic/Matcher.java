/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AudioLogic;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;

import org.mapdb.DB;
import org.mapdb.DBMaker;

import redis.clients.jedis.Jedis;

/**
 *
 * @author Kata
 */
public class Matcher {

	public NavigableMap<Long, List<PointPOJO>> hm = null;
	PrintWriter a;
	int c = 0;
	public HashMap<String, HashMap<Integer, Integer>> matching = new HashMap<String, HashMap<Integer, Integer>>();
	public HashMap<String, String> namesMus=new HashMap<String, String>();
	public NavigableMap<String, Integer> Names = null;
	DB db = null;
	 Jedis jedis = new Jedis("localhost");



private List<Result> results=new ArrayList<Result>();

public List<Result> getResults() {
	return results;
}

public Matcher() {

		db = DBMaker.newFileDB(new File("HashMaxHeights8"))
				.closeOnJvmShutdown().make();
		hm = db.getTreeMap("maxHeights");
		Names = db.getTreeMap("names");
	}

public boolean addMusic(String musicName)
{
	 
		
	musicName=getMusicName(musicName);
    	if (jedis.get(musicName) == null)
		{
			//Names.put(musicName, 1);
			jedis.lpush("names", musicName);
            return true;
			
		}
	return false;
}
public String getMusicName(String musicName)
{
	int index=musicName.indexOf("(");
	int index2=musicName.indexOf(")");
	if(index>-1&&index2>-1){
	String str=musicName.substring(index,index2+1);
	musicName=musicName.replace(str, "");
	}
	
	 index=musicName.indexOf("[");
     index2=musicName.indexOf("]");
	if(index>-1&&index2>-1){
	String str=musicName.substring(index,index2+1);
	musicName=musicName.replace(str, "");

	
}
	musicName=musicName.replace(".mp3","");

	return musicName;

}
	public void insert(long points[], PointPOJO pj) {

		long hash = Arrays.hashCode(points); 
		


		if (jedis.lrange(String.valueOf(hash),0,0)==null) {
		//	List<PointPOJO> ls = (List<PointPOJO>) hm.get(hash);
			//ls.add(pj);
			//hm.put(hash, ls);
			jedis.lpush(String.valueOf(hash), pj.location+"|"+pj.name);
		} else {

			//List<PointPOJO> ls = new ArrayList<>();
			//ls.add(pj);
			//hm.put(hash, ls);
			jedis.lpush(String.valueOf(hash), pj.location+"|"+pj.name);

		}
	}

	String name;
	
	public List<PointPOJO> getPojo(List<String> ls)
	{
		if(ls==null)
			return null;
		List<PointPOJO> ls2=new ArrayList<PointPOJO>();
		for(String ph:ls)
		{
			String a = "";
			int i=0;
			for(;ph.charAt(i)!=124;i++)
			{
				a+=ph.charAt(i);
			}
			
		//	System.out.println(ph+" "+a[0]+" "+a[1]);
			ls2.add(new PointPOJO(Integer.parseInt(a.trim()), ph.substring(a.length()+1)));
		}
		return ls2;
	}
	

	public void get(long points[], int l) {
		long key = Arrays.hashCode(points);
//System.out.println(points[0]+" "+points[1]+" "+points[2]+" "+points[3]);
		if(!db.isClosed()){
			List<PointPOJO> ls=getPojo(jedis.lrange(String.valueOf(key), 0, -1));
			//List<PointPOJO> ls = (List<PointPOJO>) hm.get(key);
		if (ls != null) {
			for (PointPOJO pj : ls) {
				HashMap matchedDistances = (HashMap) matching.get(pj.name);
				name = pj.name;
				if(!namesMus.containsKey(name));
				{
					namesMus.put(name, "asd");
				}
				if (matchedDistances != null) {
					Integer distance = (Integer) matchedDistances.get(Math.abs(pj.location - l));
					if (distance == null) {
						matchedDistances.put(Math.abs(pj.location - l), 1);
					} else {
						matchedDistances.put(Math.abs(pj.location - l), ++distance);
					}

				} else {
					HashMap a2 = new HashMap();
					a2.put(Math.abs(pj.location - l), 1);
					matching.put(pj.name, a2);
				}
			}
		}
		}

	}

	public String analyze() {
		Iterator it2 = namesMus.entrySet().iterator();
		int mx = -1;
		String nume = null;
		results.clear();
		while (it2.hasNext()) {
			Map.Entry pair2 = (Map.Entry) it2.next();

			HashMap mp = (HashMap) matching.get(pair2.getKey());
			int mx2=-1;
			if (mp != null) {
				Iterator it = mp.entrySet().iterator();
				
				while (it.hasNext()) {
					Map.Entry pair = (Map.Entry) it.next();
					if ((int) pair.getValue() > mx) {
						mx = (int) pair.getValue();
						nume = (String) pair2.getKey();
					}
					if((int) pair.getValue() > mx2)
					{
						mx2=(int) pair.getValue();
					}
					
					
					it.remove();
				}
				Result res=new Result(pair2.getKey().toString(),mx2);
				int index=add(res);
				results.add(index,res);
			}

		}
		System.out.println("\n" + mx + " " + nume + "\n");
		
		
		
		
		return nume;
	}

	
	public List<String> getNames()
	{
		Iterator it2 = Names.entrySet().iterator();
		String nume = null;
        List<String> ls=new ArrayList<String>();
		while (it2.hasNext()) {
			Map.Entry pair2 = (Map.Entry) it2.next();
			ls.add((String) pair2.getKey());
	}
		ls=jedis.lrange("names", 0, -1);
		return ls;
	}
	
	public void clear()
	{
		hm.clear();
		Names.clear();
	    db.commit();

		
	}
	
	public void revert()
	{
	       	
		db.close();
	}
	
	public int  add(Result v) {
        int index = Collections.binarySearch(results, v);
        if (index < 0) {
            index = (index + 1) * (-1);
        } else {
            index = index + 1;
        }
        return index;
	}
	

}