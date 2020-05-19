import java.util.*;
import java.io.*;

class FF{
	static LinkedHashMap<Character , List<Character>> firsts = new LinkedHashMap<Character , List<Character>>();
	static LinkedHashMap<Character , Set<Character>> follows = new LinkedHashMap<Character , Set<Character>>();
	static LinkedHashMap<Character , String[]> productions = new LinkedHashMap<Character , String[]>();
	static LinkedHashMap<Character , Character> parent = new LinkedHashMap<Character , Character>();
	static  char root;
	public static void main(String[] args) throws IOException{
		File file = new File(args[0]); 
		BufferedReader br = new BufferedReader(new FileReader(file)); 
		root =  br.readLine().charAt(0);
		br = new BufferedReader(new FileReader(file)); 
		String stt;
	    while ((stt = br.readLine()) != null){
	      	String lr[] = stt.split("->");
	      	String te[] = lr[1].split("/");
	      	for(String i:te){
	      		for(int j=0;j<i.length();j++){
	      			char c = i.charAt(j);
	      			if(Character.isUpperCase(c)&&!parent.containsKey(c)&&c!=root){
	      				parent.put(c,lr[0].charAt(0));
	      			}
	      		}
	      	}
	      	System.out.println(""+lr[0]+" -> "+lr[1]);
	      	productions.put(lr[0].charAt(0),te);
	    }


	    System.out.println("\n\t---FIRST---");
	    for(Map.Entry<Character,String[]> e: productions.entrySet()){
	    	char key = e.getKey();
	    	List<Character> l = getFirst(key);
	    	firsts.put(key,l);
	    	System.out.println("Element: "+key+" First: "+l.toString());
	    }

	    System.out.println("\n\t---FOLLOW---");
	    for(Map.Entry<Character,String[]> e: productions.entrySet()){
	    	char key = e.getKey();
	    	List<Character> l = getFollow(key);
	    	Set<Character> s = new LinkedHashSet<Character>(l);
	    	s.remove(new Character('#'));
	    	follows.put(key,s);
	    	System.out.println("Element: "+key+" First: "+s.toString());
	    }
	}

	static List<Character> getFirst(char s){
		List<Character> first = new ArrayList<Character>();
		for(String i: productions.get(s)){
		
			
					for(int j=0;j<i.length();j++){
						if(Character.isUpperCase(i.charAt(j))){
							List<Character> f = getFirst(i.charAt(j));
							first.addAll(f);
							if(j==1)
							if(!f.contains('#')){
								break;
							}
						}else{
							first.add(i.charAt(j));
							break;
						}
					}
			
		}
		return first;
	}

	static List<Character> getFollow(char s){
		List<Character> follow = new ArrayList<Character>();
		if(s==root)
			follow.add('$');
		for(Map.Entry<Character,String[]> e: productions.entrySet()){
			String p[] = e.getValue();
			for(String prod:p){
			for(int i=0;i<prod.length();i++){
				if(i!=prod.length()-1&&prod.charAt(i)==s){

					if(Character.isUpperCase(prod.charAt(i+1))){
						follow.addAll(firsts.get(prod.charAt(i+1)));
						if(firsts.get(prod.charAt(i+1)).contains('#')){
							follow.addAll(getFollow(parent.get(prod.charAt(i))));////////////////////////////
						}
					}else{
						follow.add(prod.charAt(i+1));
					}
				}
				else if(i==prod.length()-1&&prod.charAt(i)==s){
					follow = getFollow(parent.get(prod.charAt(i)));
				}
			}
			}	
		}

		return follow;
	}
}